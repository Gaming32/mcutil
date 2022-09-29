package io.github.gaming32.mcutil.mixin;

import io.github.gaming32.mcutil.client.MCUtilClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerListHud.class)
public class MixinPlayerListHud {
    @ModifyVariable(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;ILnet/minecraft/scoreboard/Scoreboard;Lnet/minecraft/scoreboard/ScoreboardObjective;)V",
        at = @At("STORE"),
        ordinal = 0
    )
    private boolean alwaysShowSkin(boolean vanilla) {
        return vanilla || MCUtilClient.getConfig().showTabSkinsInOffline;
    }
}
