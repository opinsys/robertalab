package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.transformer.JaxbAstTransformer;
import de.fhg.iais.roberta.ast.typecheck.BlocklyType;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>variables_set</b> and <b>variables_get</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for creating a variable.<br/>
 * <br>
 * User must provide name of the variable and type of the variable, if the variable is created before in the code TypeVar should be <b>NONE</b>.
 * To create an instance from this class use the method {@link #make(BlocklyType, String, boolean, String)}.<br>
 */
public class Var<V> extends Expr<V> {
    private final BlocklyType typeVar;
    private final String name;

    private Var(BlocklyType typeVar, String value, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.VAR, properties, comment);
        Assert.isTrue(!value.equals("") && typeVar != null);
        this.name = value;
        this.typeVar = typeVar;
        setReadOnly();
    }

    /**
     * creates instance of {@link Var}. This instance is read only and can not be modified.
     *
     * @param typeVar type of the variable; must be <b>not</b> null,
     * @param value name of the variable; must be <b>non-empty</b> string,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link Var}
     */
    public static <V> Var<V> make(BlocklyType typeVar, String value, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new Var<V>(typeVar, value, properties, comment);
    }

    /**
     * @return name of the variable
     */
    public String getValue() {
        return this.name;
    }

    /**
     * @return the typeVar
     */
    public BlocklyType getTypeVar() {
        return this.typeVar;
    }

    @Override
    public int getPrecedence() {
        return 999;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.NONE;
    }

    @Override
    public String toString() {
        return "Var [" + this.name + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitVar(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, JaxbAstTransformer<V> helper) {
        return helper.extractVar(block);
    }

    @Override
    public Block astToBlock() {

        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        Mutation mutation = new Mutation();
        mutation.setDatatype(getTypeVar().getBlocklyName());
        jaxbDestination.setMutation(mutation);

        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.VAR, getValue());
        return jaxbDestination;
    }
}
