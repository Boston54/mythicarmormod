package me.boston.mythicarmor.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.boston.mythicarmor.MythicArmor;
import me.boston.mythicarmor.item.ModItems;
import me.boston.mythicarmor.mythic.MythicItemHandler;
import me.boston.mythicarmor.util.RGB;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MythicElytraArmorStandLayer extends ElytraLayer<ArmorStand, ArmorStandArmorModel> {
    private final ElytraModel<ArmorStand> elytraModel;
    private static final ResourceLocation ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MythicArmor.MODID, "textures/entity/mythic_elytra.png");

    public MythicElytraArmorStandLayer(ArmorStandRenderer armorStandRenderer, EntityModelSet entityModelSet) {
        super(armorStandRenderer, entityModelSet);
        this.elytraModel = new ElytraModel<>(entityModelSet.bakeLayer(ModelLayers.ELYTRA));
    }

    @Override
    public boolean shouldRender(ItemStack stack, ArmorStand entity) {
        return stack.getItem() == ModItems.MYTHIC_ELYTRA.get();
    }

    @Override
    public ResourceLocation getElytraTexture(ItemStack stack, ArmorStand entity) {
        return ELYTRA_TEXTURE;
    }

    @Override
    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, ArmorStand pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        ItemStack itemStack = pLivingEntity.getItemBySlot(EquipmentSlot.CHEST);
        if (shouldRender(itemStack, pLivingEntity)) {
            ResourceLocation resourceLocation = getElytraTexture(itemStack, pLivingEntity);
            pMatrixStack.pushPose();
            pMatrixStack.translate(0.0F, 0.0F, 0.125F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
            RGB color = RGB.of(MythicItemHandler.MythicItemColors.getImbuementColor(itemStack, 0));
            VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(pBuffer, RenderType.armorCutoutNoCull(resourceLocation), itemStack.hasFoil());
            this.elytraModel.renderToBuffer(pMatrixStack, vertexConsumer, pPackedLight, OverlayTexture.NO_OVERLAY, color.toOpaqueInt());
            pMatrixStack.popPose();
        }
    }
}