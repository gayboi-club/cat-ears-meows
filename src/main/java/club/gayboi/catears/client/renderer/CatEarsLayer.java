package club.gayboi.catears.client.renderer;

import club.gayboi.catears.ModItems;
import club.gayboi.catears.client.model.CatEarsModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;

public class CatEarsLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final CatEarsModel<T> model;

    public CatEarsLayer(RenderLayerParent<T, M> pRenderer, CatEarsModel<T> model) {
        super(pRenderer);
        this.model = model;
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        ItemStack itemstack = pLivingEntity.getItemBySlot(EquipmentSlot.HEAD);
        if (!itemstack.isEmpty() && isCatEars(itemstack.getItem())) {
            ResourceLocation texture = getCatEarsTexture(itemstack.getItem());
            if (texture != null) {
                if (this.getParentModel() instanceof HumanoidModel<?> humanoidModel) {
                    this.model.head.copyFrom(humanoidModel.head);
                }

                // render head only :3
                VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityCutoutNoCull(texture));
                this.model.head.render(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            }
        }
    }

    private boolean isCatEars(Item item) {
        for (var deferredItem : club.gayboi.catears.ModItems.CAT_EARS.values()) {
            if (item == deferredItem.get()) {
                return true;
            }
        }
        return false;
    }

    private ResourceLocation getCatEarsTexture(Item item) {
        String name = BuiltInRegistries.ITEM.getKey(item).getPath();
        return ResourceLocation.fromNamespaceAndPath("catears", "textures/models/armor/" + name + ".png");
    }
}
