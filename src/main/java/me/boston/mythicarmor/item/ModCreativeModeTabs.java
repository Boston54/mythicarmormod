package me.boston.mythicarmor.item;

import me.boston.mythicarmor.MythicArmor;
import me.boston.mythicarmor.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MythicArmor.MODID);

    public static final RegistryObject<CreativeModeTab> MYTHIC_ARMOR_TAB = CREATIVE_MODE_TABS.register("mythic_armor_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.MYTHIC_SHARD.get()))
                    .title(Component.translatable("creativetab.mythicarmor.mythic_armor"))
                    .displayItems((itemDisplayParameters, output) -> {
                        for (RegistryObject<Block> block : ModBlocks.BLOCKS.getEntries())
                            output.accept(block.get());
                        for (RegistryObject<Item> item : ModItems.ITEMS.getEntries())
                            output.accept(item.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
