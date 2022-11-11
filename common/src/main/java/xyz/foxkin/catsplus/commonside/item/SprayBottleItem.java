package xyz.foxkin.catsplus.commonside.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import xyz.foxkin.catsplus.commonside.access.spraybottle.CatEntityAccess;
import xyz.foxkin.catsplus.commonside.init.ModSounds;

public class SprayBottleItem extends Item {

    public SprayBottleItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (entity instanceof CatEntityAccess access) {
            access.catsPlus$setFleeTicks(60);
            user.playSound(ModSounds.SPRAY.get(), 1.0F, 1.0F);
            return ActionResult.SUCCESS;
        } else {
            return ActionResult.PASS;
        }
    }
}
