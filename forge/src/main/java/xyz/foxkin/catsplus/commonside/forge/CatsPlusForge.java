package xyz.foxkin.catsplus.commonside.forge;

import dev.architectury.platform.forge.EventBuses;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import xyz.foxkin.catsplus.commonside.CatsPlus;
import xyz.foxkin.catsplus.commonside.config.ModConfig;

@Mod(CatsPlus.MOD_ID)
public class CatsPlusForge {

    public CatsPlusForge() {
        init();
        if (FMLLoader.getDist().isClient()) {
            clientInit();
        }
    }

    private void init() {
        EventBuses.registerModEventBus(CatsPlus.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        CatsPlus.init();
    }

    @OnlyIn(Dist.CLIENT)
    private void clientInit() {
        registerConfigScreen();
    }

    @OnlyIn(Dist.CLIENT)
    private void registerConfigScreen() {
        ModLoadingContext.get().registerExtensionPoint(
                ConfigGuiHandler.ConfigGuiFactory.class,
                () -> new ConfigGuiHandler.ConfigGuiFactory(
                        (client, parent) -> AutoConfig.getConfigScreen(ModConfig.class, parent).get()
                )
        );
    }
}
