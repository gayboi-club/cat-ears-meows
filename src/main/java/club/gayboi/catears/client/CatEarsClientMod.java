package club.gayboi.catears.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;

import club.gayboi.catears.CatEarsMod;
import club.gayboi.catears.client.model.CatEarsModel;
import club.gayboi.catears.client.renderer.CatEarsLayer;

public class CatEarsClientMod implements ClientModInitializer {
    public static final ModelLayerLocation CAT_EARS_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(CatEarsMod.MOD_ID, "cat_ears"), "main");

    @Override
    public void onInitializeClient() {
        ModelLayerRegistry.registerModelLayer(CAT_EARS_LAYER, CatEarsModel::createBodyLayer);

        LivingEntityRenderLayerRegistrationCallback.EVENT.register((entityType, renderer, helper, context) -> {
            if (renderer.getModel() instanceof HumanoidModel<?>) {
                CatEarsModel model = new CatEarsModel(context.bakeLayer(CAT_EARS_LAYER));
                helper.register(CatEarsClientMod.<HumanoidRenderState, HumanoidModel<HumanoidRenderState>>createLayer(renderer, model));
            }
        });
    }

    @SuppressWarnings("unchecked")
    private static <S extends HumanoidRenderState, M extends HumanoidModel<S>> CatEarsLayer<S, M> createLayer(
            LivingEntityRenderer<?, ?, ?> renderer, CatEarsModel model) {
        return new CatEarsLayer<>((RenderLayerParent<S, M>) renderer, model);
    }
}
