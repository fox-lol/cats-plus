package xyz.foxkin.catsplus.client.plugin.modmenu.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import xyz.foxkin.catsplus.commonside.config.CatsPlusConfig;

@Environment(EnvType.CLIENT)
public class CatsPlusModMenuPlugin implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(CatsPlusConfig.class, parent).get();
    }
}
