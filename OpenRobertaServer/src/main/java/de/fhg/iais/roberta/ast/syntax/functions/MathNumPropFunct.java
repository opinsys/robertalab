package de.fhg.iais.roberta.ast.syntax.functions;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.transformer.ExprParam;
import de.fhg.iais.roberta.ast.transformer.JaxbAstTransformer;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>math_number_property</b> block from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * The user must provide name of the function and list of parameters. <br>
 * To create an instance from this class use the method {@link #make(FunctionNames, List, BlocklyBlockProperties, BlocklyComment)}.<br>
 * The enumeration {@link FunctionNames} contains all allowed functions.
 */
public class MathNumPropFunct<V> extends Function<V> {
    private final FunctionNames functName;
    private final List<Expr<V>> param;

    private MathNumPropFunct(FunctionNames name, List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Kind.MATH_NUM_PROP_FUNCT, properties, comment);
        Assert.isTrue(name != null && param != null);
        this.functName = name;
        this.param = param;
        setReadOnly();
    }

    /**
     * Creates instance of {@link MathNumPropFunct}. This instance is read only and can not be modified.
     *
     * @param name of the function; must be <b>not</b> null,
     * @param param list of parameters for the function; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @return read only object of class {@link MathNumPropFunct}
     */
    public static <V> MathNumPropFunct<V> make(FunctionNames name, List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new MathNumPropFunct<V>(name, param, properties, comment);
    }

    /**
     * @return name of the function
     */
    public FunctionNames getFunctName() {
        return this.functName;
    }

    /**
     * @return list of parameters for the function
     */
    public List<Expr<V>> getParam() {
        return this.param;
    }

    @Override
    public int getPrecedence() {
        return this.functName.getPrecedence();
    }

    @Override
    public Assoc getAssoc() {
        return this.functName.getAssoc();
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitMathNumPropFunct(this);
    }

    @Override
    public String toString() {
        return "MathNumPropFunct [" + this.functName + ", " + this.param + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, JaxbAstTransformer<V> helper) {
        boolean divisorInput = block.getMutation().isDivisorInput();
        String op = helper.extractOperation(block, BlocklyConstants.PROPERTY);
        List<ExprParam> exprParams = new ArrayList<ExprParam>();
        exprParams.add(new ExprParam(BlocklyConstants.NUMBER_TO_CHECK, Integer.class));

        if ( op.equals(BlocklyConstants.DIVISIBLE_BY) ) {
            Assert.isTrue(divisorInput, "Divisor input is not equal to true!");
            exprParams.add(new ExprParam(BlocklyConstants.DIVISOR, Integer.class));
        }
        List<Expr<V>> params = helper.extractExprParameters(block, exprParams);
        return MathNumPropFunct.make(FunctionNames.get(op), params, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        Mutation mutation = new Mutation();
        mutation.setDivisorInput(false);
        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.PROPERTY, getFunctName().name());
        AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.NUMBER_TO_CHECK, getParam().get(0));
        if ( getFunctName() == FunctionNames.DIVISIBLE_BY ) {
            AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.DIVISOR, getParam().get(1));
            mutation.setDivisorInput(true);
        }
        jaxbDestination.setMutation(mutation);
        return jaxbDestination;
    }
}
