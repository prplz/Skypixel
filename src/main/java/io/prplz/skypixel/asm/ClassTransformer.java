package io.prplz.skypixel.asm;

import io.prplz.skypixel.asm.patch.ItemStackTransformer;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ClassTransformer implements IClassTransformer {

    private final Map<String, Consumer<ClassNode>> transformers = new HashMap<>();

    public ClassTransformer() {
        transformers.put("net.minecraft.item.ItemStack", new ItemStackTransformer());
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        Consumer<ClassNode> transformer = transformers.get(transformedName);
        if (transformer != null) {
            ClassReader reader = new ClassReader(bytes);
            ClassNode classNode = new ClassNode();
            reader.accept(classNode, ClassReader.SKIP_FRAMES);
            transformer.accept(classNode);
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            classNode.accept(writer);
            return writer.toByteArray();
        }
        return bytes;
    }
}
