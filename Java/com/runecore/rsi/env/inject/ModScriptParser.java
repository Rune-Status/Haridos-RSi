package com.runecore.rsi.env.inject;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import com.runecore.rsi.env.RefactorManager;
import com.runecore.rsi.env.inject.adapter.AddGetterAdapter;
import com.runecore.rsi.env.inject.adapter.AddInterfaceAdapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: Harry Andreas Date: 15/03/13 Time: 14:11 To
 * change this template use File | Settings | File Templates.
 */
public class ModScriptParser {

	private static ClassNode getNode(ClassNode[] nodes, String name) {
		for (ClassNode node : nodes) {
			if (node.name.equalsIgnoreCase(name)) {
				return node;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static FieldNode getField(ClassNode node, String name) {
		for (FieldNode fn : (List<FieldNode>) node.fields) {
			if (fn.name.equalsIgnoreCase(name)) {
				return fn;
			}
		}
		return null;
	}

	public static Map<ClassNode, List<InstructionAdapter>> parse(ClassNode[] nodes) throws Exception {
        Map<ClassNode, List<InstructionAdapter>> instructions = new HashMap<ClassNode, List<InstructionAdapter>>();
		for (ClassNode node : nodes) {
			instructions.put(node, new ArrayList<InstructionAdapter>());
		}
		URL script = new URL("http://www.runecore.org/rl/hooks.txt");
		URLConnection connection = script.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			String[] split = inputLine.split(" ");
			if(inputLine.isEmpty())
				continue;
			if (split[0].equalsIgnoreCase("addi")) {
				String classNode = split[1];
				String toInsert = split[3];
				ClassNode node = getNode(nodes, classNode);
				List<InstructionAdapter> list = instructions.get(node);
				list.add(new AddInterfaceAdapter(toInsert));
				instructions.put(node, list);
			} else if (split[0].equalsIgnoreCase("addnm")) {
				String insertInto = RefactorManager.getOriginal(split[1]);
				String nodeClass = split[2];
				String field = split[3];
				String desc = split[4];
				String methodName = split[5];
				ClassNode node = getNode(nodes, insertInto);
				ClassNode ownerNode = getNode(nodes, nodeClass);
				FieldNode fn = getField(ownerNode, field);
				List<InstructionAdapter> list = instructions.get(node);
				if (list == null) {
					list = new ArrayList<InstructionAdapter>();
				}
				list.add(new AddGetterAdapter(fn, nodeClass, methodName, desc,
						null));
				instructions.put(node, list);
			} else if (split[0].equalsIgnoreCase("addmi")) {
				String insertInto = RefactorManager.getOriginal(split[1]);
				String nodeClass = split[2];
				String field = split[3];
				String desc = split[4];
				String methodName = split[5];
				int multi = Integer.parseInt(split[6]);
				ClassNode node = getNode(nodes, insertInto);
				ClassNode ownerNode = getNode(nodes, nodeClass);
				FieldNode fn = getField(ownerNode, field);
				List<InstructionAdapter> list = instructions.get(node);
				if (list == null) {
					list = new ArrayList<InstructionAdapter>();
				}
				list.add(new AddGetterAdapter(fn, nodeClass, methodName, desc,
						new Multiplier(multi)));
				instructions.put(node, list);
			} else if (split[0].equalsIgnoreCase("refactor")) {
				String refactoredName = split[1];
				String originalName = split[2];
				RefactorManager.rename(originalName, refactoredName);
			} else {
				throw new RuntimeException("Instruction: " + split[0]
						+ " NOT SUPPORTED!");
			}
		}
		return instructions;
	}
}
