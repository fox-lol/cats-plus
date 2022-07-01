package xyz.foxkin.catsplus.commonside.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import xyz.foxkin.catsplus.commonside.CatsPlus;

@SuppressWarnings({"CanBeFinal", "FieldCanBeLocal", "FieldMayBeFinal"})
@Config(name = CatsPlus.MOD_ID)
@Config.Gui.Background(Config.Gui.Background.TRANSPARENT)
public class ModConfig implements ConfigData {

    @ConfigEntry.Category("general")
    @Comment("Should chests be openable when a cat is sitting on it?\n\nDefault value is false.")
    private boolean catSittingOnChestAllowsOpening = false;

    public boolean isCatSittingOnChestAllowsOpening() {
        return catSittingOnChestAllowsOpening;
    }
}
