package xyz.foxkin.catsplus.client.render.matrixscript;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;
import xyz.foxkin.catsplus.client.render.matrixscript.exception.InvalidScriptLineException;
import xyz.foxkin.catsplus.commonside.CatsPlus;

import java.io.IOException;
import java.io.InputStream;
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
        Map<Identifier, Resource> resources = manager.findResources("matrixscripts", path -> path.getPath().endsWith(".matrixscript"));
        for (Map.Entry<Identifier, Resource> resourceEntry : resources.entrySet()) {
            Identifier identifier = resourceEntry.getKey();
            Resource resource = resourceEntry.getValue();
            try (InputStream inputStream = resource.getInputStream()) {
                MatrixScript script = MatrixScript.Builder.parseInstructions(inputStream);
                scripts.put(identifier.getPath(), script);
            } catch (InvalidScriptLineException e) {
                CatsPlus.LOGGER.error("Line {} in script {} is invalid! Details: {}", e.getLine(), identifier.getPath(), e.getMessage());
            } catch (IOException e) {
                CatsPlus.LOGGER.error("Error occurred while loading resource file " + identifier.toString(), e);
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
