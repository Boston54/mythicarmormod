package me.boston.mythicarmor.mythic.items;

import me.boston.mythicarmor.mythic.ImbuementEffectsHandler;
import me.boston.mythicarmor.mythic.MythicItemHandler;
import me.boston.mythicarmor.mythic.items.interfaces.IMythicTool;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;

public class MythicHoeItem extends HoeItem implements IMythicTool {

    public MythicHoeItem(Tier pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        MythicItemHandler.appendHoverText(itemStack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        return ImbuementEffectsHandler.getDefaultAttributeModifiers(super.getDefaultAttributeModifiers(stack), stack);
    }
}
