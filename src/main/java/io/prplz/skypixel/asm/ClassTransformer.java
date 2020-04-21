package io.prplz.skypixel.asm;

import io.prplz.skypixel.asm.patcher.ClassPatcher;
import io.prplz.skypixel.asm.patcher.ClientCommandHandlerPatcher;
import io.prplz.skypixel.asm.patcher.EntityRendererPatcher;
import io.prplz.skypixel.asm.patcher.GuiPlayerTabOverlayPatcher;
import io.prplz.skypixel.asm.patcher.ItemStackPatcher;
import io.prplz.skypixel.asm.patcher.RenderItemPatcher;
import io.prplz.skypixel.asm.patcher.RenderManagerPatcher;
import io.prplz.skypixel.asm.patcher.TileEntityEndPortalRendererPatcher;
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
        patchers.put("net.minecraft.client.renderer.entity.RenderItem", new RenderItemPatcher());
        patchers.put("net.minecraftforge.client.ClientCommandHandler", new ClientCommandHandlerPatcher());
        patchers.put("net.minecraft.client.renderer.entity.RenderManager", new RenderManagerPatcher());
        patchers.put("net.minecraft.client.renderer.EntityRenderer", new EntityRendererPatcher());
        patchers.put("net.minecraft.client.renderer.tileentity.TileEntityEndPortalRenderer", new TileEntityEndPortalRendererPatcher());
        patchers.put("net.minecraft.client.gui.GuiPlayerTabOverlay", new GuiPlayerTabOverlayPatcher());
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
