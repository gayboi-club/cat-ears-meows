package club.gayboi.catears.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class CatEarsModel extends EntityModel<EntityRenderState> {
    public CatEarsModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition head = meshdefinition.getRoot().addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);

        PartDefinition leftEar = head.addOrReplaceChild("left_ear", CubeListBuilder.create(),
            PartPose.offsetAndRotation(3.0F, -8.0F, -0.5F, 0.0F, 0.0F, 0.25F));

        leftEar.addOrReplaceChild("left_back", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, -0.5F, 4.0F, 3.0F, 1.0F), PartPose.ZERO);
        leftEar.addOrReplaceChild("left_inner_rim", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -1.5F, 1.0F, 2.0F, 1.0F), PartPose.ZERO);
        leftEar.addOrReplaceChild("left_outer_rim", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, -2.0F, -1.5F, 1.0F, 2.0F, 1.0F), PartPose.ZERO);
        leftEar.addOrReplaceChild("left_top_peak", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -4.0F, -1.5F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
        leftEar.addOrReplaceChild("left_tip", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -5.0F, -1.0F, 1.0F, 1.0F, 1.0F), PartPose.ZERO);

        leftEar.addOrReplaceChild("left_inner_pink", CubeListBuilder.create().texOffs(16, 0).addBox(-1.0F, -2.5F, -1.0F, 2.0F, 2.5F, 1.0F), PartPose.ZERO);

        leftEar.addOrReplaceChild("left_fluff_1", CubeListBuilder.create().texOffs(0, 0).addBox(1.5F, -1.5F, -1.0F, 2.0F, 1.5F, 1.0F), PartPose.offsetAndRotation(0, 0, 0, 0, 0, -0.3F));

        PartDefinition leftRibbonRoot = leftEar.addOrReplaceChild("left_ribbon_root", CubeListBuilder.create().texOffs(48, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(1.5F, -0.5F, -1.0F, 0.1F, 0.0F, 0.1F));
        leftRibbonRoot.addOrReplaceChild("left_ribbon_tail", CubeListBuilder.create().texOffs(48, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.2F, 0.0F, 0.0F));

        leftEar.addOrReplaceChild("left_bell", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offset(1.5F, 1.0F, -1.0F));

        PartDefinition rightEar = head.addOrReplaceChild("right_ear", CubeListBuilder.create(),
            PartPose.offsetAndRotation(-3.0F, -8.0F, -0.5F, 0.0F, 0.0F, -0.25F));

        rightEar.addOrReplaceChild("right_back", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, -0.5F, 4.0F, 3.0F, 1.0F), PartPose.ZERO);
        rightEar.addOrReplaceChild("right_outer_rim", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -1.5F, 1.0F, 2.0F, 1.0F), PartPose.ZERO);
        rightEar.addOrReplaceChild("right_inner_rim", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, -2.0F, -1.5F, 1.0F, 2.0F, 1.0F), PartPose.ZERO);
        rightEar.addOrReplaceChild("right_top_peak", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -4.0F, -1.5F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
        rightEar.addOrReplaceChild("right_tip", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -5.0F, -1.0F, 1.0F, 1.0F, 1.0F), PartPose.ZERO);

        rightEar.addOrReplaceChild("right_inner_pink", CubeListBuilder.create().texOffs(16, 0).addBox(-1.0F, -2.5F, -1.0F, 2.0F, 2.5F, 1.0F), PartPose.ZERO);

        rightEar.addOrReplaceChild("right_fluff_1", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -1.5F, -1.0F, 2.0F, 1.5F, 1.0F), PartPose.offsetAndRotation(0, 0, 0, 0, 0, 0.3F));

        PartDefinition rightRibbonRoot = rightEar.addOrReplaceChild("right_ribbon_root", CubeListBuilder.create().texOffs(48, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(-1.5F, -0.5F, -1.0F, 0.1F, 0.0F, -0.1F));
        rightRibbonRoot.addOrReplaceChild("right_ribbon_tail", CubeListBuilder.create().texOffs(48, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.2F, 0.0F, 0.0F));

        rightEar.addOrReplaceChild("right_bell", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offset(-1.5F, 1.0F, -1.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }
}
