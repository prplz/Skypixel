package io.prplz.skypixel.asm.patch;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.function.Consumer;

public class ItemStackTransformer implements Consumer<ClassNode> {

    @Override
    public void accept(ClassNode classNode) {
        for (MethodNode method : classNode.methods) {
            if ((method.name.equals("damageItem") || method.name.equals("func_77972_a")) && method.desc.equals("(ILnet/minecraft/entity/EntityLivingBase;)V")) {
                /*
                 * if (ItemStackHook.damageItem()) {
                 *     return;
                 * }
                 */
                InsnList instructions = new InsnList();
                instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "io/prplz/skypixel/asm/hook/ItemStackHook", "damageItem", "()Z", false));
                LabelNode label = new LabelNode();
                instructions.add(new JumpInsnNode(Opcodes.IFEQ, label));
                instructions.add(new InsnNode(Opcodes.RETURN));
                instructions.add(label);
                method.instructions.insert(instructions);
            }
        }
    }
}
