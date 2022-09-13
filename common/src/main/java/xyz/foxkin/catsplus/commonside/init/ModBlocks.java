package xyz.foxkin.catsplus.commonside.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.shaksternano.noteblocklib.commonside.InstrumentBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.registry.Registry;
import xyz.foxkin.catsplus.commonside.CatsPlus;

public class ModBlocks {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(CatsPlus.MOD_ID, Registry.BLOCK_KEY);

    public static final RegistrySupplier<Block> CAT_BLOCK = BLOCKS.register(
            "cat_block",
            () -> new InstrumentBlock(ModCustomInstruments.CAT, AbstractBlock.Settings.of(Material.WOOD).strength(6).requiresTool().sounds(
                    new BlockSoundGroup(
                            1,
                            1,
                            SoundEvents.ENTITY_CAT_DEATH,
                            SoundEvents.ENTITY_CAT_STRAY_AMBIENT,
                            SoundEvents.ENTITY_CAT_AMBIENT,
                            SoundEvents.ENTITY_CAT_HURT,
                            SoundEvents.ENTITY_CAT_HURT
                    )
            ))
    );

    public static void registerBlocks() {
        BLOCKS.register();
    }
}
