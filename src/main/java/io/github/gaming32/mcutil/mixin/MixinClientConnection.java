package io.github.gaming32.mcutil.mixin;

import io.github.gaming32.mcutil.client.MCUtilClient;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class MixinClientConnection {
    @Inject(
        method = "handlePacket(Lnet/minecraft/network/Packet;Lnet/minecraft/network/listener/PacketListener;)V",
        at = @At("HEAD")
    )
    private static void updateLastPacketTime(Packet<?> packet, PacketListener listener, CallbackInfo ci) {
        MCUtilClient.lastReceivedPacket = System.currentTimeMillis();
    }

    @Mixin(targets = "net.minecraft.network.ClientConnection$1")
    public static class Anonymous1 {
        @Redirect(
            method = "initChannel(Lio/netty/channel/Channel;)V",
            at = @At(
                value = "INVOKE",
                target = "Lio/netty/channel/ChannelPipeline;addLast(Ljava/lang/String;Lio/netty/channel/ChannelHandler;)Lio/netty/channel/ChannelPipeline;"
            )
        )
        private ChannelPipeline removeReadTimeout(ChannelPipeline instance, String s, ChannelHandler channelHandler) {
            if (channelHandler instanceof ReadTimeoutHandler) {
                final int timeout = MCUtilClient.getConfig().connectionTimeout;
                if (timeout <= 0) {
                    return instance;
                }
                channelHandler = new ReadTimeoutHandler(timeout);
            }
            return instance.addLast(s, channelHandler);
        }
    }
}
