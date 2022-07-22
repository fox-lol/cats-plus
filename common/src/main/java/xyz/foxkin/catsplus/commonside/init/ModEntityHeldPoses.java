package xyz.foxkin.catsplus.commonside.init;

import net.minecraft.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class ModEntityHeldPoses {

    private static final Map<EntityType<?>, Integer> ENTITY_HELD_POSES_COUNTS = new HashMap<>();

    public static void addEntityHeldPosesCounts() {
        addEntityHeldPosesCount(EntityType.CAT, 1);
    }

    @SuppressWarnings("SameParameterValue")
    private static void addEntityHeldPosesCount(EntityType<?> entityType, int heldPosesCount) {
        ENTITY_HELD_POSES_COUNTS.put(entityType, heldPosesCount);
    }

    public static int getEntityHeldPosesCount(EntityType<?> entityType) {
        return ENTITY_HELD_POSES_COUNTS.getOrDefault(entityType, 0);
    }
}
