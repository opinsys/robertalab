package de.fhg.iais.roberta.ast.syntax.sensor;

import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.transformer.JaxbAstTransformer;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robSensors_encoder_getMode</b>, <b>robSensors_encoder_getSample</b> and <b>robSensors_encoder_setMode</b> blocks from Blockly
 * into
 * the AST (abstract syntax
 * tree).
 * Object from this class will generate code for setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link ActorPort} and {@link MotorTachoMode}. See enum {@link MotorTachoMode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(MotorTachoMode, ActorPort, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class EncoderSensor<V> extends Sensor<V> {
    private final MotorTachoMode mode;
    private final ActorPort motor;

    private EncoderSensor(MotorTachoMode mode, ActorPort motor, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.ENCODER_SENSING, properties, comment);
        Assert.isTrue(mode != null && motor != null);
        this.mode = mode;
        this.motor = motor;
        setReadOnly();
    }

    /**
     * Create object of the class {@link EncoderSensor}.
     *
     * @param mode in which the sensor is operating; must be <b>not</b> null; see enum {@link MotorTachoMode} for all possible modes that the sensor have,
     * @param port on where the sensor is connected; must be <b>not</b> null; see enum {@link SensorPort} for all possible sensor ports,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link EncoderSensor}
     */
    public static <V> EncoderSensor<V> make(MotorTachoMode mode, ActorPort motor, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new EncoderSensor<V>(mode, motor, properties, comment);
    }

    /**
     * @return get the mode of sensor. See enum {@link MotorTachoMode} for all possible modes that the sensor have
     */
    public MotorTachoMode getMode() {
        return this.mode;
    }

    /**
     * @return get the port on which the sensor is connected. See enum {@link SensorPort} for all possible sensor ports
     */
    public ActorPort getMotor() {
        return this.motor;
    }

    @Override
    public String toString() {
        return "DrehSensor [mode=" + this.mode + ", motor=" + this.motor + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitEncoderSensor(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, JaxbAstTransformer<V> helper) {
        if ( block.getType().equals(BlocklyConstants.ROB_SENSORS_ENCODER_RESET) ) {
            List<Field> fields = helper.extractFields(block, (short) 1);
            String portName = helper.extractField(fields, BlocklyConstants.MOTORPORT);
            return EncoderSensor.make(MotorTachoMode.RESET, ActorPort.get(portName), helper.extractBlockProperties(block), helper.extractComment(block));
        }
        List<Field> fields = helper.extractFields(block, (short) 2);
        String portName = helper.extractField(fields, BlocklyConstants.MOTORPORT);
        String modeName = helper.extractField(fields, BlocklyConstants.MODE_);
        return EncoderSensor.make(MotorTachoMode.get(modeName), ActorPort.get(portName), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        String fieldValue = getMotor().name();
        if ( getMode() == MotorTachoMode.DEGREE || getMode() == MotorTachoMode.ROTATION ) {
            AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MODE_, getMode().name());
        }
        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MOTORPORT, fieldValue);

        return jaxbDestination;
    }
}
