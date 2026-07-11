package club.gayboi.catears.client.renderer;

import club.gayboi.catears.CatEarsConfig;
import club.gayboi.catears.ModItems;
import club.gayboi.catears.client.CatEarsClientMod;
import club.gayboi.catears.client.model.CatEarsModel;
import club.gayboi.catears.network.SyncEarDataPayload;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;

public class CatEarsLayer extends RenderLayer {
    private final CatEarsModel model;

    public CatEarsLayer(RenderLayerParent<?, ?> renderer, CatEarsModel model) {
        super(renderer);
        this.model = model;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, EntityRenderState renderState, float yRot, float xRot) {
        HumanoidRenderState state = (HumanoidRenderState) renderState;
        Identifier texture = null;

        var headStack = state.headEquipment;
        if (!headStack.isEmpty()) {
            Item item = headStack.getItem();
            if (isCatEars(item)) {
                texture = getCatEarsTexture(item);
            }
        }

        if (texture == null && CatEarsConfig.showEarsLocally) {
            Player player = findRenderStatePlayer(state);
            if (player != null) {
                texture = getClientSideTexture(player);
            }
        }

        if (texture == null) return;

        if (getParentModel() instanceof HumanoidModel<?> parentModel) {
            ModelPart parentHead = parentModel.head;
            ModelPart myHead = this.model.head;
            myHead.setRotation(parentHead.xRot, parentHead.yRot, parentHead.zRot);
            myHead.x = parentHead.x;
            myHead.y = parentHead.y;
            myHead.z = parentHead.z;
        }

        submitNodeCollector.submitModelPart(this.model.head, poseStack, RenderTypes.entityCutoutNoCull(texture), packedLight, LivingEntityRenderer.getOverlayCoords(state, 0.0F), null);
    }

    private Player findRenderStatePlayer(HumanoidRenderState state) {
        if (state.nameTag != null) {
            String name = ChatFormatting.stripFormatting(state.nameTag.getString());
            var world = Minecraft.getInstance().level;
            if (world != null && name != null) {
                for (Player player : world.players()) {
                    if (player.getName().getString().equals(name)) {
                        return player;
                    }
                }
            }
        }
        return null;
    }

    private Identifier getClientSideTexture(Player player) {
        SyncEarDataPayload data = CatEarsClientMod.remoteEarData.get(player.getUUID());
        if (data == null) return null;
        String colorName = data.earColor();
        return Identifier.fromNamespaceAndPath("catears", "textures/models/armor/" + colorName + "_cat_ears.png");
    }

    private boolean isCatEars(Item item) {
        return ModItems.CAT_EARS.containsValue(item);
    }

    private Identifier getCatEarsTexture(Item item) {
        String name = BuiltInRegistries.ITEM.getKey(item).getPath();
        return Identifier.fromNamespaceAndPath("catears", "textures/models/armor/" + name + ".png");
    }
}
