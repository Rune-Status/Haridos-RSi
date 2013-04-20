package com.runecore.rsi.internal.node.actor;

import com.runecore.rsi.internal.def.NPCDefinition;
import com.runecore.rsi.internal.node.Actor;

public interface NPC extends Actor {
	
	public NPCDefinition getDefinition();

}
