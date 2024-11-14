package me.boston.mythicarmor.mythic.items;

import me.boston.mythicarmor.mythic.ImbuementEffectsHandler;
import me.boston.mythicarmor.mythic.MythicItemHandler;
import me.boston.mythicarmor.mythic.items.interfaces.IMythicArmor;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;

import java.util.List;

public class MythicArmorItem extends ArmorItem implements IMythicArmor {
    public MythicArmorItem(Holder<ArmorMaterial> pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
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
}
