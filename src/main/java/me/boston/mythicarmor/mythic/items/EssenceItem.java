package me.boston.mythicarmor.mythic.items;

import me.boston.mythicarmor.mythic.ImbueType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.LinkedHashMap;
import java.util.List;

public class EssenceItem extends Item {
    private ImbueType imbueType;

    public EssenceItem(Properties pProperties, ImbueType imbueType) {
        super(pProperties);
        this.imbueType = imbueType;
    }

    public ImbueType getImbueType() {
        return imbueType;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        LinkedHashMap<ImbueType.ImbuableEquipment, List<ImbueType.ImbueEffect>> imbueEffects = imbueType.getEffectDescriptions();
        for (ImbueType.ImbuableEquipment equipment : imbueEffects.keySet()) {
            tooltipComponents.add(Component.literal("§"+imbueType.getTextColorCode()+"To "+equipment.getDisplayName()+":"));
            for (ImbueType.ImbueEffect effect : imbueEffects.get(equipment)) {
                List<String> descriptions = effect.getFormattedDescriptionForEssenceItem();
                for (String desc : descriptions) {
                    tooltipComponents.add(Component.literal("§"+imbueType.getTextColorCode() + " * §r" + desc));
                }
            }
        }
    }
}
