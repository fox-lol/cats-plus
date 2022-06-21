package xyz.foxkin.catsplus.commonside.fabric;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.foxkin.catsplus.block.ModBlocks;
import xyz.foxkin.catsplus.commonside.CatsPlus;
import net.fabricmc.api.ModInitializer;



public class CatsPlusFabric implements ModInitializer {
    public static final String MOD_ID = "catsplus";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);


    @Override
    public void onInitialize() {
        CatsPlus.init();

        ModBlocks.registerModBlocks();
    }
}
