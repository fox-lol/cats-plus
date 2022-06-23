package xyz.foxkin.catsplus.commonside.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.foxkin.catsplus.commonside.CatsPlus;

@Mod(CatsPlus.MOD_ID)
public class CatsPlusForge {

    public CatsPlusForge() {
        EventBuses.registerModEventBus(CatsPlus.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        CatsPlus.init();
    }
}
