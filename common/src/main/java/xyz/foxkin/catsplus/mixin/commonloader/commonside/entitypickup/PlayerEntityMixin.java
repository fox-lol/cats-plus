package xyz.foxkin.catsplus.mixin.commonloader.commonside.entitypickup;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;

@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityAccess {

    @SuppressWarnings("WrongEntityDataParameterClass")
    private static final TrackedData<NbtCompound> CATS_PLUS$HELD_ENTITY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);
    private static final String CATS_PLUS$HELD_ENTITY_NBT_KEY = "catsplus:heldEntity";

    @SuppressWarnings("unused")
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    private void catsPlus$addHeldEntityTrackedData(CallbackInfo ci) {
        dataTracker.startTracking(CATS_PLUS$HELD_ENTITY, new NbtCompound());
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void catsPlus$writeHeldEntity(NbtCompound nbt, CallbackInfo ci) {
        if (catsPlus$isHoldingEntity()) {
            nbt.put(CATS_PLUS$HELD_ENTITY_NBT_KEY, catsPlus$getHeldEntity());
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void catsPlus$readHeldEntity(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains(CATS_PLUS$HELD_ENTITY_NBT_KEY, NbtElement.COMPOUND_TYPE)) {
            catsPlus$setHeldEntity(nbt.getCompound(CATS_PLUS$HELD_ENTITY_NBT_KEY));
        }
    }

    @Override
    public NbtCompound catsPlus$getHeldEntity() {
        return dataTracker.get(CATS_PLUS$HELD_ENTITY);
    }

    @Override
    public boolean catsPlus$isHoldingEntity() {
        return !catsPlus$getHeldEntity().isEmpty();
    }

    @Override
    public void catsPlus$setHeldEntity(NbtCompound nbtCompound) {
        dataTracker.set(CATS_PLUS$HELD_ENTITY, nbtCompound);
    }

    @Override
    public void catsPlus$dropHeldEntity(double x, double y, double z) {
        if (!getWorld().isClient() && catsPlus$isHoldingEntity()) {
            EntityType.getEntityFromNbt(catsPlus$getHeldEntity(), getWorld()).ifPresent(entity -> {
                entity.setPosition(x, y, z);
                ((ServerWorld) getWorld()).tryLoadEntity(entity);
            });
        }
        catsPlus$setHeldEntity(new NbtCompound());
    }
}
