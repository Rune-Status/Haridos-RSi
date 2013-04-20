package com.runecore.rsi.internal;

import com.runecore.rsi.internal.node.GameObject;
import com.runecore.rsi.internal.node.Tile;

public interface SceneGraph {
	
	public GameObject[] getObjects();
	public Tile[][][] getTiles();

}
