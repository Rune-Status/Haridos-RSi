package com.runecore.rsi.env.inject.adapter;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.runecore.rsi.env.inject.InstructionAdapter;
import com.runecore.rsi.env.inject.Multiplier;

/**
 * Created with IntelliJ IDEA.
 * User: Harry Andreas
 * Date: 15/03/13
 * Time: 14:51
 * To change this template use File | Settings | File Templates.
 */
public class AddGetterAdapter implements InstructionAdapter, Opcodes {

    private final FieldNode fieldNode;
    private final String methodName, desc, owner;
    private final Multiplier multi;

    public AddGetterAdapter(FieldNode fieldNode, String owner, String methodName, String desc, Multiplier multi) {
             this.fieldNode = fieldNode;
        this.methodName = methodName;
        this.desc = desc;
        this.multi = multi;
        this.owner = owner;
    }

    @Override
    public void run(ClassNode node) {
		if (fieldNode == null) {
			System.out.println("Field " + owner + "." + methodName
					+ " is null?");
			return;
		}
		final MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, methodName, "()" + desc, null, null);
		method.visitVarInsn(Opcodes.ALOAD, 0);
		method.visitFieldInsn((fieldNode.access & ACC_STATIC) == ACC_STATIC ? GETSTATIC
			: GETFIELD, owner, fieldNode.name, fieldNode.desc);
		if(multi != null) {
			method.visitLdcInsn(multi.getMultiple());
			method.visitInsn(IMUL);
		}
		method.visitInsn(Type.getType(fieldNode.desc).getOpcode(IRETURN));
		method.visitMaxs(0, 0);
		node.methods.add(method);
    }
}
