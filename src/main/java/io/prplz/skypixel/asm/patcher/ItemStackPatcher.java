package io.prplz.skypixel.asm.patcher;

import io.prplz.skypixel.asm.hook.ItemStackHook;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.tree.MethodNode;

import static io.prplz.skypixel.asm.ASMUtils.*;
import static org.objectweb.asm.Opcodes.RETURN;

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
        method.instructions.insert(insnListOf(
                invokeStatic("io/prplz/skypixel/asm/hook/ItemStackHook", "damageItem", "()Z"),
                ifTrue(RETURN)
        ));
    }
}
