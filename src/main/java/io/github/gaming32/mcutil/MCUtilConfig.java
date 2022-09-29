package io.github.gaming32.mcutil;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "mcutil")
@Config.Gui.Background(Config.Gui.Background.TRANSPARENT)
public class MCUtilConfig implements ConfigData {
    public boolean showTabSkinsInOffline = true;

    @ConfigEntry.Gui.Tooltip
    public int connectionTimeout = 0;

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public NotResponding notResponding = new NotResponding();

    public static class NotResponding {
        public boolean enabled = true;
        public long minTime = 5000;
        public float x = 10;
        public float y = 10;
        public boolean textShadow = true;
    }
}
