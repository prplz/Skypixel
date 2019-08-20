package io.prplz.skypixel.asm.patcher;

import io.prplz.skypixel.asm.hook.RenderItemHook;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Iterator;

import static io.prplz.skypixel.asm.ASMUtils.*;

public class RenderItemPatcher extends ClassPatcher {

    /**
     * Insert call to {@link RenderItemHook#renderItemOverlayIntoGUI(ItemStack, int, int)} before all returns of
     * {@link RenderItem#renderItemOverlayIntoGUI(FontRenderer, ItemStack, int, int, String)}
     */
    @PatchMethod(name = {"renderItemOverlayIntoGUI", "func_180453_a"}, desc = "(Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V")
    public void patchRenderItemOverlayIntoGUI(MethodNode method) {
        for (Iterator<AbstractInsnNode> iter = method.instructions.iterator(); iter.hasNext(); ) {
            AbstractInsnNode insn = iter.next();
            if (insn.getOpcode() == Opcodes.RETURN) {
                method.instructions.insertBefore(insn, insnListOf(
                        aload(2), // item
                        iload(3), // x
                        iload(4), // y
                        invokeStatic("io/prplz/skypixel/asm/hook/RenderItemHook", "renderItemOverlayIntoGUI", "(Lnet/minecraft/item/ItemStack;II)V")
                ));
            }
        }
    }
}
