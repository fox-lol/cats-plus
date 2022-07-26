package xyz.foxkin.catsplus.mixin.commonloader.client.entitypickup;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BipedEntityModel.class)
abstract class BipedEntityModelMixin extends AnimalModelMixin {

    @Shadow
    @Final
    public ModelPart leftArm;
    @Shadow
    @Final
    public ModelPart rightArm;
}
