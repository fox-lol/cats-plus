package xyz.foxkin.catsplus.mixin.commonloader.commonside.entitypickup;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import xyz.foxkin.catsplus.commonside.access.entitypickup.Matrix4fAccess;

@SuppressWarnings("unused")
@Mixin(Matrix4f.class)
abstract class Matrix4fMixin implements Matrix4fAccess {

    @Shadow
    protected float a00;
    @Shadow
    protected float a01;
    @Shadow
    protected float a02;
    @Shadow
    protected float a03;

    @Shadow
    protected float a10;
    @Shadow
    protected float a11;
    @Shadow
    protected float a12;
    @Shadow
    protected float a13;

    @Shadow
    protected float a20;
    @Shadow
    protected float a21;
    @Shadow
    protected float a22;
    @Shadow
    protected float a23;

    /*
    Matrix layout:
    a00 a01 a02 a03
    a10 a11 a12 a13
    a20 a21 a22 a23
    a30 a31 a32 a33
     */

    @Unique
    @Override
    public Vec3f catsPlus$getTranslation() {
        return new Vec3f(a03, a13, a23);
    }

    @Unique
    @Override
    public Vec3f catsPlus$getEulerAngles() {
        float xRotation = (float) MathHelper.atan2(a21, a22);
        float yRotation = (float) MathHelper.atan2(-a20, MathHelper.sqrt(a21 * a21 + a22 * a22));
        float zRotation = (float) MathHelper.atan2(a10, a00);
        return new Vec3f(xRotation, yRotation, zRotation);
    }

    @Unique
    @Override
    public Vec3f catsPlus$getScale() {
        float xScale = MathHelper.sqrt(a00 * a00 + a10 * a10 + a20 * a20);
        float yScale = MathHelper.sqrt(a01 * a01 + a11 * a11 + a21 * a21);
        float zScale = MathHelper.sqrt(a02 * a02 + a12 * a12 + a22 * a22);
        return new Vec3f(xScale, yScale, zScale);
    }
}
