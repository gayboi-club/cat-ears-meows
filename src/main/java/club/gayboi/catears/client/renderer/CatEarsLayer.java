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
        Player player = findRenderStatePlayer(state);

        Identifier texture = getTexture(state, player);
        if (texture == null) return;

        if (getParentModel() instanceof HumanoidModel<?> parentModel) {
            ModelPart parentHead = parentModel.head;
            ModelPart myHead = this.model.head;
            myHead.setRotation(parentHead.xRot, parentHead.yRot, parentHead.zRot);
            myHead.x = parentHead.x;
            myHead.y = parentHead.y;
            myHead.z = parentHead.z;
        }

        submitNodeCollector.submitModelPart(this.model.head, poseStack, RenderTypes.entityCutout(texture), packedLight, LivingEntityRenderer.getOverlayCoords(state, 0.0F), null);
    }

    private Identifier getTexture(HumanoidRenderState state, Player knownPlayer) {
        if (!state.headEquipment.isEmpty()) {
            Item item = state.headEquipment.getItem();
            if (isCatEars(item)) {
                return getCatEarsTexture(item);
            }
        }

        if (knownPlayer == null) return null;

        Minecraft client = Minecraft.getInstance();
        Player localPlayer = client.player;
        if (localPlayer == null) return null;

        boolean isLocal = knownPlayer.getUUID().equals(localPlayer.getUUID());

        if (isLocal) {
            if (CatEarsConfig.showEarsLocally) {
                return getEarTextureForColor(CatEarsConfig.earColor);
            }
            return null;
        }

        SyncEarDataPayload data = CatEarsClientMod.remoteEarData.get(knownPlayer.getUUID());
        if (data != null && data.showEars()) {
            return getEarTextureForColor(data.earColor());
        }

        return null;
    }

    private Player findRenderStatePlayer(HumanoidRenderState state) {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null || client.player == null) return null;

        if (state.nameTag != null) {
            String nameTag = ChatFormatting.stripFormatting(state.nameTag.getString());
            if (nameTag != null) {
                String localName = ChatFormatting.stripFormatting(client.player.getDisplayName().getString());
                if (localName != null && localName.equals(nameTag)) {
                    return client.player;
                }
                for (Player player : client.level.players()) {
                    String pName = ChatFormatting.stripFormatting(player.getDisplayName().getString());
                    if (pName != null && pName.equals(nameTag)) {
                        return player;
                    }
                }
            }
        }

        if (state.entityType == EntityType.PLAYER) {
            return client.player;
        }

        return null;
    }

    private boolean isCatEars(Item item) {
        return ModItems.CAT_EARS.containsValue(item);
    }

    private Identifier getCatEarsTexture(Item item) {
        String name = BuiltInRegistries.ITEM.getKey(item).getPath();
        return Identifier.fromNamespaceAndPath("catears", "textures/models/armor/" + name + ".png");
    }

    private Identifier getEarTextureForColor(String colorName) {
        return Identifier.fromNamespaceAndPath("catears", "textures/models/armor/" + colorName + "_cat_ears.png");
    }
}
