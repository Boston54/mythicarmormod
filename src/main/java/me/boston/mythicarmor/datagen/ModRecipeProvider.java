package me.boston.mythicarmor.datagen;

import me.boston.mythicarmor.MythicArmor;
import me.boston.mythicarmor.block.ModBlocks;
import me.boston.mythicarmor.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(RecipeOutput pRecipeOutput) {
        // Mythic Ingot
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MYTHIC_INGOT.get())
                .pattern("SSS")
                .pattern("SNS")
                .pattern("SSS")
                .define('S', ModItems.MYTHIC_SHARD.get())
                .define('N', Items.NETHERITE_INGOT)
                .unlockedBy(getHasName(ModItems.MYTHIC_SHARD.get()), has(ModItems.MYTHIC_SHARD.get())).save(pRecipeOutput);
        // Mythic Shard Block
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MYTHIC_SHARD_BLOCK.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItems.MYTHIC_SHARD.get())
                .unlockedBy(getHasName(ModItems.MYTHIC_SHARD.get()), has(ModItems.MYTHIC_SHARD.get())).save(pRecipeOutput);
        // Mythic Shard (from block)
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MYTHIC_SHARD.get(), 9)
                .requires(ModBlocks.MYTHIC_SHARD_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.MYTHIC_SHARD_BLOCK.get()), has(ModBlocks.MYTHIC_SHARD_BLOCK.get())).save(pRecipeOutput);
        // Mythic Shard (smelted from ore)
        oreSmelting(pRecipeOutput, List.of(ModBlocks.MYTHIC_ORE.get()), RecipeCategory.MISC, ModItems.MYTHIC_SHARD.get(), 3, 200,"mythic_shard");
        oreBlasting(pRecipeOutput, List.of(ModBlocks.MYTHIC_ORE.get()), RecipeCategory.MISC, ModItems.MYTHIC_SHARD.get(), 3, 100,"mythic_shard");
        // Imbuing Station
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.IMBUING_STATION.get())
                .pattern("SMS")
                .pattern("SDS")
                .pattern("SSS")
                .define('S', Items.STONE)
                .define('D', Items.DIAMOND_BLOCK)
                .define('M', ModBlocks.MYTHIC_SHARD_BLOCK.get())
                .unlockedBy(getHasName(ModItems.MYTHIC_SHARD.get()), has(ModItems.MYTHIC_SHARD.get())).save(pRecipeOutput);
        // Smithing
        mythicSmithing(pRecipeOutput, Items.NETHERITE_HELMET, ModItems.MYTHIC_HELMET.get());
        mythicSmithing(pRecipeOutput, Items.NETHERITE_CHESTPLATE, ModItems.MYTHIC_CHESTPLATE.get());
        mythicSmithing(pRecipeOutput, Items.NETHERITE_LEGGINGS, ModItems.MYTHIC_LEGGINGS.get());
        mythicSmithing(pRecipeOutput, Items.NETHERITE_BOOTS, ModItems.MYTHIC_BOOTS.get());
        mythicSmithing(pRecipeOutput, Items.NETHERITE_SWORD, ModItems.MYTHIC_SWORD.get());
        mythicSmithing(pRecipeOutput, Items.NETHERITE_AXE, ModItems.MYTHIC_AXE.get());
        mythicSmithing(pRecipeOutput, Items.NETHERITE_PICKAXE, ModItems.MYTHIC_PICKAXE.get());
        mythicSmithing(pRecipeOutput, Items.NETHERITE_SHOVEL, ModItems.MYTHIC_SHOVEL.get());
        mythicSmithing(pRecipeOutput, Items.NETHERITE_HOE, ModItems.MYTHIC_HOE.get());
        mythicSmithing(pRecipeOutput, Items.ELYTRA, ModItems.MYTHIC_ELYTRA.get());
        // Mythic Upgrade
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MYTHIC_UPGRADE.get())
                .pattern("SUS")
                .pattern("SES")
                .pattern("SSS")
                .define('S', ModItems.MYTHIC_SHARD.get())
                .define('U', Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                .define('E', Blocks.END_STONE)
                .unlockedBy(getHasName(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), has(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)).save(pRecipeOutput);
        // Essences
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MAGMA_ESSENCE.get(), 2)
                .requires(Items.GLASS_BOTTLE)
                .requires(Items.MAGMA_CREAM)
                .requires(Items.BLAZE_POWDER)
                .requires(ModItems.MYTHIC_SHARD.get())
                .unlockedBy(getHasName(ModItems.MYTHIC_SHARD.get()), has(ModItems.MYTHIC_SHARD.get())).save(pRecipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.END_ESSENCE.get(), 2)
                .requires(Items.GLASS_BOTTLE)
                .requires(Items.ENDER_EYE)
                .requires(Items.CHORUS_FRUIT)
                .requires(ModItems.MYTHIC_SHARD.get())
                .unlockedBy(getHasName(ModItems.MYTHIC_SHARD.get()), has(ModItems.MYTHIC_SHARD.get())).save(pRecipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.PROSPERITY_ESSENCE.get(), 2)
                .requires(Items.GLASS_BOTTLE)
                .requires(Items.EMERALD)
                .requires(Items.GOLD_INGOT)
                .requires(ModItems.MYTHIC_SHARD.get())
                .unlockedBy(getHasName(ModItems.MYTHIC_SHARD.get()), has(ModItems.MYTHIC_SHARD.get())).save(pRecipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.AMETHYST_ESSENCE.get(), 2)
                .requires(Items.GLASS_BOTTLE)
                .requires(Items.AMETHYST_SHARD)
                .requires(ModItems.MYTHIC_SHARD.get())
                .unlockedBy(getHasName(ModItems.MYTHIC_SHARD.get()), has(ModItems.MYTHIC_SHARD.get())).save(pRecipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.AGILITY_ESSENCE.get(), 2)
                .requires(Items.GLASS_BOTTLE)
                .requires(Items.SUGAR)
                .requires(Items.FEATHER)
                .requires(ModItems.MYTHIC_SHARD.get())
                .unlockedBy(getHasName(ModItems.MYTHIC_SHARD.get()), has(ModItems.MYTHIC_SHARD.get())).save(pRecipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ANCIENT_ESSENCE.get(), 2)
                .requires(Items.GLASS_BOTTLE)
                .requires(Items.ECHO_SHARD)
                .requires(ModItems.MYTHIC_SHARD.get())
                .unlockedBy(getHasName(ModItems.MYTHIC_SHARD.get()), has(ModItems.MYTHIC_SHARD.get())).save(pRecipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SEA_ESSENCE.get(), 2)
                .requires(Items.GLASS_BOTTLE)
                .requires(Items.PRISMARINE_SHARD)
                .requires(Items.PRISMARINE_CRYSTALS)
                .requires(ModItems.MYTHIC_SHARD.get())
                .unlockedBy(getHasName(ModItems.MYTHIC_SHARD.get()), has(ModItems.MYTHIC_SHARD.get())).save(pRecipeOutput);
    }

    protected static void oreSmelting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput recipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.Factory<T> factory,
                                                                       List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput, MythicArmor.MODID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }

    private static void mythicSmithing(RecipeOutput pRecipeOutput, Item pIngredientItem, Item pResultItem) {
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(ModItems.MYTHIC_UPGRADE.get()), Ingredient.of(pIngredientItem), Ingredient.of(ModItems.MYTHIC_INGOT.get()), RecipeCategory.MISC, pResultItem
                )
                .unlocks("has_mythic_ingot", has(ModItems.MYTHIC_INGOT.get()))
                .save(pRecipeOutput, getItemName(pResultItem) + "_smithing");
    }
}
