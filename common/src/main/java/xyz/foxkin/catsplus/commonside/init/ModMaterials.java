package xyz.foxkin.catsplus.commonside.init;

import io.github.shaksternano.noteblocklib.commonside.InstrumentMaterial;
import net.minecraft.block.Material;
import xyz.foxkin.catsplus.commonside.block.InstrumentMaterial;
import xyz.foxkin.catsplus.commonside.block.enums.CustomInstrument;
import xyz.foxkin.catsplus.commonside.materials.CatMaidArmorMaterial;

public class ModMaterials {
    public static final CatMaidArmorMaterial CAT_MAID_ARMOR = new CatMaidArmorMaterial();
    public static final Material CAT = new InstrumentMaterial(Material.WOOD, ModCustomInstruments.CAT);
}