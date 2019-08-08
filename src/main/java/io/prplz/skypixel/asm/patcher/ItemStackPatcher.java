package io.prplz.skypixel.asm.patcher;

import io.prplz.skypixel.asm.hook.ItemStackHook;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ItemStackPatcher extends ClassPatcher {

    /**
     * Insert call to {@link ItemStackHook#damageItem()} at the start of
     * {@link ItemStack#damageItem(int, EntityLivingBase)}.
     *
     * <pre>
     * public void damageItem(int amount, EntityLivingBase entityIn) {
     *     if (ItemStackHook.damageItem()) {
     *         return;
     *     }
     *     // Original method body
     * }
     * </pre>
     */
    @PatchMethod(name = {"damageItem", "func_77972_a"}, desc = "(ILnet/minecraft/entity/EntityLivingBase;)V")
    public void patchDamageItem(MethodNode method) {
        InsnList instructions = new InsnList();
        instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "io/prplz/skypixel/asm/hook/ItemStackHook", "damageItem", "()Z", false));
        LabelNode label = new LabelNode();
        instructions.add(new JumpInsnNode(Opcodes.IFEQ, label));
        instructions.add(new InsnNode(Opcodes.RETURN));
        instructions.add(label);
        method.instructions.insert(instructions);
    }
}
