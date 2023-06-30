package io.github.gaming32.mcutil.mixin;

import io.github.gaming32.mcutil.client.MCUtilClient;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DebugScreenOverlay.class)
public class MixinDebugScreenOverlay {
    @ModifyArg(
        method = "getGameInformation",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/Lists;newArrayList([Ljava/lang/Object;)Ljava/util/ArrayList;",
            remap = false
        )
    )
    private Object[] showServerLag(Object[] lines) {
        return ArrayUtils.insert(3, lines, "Server lag: " + MCUtilClient.getTimeSinceLastPacket() + "ms");
    }
}
