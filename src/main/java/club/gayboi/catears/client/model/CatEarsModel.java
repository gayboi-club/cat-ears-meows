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
    public final ModelPart head;

    public CatEarsModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
    }

    @Override
    public void setupAnim(EntityRenderState state) {
        // head pose copied from parent model at render time :3
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition root = meshdefinition.getRoot();

        // empty humanoid parts :3
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);

        // fluffy anime cat ears :3
        PartDefinition leftEar = head.addOrReplaceChild("left_ear", CubeListBuilder.create(),
            PartPose.offsetAndRotation(3.0F, -8.0F, -0.5F, 0.0F, 0.0F, 0.25F));

        // rims (outer fur) :3
        leftEar.addOrReplaceChild("left_back", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, -0.5F, 4.0F, 3.0F, 1.0F), PartPose.ZERO);
        leftEar.addOrReplaceChild("left_inner_rim", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -1.5F, 1.0F, 2.0F, 1.0F), PartPose.ZERO);
        leftEar.addOrReplaceChild("left_outer_rim", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, -2.0F, -1.5F, 1.0F, 2.0F, 1.0F), PartPose.ZERO);
        leftEar.addOrReplaceChild("left_top_peak", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -4.0F, -1.5F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
        leftEar.addOrReplaceChild("left_tip", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -5.0F, -1.0F, 1.0F, 1.0F, 1.0F), PartPose.ZERO);

        // pink inner ear :3
        leftEar.addOrReplaceChild("left_inner_pink", CubeListBuilder.create().texOffs(16, 0).addBox(-1.0F, -2.5F, -1.0F, 2.0F, 2.5F, 1.0F), PartPose.ZERO);

        // fluff tufts :3
        leftEar.addOrReplaceChild("left_fluff_1", CubeListBuilder.create().texOffs(0, 0).addBox(1.5F, -1.5F, -1.0F, 2.0F, 1.5F, 1.0F), PartPose.offsetAndRotation(0, 0, 0, 0, 0, -0.3F));

        // ribbon (uv:48,0) :3
        PartDefinition leftRibbonRoot = leftEar.addOrReplaceChild("left_ribbon_root", CubeListBuilder.create().texOffs(48, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(1.5F, -0.5F, -1.0F, 0.1F, 0.0F, 0.1F));
        leftRibbonRoot.addOrReplaceChild("left_ribbon_tail", CubeListBuilder.create().texOffs(48, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.2F, 0.0F, 0.0F));

        // bell (uv:0,16) :3
        leftEar.addOrReplaceChild("left_bell", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offset(1.5F, 1.0F, -1.0F));

        // right ear :3
        PartDefinition rightEar = head.addOrReplaceChild("right_ear", CubeListBuilder.create(),
            PartPose.offsetAndRotation(-3.0F, -8.0F, -0.5F, 0.0F, 0.0F, -0.25F));

        // rims (outer fur) :3
        rightEar.addOrReplaceChild("right_back", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, -0.5F, 4.0F, 3.0F, 1.0F), PartPose.ZERO);
        rightEar.addOrReplaceChild("right_outer_rim", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -1.5F, 1.0F, 2.0F, 1.0F), PartPose.ZERO);
        rightEar.addOrReplaceChild("right_inner_rim", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, -2.0F, -1.5F, 1.0F, 2.0F, 1.0F), PartPose.ZERO);
        rightEar.addOrReplaceChild("right_top_peak", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -4.0F, -1.5F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
        rightEar.addOrReplaceChild("right_tip", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -5.0F, -1.0F, 1.0F, 1.0F, 1.0F), PartPose.ZERO);

        // pink inner ear :3
        rightEar.addOrReplaceChild("right_inner_pink", CubeListBuilder.create().texOffs(16, 0).addBox(-1.0F, -2.5F, -1.0F, 2.0F, 2.5F, 1.0F), PartPose.ZERO);

        // fluff tufts :3
        rightEar.addOrReplaceChild("right_fluff_1", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -1.5F, -1.0F, 2.0F, 1.5F, 1.0F), PartPose.offsetAndRotation(0, 0, 0, 0, 0, 0.3F));

        // ribbon (uv:48,0) :3
        PartDefinition rightRibbonRoot = rightEar.addOrReplaceChild("right_ribbon_root", CubeListBuilder.create().texOffs(48, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(-1.5F, -0.5F, -1.0F, 0.1F, 0.0F, -0.1F));
        rightRibbonRoot.addOrReplaceChild("right_ribbon_tail", CubeListBuilder.create().texOffs(48, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.2F, 0.0F, 0.0F));

        // bell (uv:0,16) :3
        rightEar.addOrReplaceChild("right_bell", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offset(-1.5F, 1.0F, -1.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }
}
