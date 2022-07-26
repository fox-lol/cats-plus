package xyz.foxkin.catsplus.commonside.init;

import io.github.shaksternano.noteblocklib.commonside.InstrumentMaterial;
import net.minecraft.block.Material;
import net.minecraft.item.ArmorMaterial;
import xyz.foxkin.catsplus.commonside.materials.CatMaidArmorMaterial;

public class ModMaterials {

    // Block materials
    public static final Material CAT = new InstrumentMaterial(Material.WOOD, ModCustomInstruments.CAT);

    // Armor materials
    public static final ArmorMaterial CAT_MAID_ARMOR = new CatMaidArmorMaterial();
}
