package io.github.gaming32.mcutil.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.gaming32.mcutil.client.MCUtilClient;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class MixinConnection {
    @Inject(
        method = "genericsFtw",
        at = @At("HEAD")
    )
    private static void updateLastPacketTime(Packet<?> packet, PacketListener listener, CallbackInfo ci) {
        MCUtilClient.lastReceivedPacket = System.currentTimeMillis();
    }

    @Mixin(targets = "net.minecraft.network.Connection$1")
    public static class Anonymous1 {
        @WrapOperation(
            method = "initChannel(Lio/netty/channel/Channel;)V",
            at = @At(
                value = "INVOKE",
                target = "Lio/netty/channel/ChannelPipeline;addLast(Ljava/lang/String;Lio/netty/channel/ChannelHandler;)Lio/netty/channel/ChannelPipeline;",
                remap = false
            ),
            remap = false
        )
        private ChannelPipeline removeReadTimeout(ChannelPipeline instance, String name, ChannelHandler handler, Operation<ChannelPipeline> original) {
            if (handler instanceof ReadTimeoutHandler) {
                final int timeout = MCUtilClient.getConfig().connectionTimeout;
                if (timeout <= 0) {
                    return instance;
                }
                handler = new ReadTimeoutHandler(timeout);
            }
            return original.call(instance, name, handler);
        }
    }
}
