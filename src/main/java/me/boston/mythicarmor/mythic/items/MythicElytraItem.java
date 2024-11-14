package me.boston.mythicarmor.mythic.items;

import me.boston.mythicarmor.mythic.ImbuementEffectsHandler;
import me.boston.mythicarmor.mythic.MythicItemHandler;
import me.boston.mythicarmor.mythic.items.interfaces.IMythicArmor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;

import java.util.List;

public class MythicElytraItem extends ElytraItem implements IMythicArmor {
    public MythicElytraItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        MythicItemHandler.appendHoverText(itemStack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        MythicItemHandler.updateColor(pStack);
        ImbuementEffectsHandler.attemptArmorTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        return ImbuementEffectsHandler.getDefaultAttributeModifiers(super.getDefaultAttributeModifiers(stack), stack);
    }

    public static boolean isUsable(ItemStack stack) {
        return stack.getDamageValue() < stack.getMaxDamage() - 1;
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return isUsable(stack);
    }
}
