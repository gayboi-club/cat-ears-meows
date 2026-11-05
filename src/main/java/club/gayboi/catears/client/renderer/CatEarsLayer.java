package club.gayboi.catears.client.renderer;

import java.util.IdentityHashMap;
import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import club.gayboi.catears.CatEarsMod;
import club.gayboi.catears.ModItems;
import club.gayboi.catears.client.model.CatEarsModel;

public class CatEarsLayer<S extends HumanoidRenderState, M extends HumanoidModel<S>> extends RenderLayer<S, M> {
    private final CatEarsModel model;
    private final Map<Item, Identifier> textures = new IdentityHashMap<>();

    public CatEarsLayer(RenderLayerParent<S, M> renderer, CatEarsModel model) {
        super(renderer);
        this.model = model;
        ModItems.CAT_EARS.forEach((color, item) -> textures.put(item,
                Identifier.fromNamespaceAndPath(CatEarsMod.MOD_ID,
                        "textures/models/armor/" + color.getName() + "_cat_ears.png")));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int packedLight, S state, float yRot, float xRot) {
        if (state.headEquipment.isEmpty()) return;

        Identifier texture = textures.get(state.headEquipment.getItem());
        if (texture == null) return;

        M parent = getParentModel();
        if (!parent.head.visible) return;

        poseStack.pushPose();
        parent.root().translateAndRotate(poseStack);
        parent.translateToHead(poseStack);

        RenderType renderType = state.headEquipment.hasFoil()
                ? RenderTypes.armorCutoutNoCullGlint(texture)
                : RenderTypes.entityCutout(texture);
        int overlay = LivingEntityRenderer.getOverlayCoords(state, 0.0F);
        collector.submitModel(this.model, state, poseStack, renderType,
                packedLight, overlay, -1, null, state.outlineColor);

        poseStack.popPose();
    }
}
