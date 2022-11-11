package xyz.foxkin.catsplus.commonside.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import xyz.foxkin.catsplus.commonside.access.spraybottle.CatEntityAccess;
import xyz.foxkin.catsplus.commonside.init.ModSounds;

public class SprayBottleItem extends Item {

    public SprayBottleItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (entity instanceof CatEntity cat) {
            CatEntityAccess access = (CatEntityAccess) cat;
            access.catsPlus$setFleeTicks(60);
            cat.setSitting(false);
            cat.addVelocity(0, 0.5, 0);
        }
        return ActionResult.PASS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        world.playSound(user, user.getX(), user.getY(), user.getZ(), ModSounds.SPRAY.get(), SoundCategory.NEUTRAL, 1, 1);
        return super.use(world, user, hand);
    }
}
