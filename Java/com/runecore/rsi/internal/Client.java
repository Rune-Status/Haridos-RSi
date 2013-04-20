package com.runecore.rsi.internal;

import com.runecore.rsi.internal.node.Widget;
import com.runecore.rsi.internal.node.actor.NPC;
import com.runecore.rsi.internal.node.actor.Player;

public interface Client {
	
    public int[] getSkills();
    public int[] getBaseSkills();
    public int[] getExperienceSkills();
    public int[] getSettings();
    
    public Player getLocalPlayer();
    public Player[] getPlayerArray();
    public NPC[] getNPCArray();
    
    public Object[][][] getGroundItems();
    public Widget[][] getWidgets();
    
    public int getFPS();
    public int getClientState();
    public int getCameraX();
    public int getCameraY();
    public int getCameraZ();

}