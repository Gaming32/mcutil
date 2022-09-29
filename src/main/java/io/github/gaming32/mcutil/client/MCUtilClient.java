package io.github.gaming32.mcutil.client;

import com.mojang.logging.LogUtils;
import io.github.gaming32.mcutil.MCUtilConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class MCUtilClient implements ClientModInitializer {
    public static final Logger LOGGER = LogUtils.getLogger();

    public static long lastReceivedPacket;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(MCUtilConfig.class, GsonConfigSerializer::new);

        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
            if (MinecraftClient.getInstance().options.debugEnabled) return;
            final MCUtilConfig.NotResponding config = getConfig().notResponding;
            if (config.enabled && getTimeSinceLastPacket() > config.minTime) {
                final Text text =
                    Text.literal("Server not responding for ")
                        .append(
                            Text.literal(getTimeSinceLastPacket() / 100 / 10.0 + "s")
                                .formatted(Formatting.RED)
                        );
                if (config.textShadow) {
                    MinecraftClient.getInstance().textRenderer.drawWithShadow(
                        matrices, text, config.x, config.y, 0xffffffff
                    );
                } else {
                    MinecraftClient.getInstance().textRenderer.draw(
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
