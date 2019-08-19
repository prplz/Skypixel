package io.prplz.skypixel.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ASMUtils {

    public static InsnList insnListOf(Object... args) {
        InsnList list = new InsnList();
        for (Object arg : args) {
            if (arg instanceof AbstractInsnNode) {
                list.add((AbstractInsnNode) arg);
            } else if (arg instanceof InsnList) {
                list.add((InsnList) arg);
            } else if (arg instanceof Integer) {
                list.add(new InsnNode((Integer) arg));
            } else {
                throw new IllegalArgumentException("Arguments must be AbstractInsnNode or InsnList");
            }
        }
        return list;
    }

    public static InsnList ifTrue(Object... args) {
        LabelNode label = new LabelNode();
        return insnListOf(new JumpInsnNode(Opcodes.IFEQ, label), insnListOf(args), label);
    }

    public static MethodInsnNode invokeStatic(String owner, String name, String desc) {
        return new MethodInsnNode(Opcodes.INVOKESTATIC, owner, name, desc, false);
    }

    public static VarInsnNode aload(int var) {
        return new VarInsnNode(Opcodes.ALOAD, var);
    }
}
