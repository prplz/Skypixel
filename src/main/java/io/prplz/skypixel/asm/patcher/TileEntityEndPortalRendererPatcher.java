package io.prplz.skypixel.asm.patcher;

import io.prplz.skypixel.asm.hook.TileEntityEndPortalRendererHook;
import net.minecraft.client.renderer.tileentity.TileEntityEndPortalRenderer;
import net.minecraft.tileentity.TileEntityEndPortal;
import org.objectweb.asm.tree.MethodNode;

import static io.prplz.skypixel.asm.ASMUtils.*;
import static org.objectweb.asm.Opcodes.RETURN;

public class TileEntityEndPortalRendererPatcher extends ClassPatcher {

    /**
     * Insert call to {@link TileEntityEndPortalRendererHook#renderTileEntityAt()} at the start of
     * {@link TileEntityEndPortalRenderer#renderTileEntityAt(TileEntityEndPortal, double, double, double, float, int)}
     *
     * <pre>
     * public void renderTileEntityAt(TileEntityEndPortal te, double x, double y, double z, float partialTicks, int destroyStage) {
     *     if (TileEntityEndPortalRendererHook.renderTileEntityAt()) {
     *         return;
     *     }
     *     // Original method body
     * }
     * </pre>
     */
    @PatchMethod(name = {"renderTileEntityAt", "func_180535_a"}, desc = "(Lnet/minecraft/tileentity/TileEntityEndPortal;DDDFI)V")
    public void patchRenderTileEntityAt(MethodNode method) {
        method.instructions.insert(insnListOf(
                invokeStatic("io/prplz/skypixel/asm/hook/TileEntityEndPortalRendererHook", "renderTileEntityAt", "()Z"),
                ifTrue(RETURN)
        ));
    }
}
