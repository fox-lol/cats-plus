package xyz.foxkin.catsplus.mixin.commonloader;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import dev.architectury.platform.Platform;
import io.github.shaksternano.noteblocklib.commonside.CustomInstrumentRegistry;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import xyz.foxkin.catsplus.commonside.init.ModCustomInstruments;

import java.util.List;
import java.util.Set;

public class CatsPlusMixinPlugin implements IMixinConfigPlugin {

    @Nullable
    private String developmentMixinPackage;

    /**
     * Initialise MixinExtras and register custom instruments.
     */
    @Override
    public void onLoad(String mixinPackage) {
        MixinExtrasBootstrap.init();

        CustomInstrumentRegistry.registerInstruments(
                ModCustomInstruments.CAT
        );

        developmentMixinPackage = mixinPackage + ".commonside.development.";
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (developmentMixinPackage != null && mixinClassName.startsWith(developmentMixinPackage)) {
            return Platform.isDevelopmentEnvironment();
        } else {
            return true;
        }
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
