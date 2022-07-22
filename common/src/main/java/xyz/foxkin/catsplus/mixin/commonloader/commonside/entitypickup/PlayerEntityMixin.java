package xyz.foxkin.catsplus.mixin.commonloader.commonside.entitypickup;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.foxkin.catsplus.commonside.access.entitypickup.EntityAccess;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;

import java.util.Optional;

@SuppressWarnings("WrongEntityDataParameterClass")
@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityAccess {

    @Unique
    private static final TrackedData<NbtCompound> CATS_PLUS$HELD_ENTITY_TRACKER_KEY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);
    @Unique
    private static final String CATS_PLUS$HELD_ENTITY_NBT_KEY = "catsplus:heldEntity";

    @SuppressWarnings("unused")
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    private void catsPlus$trackCustomData(CallbackInfo ci) {
        dataTracker.startTracking(CATS_PLUS$HELD_ENTITY_TRACKER_KEY, new NbtCompound());
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void catsPlus$dropHeldEntityIfItemEquipped(CallbackInfo ci) {
        if (!getMainHandStack().isEmpty() || !getOffHandStack().isEmpty()) {
            catsPlus$dropHeldEntity(getPos());
        }
    }

    /**
     * Writes information about the player's held entity to NBT.
     */
    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void catsPlus$writeHeldEntity(NbtCompound nbt, CallbackInfo ci) {
        if (catsPlus$isHoldingEntity()) {
            nbt.put(CATS_PLUS$HELD_ENTITY_NBT_KEY, catsPlus$getHeldEntityNbt());
        }
    }

    /**
     * Reads information about the player's held entity from NBT.
     */
    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void catsPlus$readHeldEntity(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains(CATS_PLUS$HELD_ENTITY_NBT_KEY, NbtElement.COMPOUND_TYPE)) {
            NbtCompound entityNbt = nbt.getCompound(CATS_PLUS$HELD_ENTITY_NBT_KEY);
            catsPlus$setHeldEntityNbt(entityNbt);
        }
    }

    @Override
    public NbtCompound catsPlus$getHeldEntityNbt() {
        return dataTracker.get(CATS_PLUS$HELD_ENTITY_TRACKER_KEY);
    }

    @Override
    public Optional<Entity> catsPlus$getHeldEntity() {
        return EntityType.getEntityFromNbt(catsPlus$getHeldEntityNbt(), getWorld());
    }

    @Override
    public boolean catsPlus$isHoldingEntity() {
        return !catsPlus$getHeldEntityNbt().isEmpty();
    }

    @Override
    public void catsPlus$setHeldEntityNbt(NbtCompound entityNbt) {
        dataTracker.set(CATS_PLUS$HELD_ENTITY_TRACKER_KEY, entityNbt);
    }

    @Override
    public void catsPlus$setHeldEntity(Entity entity) {
        NbtCompound entityNbt = new NbtCompound();
        Identifier entityId = EntityType.getId(entity.getType());
        entityNbt.putString("id", entityId.toString());
        entity.writeNbt(entityNbt);
        catsPlus$setHeldEntityNbt(entityNbt);
    }

    private void catsPlus$clearHeldEntity() {
        catsPlus$setHeldEntityNbt(new NbtCompound());
    }

    @Override
    public void catsPlus$dropHeldEntity(double x, double y, double z) {
        if (!getWorld().isClient() && catsPlus$isHoldingEntity()) {
            catsPlus$getHeldEntity().ifPresent(entity -> {
                EntityAccess entityAccess = (EntityAccess) entity;
                entityAccess.catsPlus$setHeldPoseNumber(0);
                entity.setPosition(x, y, z);
                getWorld().spawnEntity(entity);
            });
        }
        catsPlus$clearHeldEntity();
    }

    @Override
    public void catsPlus$dropHeldEntity(Vec3d pos) {
        catsPlus$dropHeldEntity(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void catsPlus$throwHeldEntity(double speed) {
        if (!getWorld().isClient() && catsPlus$isHoldingEntity()) {
            catsPlus$getHeldEntity().ifPresent(entity -> {
                Vec3d playerPosition = getPos();
                Vec3d playerHeading = Vec3d.fromPolar(getPitch(), getYaw());

                double playerWidth = getBoundingBox().getXLength();
                double playerBoxDiagonal = MathHelper.hypot(playerWidth, playerWidth);

                double entityWidth = entity.getBoundingBox().getXLength();
                double entityHeight = entity.getBoundingBox().getYLength();
                double entityBoxDiagonal = MathHelper.hypot(entityWidth, entityWidth);

                Vec3d throwStartDistanceFromPlayer = playerHeading.multiply((playerBoxDiagonal + entityBoxDiagonal) / 2);
                Vec3d entityVelocity = playerHeading.multiply(speed);

                EntityAccess entityAccess = (EntityAccess) entity;
                entityAccess.catsPlus$setHeldPoseNumber(0);
                entity.setPosition(
                        playerPosition.getX() + throwStartDistanceFromPlayer.getX(),
                        getEyeY() - entityHeight / 2,
                        playerPosition.getZ() + throwStartDistanceFromPlayer.getZ()
                );
                entity.setVelocity(entityVelocity);
                getWorld().spawnEntity(entity);
                swingHand(Hand.MAIN_HAND, true);
            });
        }
        catsPlus$clearHeldEntity();
    }
}
