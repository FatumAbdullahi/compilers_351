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

package ece351.f.parboiled;

import org.parboiled.Rule;

import ece351.common.ast.AndExpr;
import ece351.common.ast.AssignmentStatement;
import ece351.common.ast.ConstantExpr;
import ece351.common.ast.Constants;
import ece351.common.ast.Expr;
import ece351.common.ast.NotExpr;
import ece351.common.ast.OrExpr;
import ece351.common.ast.VarExpr;
import ece351.f.ast.FProgram;
import ece351.util.CommandLine;

// Parboiled requires that this class not be final
public /*final*/ class FParboiledParser extends FBase implements Constants {

	
	public static void main(final String[] args) {
    	final CommandLine c = new CommandLine(args);
    	final String input = c.readInputSpec();
    	final FProgram fprogram = parse(input);
    	assert fprogram.repOk();
    	final String output = fprogram.toString();
    	
    	// if we strip spaces and parens input and output should be the same
    	if (strip(input).equals(strip(output))) {
    		// success: return quietly
    		return;
    	} else {
    		// failure: make a noise
    		System.err.println("parsed value not equal to input:");
    		System.err.println("    " + strip(input));
    		System.err.println("    " + strip(output));
    		System.exit(1);
    	}
    }
	
	private static String strip(final String s) {
		return s.replaceAll("\\s", "").replaceAll("\\(", "").replaceAll("\\)", "");
	}
	
	public static FProgram parse(final String inputText) {
		final FProgram result = (FProgram) process(FParboiledParser.class, inputText).resultValue;
		assert result.repOk();
		return result;
	}

	@Override
	public Rule Program() {
// TODO: longer code snippet

		return Sequence(
				push(new FProgram()),
				// [FProgram]
				Sequence(OneOrMore(Formula()), W0(), EOI)
		);
	}

	public Rule Formula() {
		// handles trailing W
		return Sequence(
				Var(),
				// [FProgram, VarExpr]
				W0(), "<=", W0(),
				Expr(),
				// [FProgram, VarExpr, Expr]
				swap(),
				push(new AssignmentStatement((VarExpr)pop(),(Expr)pop())),
				// [FProgram, AssignmentStatement]
				swap(),
				push(((FProgram)pop()).append(pop())),
				// [FProgram]
				W0(), ";", W0()
		);
	}

	public Rule Expr() {

		return Sequence(
				Term(),
				ZeroOrMore(
						Sequence(
								W1(), "or", W1(),
								Term(),
								swap(),
								push(new OrExpr((Expr)pop(),(Expr)pop()))
						)
				)
		);
	}

	public Rule Term() {
		return Sequence(
				Factor(),
				// [FProgram, VarExpr, Var/ConstExpr, Var/ConstExpr]
				ZeroOrMore(
						Sequence(
								W1(), "and", W1(),
								Factor(),
								swap(),
								push(new AndExpr((Expr)pop(),(Expr)pop()))
							)
					)
				);
	}

	public Rule Factor() {
		return FirstOf(
				Sequence(
						"not", W1(),
						Factor(),
						push(new NotExpr((Expr)pop()))
				),
				Sequence("(", W0(), Expr(), W0(), ")"), Var(), Constant()
		);
	}

	public Rule Constant() {
//		return CharRange('0', '1');
//		return FirstOf(Sequence("'", "0", "'"), Sequence("'","1", "'"));
		return Sequence(
				W0(), "'", FirstOf("0","1"),
				push(ConstantExpr.make(match())),
				"'"
		);
	}

	public Rule Var() {

		return Sequence(
				Sequence(Letter(), ZeroOrMore(FirstOf(Letter(), CharRange('0', '9'), '_'))),
				push(new VarExpr(match()))
		);
	}

	public Rule Letter() {
		return FirstOf(CharRange('A', 'Z'), CharRange('a', 'z'));
	}
}
