package xyz.foxkin.catsplus.commonside.access;

import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;

@SuppressWarnings("unused")
public interface CatSitOnBlockGoalAccess {

    boolean catsPlus$extraStartCondition();

    TagKey<Block> catsPlus$getBlockTag();

    void catsPlus$setInPose(boolean inPose);
}
