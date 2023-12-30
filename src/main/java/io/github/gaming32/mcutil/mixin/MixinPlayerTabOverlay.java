package io.github.gaming32.mcutil.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import io.github.gaming32.mcutil.client.MCUtilClient;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerTabOverlay.class)
public class MixinPlayerTabOverlay {
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "render", at = @At("HEAD"))
    private void initPingWidth(
        GuiGraphics guiGraphics, int width, Scoreboard scoreboard, Objective objective, CallbackInfo ci,
        @Share("pingWidth") LocalIntRef pingWidth
    ) {
        pingWidth.set(10);
    }

    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;getNameForDisplay(Lnet/minecraft/client/multiplayer/PlayerInfo;)Lnet/minecraft/network/chat/Component;"
        )
    )
    private void trackPingWidth(
        GuiGraphics guiGraphics, int width, Scoreboard scoreboard, Objective objective, CallbackInfo ci,
        @Local PlayerInfo playerInfo,
        @Share("pingWidth") LocalIntRef pingWidth
    ) {
        if (!MCUtilClient.getConfig().useNumericPingDisplay || playerInfo.getLatency() < 0) return;
        pingWidth.set(Math.max(pingWidth.get(), minecraft.font.width(Integer.toString(playerInfo.getLatency()))));
    }

    @ModifyConstant(method = "render", constant = @Constant(intValue = 13))
    private int widenLines(
        int constant,
        @Share("pingWidth") LocalIntRef pingWidth
    ) {
        return constant - 10 + pingWidth.get();
    }

    @Inject(method = "renderPingIcon", at = @At("HEAD"), cancellable = true)
    private void numericPingDisplay(GuiGraphics guiGraphics, int i, int j, int y, PlayerInfo playerInfo, CallbackInfo ci) {
        if (!MCUtilClient.getConfig().useNumericPingDisplay || playerInfo.getLatency() < 0) return;
        ci.cancel();

        final ChatFormatting color;
        if (playerInfo.getLatency() < 150) {
            color = ChatFormatting.GREEN;
        } else if (playerInfo.getLatency() < 300) {
            color = ChatFormatting.YELLOW;
        } else if (playerInfo.getLatency() < 600) {
            color = ChatFormatting.GOLD;
        } else if (playerInfo.getLatency() < 1000) {
            color = ChatFormatting.RED;
        } else {
            color = ChatFormatting.DARK_RED;
        }

        final String text = Integer.toString(playerInfo.getLatency());
        final int width = minecraft.font.width(text);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 100.0F);
        guiGraphics.drawString(minecraft.font, text, j + i - 1 - width, y, color.getColor());
        guiGraphics.pose().popPose();
    }
}
