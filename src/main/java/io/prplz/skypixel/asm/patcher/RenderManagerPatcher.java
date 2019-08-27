package io.prplz.skypixel.asm.patcher;

import io.prplz.skypixel.asm.hook.RenderManagerHook;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

import static io.prplz.skypixel.asm.ASMUtils.*;

public class RenderManagerPatcher extends ClassPatcher {

    /**
     * Insert call to {@link RenderManagerHook#shouldRender(Entity)} at the start of
     * {@link RenderManager#shouldRender(Entity, ICamera, double, double, double)}.
     *
     * <pre>
     * public boolean shouldRender(Entity entity, ICamera camera, double camX, double camY, double camZ) {
     *     if (!RenderManagerHook.shouldRender(entity)) {
     *         return false;
     *     }
     *     // Original method body
     * }
     * </pre>
     */
    @PatchMethod(name = {"shouldRender", "func_178635_a"}, desc = "(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;DDD)Z")
    public void patchShouldRender(MethodNode method) {
        method.instructions.insert(insnListOf(
                aload(1), // entity
                invokeStatic("io/prplz/skypixel/asm/hook/RenderManagerHook", "shouldRender", "(Lnet/minecraft/entity/Entity;)Z"),
                ifFalse(Opcodes.ICONST_0, Opcodes.IRETURN)
        ));
    }
}
