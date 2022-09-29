package io.github.gaming32.mcutil.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.gaming32.mcutil.MCUtilConfig;
import me.shedaniel.autoconfig.AutoConfig;

public class MCUtilModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(MCUtilConfig.class, parent).get();
    }
}
