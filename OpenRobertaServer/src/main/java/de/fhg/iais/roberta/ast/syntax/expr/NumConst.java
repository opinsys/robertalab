package de.fhg.iais.roberta.ast.syntax.expr;

import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.transformer.JaxbAstTransformer;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>math_number</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code numerical value.<br/>
 * <br>
 * To create an instance from this class use the method {@link #make(String, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class NumConst<V> extends Expr<V> {
    private final String value;

    private NumConst(String value, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.NUM_CONST, properties, comment);
        Assert.isTrue(!value.equals(""));
        this.value = value;
        setReadOnly();
    }

    /**
     * creates instance of {@link NumConst}. This instance is read only and can not be modified.
     *
     * @param value of the numerical constant; must be <b>non-empty</b> string,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link NumConst}
     */
    public static <V> NumConst<V> make(String value, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new NumConst<V>(value, properties, comment);
    }

    /**
     * @return value of the numerical constant
     */
    public String getValue() {
        return this.value;
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
        return "NumConst [" + this.value + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitNumConst(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, JaxbAstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);
        String field = helper.extractField(fields, BlocklyConstants.NUM);
        return NumConst.make(field, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.NUM, getValue());
        return jaxbDestination;
    }
}
