package me.boston.mythicarmor.datagen;

import me.boston.mythicarmor.MythicArmor;
import me.boston.mythicarmor.block.ModBlocks;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.concurrent.CompletableFuture;

public class ModBlockStateProvider extends BlockStateProvider {
    BlockModelProvider blockModels;

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MythicArmor.MODID, exFileHelper);

        blockModels = new BlockModelProvider(output, MythicArmor.MODID, exFileHelper) {
            @Override public CompletableFuture<?> run(CachedOutput cache) { return CompletableFuture.allOf(); }

            @Override protected void registerModels() {}
        };
    }

    @Override
    protected void registerStatesAndModels() {

        blockWithItem(ModBlocks.MYTHIC_ORE);
        blockWithItem(ModBlocks.MYTHIC_SHARD_BLOCK);
        blockWithItem(ModBlocks.MYTHIC_BLOCK);
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
