package io.prplz.skypixel.asm.patcher;

import net.minecraft.client.network.NetHandlerPlayClient;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static io.prplz.skypixel.asm.ASMUtils.deobfMatch;
import static io.prplz.skypixel.asm.ASMUtils.invokeStatic;

public class GuiPlayerTabOverlayPatcher extends ClassPatcher {

    /**
     * Finds calls to {@link NetHandlerPlayClient#getPlayerInfoMap()} and wraps the return value in a call to
     * {@link io.prplz.skypixel.asm.hook.GuiPlayerTabOverlayHook#getPlayerInfoMap(Collection)}
     *
     * <pre>
     * List<NetworkPlayerInfo> list = field_175252_a.<NetworkPlayerInfo>sortedCopy(GuiPlayerTabOverlayHook.getPlayerInfoMap(nethandlerplayclient.getPlayerInfoMap()));
     * </pre>
     */
    @PatchMethod(name = {"renderPlayerlist", "func_175249_a"}, desc = "(ILnet/minecraft/scoreboard/Scoreboard;Lnet/minecraft/scoreboard/ScoreObjective;)V")
    public void patchRenderPlayerList(MethodNode method) {
        for (Iterator<AbstractInsnNode> iter = method.instructions.iterator(); iter.hasNext(); ) {
            AbstractInsnNode insn = iter.next();
            if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                MethodInsnNode methodInsn = (MethodInsnNode) insn;
                if (deobfMatch(
                        methodInsn,
                        "net/minecraft/client/network/NetHandlerPlayClient",
                        Arrays.asList("getPlayerInfoMap", "func_175106_d"),
                        "()Ljava/util/Collection;")) {
                    method.instructions.insert(insn, invokeStatic("io/prplz/skypixel/asm/hook/GuiPlayerTabOverlayHook", "getPlayerInfoMap", "(Ljava/util/Collection;)Ljava/util/Collection;"));
                }
            }
        }
    }
}
