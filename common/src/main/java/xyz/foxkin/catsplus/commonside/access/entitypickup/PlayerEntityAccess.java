package xyz.foxkin.catsplus.commonside.access.entitypickup;

import net.minecraft.nbt.NbtCompound;

public interface PlayerEntityAccess {

    NbtCompound catsPlus$getHeldEntity();

    boolean catsPlus$isHoldingEntity();

    void catsPlus$setHeldEntity(NbtCompound nbtCompound);

    void catsPlus$clearHeldEntity();

    void catsPlus$dropHeldEntity(double x, double y, double z);

    void catsPlus$throwHeldEntity(double speed);
}
