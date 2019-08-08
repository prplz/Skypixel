package io.prplz.skypixel.asm.patcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public abstract class ClassPatcher {

    private static final Logger logger = LogManager.getLogger();

    public void patch(ClassNode classNode) {
        dispatchMethodPatchers(classNode);
    }

    public void dispatchMethodPatchers(ClassNode classNode) {
        for (Method method : getClass().getMethods()) {
            PatchMethod annotation = method.getAnnotation(PatchMethod.class);
            if (annotation != null) {
                List<String> names = Arrays.asList(annotation.name());
                int found = 0;
                for (MethodNode methodNode : classNode.methods) {
                    if (methodNode.desc.equals(annotation.desc()) && names.contains(methodNode.name)) {
                        found++;
                        try {
                            method.invoke(this, methodNode);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
                if (found == 0) {
                    logger.warn("Couldn't find {}{}", names, annotation.desc());
                } else if (found > 1) {
                    logger.warn("Found {} methods matching {}{}", found, names, annotation.desc());
                }
            }
        }
    }
}
