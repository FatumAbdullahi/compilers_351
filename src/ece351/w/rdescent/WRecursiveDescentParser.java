/* *********************************************************************
 * ECE351 
 * Department of Electrical and Computer Engineering 
 * University of Waterloo 
 * Term: Summer 2016 (1165)
 *
 * The base version of this file is the intellectual property of the
 * University of Waterloo. Redistribution is prohibited.
 *
 * By pushing changes to this file I affirm that I am the author of
 * all changes. I affirm that I have complied with the course
 * collaboration policy and have not plagiarized my work. 
 *
 * I understand that redistributing this file might expose me to
 * disciplinary action under UW Policy 71. I understand that Policy 71
 * allows for retroactive modification of my final grade in a course.
 * For example, if I post my solutions to these labs on GitHub after I
 * finish ECE351, and a future student plagiarizes them, then I too
 * could be found guilty of plagiarism. Consequently, my final grade
 * in ECE351 could be retroactively lowered. This might require that I
 * repeat ECE351, which in turn might delay my graduation.
 *
 * https://uwaterloo.ca/secretariat-general-counsel/policies-procedures-guidelines/policy-71
 * 
 * ********************************************************************/

package ece351.w.rdescent;

import org.parboiled.common.ImmutableList;

import ece351.util.Lexer;
import ece351.w.ast.WProgram;
import ece351.w.ast.Waveform;

public final class WRecursiveDescentParser {
    private final Lexer lexer;

    public WRecursiveDescentParser(final Lexer lexer) {
        this.lexer = lexer;
    }

    public static WProgram parse(final String input) {
    	final WRecursiveDescentParser p = new WRecursiveDescentParser(new Lexer(input));
        return p.parse();
    }

    public WProgram parse() {
    	// STUB: return null;
// TODO: longer code snippet
//throw new ece351.util.Todo351Exception();
    	final String[] bit_tokens = {"0","1"};
    	
    	WProgram wp = new WProgram();
    	
    	while (!lexer.inspectEOF()) {
            Waveform wf = new Waveform();
            
            while (lexer.inspectID()) {
	    		String id = lexer.consumeID();
	    		wf = wf.rename(id);
        	}

    		lexer.consume(":");

        	
        	while (lexer.inspect(bit_tokens)) {
        		if (lexer.inspect(bit_tokens)) {
            		String bit = lexer.consume(bit_tokens);
            		wf = wf.append(bit);
            	}
        	}
        	
        	if (lexer.inspect(";")) {
        		lexer.consume(";");
        	} else {
        		throw new RuntimeException();
        	}
        	
        	wp = wp.append(wf);
        	
        }
    	
    	return wp;
    	
    
    }
    
}
