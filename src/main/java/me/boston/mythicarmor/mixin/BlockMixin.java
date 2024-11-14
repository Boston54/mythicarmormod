package me.boston.mythicarmor.mixin;

import me.boston.mythicarmor.mythic.ImbueType;
import me.boston.mythicarmor.mythic.ImbuementEffectsHandler;
import me.boston.mythicarmor.mythic.MythicItemHandler;
import me.boston.mythicarmor.mythic.items.interfaces.IMythicTool;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Inject(
            method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;",
            at = @At(value = "RETURN"),
            cancellable = true
    )
    private static void smeltDrops(BlockState state, ServerLevel level, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack tool, CallbackInfoReturnable<List<ItemStack>> cir) {
        List<ItemStack> originalDrops = cir.getReturnValue();
        if (!(tool.getItem() instanceof IMythicTool)) {
            cir.setReturnValue(originalDrops);
            return;
        }

        if (ImbuementEffectsHandler.hasSilkTouch(tool, level)) {
            cir.setReturnValue(originalDrops);
            return;
        }

        int magma = MythicItemHandler.getImbuePercentage(tool, ImbueType.MAGMA);
        if (magma > 0) {
            List<ItemStack> drops = new ArrayList<>();
            float chance = magma * ImbuementEffectsHandler.MAGMA_AUTO_SMELT_CHANCE;
            for (ItemStack drop : originalDrops) {
                Optional<RecipeHolder<SmeltingRecipe>> recipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(drop), level);
                if (recipe.isPresent()) {
                    Item smeltedItem = recipe.get().value().getResultItem(level.registryAccess()).copy().getItem();
                    int totalSmelted = 0;
                    int totalUnsmelted = 0;
                    for (int i = 0; i < drop.getCount(); i++) {
                        if (ImbuementEffectsHandler.getProc(chance)) {
                            totalSmelted++;
                        } else {
                            totalUnsmelted++;
                        }
                    }
                    drops.add(new ItemStack(smeltedItem, totalSmelted));
                    drop.setCount(totalUnsmelted);
                    drops.add(drop);
                } else {
                    drops.add(drop);
                }
            }

            cir.setReturnValue(drops);
        } else {
            cir.setReturnValue(originalDrops);
        }
    }
}
