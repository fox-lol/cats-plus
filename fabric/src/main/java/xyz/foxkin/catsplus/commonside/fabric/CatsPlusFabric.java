package xyz.foxkin.catsplus.commonside.fabric;


import net.fabricmc.api.ModInitializer;
import xyz.foxkin.catsplus.commonside.CatsPlus;

public class CatsPlusFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CatsPlus.init();
    }
}
