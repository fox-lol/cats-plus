package xyz.foxkin.catsplus.mixin.commonloader.commonside.entitypickup;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
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

import java.util.Optional;

@SuppressWarnings("WrongEntityDataParameterClass")
@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityAccess {

    @Unique
    private static final TrackedData<NbtCompound> CATS_PLUS$HELD_ENTITY_TRACKER_KEY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);

    @Unique
    private static final String CATS_PLUS$HELD_ENTITY_NBT_KEY = "catsplus:heldEntity";

    @Unique
    @Nullable
    private Entity catsPlus$heldEntity;

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
        if (catsPlus$heldEntity != null) {
            NbtCompound entityNbt = catsPlus$serializeEntity(catsPlus$heldEntity);
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

    @SuppressWarnings("ConstantConditions")
    @Override
    public void catsPlus$setHeldEntity(@Nullable Entity entity) {
        catsPlus$heldEntity = entity;
        if (!getWorld().isClient()) {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            NbtCompound entityNbt = entity == null ? new NbtCompound() : catsPlus$serializeEntity(entity);
            buf.writeNbt(entityNbt);
            NetworkManager.sendToPlayer((ServerPlayerEntity) (Object) this, ModNetworkReceivers.SYNC_HELD_ENTITY_TO_CLIENT, buf);
        }
    }

    @Override
    public void catsPlus$clearHeldEntity() {
        catsPlus$setHeldEntity(null);
    }

    @Override
    public void catsPlus$dropHeldEntity(double x, double y, double z) {
        if (!getWorld().isClient()) {
            catsPlus$getHeldEntity().ifPresent(entity -> {
                Entity copy = catsPlus$copyEntity(entity, getWorld());
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
                Entity copy = catsPlus$copyEntity(entity, getWorld());

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

    private static NbtCompound catsPlus$serializeEntity(Entity entity) {
        NbtCompound entityNbt = new NbtCompound();
        Identifier entityId = EntityType.getId(entity.getType());
        entityNbt.putString("id", entityId.toString());
        entity.writeNbt(entityNbt);
        return entityNbt;
    }

    private static Entity catsPlus$copyEntity(Entity entity, World world) {
        NbtCompound entityNbt = catsPlus$serializeEntity(entity);
        return EntityType.getEntityFromNbt(entityNbt, world).orElseThrow();
    }
}
