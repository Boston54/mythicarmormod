package me.boston.mythicarmor.item;

import me.boston.mythicarmor.MythicArmor;
import me.boston.mythicarmor.mythic.ImbueType;
import me.boston.mythicarmor.mythic.items.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MythicArmor.MODID);


    public static final RegistryObject<Item> MYTHIC_SHARD = ITEMS.register("mythic_shard",
            () -> new Item(new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> MYTHIC_INGOT = ITEMS.register("mythic_ingot",
            () -> new Item(new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));

    private static final List<ResourceLocation> EMPTY_SLOTS = List.of(ResourceLocation.withDefaultNamespace("item/empty_armor_slot_helmet"), ResourceLocation.withDefaultNamespace("item/empty_armor_slot_chestplate"),
            ResourceLocation.withDefaultNamespace("item/empty_armor_slot_leggings"), ResourceLocation.withDefaultNamespace("item/empty_armor_slot_boots"), ResourceLocation.withDefaultNamespace("item/empty_slot_axe"),
            ResourceLocation.withDefaultNamespace("item/empty_slot_sword"), ResourceLocation.withDefaultNamespace("item/empty_slot_shovel"), ResourceLocation.withDefaultNamespace("item/empty_slot_pickaxe"));
    public static final RegistryObject<Item> MYTHIC_UPGRADE = ITEMS.register("mythic_upgrade",
            () -> new SmithingTemplateItem(Component.literal("§8Netherite Equipment, Elytra"), Component.literal("§8Mythic Ingot"),
                    Component.literal("§8Mythic Upgrade"), Component.literal("Add Netherite Armor, Weapon, or Tool, or Elytra"),
                    Component.literal("Add Mythic Ingot"), EMPTY_SLOTS, List.of(ResourceLocation.withDefaultNamespace("item/empty_slot_ingot"))));

    public static final RegistryObject<Item> MYTHIC_SWORD = ITEMS.register("mythic_sword",
            () -> new MythicSwordItem(ModToolTiers.MYTHIC, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModToolTiers.MYTHIC, 3, -2.4f))
                    .fireResistant().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> MYTHIC_AXE = ITEMS.register("mythic_axe",
            () -> new MythicAxeItem(ModToolTiers.MYTHIC, new Item.Properties()
                    .attributes(AxeItem.createAttributes(ModToolTiers.MYTHIC, 5, -3f))
                    .fireResistant().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> MYTHIC_PICKAXE = ITEMS.register("mythic_pickaxe",
            () -> new MythicPickaxeItem(ModToolTiers.MYTHIC, new Item.Properties()
                    .attributes(PickaxeItem.createAttributes(ModToolTiers.MYTHIC, 1, -2.8f))
                    .fireResistant().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> MYTHIC_SHOVEL = ITEMS.register("mythic_shovel",
            () -> new MythicShovelItem(ModToolTiers.MYTHIC, new Item.Properties()
                    .attributes(ShovelItem.createAttributes(ModToolTiers.MYTHIC, 1.5f, -3.0f))
                    .fireResistant().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> MYTHIC_HOE = ITEMS.register("mythic_hoe",
            () -> new MythicHoeItem(ModToolTiers.MYTHIC, new Item.Properties()
                    .attributes(HoeItem.createAttributes(ModToolTiers.MYTHIC, -4f, 0f))
                    .fireResistant().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> MAGMA_ESSENCE = ITEMS.register("magma_essence",
            () -> new EssenceItem(new Item.Properties().rarity(Rarity.UNCOMMON), ImbueType.MAGMA));
    public static final RegistryObject<Item> END_ESSENCE = ITEMS.register("end_essence",
            () -> new EssenceItem(new Item.Properties().rarity(Rarity.UNCOMMON), ImbueType.END));
    public static final RegistryObject<Item> PROSPERITY_ESSENCE = ITEMS.register("prosperity_essence",
            () -> new EssenceItem(new Item.Properties().rarity(Rarity.UNCOMMON), ImbueType.PROSPERITY));
    public static final RegistryObject<Item> ANCIENT_ESSENCE = ITEMS.register("ancient_essence",
            () -> new EssenceItem(new Item.Properties().rarity(Rarity.UNCOMMON), ImbueType.ANCIENT));
    public static final RegistryObject<Item> AGILITY_ESSENCE = ITEMS.register("agility_essence",
            () -> new EssenceItem(new Item.Properties().rarity(Rarity.UNCOMMON), ImbueType.AGILITY));
    public static final RegistryObject<Item> AMETHYST_ESSENCE = ITEMS.register("amethyst_essence",
            () -> new EssenceItem(new Item.Properties().rarity(Rarity.UNCOMMON), ImbueType.AMETHYST));
    public static final RegistryObject<Item> SEA_ESSENCE = ITEMS.register("sea_essence",
            () -> new EssenceItem(new Item.Properties().rarity(Rarity.UNCOMMON), ImbueType.SEA));


    public static final RegistryObject<Item> MYTHIC_HELMET = ITEMS.register("mythic_helmet",
            () -> new MythicArmorItem(ModArmorMaterials.MYTHIC, ArmorItem.Type.HELMET, new Item.Properties()
                    .durability(ArmorItem.Type.HELMET.getDurability(45))
                    .fireResistant().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> MYTHIC_CHESTPLATE = ITEMS.register("mythic_chestplate",
            () -> new MythicArmorItem(ModArmorMaterials.MYTHIC, ArmorItem.Type.CHESTPLATE, new Item.Properties()
                    .durability(ArmorItem.Type.CHESTPLATE.getDurability(45))
                    .fireResistant().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> MYTHIC_LEGGINGS = ITEMS.register("mythic_leggings",
            () -> new MythicArmorItem(ModArmorMaterials.MYTHIC, ArmorItem.Type.LEGGINGS, new Item.Properties()
                    .durability(ArmorItem.Type.LEGGINGS.getDurability(45))
                    .fireResistant().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> MYTHIC_BOOTS = ITEMS.register("mythic_boots",
            () -> new MythicArmorItem(ModArmorMaterials.MYTHIC, ArmorItem.Type.BOOTS, new Item.Properties()
                    .durability(ArmorItem.Type.BOOTS.getDurability(45))
                    .fireResistant().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> MYTHIC_ELYTRA = ITEMS.register("mythic_elytra",
            () -> new MythicElytraItem(new Item.Properties().fireResistant().rarity(Rarity.RARE).durability(700)));


    public static final RegistryObject<Item>[] MYTHIC_ITEMS = new RegistryObject[] {
            MYTHIC_SWORD, MYTHIC_AXE, MYTHIC_PICKAXE, MYTHIC_SHOVEL, MYTHIC_HOE,
            MYTHIC_HELMET, MYTHIC_CHESTPLATE, MYTHIC_LEGGINGS, MYTHIC_BOOTS,
            MYTHIC_ELYTRA
    };

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
