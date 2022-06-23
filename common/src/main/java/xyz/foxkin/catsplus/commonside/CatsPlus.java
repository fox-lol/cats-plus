package xyz.foxkin.catsplus.commonside;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.foxkin.catsplus.commonside.registry.CatsPlusBlocks;
import xyz.foxkin.catsplus.commonside.registry.CatsPlusItems;

public class CatsPlus {

    public static final String MOD_ID = "catsplus";

    public static final Logger LOGGER = LoggerFactory.getLogger("Cats Plus");

    public static void init() {
        CatsPlusBlocks.registerBlocks();
        CatsPlusItems.registerItems();
    }
}
