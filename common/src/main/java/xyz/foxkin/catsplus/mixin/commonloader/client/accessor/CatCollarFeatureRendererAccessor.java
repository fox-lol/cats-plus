package xyz.foxkin.catsplus.mixin.commonloader.client.accessor;

import net.minecraft.client.render.entity.feature.CatCollarFeatureRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CatCollarFeatureRenderer.class)
public interface CatCollarFeatureRendererAccessor {

    @Accessor("SKIN")
    static Identifier catsPlus$getSkin() {
        throw new AssertionError();
    }
}
