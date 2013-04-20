package com.runecore.rsi.env.inject;

import org.objectweb.asm.Opcodes;

/**
 * Created with IntelliJ IDEA.
 * User: Harry Andreas
 * Date: 15/03/13
 * Time: 14:54
 * To change this template use File | Settings | File Templates.
 */
public class Multiplier {

    private enum MULTIPLIER_TYPE {
        INTEGER, DOUBLE, FLOAT, LONG
    }

    private int i = -1;
    private double d = -1;
    private float f = -1;
    private long l = -1;

    public Multiplier(final int i) {
        this.i = i;
    }

    public Multiplier(final double d) {
        this.d = d;
    }

    public Multiplier(final float f) {
        this.f = f;
    }

    public Multiplier(final long l) {
        this.l = l;
    }

    private MULTIPLIER_TYPE getType() {
        return i != -1 ? MULTIPLIER_TYPE.INTEGER : d != -1 ? MULTIPLIER_TYPE.DOUBLE : f != -1 ? MULTIPLIER_TYPE.FLOAT : MULTIPLIER_TYPE.LONG;
    }

    public Object getMultiple() {
        final MULTIPLIER_TYPE type = getType();
        if (type == MULTIPLIER_TYPE.INTEGER) {
            return i;
        } else if (type == MULTIPLIER_TYPE.DOUBLE) {
            return d;
        } else if (type == MULTIPLIER_TYPE.FLOAT) {
            return f;
        } else {
            return l;
        }
    }

    public int getMUL() {
        final MULTIPLIER_TYPE type = getType();
        if (type == MULTIPLIER_TYPE.INTEGER) {
            return Opcodes.IMUL;
        } else if (type == MULTIPLIER_TYPE.DOUBLE) {
            return Opcodes.DMUL;
        } else if (type == MULTIPLIER_TYPE.FLOAT) {
            return Opcodes.FMUL;
        } else {
            return Opcodes.LMUL;
        }
    }

    public int getRETURN() {
        final MULTIPLIER_TYPE type = getType();
        if (type == MULTIPLIER_TYPE.INTEGER) {
            return Opcodes.IRETURN;
        } else if (type == MULTIPLIER_TYPE.DOUBLE) {
            return Opcodes.DRETURN;
        } else if (type == MULTIPLIER_TYPE.FLOAT) {
            return Opcodes.FRETURN;
        } else {
            return Opcodes.LRETURN;
        }
    }
}
