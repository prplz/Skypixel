package io.prplz.skypixel.asm.patcher;

import io.prplz.skypixel.asm.hook.GuiScreenHook;
import net.minecraft.client.gui.GuiScreen;
import org.objectweb.asm.tree.MethodNode;

import static io.prplz.skypixel.asm.ASMUtils.*;
import static org.objectweb.asm.Opcodes.RETURN;

public class GuiScreenPatcher extends ClassPatcher {

    /**
     * Insert call to {@link GuiScreenHook#sendChatMessage(String)} at the start of
     * {@link GuiScreen#sendChatMessage(String, boolean)}.
     *
     * <pre>
     * public void sendChatMessage(String msg, boolean addToChat) {
     *     if (GuiScreenHook.sendChatMessage(msg)) {
     *         return;
     *     }
     *     // Original method body
     * }
     * </pre>
     */
    @PatchMethod(name = {"sendChatMessage", "func_175281_b"}, desc = "(Ljava/lang/String;Z)V")
    public void patchSendChatMessage(MethodNode method) {
        method.instructions.insert(insnListOf(
                aload(1),
                invokeStatic("io/prplz/skypixel/asm/hook/GuiScreenHook", "sendChatMessage", "(Ljava/lang/String;)Z"),
                ifTrue(RETURN)
        ));
    }
}
