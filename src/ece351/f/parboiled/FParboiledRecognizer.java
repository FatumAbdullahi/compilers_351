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

//import org.apache.tools.ant.types.resources.First;
import org.parboiled.Rule;

import ece351.common.ast.Constants;
import ece351.util.CommandLine;

//Parboiled requires that this class not be final
public /*final*/ class FParboiledRecognizer extends FBase implements Constants {

	
	public static void main(final String... args) {
		final CommandLine c = new CommandLine(args);
    	process(FParboiledRecognizer.class, c.readInputSpec());
    }

	@Override
	public Rule Program() {
		// STUB: return NOTHING; // TODO: replace this stub
		// For the grammar production Id, ensure that the Id does not match any of the keywords specified
		// in the rule, 'Keyword'
// TODO: longer code snippet
		return Sequence(OneOrMore(Formula()), W0(), EOI);
	}

	public Rule Formula() {
		// handles trailing W
		return Sequence(Var(), W0(), "<=", W0(), Expr(), W0(), ";", W0());
	}

	public Rule Expr() {

		return Sequence(Term(), ZeroOrMore(W1(), "or", W1(), Term()));
	}

	public Rule Term() {
		return Sequence(Factor(), ZeroOrMore(W1(), "and", W1(), Factor()));
	}

	public Rule Factor() {
		return FirstOf(Sequence("not", W1(), Factor()), Sequence("(", W0(), Expr(), W0(), ")"), Var(), Constant());
	}

	public Rule Constant() {
//		return CharRange('0', '1');
//		return FirstOf(Sequence("'", "0", "'"), Sequence("'","1", "'"));
		return Sequence(W0(), "'", FirstOf("0","1"), "'");
	}

	public Rule Var() {

		return Sequence(Letter(), ZeroOrMore(FirstOf(Letter(), CharRange('0', '9'), '_')));
	}

	public Rule Letter() {
		return FirstOf(CharRange('A', 'Z'), CharRange('a', 'z'));
	}
}
