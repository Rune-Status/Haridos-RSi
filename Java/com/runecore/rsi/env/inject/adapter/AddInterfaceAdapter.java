package com.runecore.rsi.env.inject.adapter;

import org.objectweb.asm.tree.ClassNode;

import com.runecore.rsi.env.inject.InstructionAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: Harry Andreas
 * Date: 15/03/13
 * Time: 14:48
 * To change this template use File | Settings | File Templates.
 */
public class AddInterfaceAdapter implements InstructionAdapter {

    private String[] interfacesToAdd;

    public AddInterfaceAdapter(String... interfacesToAdd) {
        this.interfacesToAdd = interfacesToAdd;
    }

    @SuppressWarnings("unchecked")
	@Override
    public void run(ClassNode node) {
        for(String s : interfacesToAdd) {
        	node.interfaces.add(s);
        }
    }

}