package com.runecore.rsi.internal.node;

public interface Actor {
	
    public boolean isAnimating();
    public String getTextSpoken();
    
    public int[] getHitCycles();
    public int[] getHitDamages();
    public int[] getHitTypes();
    public int getAnimation();
    
    public int getX();
    public int getY();
    
    public int[] getStepsX();
    public int[] getStepsY();
    public boolean[] getRunSteps();

}
