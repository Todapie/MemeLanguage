package parser;

import java.util.ArrayList;

import Random.BuiltInType;
import Random.Parameter;
import block.Block;
import block.Method;
import tokenizer.Token;
import tokenizer.Tokenizer;

public class MethodParser extends Parser<Method> {

	@Override
	public boolean shouldParse(String line) {
		String[] firstSplit = line.split("\\(");
		String[] secondSplit , params;
		if(firstSplit.length == 2) {
			secondSplit = firstSplit[1].split("\\)");
			params = secondSplit[0].split(",");
		}
		else {
			return false;
		}

		
		if(firstSplit[0].matches("method [a-zA-Z][a-zA-Z0-9]* requires ")){
			for(String s : params) {
				 if(!s.equals("") && !s.matches("[a-zA-Z][a-zA-Z0-9]* [a-zA-Z][a-zA-Z0-9]*")){
					 System.out.println("false");
					 return false;
				 }
			}
			
			if(secondSplit[1].matches(" returns [a-zA-Z][a-zA-Z0-9]*")) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public Method parse(Block superBlock, Tokenizer tokenizer) {
		tokenizer.nextToken(); // Skip the method token.
		
		String name = tokenizer.nextToken().getToken();
		
		tokenizer.nextToken(); // Skip the requires token.
		tokenizer.nextToken(); // Skip the ( token.
		
		Token first = tokenizer.nextToken();
		
		ArrayList<Parameter> params = new ArrayList<>();
		
		if (!first.getToken().equals(")")) {
			String[] paramData = new String[] { first.getToken(), null }; // 0 = type, 1 = value
			
			while (tokenizer.hasNextToken()) {
				Token token = tokenizer.nextToken();
				
				if (token.getToken().equals(")")) {
					break;
				}
				
				if (paramData[0] == null) {
					paramData[0] = token.getToken();
				}
				
				else {
					paramData[1] = token.getToken();
					
					params.add(new Parameter(BuiltInType.valueOf(paramData[0].toUpperCase()), paramData[1]));
					
					paramData = new String[2];
				}
			}
		}
		
		tokenizer.nextToken(); // Skip the returns token.
		
		String returnType = tokenizer.nextToken().getToken();
		
		return new Method(superBlock, name, returnType, params.toArray(new Parameter[params.size()]));
	}
}