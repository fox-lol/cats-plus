package xyz.foxkin.catsplus.commonside;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.foxkin.catsplus.commonside.registry.ModBlocks;
import xyz.foxkin.catsplus.commonside.registry.ModItems;
import xyz.foxkin.catsplus.commonside.registry.ModSounds;

public class CatsPlus {

    public static final String MOD_ID = "catsplus";

    public static final Logger LOGGER = LoggerFactory.getLogger("Cats Plus");

    public static void init() {
        ModSounds.registerSounds();
        ModBlocks.registerBlocks();
        ModItems.registerItems();
    }
}
