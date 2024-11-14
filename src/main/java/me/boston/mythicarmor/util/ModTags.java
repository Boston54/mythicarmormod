package me.boston.mythicarmor.util;

import me.boston.mythicarmor.MythicArmor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(MythicArmor.MODID, name));
        }
    }

    public static class Items {

        public static final TagKey<Item> MYTHIC = createTag("mythic");
        public static final TagKey<Item> ESSENCE = createTag("essence");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(MythicArmor.MODID, name));
        }
    }
}
