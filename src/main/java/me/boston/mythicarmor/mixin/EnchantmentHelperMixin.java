package me.boston.mythicarmor.mixin;

import me.boston.mythicarmor.mythic.ImbueType;
import me.boston.mythicarmor.mythic.ImbuementEffectsHandler;
import me.boston.mythicarmor.mythic.MythicItemHandler;
import me.boston.mythicarmor.mythic.items.interfaces.IMythicTool;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "getItemEnchantmentLevel", at = @At(value = "RETURN"), cancellable = true)
    private static void getItemEnchantmentLevel(Holder<Enchantment> pEnchantment, ItemStack pStack, CallbackInfoReturnable<Integer> cir) {
        if (pEnchantment.getRegisteredName().equals("minecraft:fortune")) {
            if (pStack.getItem() instanceof IMythicTool) {
                int prosperity = MythicItemHandler.getImbuePercentage(pStack, ImbueType.PROSPERITY);
                if (prosperity > 0) {
                    int totalFortuneIncrease = 0;
                    for (int percentage : ImbuementEffectsHandler.PROSPERITY_FORTUNE_PERCENTAGES) {
                        if (prosperity >= percentage)
                            totalFortuneIncrease++;
                    }
                    cir.setReturnValue(cir.getReturnValue() + totalFortuneIncrease);
                    return;
                }
            }
            cir.setReturnValue(cir.getReturnValue());
        } else {
            cir.setReturnValue(cir.getReturnValue());
        }
    }
}
