package me.boston.mythicarmor.datagen;

import me.boston.mythicarmor.MythicArmor;
import me.boston.mythicarmor.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper pExistingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, MythicArmor.MODID, pExistingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        // Dyeable mythic items
        tag(ItemTags.DYEABLE).add(
                ModItems.MYTHIC_HELMET.get(),
                ModItems.MYTHIC_CHESTPLATE.get(),
                ModItems.MYTHIC_LEGGINGS.get(),
                ModItems.MYTHIC_BOOTS.get()
        );

        tag(ItemTags.TRIMMABLE_ARMOR).add(
                ModItems.MYTHIC_HELMET.get(),
                ModItems.MYTHIC_CHESTPLATE.get(),
                ModItems.MYTHIC_LEGGINGS.get(),
                ModItems.MYTHIC_BOOTS.get()
        );

        tag(ItemTags.TRIM_MATERIALS).add(
                ModItems.MYTHIC_INGOT.get()
        );

        tag(ItemTags.SWORDS).add(ModItems.MYTHIC_SWORD.get());
        tag(ItemTags.AXES).add(ModItems.MYTHIC_AXE.get());
        tag(ItemTags.PICKAXES).add(ModItems.MYTHIC_PICKAXE.get());
        tag(ItemTags.SHOVELS).add(ModItems.MYTHIC_SHOVEL.get());
        tag(ItemTags.HOES).add(ModItems.MYTHIC_HOE.get());

        tag(ItemTags.HEAD_ARMOR).add(ModItems.MYTHIC_HELMET.get());
        tag(ItemTags.CHEST_ARMOR).add(ModItems.MYTHIC_CHESTPLATE.get());
        tag(ItemTags.CHEST_ARMOR).add(ModItems.MYTHIC_ELYTRA.get());
        tag(ItemTags.LEG_ARMOR).add(ModItems.MYTHIC_LEGGINGS.get());
        tag(ItemTags.FOOT_ARMOR).add(ModItems.MYTHIC_BOOTS.get());
    }
}
