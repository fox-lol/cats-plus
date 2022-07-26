package xyz.foxkin.catsplus.commonside.animation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import net.minecraft.entity.EntityType;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;
import xyz.foxkin.catsplus.commonside.CatsPlus;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public enum EntityHeldPosesManager implements SynchronousResourceReloader {

    INSTANCE;

    private final Map<EntityType<?>, Integer> entityHeldPosesCounts = new HashMap<>();

    @Override
    public void reload(ResourceManager manager) {
        entityHeldPosesCounts.clear();
        Map<Identifier, Resource> resources = manager.findResources("entityheldposes", path -> path.getPath().endsWith(".json"));
        for (Map.Entry<Identifier, Resource> resourceEntry : resources.entrySet()) {
            Identifier identifier = resourceEntry.getKey();
            Resource resource = resourceEntry.getValue();
            try (InputStream inputStream = resource.getInputStream()) {
                parseEntityHeldPosesCounts(inputStream, identifier);
            } catch (IOException e) {
                CatsPlus.LOGGER.error("Error occurred while loading resource file " + identifier, e);
            }
        }
    }

    /**
     * Parses the entity held poses counts from the given input stream.
     *
     * @param inputStream the input stream to parse from.
     * @param identifier  the identifier of the resource file.
     */
    public void parseEntityHeldPosesCounts(InputStream inputStream, Identifier identifier) {
        Reader reader = new InputStreamReader(inputStream);
        JsonElement jsonElement = JsonParser.parseReader(reader);
        if (jsonElement instanceof JsonObject jsonObject) {
            JsonElement replaceElement = jsonObject.get("replace");
            if (replaceElement instanceof JsonPrimitive replacePrimitive) {
                if (replacePrimitive.isBoolean()) {
                    if (replacePrimitive.getAsBoolean()) {
                        entityHeldPosesCounts.clear();
                    }
                }
            }

            JsonElement heldPosesCountsElement = jsonObject.get("held_poses_counts");
            if (heldPosesCountsElement instanceof JsonObject heldPosesCountsObject) {
                for (String key : heldPosesCountsObject.keySet()) {
                    JsonElement valueElement = heldPosesCountsObject.get(key);
                    if (valueElement instanceof JsonPrimitive valuePrimitive) {
                        if (valuePrimitive.isNumber()) {
                            int value = valuePrimitive.getAsInt();
                            EntityType.get(key).ifPresentOrElse(
                                    entityType -> entityHeldPosesCounts.put(entityType, value),
                                    () -> CatsPlus.LOGGER.warn("Could not parse entity held poses counts from resource file " + identifier)
                            );
                            return;
                        }
                    }
                }
            }
        }

        CatsPlus.LOGGER.warn("Could not parse entity held poses counts from resource file " + identifier);
    }

    /**
     * Gets the held poses count for the given entity type.
     *
     * @param entityType the entity type to get the held poses count for.
     * @return The held poses count.
     */
    public int getEntityHeldPosesCount(EntityType<?> entityType) {
        return entityHeldPosesCounts.getOrDefault(entityType, 0);
    }
}
