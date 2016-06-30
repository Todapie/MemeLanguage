package Random;
import java.util.ArrayList;

import block.Block;
import block.Class;
import block.Method;
import block.VariableBlock;
import parser.ClassParser;
import parser.MethodParser;
import parser.Parser;
import parser.VariableParser;
import tokenizer.Tokenizer;

public class Runtime {
	
	private ArrayList<Class> classes;
	
	public Runtime() {
		this.classes = new ArrayList<Class>();
		String code = 	"class defaults" + "\n" + 
					  	"method printString requires (String s) returns void" + "\n";
		
		code += "class HelloWorld" + "\n" +
			    "method main requires () returns void" + "\n" +
		        "printString(str)" + "\n" +
		        "printString(\"Hello\")";

		Parser<?>[] parsers = new Parser<?>[] { new ClassParser(), new MethodParser(), new VariableParser() };

		Class main = null;
		
		Block block = null;
		
		boolean success = false;
		
		for (String line : code.split("\n")) {
			success = false;
			line = line.trim();
			Tokenizer tokenizer = new Tokenizer(line);
			
			for (Parser<?> parser : parsers) {
				
				if (parser.shouldParse(line)) {
					System.out.println(line);
					Block newBlock = parser.parse(block, tokenizer);
					
					if (newBlock instanceof Class) {
						classes.add((Class) newBlock);
					}
					
					else if (newBlock instanceof Method) {
						block.getBlockTree().get(0).addBlock(newBlock);
					}
					
					else {
						block.addBlock(newBlock);
					}
					
					block = newBlock;
					success = true;
					break;
				}
			}
			
			if(!success) {
				
				for (Class c : classes) {
					System.out.println(c.getName());
					ArrayList<Block> blockTree = c.getBlockTree();
					for(Block b: blockTree) {
						if(b instanceof Method) {
							String[] parts = line.split("\\(");
							System.out.println(((Method) b).getName());
							if(((Method) b).getName().equals(parts[0])) {
								success = true;
							}
							
						}
					}
				}
//				for(Block b: defaultMethodClass.getBlockTree()) {
//					System.out.println("HERE");
//					if(b instanceof Method) {
//						String[] parts = line.split("\\(");
//						System.out.println(((Method) b).getName());
//						if(((Method) b).getName().equals(parts[0])) {
//							success = true;
//						}
//						
//					}
//				}
			}
			
			if (!success) {
				throw new IllegalArgumentException("Invalid line " + line);
			}
		}
		
		for (Class c : classes) {
			for (Block b : c.getSubBlocks()) {
				if (b instanceof Method) {
					Method method = (Method) b;
					if (method.getName().equals("main") && method.getType().equals("void") && method.getParameters().length == 0) {
						main = c;
					}
				}
			}
		}
		
		if (main == null) {
			throw new IllegalStateException("No main method.");
		}
		
		main.run();
	}

	public static void main(String[] args) {
		new Runtime();
		System.out.println();
	}
}