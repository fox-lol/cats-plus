package xyz.foxkin.catsplus.commonside.access.entitypickup;

import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;

public interface EntityAccess {

    Optional<PlayerEntity> catsPlus$getHolder();

    void catsPlus$setHolder(PlayerEntity holder);
}
