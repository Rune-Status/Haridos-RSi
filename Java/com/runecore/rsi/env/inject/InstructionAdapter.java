package com.runecore.rsi.env.inject;

import org.objectweb.asm.tree.ClassNode;

/**
 * Created with IntelliJ IDEA.
 * User: Harry Andreas
 * Date: 15/03/13
 * Time: 14:47
 * To change this template use File | Settings | File Templates.
 */
public interface InstructionAdapter {

    public void run(ClassNode node);

}