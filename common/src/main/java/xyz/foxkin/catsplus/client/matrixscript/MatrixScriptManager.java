package xyz.foxkin.catsplus.client.matrixscript;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;
import xyz.foxkin.catsplus.client.matrixscript.exception.InvalidScriptLineException;
import xyz.foxkin.catsplus.commonside.CatsPlus;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public enum MatrixScriptManager implements SynchronousResourceReloader {

    INSTANCE;

    private final Map<String, MatrixScript> scripts = new HashMap<>();

    @Override
    public void reload(ResourceManager manager) {
        scripts.clear();
        Collection<Identifier> resourceIds = manager.findResources("matrixscripts", path -> path.endsWith(".matrixscript"));
        for (Identifier resourceId : resourceIds) {
            try (Resource resource = manager.getResource(resourceId)) {
                MatrixScript script = MatrixScript.Builder.parseInstructions(resource.getInputStream());
                scripts.put(resourceId.getPath(), script);
            } catch (InvalidScriptLineException e) {
                CatsPlus.LOGGER.error("Line {} in script {} is invalid! Details: {}", e.getLine(), resourceId.getPath(), e.getMessage());
            } catch (IOException e) {
                CatsPlus.LOGGER.error("Error occurred while loading resource file " + resourceId, e);
            }
        }
    }

    public Optional<MatrixScript> getScript(String path) {
        return Optional.ofNullable(scripts.get(path));
    }

    @SuppressWarnings("unused")
    public void executeIfPresent(String path, MatrixStack matrices) {
        getScript(path).ifPresent(script -> script.execute(matrices));
    }
}
