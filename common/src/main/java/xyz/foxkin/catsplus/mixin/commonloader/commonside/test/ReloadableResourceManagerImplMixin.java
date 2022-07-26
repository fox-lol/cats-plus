package xyz.foxkin.catsplus.mixin.commonloader.commonside.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.architectury.platform.Platform;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceReload;
import net.minecraft.util.Unit;
import org.apache.commons.io.FileUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.foxkin.catsplus.commonside.CatsPlus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ReloadableResourceManagerImpl.class)
abstract class ReloadableResourceManagerImplMixin {

    /**
     * Copies the mod's assets folder to a resource pack to allow for resource reloading to work.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Inject(method = "reload", at = @At("HEAD"))
    private void catsPlus$copyResourcesToResourcePack(Executor prepareExecutor, Executor applyExecutor, CompletableFuture<Unit> initialStage, List<ResourcePack> packs, CallbackInfoReturnable<ResourceReload> cir) {
        if (Platform.isDevelopmentEnvironment()) {
            Path gamePath = Platform.getGameFolder();

            Path testResourcePackPath = gamePath.resolve(Path.of("resourcepacks", "Test Resource Pack"));
            boolean deleted = FileUtils.deleteQuietly(testResourcePackPath.toFile());
            if (!deleted) {
                CatsPlus.LOGGER.error("Failed to delete old test resource pack folder");
            }

            Path resourcePackAssetsPath = testResourcePackPath.resolve("assets");
            File resourcePackAssetsFile = resourcePackAssetsPath.toFile();
            resourcePackAssetsFile.mkdirs();

            File packMcMeta = testResourcePackPath.resolve("pack.mcmeta").toFile();
            try (Writer writer = new FileWriter(packMcMeta)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(catsPlus$createPackMcMeta(), writer);

                Path workingDirectory = Path.of(System.getProperty("user.dir"));
                while (!Objects.equals(workingDirectory.getFileName().toString(), "run")) {
                    workingDirectory = workingDirectory.getParent();
                }

                Path modAssetsPath = workingDirectory.getParent().getParent().resolve(Path.of("common", "src", "main", "resources", "assets"));
                FileUtils.copyDirectory(modAssetsPath.toFile(), resourcePackAssetsFile);
                CatsPlus.LOGGER.info("Copied mod assets to test resource pack");
            } catch (IOException e) {
                CatsPlus.LOGGER.error("Error creating test resource pack", e);
            }
        }
    }

    /**
     * Creates a {@code JsonElement} representing a resource pack's pack.mcmeta file.
     *
     * @return A {@code JsonElement} representing the pack.mcmeta file.
     */
    private static JsonElement catsPlus$createPackMcMeta() {
        JsonObject packObject = new JsonObject();
        packObject.addProperty("description", "Mod assets copied to a resource pack");
        packObject.addProperty("pack_format", 9);

        JsonObject packMcMeta = new JsonObject();
        packMcMeta.add("pack", packObject);
        return packMcMeta;
    }
}
