package xyz.foxkin.catsplus.commonside.init;

import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import xyz.foxkin.catsplus.commonside.block.InstrumentMaterial;
import xyz.foxkin.catsplus.commonside.block.enums.CustomInstrument;
import xyz.foxkin.catsplus.commonside.materials.CatMaidArmorMaterial;

public class ModMaterials {

    public static final Material CAT = new InstrumentMaterial(MapColor.OAK_TAN, CustomInstrument.CAT);
    public static final CatMaidArmorMaterial CAT_MAID_ARMOR = new CatMaidArmorMaterial();
}
