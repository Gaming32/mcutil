package io.github.gaming32.mcutil.mixin;

import io.github.gaming32.mcutil.client.MCUtilClient;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerTabOverlay.class)
public class MixinTabOverlay {
    @ModifyVariable(method = "render", at = @At("STORE"), ordinal = 0)
    private boolean alwaysShowSkin(boolean vanilla) {
        return vanilla || MCUtilClient.getConfig().showTabSkinsInOffline;
    }
}
