package me.boston.mythicarmor.mixin;

import com.google.common.collect.ImmutableList;
import me.boston.mythicarmor.mythic.ImbueType;
import me.boston.mythicarmor.mythic.ImbuementEffectsHandler;
import me.boston.mythicarmor.mythic.MythicItemHandler;
import me.boston.mythicarmor.mythic.items.interfaces.IMythicWeapon;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "getComponents", at = @At(value = "RETURN"), cancellable = true)
    private void modifyComponents(CallbackInfoReturnable<DataComponentMap> cir) {
        ItemStack stack = (ItemStack)((Object)this);
        DataComponentMap original = cir.getReturnValue();

        if (!(stack.getItem() instanceof IMythicWeapon)) {
            cir.setReturnValue(original);
            return;
        }

        DataComponentMap newDataMap = new DataComponentMap() {

            @Override
            public @Nullable <T> T get(DataComponentType<? extends T> pComponent) {

                if (pComponent != DataComponents.ATTRIBUTE_MODIFIERS) {
                    return original.get(pComponent);
                }

                int agility = MythicItemHandler.getImbuePercentage(stack, ImbueType.AGILITY);
                if (agility > 0) {
                    ItemAttributeModifiers originalModifiers = original.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);

                    ImmutableList.Builder<ItemAttributeModifiers.Entry> builder = ImmutableList.builderWithExpectedSize(originalModifiers.modifiers().size() + 1);

                    for (ItemAttributeModifiers.Entry entry : originalModifiers.modifiers()) {
                        builder.add(entry);
                    }

                    builder.add(new ItemAttributeModifiers.Entry(
                            Attributes.ATTACK_SPEED,
                            new AttributeModifier(
                                    ImbuementEffectsHandler.AGILITY_ATTACK_SPEED_RESOURCE,
                                    agility * ImbuementEffectsHandler.AGILITY_ATTACK_SPEED / 100f,
                                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            EquipmentSlotGroup.MAINHAND
                    ));

                    ItemAttributeModifiers newModifiers = new ItemAttributeModifiers(builder.build(), true);

                    return (T)newModifiers;
                }

                return original.get(pComponent);
            }

            @Override
            public Set<DataComponentType<?>> keySet() {
                int agility = MythicItemHandler.getImbuePercentage(stack, ImbueType.AGILITY);
                if (agility > 0) {
                    original.keySet().add(DataComponents.ATTRIBUTE_MODIFIERS);
                    return original.keySet();
                }

                return original.keySet();
            }
        };

        cir.setReturnValue(newDataMap);
    }
}
