package xyz.foxkin.catsplus.commonside.registry;

import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import xyz.foxkin.catsplus.commonside.CatsPlus;

public class ModTags {

    /**
     * Represents blocks that a cat will sit on.
     */
    public static final TagKey<Block> CAT_SIT_ON = TagKey.of(Registry.BLOCK_KEY, new Identifier(CatsPlus.MOD_ID, "cat_sit_on"));
}
