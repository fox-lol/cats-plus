package xyz.foxkin.catsplus.mixin.commonloader.commonside.entitypickup;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xyz.foxkin.catsplus.commonside.access.entitypickup.HoldableTickable;
import xyz.foxkin.catsplus.commonside.access.entitypickup.PlayerEntityAccess;

@SuppressWarnings("unused")
@Mixin(CatEntity.class)
abstract class CatEntityMixin extends TameableEntity implements HoldableTickable {

    @Unique
    private int heldSoundCooldown = 0;

    protected CatEntityMixin(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void catsPlus$heldTick(PlayerEntity holder) {
        if (heldSoundCooldown > 0) {
            heldSoundCooldown--;
        }
        PlayerEntityAccess holderAccess = (PlayerEntityAccess) holder;
        if (holderAccess.catsPlus$isInteractingWithHeldEntity()) {
            if (heldSoundCooldown == 0) {
                holder.getWorld().playSoundFromEntity(holder, holder, SoundEvents.ENTITY_CAT_PURR, getSoundCategory(), 0.6F + 0.4F * (random.nextFloat() - random.nextFloat()), 1);
                heldSoundCooldown = 5;
            }
        }
    }
}
