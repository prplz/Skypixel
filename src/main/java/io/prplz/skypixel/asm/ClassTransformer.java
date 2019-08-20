package io.prplz.skypixel.asm;

import io.prplz.skypixel.asm.patcher.ClassPatcher;
import io.prplz.skypixel.asm.patcher.GuiScreenPatcher;
import io.prplz.skypixel.asm.patcher.ItemStackPatcher;
import io.prplz.skypixel.asm.patcher.RenderItemPatcher;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.util.HashMap;
import java.util.Map;

public class ClassTransformer implements IClassTransformer {

    private final Map<String, ClassPatcher> patchers = new HashMap<>();

    public ClassTransformer() {
        patchers.put("net.minecraft.item.ItemStack", new ItemStackPatcher());
        patchers.put("net.minecraft.client.gui.GuiScreen", new GuiScreenPatcher());
        patchers.put("net.minecraft.client.renderer.entity.RenderItem", new RenderItemPatcher());
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        ClassPatcher patcher = patchers.get(transformedName);
        if (patcher != null) {
            ClassReader reader = new ClassReader(bytes);
            ClassNode classNode = new ClassNode();
            reader.accept(classNode, ClassReader.SKIP_FRAMES);
            patcher.patch(classNode);
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            classNode.accept(writer);
            return writer.toByteArray();
        }
        return bytes;
    }
}
