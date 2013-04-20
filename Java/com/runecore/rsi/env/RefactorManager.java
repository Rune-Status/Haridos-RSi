package com.runecore.rsi.env;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Harry Andreas
 * Date: 15/03/13
 * Time: 14:14
 * To change this template use File | Settings | File Templates.
 */
public class RefactorManager {

    private static final Map<String, String> refactored = new HashMap<String, String>();

    public static Map<String, String> getNames() {
        return refactored;
    }

    public static void rename(String from, String to) {
        refactored.put(to, from);
    }

    public static String getOriginal(String refactoredName) {
        return refactored.get(refactoredName);
    }

}