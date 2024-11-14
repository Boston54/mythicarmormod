package me.boston.mythicarmor.datagen;

import me.boston.mythicarmor.MythicArmor;
import me.boston.mythicarmor.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MythicArmor.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.MYTHIC_ORE.get())
                .add(ModBlocks.MYTHIC_SHARD_BLOCK.get())
                .add(ModBlocks.MYTHIC_BLOCK.get());

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.MYTHIC_ORE.get())
                .add(ModBlocks.MYTHIC_SHARD_BLOCK.get())
                .add(ModBlocks.MYTHIC_BLOCK.get());
    }
}
