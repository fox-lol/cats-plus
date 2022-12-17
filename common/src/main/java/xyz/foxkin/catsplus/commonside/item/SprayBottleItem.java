package xyz.foxkin.catsplus.commonside.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import xyz.foxkin.catsplus.commonside.access.spraybottle.CatEntityAccess;
import xyz.foxkin.catsplus.commonside.access.spraybottle.PlayerEntityAccess;
import xyz.foxkin.catsplus.commonside.init.ModSounds;

public class SprayBottleItem extends Item {

    public SprayBottleItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        doSprayEffects(user);
        if (entity instanceof CatEntity cat) {
            CatEntityAccess access = (CatEntityAccess) cat;
            access.catsPlus$setFleeTicks(60);
            cat.addVelocity(0, 0.5, 0);
        }
        return ActionResult.PASS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        doSprayEffects(user);
        return super.use(world, user, hand);
    }

    private static void doSprayEffects(PlayerEntity user) {
        World world = user.getWorld();
        world.playSound(user, user.getX(), user.getY(), user.getZ(), ModSounds.SPRAY.get(), SoundCategory.NEUTRAL, 1, 1);
        PlayerEntityAccess access = (PlayerEntityAccess) user;
        if (access.catsPlus$canSpawnSprayBottleParticles()) {
            access.catsPlus$setSprayBottleParticleCooldown(10);
            spawnParticles(user);
        }
    }

    private static void spawnParticles(PlayerEntity user) {
        World world = user.getWorld();

        double playerX = user.getX();
        double playerY = user.getY() + user.getActiveEyeHeight(user.getPose(), user.getDimensions(user.getPose()));
        double playerZ = user.getZ();

        Vec3d rotation = Vec3d.fromPolar(user.getPitch(), user.getHeadYaw());
        double particleDistanceMultiplier = 0.5;
        double particleX = playerX + rotation.getX() * particleDistanceMultiplier;
        double particleY = playerY + rotation.getY() * particleDistanceMultiplier;
        double particleZ = playerZ + rotation.getZ() * particleDistanceMultiplier;

        world.addParticle(new DustParticleEffect(new Vec3f(Vec3d.unpackRgb(0xFFFFFF)), 2), particleX, particleY, particleZ, 0, 0, 0);
    }
}
