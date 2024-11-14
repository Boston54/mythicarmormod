package me.boston.mythicarmor.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class ModToolTiers {
    public static final Tier MYTHIC = new ForgeTier(2500, 9, 4, 20,
            BlockTags.NEEDS_DIAMOND_TOOL, () -> Ingredient.of(ModItems.MYTHIC_INGOT.get()),
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL);
}
