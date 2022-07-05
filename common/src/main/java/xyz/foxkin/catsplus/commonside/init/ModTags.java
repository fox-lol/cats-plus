package xyz.foxkin.catsplus.commonside.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import xyz.foxkin.catsplus.commonside.CatsPlus;

public class ModTags {

    /**
     * Represents blocks that a cat will sit on.
     */
    public static final TagKey<Block> CAT_SIT_ON = TagKey.of(Registry.BLOCK_KEY, new Identifier(CatsPlus.MOD_ID, "cat_sit_on"));
    public static final TagKey<Item> TOGGLE_PET_FOLLOWING = TagKey.of(Registry.ITEM_KEY, new Identifier(CatsPlus.MOD_ID, "toggle_pet_following"));
    public static final TagKey<Item> TOGGLE_CAT_FOLLOWING = TagKey.of(Registry.ITEM_KEY, new Identifier(CatsPlus.MOD_ID, "toggle_cat_following"));
    public static final TagKey<Item> TOGGLE_WOLF_FOLLOWING = TagKey.of(Registry.ITEM_KEY, new Identifier(CatsPlus.MOD_ID, "toggle_wolf_following"));
    public static final TagKey<Item> TOGGLE_PARROT_FOLLOWING = TagKey.of(Registry.ITEM_KEY, new Identifier(CatsPlus.MOD_ID, "toggle_parrot_following"));
}
