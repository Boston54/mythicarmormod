package me.boston.mythicarmor.mixin;

import me.boston.mythicarmor.mythic.ImbueType;
import me.boston.mythicarmor.mythic.ImbuementEffectsHandler;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "getAttributeValue", at = @At(value = "RETURN"), cancellable = true)
    private void extraSwimSpeed(Holder<Attribute> pAttribute, CallbackInfoReturnable<Double> cir) {
        if (!pAttribute.get().getDescriptionId().equals("forge.swim_speed")) {
            cir.setReturnValue(cir.getReturnValue());
            return;
        }

        LivingEntity livingEntity = (LivingEntity)((Object)this);
        List<ItemStack> armor = ImbuementEffectsHandler.getEquippedMythicArmorItems(livingEntity);

        if (!armor.isEmpty()) {
            int sea = ImbuementEffectsHandler.getImbuePercentage(armor, ImbueType.SEA);
            if (sea > 0) {
                cir.setReturnValue(cir.getReturnValue() + (sea * ImbuementEffectsHandler.SEA_SWIM_SPEED / 100f));
                return;
            }
        }
        cir.setReturnValue(cir.getReturnValue());
    }
}
