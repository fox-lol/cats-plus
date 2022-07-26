package xyz.foxkin.catsplus.mixin.commonloader.commonside.entitypickup;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
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
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.foxkin.catsplus.commonside.CatsPlus;
import xyz.foxkin.catsplus.commonside.access.entitypickup.EntityAccess;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;
import xyz.foxkin.catsplus.commonside.init.ModNetworkReceivers;
import xyz.foxkin.catsplus.commonside.util.EntityUtil;
import xyz.foxkin.catsplus.commonside.util.PlayerLookup;
import xyz.foxkin.catsplus.mixin.commonloader.commonside.accessor.MobEntityAccessor;

import java.util.Optional;

@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityAccess {

    @Unique
    private static final String CATS_PLUS$HELD_ENTITY_NBT_KEY = "catsplus:heldEntity";

    @Unique
    @Nullable
    private Entity catsPlus$heldEntity;

    @SuppressWarnings("unused")
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
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

        // Syncs the held entity to clients.
        if (!getWorld().isClient()) {
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
                SoundEvent soundEvent = mobAccessor.catsPlus$invokeGetAmbientSound();
                if (soundEvent != null) {
                    playSound(soundEvent, mobAccessor.catsPlus$invokeGetSoundVolume(), mob.getSoundPitch());
                }
            }
        }
    }

    /**
     * Writes information about the player's held entity to NBT.
     */
    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void catsPlus$writeHeldEntity(NbtCompound nbt, CallbackInfo ci) {
        if (catsPlus$heldEntity != null) {
            NbtCompound entityNbt = EntityUtil.serializeEntity(catsPlus$heldEntity);
            nbt.put(CATS_PLUS$HELD_ENTITY_NBT_KEY, entityNbt);
        }
    }

    /**
     * Reads information about the player's held entity from NBT.
     */
    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void catsPlus$readHeldEntity(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains(CATS_PLUS$HELD_ENTITY_NBT_KEY, NbtElement.COMPOUND_TYPE)) {
            NbtCompound entityNbt = nbt.getCompound(CATS_PLUS$HELD_ENTITY_NBT_KEY);
            if (!entityNbt.isEmpty()) {
                EntityType.getEntityFromNbt(entityNbt, getWorld()).ifPresentOrElse(
                        this::catsPlus$setHeldEntity,
                        () -> CatsPlus.LOGGER.error("Failed to deserialize held entity from NBT: " + entityNbt)
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
                EntityAccess copyAccess = (EntityAccess) copy;
                copyAccess.catsPlus$setHeldPoseNumber(0);
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
    public void catsPlus$throwHeldEntity(double speed) {
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
                Vec3d entityVelocity = playerHeading.multiply(speed);

                EntityAccess copyAccess = (EntityAccess) copy;
                copyAccess.catsPlus$setHeldPoseNumber(0);
                copy.setPosition(
                        playerPosition.getX() + throwStartDistanceFromPlayer.getX(),
                        getEyeY() - entityHeight / 2,
                        playerPosition.getZ() + throwStartDistanceFromPlayer.getZ()
                );
                copy.setVelocity(entityVelocity);
                getWorld().spawnEntity(copy);
                swingHand(Hand.MAIN_HAND, true);
            });
        }
        catsPlus$clearHeldEntity();
    }
}
