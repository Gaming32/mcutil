package io.github.gaming32.mcutil.client;

import com.mojang.logging.LogUtils;
import io.github.gaming32.mcutil.MCUtilConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class MCUtilClient implements ClientModInitializer {
    public static final Logger LOGGER = LogUtils.getLogger();

    public static long lastReceivedPacket;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(MCUtilConfig.class, GsonConfigSerializer::new);

        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
            final Minecraft minecraft = Minecraft.getInstance();
            //noinspection DataFlowIssue
            if (
                minecraft.options.renderDebug || (
                    minecraft.hasSingleplayerServer() &&
                    !minecraft.getSingleplayerServer().isPublished() &&
                    minecraft.isPaused()
                )
            ) return;
            final MCUtilConfig.NotResponding config = getConfig().notResponding;
            if (config.enabled && getTimeSinceLastPacket() > config.minTime) {
                final Component text =
                    Component.literal("Server not responding for ")
                        .append(
                            Component.literal(getTimeSinceLastPacket() / 100 / 10.0 + "s")
                                .withStyle(ChatFormatting.RED)
                        );
                if (config.textShadow) {
                    minecraft.font.drawShadow(
                        matrices, text, config.x, config.y, 0xffffffff
                    );
                } else {
                    minecraft.font.draw(
                        matrices, text, config.x, config.y, 0xffffffff
                    );
                }
            }
        });

        LOGGER.info("MCUtil initialized!");
    }

    public static long getTimeSinceLastPacket() {
        return System.currentTimeMillis() - lastReceivedPacket;
    }

    public static MCUtilConfig getConfig() {
        return AutoConfig.getConfigHolder(MCUtilConfig.class).getConfig();
    }
}
