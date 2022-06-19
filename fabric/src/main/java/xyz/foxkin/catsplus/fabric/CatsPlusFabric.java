package xyz.foxkin.catsplus.fabric;

import xyz.foxkin.catsplus.CatsPlus;
import net.fabricmc.api.ModInitializer;

public class CatsPlusFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CatsPlus.init();
    }
}
