package block;

import java.util.Arrays;

import Random.BuiltInType;
import Random.Parameter;
import Random.Type;
import Random.Value;
import Random.Variable;

public class Method extends Block {
	
	private String name, type;
	private Parameter[] params;
	private Value returnValue;

	public Method(Block superBlock, String name, String type, Parameter[] params) {
		super(superBlock);
		
		this.name = name;
		this.type = type;
		this.params = params;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public Parameter[] getParameters() {
		return params;
	}

	@Override
	public void run() {
		invoke();
	}
	
	public Value invoke(Value... values) {
		Type t = Type.match(type);
		
		if (values.length != params.length) {
			throw new IllegalArgumentException("Wrong number of values for parameters.");
		}
		
		for (int i = 0; i < values.length && i < params.length; i++) {
			Parameter p = params[i];
			Value v = values[i];
			
			if (p.getType() != v.getType()) {
				throw new IllegalStateException("Parameter " + p.getName() + " should be " + p.getType() + ". Got " + v.getType());
			}
			
			addVariable(new Variable(this, p.getType(), p.getName(), v.getValue()));
		}
		
		for (Block b : getSubBlocks()) {
			b.run();
			
			if (returnValue != null) {
				break;
			}
		}
		
		if (returnValue == null && t != BuiltInType.VOID) {
			throw new IllegalStateException("Expected return value, got none.");
		}
		
		Value localReturnValue = returnValue;
		returnValue = null;
		return localReturnValue;
	}
}