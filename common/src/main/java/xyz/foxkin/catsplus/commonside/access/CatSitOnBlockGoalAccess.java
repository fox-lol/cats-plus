package xyz.foxkin.catsplus.commonside.access;

import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;

@SuppressWarnings("unused")
public interface CatSitOnBlockGoalAccess {

    /**
     * An extra condition that must be satisfied before a cat can sit or sleep on a block.
     *
     * @return The extra condition.
     */
    boolean catsPlus$extraStartCondition();

    /**
     * Gets the tag that includes all the blocks that a cat can sit or sleep on.
     *
     * @return The tag.
     */
    TagKey<Block> catsPlus$getBlockTag();

    /**
     * Sets the pose that a cat will be set in once it gets on a valid block.
     *
     * @param inPose Wether to enter or exit the pose.
     */
    void catsPlus$setInPose(boolean inPose);
}
