package block;

import java.util.ArrayList;
import java.util.Collections;

import Random.Variable;
import parser.ClassParser;
import parser.MethodParser;
import parser.Parser;
import parser.VariableParser;
import tokenizer.Tokenizer;

/**
 * Represents a block of code.
 */
public abstract class Block {

	private Block superBlock;
	private ArrayList<Block> subBlocks;
	private ArrayList<Variable> variables;
	
	public Block(Block superBlock) {
		this.superBlock = superBlock;
		this.subBlocks = new ArrayList<>();
		this.variables = new ArrayList<>();
	}
	
	public Block getSuperBlock() {
		return superBlock;
	}
	
	public ArrayList<Block> getBlockTree() {
		ArrayList<Block> blocks = new ArrayList<Block>();
		
		Block block = this;
		
		do {
			blocks.add(block);
			block = block.getSuperBlock();
		} while (block != null);
		
		Collections.reverse(blocks);
		
		return blocks;
	}
	
	public Block[] getSubBlocks() {
		return subBlocks.toArray(new Block[subBlocks.size()]);
	}
	
	public void addBlock(Block block) {
		subBlocks.add(block);
	}
	
	public Variable getVariable(String name) {
		// Check the superblocks first.
		
		for (Variable v : variables) {
			if (v.getName().equals(name)) {
				return v;
			}
		}
		
		return null;
	}
	
	public void addVariable(Variable v) {
		variables.add(v);
	}
	
	public abstract void run();
}