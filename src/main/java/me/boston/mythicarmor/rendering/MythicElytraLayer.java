package me.boston.mythicarmor.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.boston.mythicarmor.MythicArmor;
import me.boston.mythicarmor.item.ModItems;
import me.boston.mythicarmor.mythic.MythicItemHandler;
import me.boston.mythicarmor.util.RGB;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MythicElytraLayer extends ElytraLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private final ElytraModel<AbstractClientPlayer> elytraModel;

    private static final ResourceLocation ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MythicArmor.MODID, "textures/entity/mythic_elytra.png");

    public MythicElytraLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> rendererIn, EntityModelSet modelSet) {
        super(rendererIn, modelSet);
        this.elytraModel = new ElytraModel<>(modelSet.bakeLayer(ModelLayers.ELYTRA));
    }

    @Override
    public boolean shouldRender(ItemStack stack, AbstractClientPlayer entity) {
        return stack.getItem() == ModItems.MYTHIC_ELYTRA.get();
    }

    @Override
    public ResourceLocation getElytraTexture(ItemStack stack, AbstractClientPlayer entity) {
        return ELYTRA_TEXTURE;
    }

    @Override
    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, AbstractClientPlayer pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        ItemStack itemStack = pLivingEntity.getItemBySlot(EquipmentSlot.CHEST);
        if (shouldRender(itemStack, pLivingEntity)) {
            ResourceLocation resourceLocation = getElytraTexture(itemStack, pLivingEntity);
//            if (pLivingEntity instanceof AbstractClientPlayer abstractClientPlayer) {
//                if (abstractClientPlayer.isElytraLoaded() && abstractClientPlayer.getElytraTextureLocation() != null) {
//                    resourceLocation = abstractClientPlayer.getElytraTextureLocation();
//                } else if (abstractClientPlayer.isCapeLoaded() && abstractClientPlayer.getCloakTextureLocation() != null && abstractClientPlayer.isModelPartShown(PlayerModelPart.CAPE)) {
//                    resourceLocation = abstractClientPlayer.getCloakTextureLocation();
//                } else {
//                    resourceLocation = getElytraTexture(itemStack, pLivingEntity);
//                }
//            } else {
//                resourceLocation = getElytraTexture(itemStack, pLivingEntity);
//            }

            // todo notes to self: this method IS getting called but the model isn't rendering. the init code in MythicArmor.java is running.

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