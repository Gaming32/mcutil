package io.github.gaming32.mcutil.mixin;

import io.github.gaming32.mcutil.client.MCUtilClient;
import net.minecraft.client.gui.hud.DebugHud;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DebugHud.class)
public class MixinDebugHud {
    @ModifyArg(
        method = "getLeftText()Ljava/util/List;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/Lists;newArrayList([Ljava/lang/Object;)Ljava/util/ArrayList;"
        )
    )
    private Object[] showServerLag(Object[] lines) {
        return ArrayUtils.insert(3, lines, "Server lag: " + MCUtilClient.getTimeSinceLastPacket() + "ms");
    }
}
