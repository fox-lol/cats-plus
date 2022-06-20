package xyz.foxkin.catsplus.commonside.fabric;

import xyz.foxkin.catsplus.commonside.CatsPlus;
import net.fabricmc.api.ModInitializer;

public class CatsPlusFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CatsPlus.init();
    }
}
