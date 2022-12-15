package xyz.foxkin.catsplus.client.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import xyz.foxkin.catsplus.client.CatsPlusClient;

@Environment(EnvType.CLIENT)
public class CatsPlusFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CatsPlusClient.init();
    }
}
