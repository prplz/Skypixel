package io.prplz.skypixel.asm.patcher;

import io.prplz.skypixel.asm.hook.ClientCommandHandlerHook;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.client.ClientCommandHandler;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

import static io.prplz.skypixel.asm.ASMUtils.*;

public class ClientCommandHandlerPatcher extends ClassPatcher {

    /**
     * Insert call to {@link ClientCommandHandlerHook#executeCommand(String)} at the start of forge's
     * {@link ClientCommandHandler#executeCommand(ICommandSender, String)}
     *
     * <pre>
     * public int executeCommand(ICommandSender sender, String message) {
     *     if (ClientCommandHandlerHook.executeCommand(message) {
     *         return 0;
     *     }
     *     // Original method body
     * }
     * </pre>
     */
    @PatchMethod(name = {"executeCommand", "func_71556_a"}, desc = "(Lnet/minecraft/command/ICommandSender;Ljava/lang/String;)I")
    public void patchExecuteCommand(MethodNode method) {
        method.instructions.insert(insnListOf(
                aload(2), // message
                invokeStatic("io/prplz/skypixel/asm/hook/ClientCommandHandlerHook", "executeCommand", "(Ljava/lang/String;)Z"),
                ifTrue(Opcodes.ICONST_0, Opcodes.IRETURN)
        ));
    }
}
