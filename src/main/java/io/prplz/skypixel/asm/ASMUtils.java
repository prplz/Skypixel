package io.prplz.skypixel.asm;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.List;

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

    public static InsnList ifFalse(Object... args) {
        LabelNode label = new LabelNode();
        return insnListOf(new JumpInsnNode(Opcodes.IFNE, label), insnListOf(args), label);
    }

    public static MethodInsnNode invokeStatic(String owner, String name, String desc) {
        return new MethodInsnNode(Opcodes.INVOKESTATIC, owner, name, desc, false);
    }

    public static VarInsnNode aload(int var) {
        return new VarInsnNode(Opcodes.ALOAD, var);
    }

    public static VarInsnNode iload(int var) {
        return new VarInsnNode(Opcodes.ILOAD, var);
    }

    public static boolean deobfMatch(MethodInsnNode methodInsn, String owner, List<String> name, String desc) {
        FMLDeobfuscatingRemapper remapper = FMLDeobfuscatingRemapper.INSTANCE;
        return owner.equals(remapper.map(methodInsn.owner))
                && name.contains(remapper.mapMethodName(methodInsn.owner, methodInsn.name, methodInsn.desc))
                && desc.equals(remapper.mapMethodDesc(methodInsn.desc));
    }
}
