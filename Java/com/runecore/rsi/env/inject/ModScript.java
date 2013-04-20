package com.runecore.rsi.env.inject;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Harry Andreas
 * Date: 15/03/13
 * Time: 14:29
 * To change this template use File | Settings | File Templates.
 */
public class ModScript {

    @SuppressWarnings("unchecked")
	public static void inject(Map<ClassNode, List<InstructionAdapter>> map, File file, ClassNode[] nodes) {
    	for (Map.Entry<ClassNode, List<InstructionAdapter>> entry : map.entrySet()) {
            List<InstructionAdapter> adapters = entry.getValue();
            for(InstructionAdapter ia : adapters) {
                if(entry.getKey() != null) {
                	ia.run(entry.getKey());
                }
            }
        }
        for(ClassNode node : nodes) {
        	if(node.superName.equalsIgnoreCase("java/awt/Canvas")) {
        		node.superName = "com/runecore/rsi/internal/ClientCanvas";
        		for (MethodNode mn : (List<MethodNode>) node.methods) {
        			if (mn.name.equals("<init>")) {
        				for (AbstractInsnNode insn : mn.instructions.toArray()) {
        					if (insn.getOpcode() == Opcodes.INVOKESPECIAL) {
        						MethodInsnNode min = (MethodInsnNode) insn;
        						if (min.owner.equals("java/awt/Canvas") && min.name.equals("<init>")) {
        							min.owner = "com/runecore/rsi/internal/ClientCanvas";
        							break;
        						}
        					}
        				}
        			}
        		}
        	}
        }
        
        try {
        	JarOutputStream out = new JarOutputStream(new FileOutputStream(file));
            for (ClassNode node : nodes) {
                JarEntry entry = new JarEntry(node.name + ".class");
                out.putNextEntry(entry);
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                node.accept(writer);
                out.write(writer.toByteArray());
            }
            out.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
