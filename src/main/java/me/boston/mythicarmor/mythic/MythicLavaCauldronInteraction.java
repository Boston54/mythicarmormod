package me.boston.mythicarmor.mythic;

import me.boston.mythicarmor.item.ModItems;
import me.boston.mythicarmor.mythic.items.interfaces.IMythicItem;
import me.boston.mythicarmor.util.ModStats;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Random;


public class MythicLavaCauldronInteraction {
    private final static CauldronInteraction MYTHIC_ITEM_INTERACTION = (blockState, level, blockPos, player, interactionHand, stack) -> {
        Item item = stack.getItem();
        if (!(item instanceof IMythicItem)) {
            return ItemInteractionResult.SUCCESS;
        } else if (MythicItemHandler.getTotalImbuePercentage(stack) == 0) {
            return ItemInteractionResult.SUCCESS;
        } else {
            if (!level.isClientSide) {
                int amountBurnt = burnImbuements(stack);

                // 10% Chance to remove the lava
                if ((new Random()).nextInt(10) == 0) {
                    level.setBlockAndUpdate(blockPos, Blocks.CAULDRON.defaultBlockState());
                    level.playSound(null, blockPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1f, 0.5f);
                } else {
                    level.playSound(null, blockPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1f, 2f);
                }

                player.awardStat(ModStats.BURNT_IMBUEMENTS_STAT, amountBurnt);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
    };

    private static int burnImbuements(ItemStack itemStack) {
        // Get the imbuements on the item for each type
        HashMap<ImbueType, Integer> currentImbuements = new HashMap<>();
        for (ImbueType type : ImbueType.values()) {
            int amount = MythicItemHandler.getImbuePercentage(itemStack, type, true);
            if (amount > 0)
                currentImbuements.put(type, amount);
        }

        // Burn up to 10 imbuements off, but limit it at the total imbue amount
        int burnAmount = Math.min(currentImbuements.values().stream().mapToInt(Integer::intValue).sum(), 10);

        Random random = new Random();
        // Remove burnAmount imbuements
        for (int i = 0; i < burnAmount; i++) {
            // Choose a random imbuement that this item has and subtract 1 to account for its removal
            ImbueType[] choices = currentImbuements.keySet().toArray(ImbueType[]::new);
            ImbueType type = choices[random.nextInt(choices.length)];
            currentImbuements.put(type, currentImbuements.get(type) - 1);
            if (currentImbuements.get(type) == 0)
                currentImbuements.remove(type);

            MythicItemHandler.removeImbue(itemStack, type);
        }

        return burnAmount;
    }

    public static void init() {
        for (RegistryObject<Item> mythicItem : ModItems.MYTHIC_ITEMS)
            CauldronInteraction.LAVA.map().put(mythicItem.get(), MYTHIC_ITEM_INTERACTION);
    }
}

