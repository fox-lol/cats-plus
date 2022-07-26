package xyz.foxkin.catsplus.mixin.commonloader.client.entitypickup;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import xyz.foxkin.catsplus.commonside.access.entitypickup.EntityContainer;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@SuppressWarnings("unused")
@Mixin(PlayerEntityModel.class)
abstract class PlayerEntityModelMixin extends BipedEntityModelMixin implements EntityContainer<PlayerEntity> {

    @Shadow
    @Final
    public ModelPart rightSleeve;
    @Shadow
    @Final
    public ModelPart leftSleeve;

    @Unique
    @Nullable
    private PlayerEntity catsPlus$player;

    /**
     * Prevents rendering of the player's arms so that a custom model can be rendered instead.
     */
    @Override
    protected Iterable<ModelPart> catsPlus$removeVanillaArms(Iterable<ModelPart> bodyParts, MatrixStack matrices, VertexConsumer vertices, int light) {
        if (catsPlus$player != null) {
            PlayerEntityAccess playerAccess = (PlayerEntityAccess) catsPlus$player;
            Optional<Entity> entityOptional = playerAccess.catsPlus$getHeldEntity();
            if (entityOptional.isPresent()) {
                return StreamSupport.stream(bodyParts.spliterator(), false)
                        .filter(modelPart -> !Objects.equals(modelPart, rightArm)
                                && !Objects.equals(modelPart, leftArm)
                                && !Objects.equals(modelPart, rightSleeve)
                                && !Objects.equals(modelPart, leftSleeve)
                        )
                        .collect(ImmutableList.toImmutableList());
            }
        }
        return super.catsPlus$removeVanillaArms(bodyParts, matrices, vertices, light);
    }

    @Override
    public void setEntity(PlayerEntity entity) {
        catsPlus$player = entity;
    }
}
