package me.boston.mythicarmor.mythic;

import me.boston.mythicarmor.MythicArmor;
import me.boston.mythicarmor.component.ModDataComponentTypes;
import me.boston.mythicarmor.item.ModItems;
import me.boston.mythicarmor.mythic.items.MythicElytraItem;
import me.boston.mythicarmor.mythic.items.interfaces.IMythicItem;
import me.boston.mythicarmor.util.RGB;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MythicItemHandler {

    public static void updateColor(ItemStack itemStack) {
        itemStack.set(DataComponents.DYED_COLOR, RGB.of(MythicItemColors.getImbuementColor(itemStack, 0)).toDyedItemColor());
    }

    public static void imbueItem(ItemStack itemStack, ImbueType imbueType) {
        imbueItem(itemStack, imbueType, 1);
    }

    public static void imbueItem(ItemStack itemStack, ImbueType imbueType, int amount) {
        if (!canImbueItem(itemStack, amount))
            return;

        int currentImbueAmount = getImbuePercentage(itemStack, imbueType, true);
        itemStack.set(imbueType.getDataComponentType(), currentImbueAmount + amount);
        int totalImbueAmount = getTotalImbuePercentage(itemStack);
        itemStack.set(ModDataComponentTypes.IMBUEMENT_TOTAL.get(), totalImbueAmount + amount);

        updateColor(itemStack);

        ImbuementEffectsHandler.onImbue(itemStack, imbueType);
    }

    public static void removeImbue(ItemStack itemStack, ImbueType imbueType) {
        removeImbue(itemStack, imbueType, 1);
    }

    public static void removeImbue(ItemStack itemStack, ImbueType imbueType, int amount) {
        imbueItem(itemStack, imbueType, -amount);
    }

    public static int getImbuePercentage(ItemStack itemStack, ImbueType imbueType) {
        return getImbuePercentage(itemStack, imbueType, false);
    }

    public static int getImbuePercentage(ItemStack itemStack, ImbueType imbueType, boolean includeBroken) {
        if (itemStack == null || !(itemStack.getItem() instanceof IMythicItem))
            return 0;

        if (!includeBroken && itemStack.getItem() == ModItems.MYTHIC_ELYTRA.get() && !MythicElytraItem.isUsable(itemStack))
            return 0;

        Integer amount = itemStack.get(imbueType.getDataComponentType());
        return amount == null ? 0 : amount;
    }

    public static int getTotalImbuePercentage(ItemStack itemStack) {
        if (itemStack == null || !(itemStack.getItem() instanceof IMythicItem))
            return 0;
        Integer total = itemStack.get(ModDataComponentTypes.IMBUEMENT_TOTAL.get());
        return total == null ? 0 : total;
    }

    public static boolean canImbueItem(ItemStack itemStack, int amount) {
        return getTotalImbuePercentage(itemStack) + amount <= 100;
    }

    private static List<ImbueLevel> getTrueImbueLevels(ItemStack itemStack) {
        List<ImbueLevel> levels = new ArrayList<>();
        for (ImbueType imbueType : ImbueType.values()) {
            int amount = getImbuePercentage(itemStack, imbueType, true);
            if (amount == 0)
                continue;
            levels.add(new ImbueLevel(imbueType, amount));
        }
        return levels;
    }

    public static void appendHoverText(ItemStack itemStack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        int totalImbueAmount = getTotalImbuePercentage(itemStack);

        List<ImbueLevel> imbueLevels;
        if (totalImbueAmount == 0) // don't bother doing this expensive operation if the item is known to have no imbues
            imbueLevels = new ArrayList<>();
        else
            imbueLevels = getTrueImbueLevels(itemStack);

        // Display information about the imbued amounts
        if (totalImbueAmount >= 100) {
            tooltipComponents.add(Component.literal("§dThis item is fully imbued."));
        } else {
            tooltipComponents.add(Component.literal("This item is §d" + totalImbueAmount + "%§r imbued."));
        }

        // If there are no imbuements then tell the player they can imbue this item
        if (totalImbueAmount == 0) {
            tooltipComponents.add(Component.literal("§dImbue§r this item at an §bImbuing Station§r."));

            // All the things relevant to 0 imbue amount are added.
            return;
        }

        if (itemStack.getItem() == ModItems.MYTHIC_ELYTRA.get() && !MythicElytraItem.isUsable(itemStack)) {
            tooltipComponents.add(Component.literal("§4Broken elytra provide no effects"));
        }

        // Display the imbued amounts
        for (ImbueLevel imbueLevel : imbueLevels) {
            tooltipComponents.add(Component.literal(imbueLevel.imbueType().getFormattedDisplayPercentage(imbueLevel.level())));
        }

        // If shift is held, display more information
        tooltipComponents.add(Component.literal(""));
        if (!Screen.hasShiftDown()) {
            tooltipComponents.add(Component.literal("§e§o" + "[SHIFT] for imbued effects"));
        } else {
            for (ImbueLevel imbueLevel : imbueLevels) {
                ImbueType imbueType = imbueLevel.imbueType();
                int level = imbueLevel.level();

                LinkedHashMap<ImbueType.ImbuableEquipment, List<ImbueType.ImbueEffect>> imbueEffects = imbueType.getEffectDescriptions();

                for (ImbueType.ImbuableEquipment equipment : imbueEffects.keySet()) {
                    if (!equipment.is(itemStack.getItem()))
                        continue;

                    for (ImbueType.ImbueEffect effect : imbueEffects.get(equipment)) {
                        String description = effect.getFormattedDescriptionForLevel(level);
                        if (description != null)
                            tooltipComponents.add(Component.literal("§"+imbueType.getTextColorCode() + "* §r" + description));
                    }
                }

                if (ImbueType.ImbuableEquipment.TOOL.is(itemStack.getItem()) && ImbuementEffectsHandler.hasSilkTouch(itemStack, context.level())) {
                    int magma = getImbuePercentage(itemStack, imbueType, true);
                    if (magma > 0) {
                        tooltipComponents.add(Component.literal("§4Magma auto smelt is incompatible with silk touch"));
                    }
                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = MythicArmor.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class MythicItemColors {
        private static final RGB DEFAULT_MYTHIC_COLOR = new RGB(255, 45, 245);

        public static int getImbuementColor(ItemStack stack, int layer) {
            if (layer != 0)
                return 0xFFFFFFFF;

            double totalImbue = (100 - getTotalImbuePercentage(stack)) / 100.0;
            RGB rgb = DEFAULT_MYTHIC_COLOR.multiply(totalImbue);
            for (ImbueType type : ImbueType.values()) {
                double weighting = getImbuePercentage(stack, type, true) / 100f;
                RGB typeColor = type.getColor();
                rgb.addInPlace(typeColor.multiply(weighting));
            }
            return rgb.toOpaqueInt();
        }

        @SubscribeEvent
        public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
            ItemLike[] items = ModItems.ITEMS.getEntries().stream()
                    .filter(item -> item.get() instanceof IMythicItem)
                    .map(RegistryObject::get)
                    .toArray(ItemLike[]::new);
            event.register(MythicItemColors::getImbuementColor, items);
        }
    }

    private record ImbueLevel(ImbueType imbueType, int level) {}
}
