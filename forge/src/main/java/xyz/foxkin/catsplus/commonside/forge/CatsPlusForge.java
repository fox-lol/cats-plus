package xyz.foxkin.catsplus.commonside.forge;

import dev.architectury.platform.forge.EventBuses;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import xyz.foxkin.catsplus.commonside.CatsPlus;
import xyz.foxkin.catsplus.commonside.config.CatsPlusConfig;

@Mod(CatsPlus.MOD_ID)
public class CatsPlusForge {

    public CatsPlusForge() {
        init();
        if (FMLLoader.getDist().isClient()) {
            clientInit();
        }
    }

    private static void init() {
        EventBuses.registerModEventBus(CatsPlus.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        CatsPlus.init();
    }

    @OnlyIn(Dist.CLIENT)
    private static void clientInit() {
        CatsPlus.clientInit();
        registerConfigScreen();
    }

    /**
     * Adds the mod's config screen to the mod menu.
     */
    @OnlyIn(Dist.CLIENT)
    private static void registerConfigScreen() {
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (client, screen) -> AutoConfig.getConfigScreen(CatsPlusConfig.class, screen).get()
                )
        );
    }
}
