package intexpr;

import java.io.File;
import java.io.IOException;
import java.util.List;

import edu.mit.eecs.parserlib.ParseTree;
import edu.mit.eecs.parserlib.Parser;
import edu.mit.eecs.parserlib.UnableToParseException;
import edu.mit.eecs.parserlib.Visualizer;

public class IntegerExpressionParser {

    /**
     * Main method. Parses and evaluates an example expression.
     * 
     * @param args command line arguments, not used
     * @throws UnableToParseException if example expression can't be parsed
     */
    public static void main(final String[] args) throws UnableToParseException {
        String input = "54+(2+ 89)";
        // String input = "TODO"; // different parse tree, same AST
        // String input = "TODO"; // different parse tree, different AST, same AST leaf
        // nodes (54, 2, 89 in that order)
        // String input = "TODO"; // parse tree with fewest possible "primary" nodes,
        // same AST leaf nodes in order

        System.out.println(input);
        System.out.println("value " + value);
    }

    // the nonterminals of the grammar
    private static enum IntegerGrammar {
        EXPR, SUM, PRIMARY, NUMBER, WHITESPACE,
    }

    private static Parser<IntegerGrammar> parser = makeParser();

    /**
     * Compile the grammar into a parser.
     * 
     * @param grammarFilename <b>Must be in this class's Java package.</b>
     * @return parser for the grammar
     * @throws RuntimeException if grammar file can't be read or has syntax errors
     */
    private static Parser<IntegerGrammar> makeParser() {
        try {
            // read the grammar as a file, relative to the project root.
            final File grammarFile = new File("src/intexpr/IntegerExpression.g");
            return Parser.compile(grammarFile, IntegerGrammar.EXPR);

            // A better way would read the grammar as a "classpath resource", which would
            // allow this code
            // to be packed up in a jar and still be able to find its grammar file:
            //
            // final InputStream grammarStream =
            // IntegerExpression.class.getResourceAsStream("IntegerExpression.g");
            // return Parser.compile(grammarStream, IntegerGrammar.EXPR);
            //
            // See
            // http://www.javaworld.com/article/2077352/java-se/smartly-load-your-properties.html
            // for a discussion of classpath resources.

            // Parser.compile() throws two checked exceptions.
            // Translate these checked exceptions into unchecked RuntimeExceptions,
            // because these failures indicate internal bugs rather than client errors
        } catch (IOException e) {
            throw new RuntimeException("can't read the grammar file", e);
        } catch (UnableToParseException e) {
            throw new RuntimeException("the grammar has a syntax error", e);
        }
    }

    /**
     * Parse a string into an expression.
     * 
     * @param string string to parse
     * @return IntegerExpression(AST) parsed from the string
     * @throws UnableToParseException if the string doesn't match the
     *                                IntegerExpression grammar
     */
    public static IntegerExpression parse(final String string) throws UnableToParseException {
        // parse the example into a parse tree
        final ParseTree<IntegerGrammar> parseTree = parser.parse(string);
        System.out.println("parse tree:\n" + parseTree);

        // display the parse tree in a web browser, for debugging only
        Visualizer.showInBrowser(parseTree);

        // make an AST from the parse tree
        final IntegerExpression expression = makeAbstractSyntaxTree(parseTree);
        System.out.println("abstract syntax tree:\n" + expression);

        return expression;
    }

    /**
     * Convert a parse tree into an abstract syntax tree.
     * 
     * @param parseTree constructed according to the grammar in IntegerExpression.g
     * @return abstract syntax tree corresponding to parseTree
     */
    private static IntegerExpression makeAbstractSyntaxTree(final ParseTree<IntegerGrammar> parseTree) {
        switch (parseTree.name()) {
            case EXPR: // expr ::= sum;
            {
                final ParseTree<IntegerGrammar> child = parseTree.children().get(0);
                return makeAbstractSyntaxTree(child);
            }

            case SUM: // sum ::= primary ('+' primary)*;
            {
                final List<ParseTree<IntegerGrammar>> children = parseTree.children();
                IntegerExpression expression = makeAbstractSyntaxTree(children.get(0));
                for (int i = 1; i < children.size(); ++i) {
                    expression = new Plus(expression, makeAbstractSyntaxTree(children.get(i)));
                }
                return expression;
            }

            case PRIMARY: // primary ::= number | '(' sum ')';
            {
                final ParseTree<IntegerGrammar> child = parseTree.children().get(0);
                // check which alternative (number or sum) was actually matched
                switch (child.name()) {
                    case NUMBER:
                        return makeAbstractSyntaxTree(child);
                    case SUM:
                        return makeAbstractSyntaxTree(child); // in this case, we do the
                                                              // same thing either way
                    default:
                        throw new AssertionError("should never get here");
                }
            }

            case NUMBER: // number ::= [0-9]+;
            {
                final int n = Integer.parseInt(parseTree.text());
                return new Number(n);
            }

            default:
                throw new AssertionError("should never get here");
        }
    }
}
