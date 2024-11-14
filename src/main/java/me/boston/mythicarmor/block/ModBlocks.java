package me.boston.mythicarmor.block;

import me.boston.mythicarmor.MythicArmor;
import me.boston.mythicarmor.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MythicArmor.MODID);


    public static final RegistryObject<Block> MYTHIC_ORE = registerBlock("mythic_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(5, 600).mapColor(MapColor.COLOR_PINK).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final RegistryObject<Block> MYTHIC_SHARD_BLOCK = registerBlock("mythic_shard_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(10, 600).mapColor(MapColor.COLOR_PINK).requiresCorrectToolForDrops().sound(SoundType.AMETHYST).lightLevel(blockState -> 15)),
            new Item.Properties().fireResistant().rarity(Rarity.RARE));
    public static final RegistryObject<Block> MYTHIC_BLOCK = registerBlock("mythic_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(30, 1200).mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK).lightLevel(blockState -> 3)),
            new Item.Properties().fireResistant().rarity(Rarity.EPIC));

    public static final RegistryObject<Block> IMBUING_STATION = registerBlock("imbuing_station",
            () -> new ImbuingStation(BlockBehaviour.Properties.of().strength(2.5f).mapColor(MapColor.WOOD).sound(SoundType.STONE)),
            new Item.Properties().rarity(Rarity.UNCOMMON));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, Item.Properties itemProperties) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, itemProperties);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        return registerBlock(name, block, new Item.Properties());
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block, Item.Properties properties) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), properties));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
