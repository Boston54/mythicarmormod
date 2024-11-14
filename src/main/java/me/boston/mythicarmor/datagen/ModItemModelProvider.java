package me.boston.mythicarmor.datagen;

import me.boston.mythicarmor.MythicArmor;
import me.boston.mythicarmor.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;

public class ModItemModelProvider extends ItemModelProvider {
    private static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
    }

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MythicArmor.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.MYTHIC_SHARD.get());
        basicItem(ModItems.MYTHIC_INGOT.get());

        basicItem(ModItems.MYTHIC_UPGRADE.get());

        overlayedHandheld(ModItems.MYTHIC_SWORD.get());
        overlayedHandheld(ModItems.MYTHIC_AXE.get());
        overlayedHandheld(ModItems.MYTHIC_PICKAXE.get());
        overlayedHandheld(ModItems.MYTHIC_SHOVEL.get());
        overlayedHandheld(ModItems.MYTHIC_HOE.get());

        trimmedArmorItem(ModItems.MYTHIC_HELMET);
        trimmedArmorItem(ModItems.MYTHIC_CHESTPLATE);
        trimmedArmorItem(ModItems.MYTHIC_LEGGINGS);
        trimmedArmorItem(ModItems.MYTHIC_BOOTS);

        basicItem(ModItems.MAGMA_ESSENCE.get());
        basicItem(ModItems.END_ESSENCE.get());
        basicItem(ModItems.PROSPERITY_ESSENCE.get());
        basicItem(ModItems.AMETHYST_ESSENCE.get());
        basicItem(ModItems.AGILITY_ESSENCE.get());
        basicItem(ModItems.ANCIENT_ESSENCE.get());
        basicItem(ModItems.SEA_ESSENCE.get());
    }

    /**
     * Creates an ItemModelBuilder for the given item with the base layer as layer0 and the base overlay as layer1.
     * The overlay is assumed to have the same name as the base layer with '_overlay' appended.
     */
    private ItemModelBuilder overlayedHandheld(Item item) {
        ResourceLocation resource = ForgeRegistries.ITEMS.getKey(item);
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", resource.withPrefix("item/"))
                .texture("layer1", resource.withPrefix("item/").withSuffix("_overlay"));
    }

    private void trimmedArmorItem(RegistryObject<Item> itemRegistryObject) {
        final String MODID = MythicArmor.MODID;

        if(itemRegistryObject.get() instanceof ArmorItem armorItem) {
            trimMaterials.forEach((trimMaterial, value) -> {
                float trimValue = value;

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String armorItemPath = armorItem.toString();
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemResLoc = ResourceLocation.parse(armorItemPath);
                ResourceLocation trimResLoc = ResourceLocation.parse(trimPath); // minecraft namespace
                ResourceLocation trimNameResLoc = ResourceLocation.parse(currentTrimName);

                // This is used for making the ExistingFileHelper acknowledge that this texture exist, so this will
                // avoid an IllegalArgumentException
                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                // Trimmed armorItem files
                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc.getNamespace() + ":item/" + armorItemResLoc.getPath())
                        .texture("layer1", armorItemResLoc.getNamespace() + ":item/" + armorItemResLoc.getPath() + "_overlay")
                        .texture("layer2", trimResLoc);

                // Non-trimmed armorItem file (normal variant)
                this.withExistingParent(itemRegistryObject.getId().getPath(),
                                mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc.getNamespace()  + ":item/" + trimNameResLoc.getPath()))
                        .predicate(mcLoc("trim_type"), trimValue).end()
                        .texture("layer0", ResourceLocation.fromNamespaceAndPath(MODID, "item/" + itemRegistryObject.getId().getPath()))
                        .texture("layer1", ResourceLocation.fromNamespaceAndPath(MODID, "item/" + itemRegistryObject.getId().getPath()).withSuffix("_overlay"));
            });
        }
    }
}
