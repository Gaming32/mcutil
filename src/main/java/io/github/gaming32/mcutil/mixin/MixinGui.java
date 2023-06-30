package io.github.gaming32.mcutil.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.gaming32.mcutil.client.MCUtilClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public class MixinGui {
    @WrapOperation(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Minecraft;isLocalServer()Z"
        )
    )
    private boolean alwaysShowTabList(Minecraft instance, Operation<Boolean> original) {
        return !MCUtilClient.getConfig().alwaysAllowTabList && original.call(instance);
    }
}
