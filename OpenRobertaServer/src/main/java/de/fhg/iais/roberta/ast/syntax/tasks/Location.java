package de.fhg.iais.roberta.ast.syntax.tasks;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class stores the information of the location of the top block in a blockly program.
 *
 * @author kcvejoski
 */
public class Location<V> extends Task<V> {
    private final String x;
    private final String y;

    private Location(String x, String y) {
        super(Phrase.Kind.LOCATION, BlocklyBlockProperties.make("t", "t", true, false, false, false, false, true), null);
        Assert.isTrue(!x.equals("") && !y.equals(""));
        this.x = x;
        this.y = y;
        setReadOnly();
    }

    /**
     * creates instance of {@link Location}. This instance is read only and cannot be modified.
     *
     * @param x coordinate of the position of the block, must be <b>non-empty</b> string,
     * @param y coordinate of the position of the block, must be <b>non-empty</b> string,
     * @return read only object of class {@link Location}
     */
    public static <V> Location<V> make(String x, String y) {
        return new Location<V>(x, y);
    }

    /**
     * @return x coordinate of the block position
     */
    public String getX() {
        return this.x;
    }

    /**
     * @return y coordinate of the block position
     */
    public String getY() {
        return this.y;
    }

    @Override
    public int getPrecedence() {
        return 0;
    }

    @Override
    public Assoc getAssoc() {
        return null;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitLocation(this);
    }

    @Override
    public String toString() {
        return "Location [x=" + this.x + ", y=" + this.y + "]";
    }

    @Override
    public Block astToBlock() {
        return null;
    }

}
