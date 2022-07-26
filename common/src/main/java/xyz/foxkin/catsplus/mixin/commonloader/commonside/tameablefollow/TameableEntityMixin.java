package xyz.foxkin.catsplus.mixin.commonloader.commonside.tameablefollow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.foxkin.catsplus.commonside.CatsPlus;
import xyz.foxkin.catsplus.commonside.access.tameablefollow.TameableEntityAccess;

@Mixin(TameableEntity.class)
abstract class TameableEntityMixin extends AnimalEntity implements Tameable, TameableEntityAccess {

    @Unique
    private static final String CATS_PLUS$FOLLOWING_NBT_KEY = "catsplus:following";

    @Unique
    private boolean catsPlus$following = true;

    protected TameableEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract boolean isTamed();

    @Shadow
    public abstract boolean isOwner(LivingEntity entity);

    /**
     * Writes whether the tameable entity is set to follow its owner or not to NBT,
     * so that it persists when quitting the game.
     */
    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void catsPlus$writeFollowing(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean(CATS_PLUS$FOLLOWING_NBT_KEY, catsPlus$following);
    }

    /**
     * Reads whether the tameable entity is set to follow its owner or not from NBT.
     */
    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void catsPlus$readFollowing(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains(CATS_PLUS$FOLLOWING_NBT_KEY, NbtElement.BYTE_TYPE)) {
            catsPlus$following = nbt.getBoolean(CATS_PLUS$FOLLOWING_NBT_KEY);
        }
    }

    @Unique
    @Override
    public boolean catsPlus$isFollowing() {
        return catsPlus$following;
    }

    /**
     * Toggles whether the tameable entity should follow its owner or not.
     */
    @Unique
    protected void catsPlus$toggleFollowing() {
        catsPlus$following = !catsPlus$following;
        if (getOwner() instanceof PlayerEntity owner) {
            String translationKey = catsPlus$following ?
                    "catsplus.message.actionBar.following" :
                    "catsplus.message.actionBar.wandering";
            owner.sendMessage(Text.translatable(translationKey), true);
        } else {
            CatsPlus.LOGGER.error("Could not send following status message because the owner is not a player, this shouldn't happen");
        }
    }
}
