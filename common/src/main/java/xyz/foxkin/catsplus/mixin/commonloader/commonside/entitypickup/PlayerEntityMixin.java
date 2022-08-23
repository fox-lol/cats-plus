package xyz.foxkin.catsplus.mixin.commonloader.commonside.entitypickup;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.foxkin.catsplus.commonside.CatsPlus;
import xyz.foxkin.catsplus.commonside.access.entitypickup.EntityAccess;
import xyz.foxkin.catsplus.commonside.access.entitypickup.HoldableTickable;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;
import xyz.foxkin.catsplus.commonside.animation.EntityHeldPosesManager;
import xyz.foxkin.catsplus.commonside.init.ModNetworkReceivers;
import xyz.foxkin.catsplus.commonside.util.EntityUtil;
import xyz.foxkin.catsplus.commonside.util.PlayerLookup;
import xyz.foxkin.catsplus.mixin.commonloader.commonside.accessor.LivingEntityAccessor;
import xyz.foxkin.catsplus.mixin.commonloader.commonside.accessor.MobEntityAccessor;

import java.util.Optional;

@SuppressWarnings("WrongEntityDataParameterClass")
@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityAccess {

    @Unique
    private static final Random CATS_PLUS$RANDOM = Random.create();

    @Unique
    private static final String CATS_PLUS$HELD_ENTITY_NBT_KEY = "catsplus:heldEntity";
    @Unique
    private static final String CATS_PLUS$HELD_POSE_NBT_KEY = "catsplus:heldPose";

    @Unique
    private static final TrackedData<Integer> CATS_PLUS$HELD_POSE_DATA_TRACKER_KEY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private static final TrackedData<Boolean> CATS_PLUS$INTERACTING_WITH_HELD_ENTITY_DATA_TRACKER_KEY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Unique
    @Nullable
    private Entity catsPlus$heldEntity;

    @SuppressWarnings("unused")
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    private void catsPlus$trackCustomData(CallbackInfo ci) {
        dataTracker.startTracking(CATS_PLUS$INTERACTING_WITH_HELD_ENTITY_DATA_TRACKER_KEY, false);
        dataTracker.startTracking(CATS_PLUS$HELD_POSE_DATA_TRACKER_KEY, 0);
    }

    /**
     * Runs custom code every tick.
     */
    @SuppressWarnings("ConstantConditions")
    @Inject(method = "tick", at = @At("RETURN"))
    private void catsPlus$tick(CallbackInfo ci) {
        // Drops the held entity is an item is equipped in either hand.
        if (!getMainHandStack().isEmpty() || !getOffHandStack().isEmpty()) {
            catsPlus$dropHeldEntity(getPos());
        }

        if (!getWorld().isClient()) {
            // Syncs the held entity to clients.
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeUuid(getUuid());
            NbtCompound entityNbt = catsPlus$heldEntity == null ? new NbtCompound() : EntityUtil.serializeEntity(catsPlus$heldEntity);
            buf.writeNbt(entityNbt);
            NetworkManager.sendToPlayer((ServerPlayerEntity) (Object) this, ModNetworkReceivers.SYNC_HELD_ENTITY_TO_CLIENT, buf);
            for (ServerPlayerEntity player : PlayerLookup.tracking(this)) {
                NetworkManager.sendToPlayer(player, ModNetworkReceivers.SYNC_HELD_ENTITY_TO_CLIENT, buf);
            }
        }

        // Play the ambient sound of the held entity if it is a mob entity.
        if (catsPlus$heldEntity instanceof MobEntity mob) {
            if (mob.getRandom().nextInt(1000) < mob.ambientSoundChance++) {
                MobEntityAccessor mobAccessor = (MobEntityAccessor) mob;
                mobAccessor.catsPlus$invokeResetSoundDelay();
                SoundEvent ambient = mobAccessor.catsPlus$invokeGetAmbientSound();
                if (ambient != null) {
                    getWorld().playSoundFromEntity((PlayerEntity) (Object) this, this, ambient, catsPlus$heldEntity.getSoundCategory(), mobAccessor.catsPlus$invokeGetSoundVolume(), mob.getSoundPitch());
                }
            }
        }

        if (catsPlus$heldEntity instanceof HoldableTickable tickable) {
            tickable.catsPlus$heldTick((PlayerEntity) (Object) this);
        }
    }

    /**
     * Writes custom data to NBT.
     */
    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void catsPlus$writeCustomData(NbtCompound nbt, CallbackInfo ci) {
        if (catsPlus$heldEntity != null) {
            NbtCompound entityNbt = EntityUtil.serializeEntity(catsPlus$heldEntity);
            nbt.put(CATS_PLUS$HELD_ENTITY_NBT_KEY, entityNbt);

            int heldPoseNumber = catsPlus$getHeldPoseNumber();
            if (heldPoseNumber > 0) {
                nbt.putInt(CATS_PLUS$HELD_POSE_NBT_KEY, heldPoseNumber);
            }
        }
    }

    /**
     * Reads custom data from NBT.
     */
    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void catsPlus$readCustomData(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains(CATS_PLUS$HELD_ENTITY_NBT_KEY, NbtElement.COMPOUND_TYPE)) {
            NbtCompound entityNbt = nbt.getCompound(CATS_PLUS$HELD_ENTITY_NBT_KEY);
            if (!entityNbt.isEmpty()) {
                EntityType.getEntityFromNbt(entityNbt, getWorld()).ifPresentOrElse(
                        entity -> {
                            catsPlus$setHeldEntity(entity);

                            int heldPoseNumber = 0;
                            if (nbt.contains(CATS_PLUS$HELD_POSE_NBT_KEY, NbtElement.INT_TYPE)) {
                                heldPoseNumber = nbt.getInt(CATS_PLUS$HELD_POSE_NBT_KEY);
                                catsPlus$setHeldPoseNumber(heldPoseNumber);
                            }
                            if (heldPoseNumber <= 0) {
                                catsPlus$setRandomHeldPoseNumber();
                            }
                        },
                        () -> CatsPlus.LOGGER.warn("Failed to deserialize held entity from NBT: " + entityNbt)
                );
            }
        }
    }

    @Override
    public Optional<Entity> catsPlus$getHeldEntity() {
        return Optional.ofNullable(catsPlus$heldEntity);
    }

    @Override
    public void catsPlus$setHeldEntity(@Nullable Entity entity) {
        if (entity == null) {
            if (catsPlus$heldEntity != null) {
                EntityAccess currentHeldEntityAccess = (EntityAccess) catsPlus$heldEntity;
                currentHeldEntityAccess.catsPlus$setHolder(null);
            }
            catsPlus$setHeldPoseNumber(0);
        } else {
            EntityAccess newHeldEntityAccess = (EntityAccess) entity;
            newHeldEntityAccess.catsPlus$setHolder((PlayerEntity) (Object) this);
        }
        catsPlus$heldEntity = entity;
    }

    @Override
    public void catsPlus$clearHeldEntity() {
        catsPlus$setHeldEntity(null);
    }

    @Override
    public void catsPlus$dropHeldEntity(double x, double y, double z) {
        if (!getWorld().isClient()) {
            catsPlus$getHeldEntity().ifPresent(entity -> {
                Entity copy = EntityUtil.copyEntity(entity, getWorld());
                copy.setPosition(x, y, z);
                getWorld().spawnEntity(copy);
            });
        }
        catsPlus$clearHeldEntity();
    }

    @Override
    public void catsPlus$dropHeldEntity(Vec3d pos) {
        catsPlus$dropHeldEntity(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void catsPlus$throwHeldEntity(double throwSpeed) {
        if (!getWorld().isClient()) {
            catsPlus$getHeldEntity().ifPresent(entity -> {
                Entity copy = EntityUtil.copyEntity(entity, getWorld());

                Vec3d playerPosition = getPos();
                Vec3d playerHeading = Vec3d.fromPolar(getPitch(), getYaw());

                double playerWidth = getBoundingBox().getXLength();
                double playerBoxDiagonal = MathHelper.hypot(playerWidth, playerWidth);

                double entityWidth = copy.getBoundingBox().getXLength();
                double entityHeight = copy.getBoundingBox().getYLength();
                double entityBoxDiagonal = MathHelper.hypot(entityWidth, entityWidth);

                Vec3d throwStartDistanceFromPlayer = playerHeading.multiply((playerBoxDiagonal + entityBoxDiagonal) / 2);
                Vec3d entityVelocity = playerHeading.multiply(throwSpeed);

                copy.setPosition(
                        playerPosition.getX() + throwStartDistanceFromPlayer.getX(),
                        getEyeY() - entityHeight / 2,
                        playerPosition.getZ() + throwStartDistanceFromPlayer.getZ()
                );
                copy.setVelocity(entityVelocity);
                getWorld().spawnEntity(copy);
                swingHand(Hand.MAIN_HAND, true);
                if (copy instanceof LivingEntity livingEntity) {
                    LivingEntityAccessor livingEntityAccessor = (LivingEntityAccessor) livingEntity;
                    SoundEvent hurt = livingEntityAccessor.catsPlus$invokeGetHurtSound(DamageSource.FALL);
                    if (hurt != null) {
                        getWorld().playSoundFromEntity(null, this, hurt, copy.getSoundCategory(), livingEntityAccessor.catsPlus$invokeGetSoundVolume(), livingEntity.getSoundPitch());
                    }
                }
            });
        }
        catsPlus$clearHeldEntity();
    }

    @Override
    public int catsPlus$getHeldPoseNumber() {
        return dataTracker.get(CATS_PLUS$HELD_POSE_DATA_TRACKER_KEY);
    }

    protected void catsPlus$setHeldPoseNumber(int heldPoseNumber) {
        dataTracker.set(CATS_PLUS$HELD_POSE_DATA_TRACKER_KEY, heldPoseNumber);
    }

    @Override
    public void catsPlus$setRandomHeldPoseNumber() {
        if (catsPlus$heldEntity == null) {
            throw new IllegalStateException("Cannot set random pose number when no held entity is set.");
        } else {
            int heldPosesCount = EntityHeldPosesManager.INSTANCE.getEntityHeldPosesCount(catsPlus$heldEntity.getType());
            if (heldPosesCount > 0) {
                int heldPoseNumber;
                if (heldPosesCount == 1) {
                    heldPoseNumber = 1;
                } else {
                    heldPoseNumber = CATS_PLUS$RANDOM.nextInt(heldPosesCount - 1) + 1;
                }
                catsPlus$setHeldPoseNumber(heldPoseNumber);
            }
        }
    }

    @Override
    public boolean catsPlus$isInteractingWithHeldEntity() {
        return dataTracker.get(CATS_PLUS$INTERACTING_WITH_HELD_ENTITY_DATA_TRACKER_KEY);
    }

    @Override
    public void catsPlus$setInteractingWithHeldEntity(boolean interacting) {
        dataTracker.set(CATS_PLUS$INTERACTING_WITH_HELD_ENTITY_DATA_TRACKER_KEY, interacting);
    }
}
