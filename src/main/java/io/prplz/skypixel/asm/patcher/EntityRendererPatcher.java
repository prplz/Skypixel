package io.prplz.skypixel.asm.patcher;

import com.google.common.base.Predicate;
import io.prplz.skypixel.asm.hook.EntityRendererHook;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static io.prplz.skypixel.asm.ASMUtils.deobfMatch;
import static io.prplz.skypixel.asm.ASMUtils.invokeStatic;

public class EntityRendererPatcher extends ClassPatcher {

    /**
     * Finds the first call to {@link WorldClient#getEntitiesInAABBexcluding(Entity, AxisAlignedBB, Predicate)} and
     * wraps the return value in a call to {@link EntityRendererHook#getMouseOverEntityList(List)}.
     *
     * <pre>
     * List<Entity> list = EntityRendererHook.getMouseOverEntityList(mc.theWorld.getEntitiesInAABBexcluding(...));
     * </pre>
     */
    @PatchMethod(name = {"getMouseOver", "func_78473_a"}, desc = "(F)V")
    public void patchGetMouseOver(MethodNode method) {
        for (Iterator<AbstractInsnNode> iter = method.instructions.iterator(); iter.hasNext(); ) {
            AbstractInsnNode insn = iter.next();
            if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                MethodInsnNode methodInsn = (MethodInsnNode) insn;
                if (deobfMatch(
                        methodInsn,
                        "net/minecraft/client/multiplayer/WorldClient",
                        Arrays.asList("getEntitiesInAABBexcluding", "func_175674_a"),
                        "(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;")) {
                    method.instructions.insert(insn, invokeStatic("io/prplz/skypixel/asm/hook/EntityRendererHook", "getMouseOverEntityList", "(Ljava/util/List;)Ljava/util/List;"));
                    return;
                }
            }
        }
    }
}
