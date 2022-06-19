package xyz.foxkin.catsplus.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.foxkin.catsplus.CatsPlus;

@Mod(CatsPlus.MOD_ID)
public class CatsPlusForge {

    public CatsPlusForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(CatsPlus.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        CatsPlus.init();
    }
}
