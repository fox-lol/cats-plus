package xyz.foxkin.catsplus.commonside.init;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import xyz.foxkin.catsplus.commonside.CatsPlus;

public class ModTags {

    /**
     * Represents blocks that a {@link CatEntity} will sit on.
     */
    public static final TagKey<Block> CAT_SIT_ON = TagKey.of(Registry.BLOCK_KEY, new Identifier(CatsPlus.MOD_ID, "cat_sit_on"));
    /**
     * Represents blocks that a {@link CatEntity} will sleep on.
     */
    public static final TagKey<Block> CAT_SLEEP_ON = TagKey.of(Registry.BLOCK_KEY, new Identifier(CatsPlus.MOD_ID, "cat_sleep_on"));
    /**
     * Represents items that will toggle whether a {@link CatEntity} follows its owner or not when used to interact with the {@code CatEntity}.
     */
    public static final TagKey<Item> TOGGLE_CAT_FOLLOWING = TagKey.of(Registry.ITEM_KEY, new Identifier(CatsPlus.MOD_ID, "toggle_cat_following"));
    /**
     * Represents items that will toggle whether a {@link WolfEntity} follows its owner or not when used to interact with the {@code WolfEntity}.
     */
    public static final TagKey<Item> TOGGLE_WOLF_FOLLOWING = TagKey.of(Registry.ITEM_KEY, new Identifier(CatsPlus.MOD_ID, "toggle_wolf_following"));
    /**
     * Represents items that will toggle whether a {@link ParrotEntity} follows its owner or not when used to interact with the {@code ParrotEntity}.
     */
    public static final TagKey<Item> TOGGLE_PARROT_FOLLOWING = TagKey.of(Registry.ITEM_KEY, new Identifier(CatsPlus.MOD_ID, "toggle_parrot_following"));
    public static final TagKey<EntityType<?>> CAN_PICK_UP = TagKey.of(Registry.ENTITY_TYPE_KEY, new Identifier(CatsPlus.MOD_ID, "can_pick_up"));
}
