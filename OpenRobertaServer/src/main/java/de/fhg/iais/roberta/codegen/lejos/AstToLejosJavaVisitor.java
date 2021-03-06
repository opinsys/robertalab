package de.fhg.iais.roberta.codegen.lejos;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;

import de.fhg.iais.roberta.ast.syntax.IndexLocation;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.Phrase.Kind;
import de.fhg.iais.roberta.ast.syntax.action.ClearDisplayAction;
import de.fhg.iais.roberta.ast.syntax.action.DriveAction;
import de.fhg.iais.roberta.ast.syntax.action.LightAction;
import de.fhg.iais.roberta.ast.syntax.action.LightStatusAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorDriveStopAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorGetPowerAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorOnAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorSetPowerAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorStopAction;
import de.fhg.iais.roberta.ast.syntax.action.PlayFileAction;
import de.fhg.iais.roberta.ast.syntax.action.ShowPictureAction;
import de.fhg.iais.roberta.ast.syntax.action.ShowTextAction;
import de.fhg.iais.roberta.ast.syntax.action.ToneAction;
import de.fhg.iais.roberta.ast.syntax.action.TurnAction;
import de.fhg.iais.roberta.ast.syntax.action.VolumeAction;
import de.fhg.iais.roberta.ast.syntax.action.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.ast.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.ast.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.ast.syntax.action.communication.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.ast.syntax.expr.ActionExpr;
import de.fhg.iais.roberta.ast.syntax.expr.Binary;
import de.fhg.iais.roberta.ast.syntax.expr.Binary.Op;
import de.fhg.iais.roberta.ast.syntax.expr.BoolConst;
import de.fhg.iais.roberta.ast.syntax.expr.ColorConst;
import de.fhg.iais.roberta.ast.syntax.expr.EmptyExpr;
import de.fhg.iais.roberta.ast.syntax.expr.EmptyList;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.syntax.expr.ExprList;
import de.fhg.iais.roberta.ast.syntax.expr.FunctionExpr;
import de.fhg.iais.roberta.ast.syntax.expr.ListCreate;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst;
import de.fhg.iais.roberta.ast.syntax.expr.MethodExpr;
import de.fhg.iais.roberta.ast.syntax.expr.NullConst;
import de.fhg.iais.roberta.ast.syntax.expr.NumConst;
import de.fhg.iais.roberta.ast.syntax.expr.SensorExpr;
import de.fhg.iais.roberta.ast.syntax.expr.StringConst;
import de.fhg.iais.roberta.ast.syntax.expr.Unary;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.syntax.expr.VarDeclaration;
import de.fhg.iais.roberta.ast.syntax.functions.GetSubFunct;
import de.fhg.iais.roberta.ast.syntax.functions.IndexOfFunct;
import de.fhg.iais.roberta.ast.syntax.functions.LenghtOfIsEmptyFunct;
import de.fhg.iais.roberta.ast.syntax.functions.ListGetIndex;
import de.fhg.iais.roberta.ast.syntax.functions.ListRepeat;
import de.fhg.iais.roberta.ast.syntax.functions.ListSetIndex;
import de.fhg.iais.roberta.ast.syntax.functions.MathConstrainFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathNumPropFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathOnListFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathPowerFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathSingleFunct;
import de.fhg.iais.roberta.ast.syntax.functions.TextJoinFunct;
import de.fhg.iais.roberta.ast.syntax.functions.TextPrintFunct;
import de.fhg.iais.roberta.ast.syntax.methods.MethodCall;
import de.fhg.iais.roberta.ast.syntax.methods.MethodIfReturn;
import de.fhg.iais.roberta.ast.syntax.methods.MethodReturn;
import de.fhg.iais.roberta.ast.syntax.methods.MethodVoid;
import de.fhg.iais.roberta.ast.syntax.sensor.BrickSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.ColorSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.EncoderSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.GetSampleSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.GyroSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.GyroSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.InfraredSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.MotorTachoMode;
import de.fhg.iais.roberta.ast.syntax.sensor.TimerSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.TouchSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.UltrasonicSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.UltrasonicSensorMode;
import de.fhg.iais.roberta.ast.syntax.stmt.ActionStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.ExprStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.FunctionStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.MethodStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt.Mode;
import de.fhg.iais.roberta.ast.syntax.stmt.SensorStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.Stmt;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtList;
import de.fhg.iais.roberta.ast.syntax.stmt.WaitStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.ast.syntax.tasks.ActivityTask;
import de.fhg.iais.roberta.ast.syntax.tasks.Location;
import de.fhg.iais.roberta.ast.syntax.tasks.MainTask;
import de.fhg.iais.roberta.ast.syntax.tasks.StartActivityTask;
import de.fhg.iais.roberta.ast.usedhardwarecheck.HardwareCheckVisitor;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;
import de.fhg.iais.roberta.ev3.EV3BrickConfiguration;
import de.fhg.iais.roberta.ev3.EV3Sensors;
import de.fhg.iais.roberta.hardwarecomponents.Category;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they
 * append a human-readable JAVA code representation of a phrase to a StringBuilder. <b>This representation is correct JAVA code.</b> <br>
 */
public class AstToLejosJavaVisitor implements AstVisitor<Void> {
    public static final String INDENT = "    ";

    private final EV3BrickConfiguration brickConfiguration;
    private final String programName;
    private final StringBuilder sb = new StringBuilder();
    private final Set<EV3Sensors> usedSensors;
    private int indentation;

    /**
     * initialize the Java code generator visitor.
     *
     * @param programName name of the program
     * @param brickConfiguration hardware configuration of the brick
     * @param usedSensors in the current program
     * @param indentation to start with. Will be ince/decr depending on block structure
     */
    AstToLejosJavaVisitor(String programName, EV3BrickConfiguration brickConfiguration, Set<EV3Sensors> usedSensors, int indentation) {
        this.programName = programName;
        this.brickConfiguration = brickConfiguration;
        this.indentation = indentation;
        this.usedSensors = usedSensors;
    }

    /**
     * factory method to generate Java code from an AST.<br>
     *
     * @param programName name of the program
     * @param brickConfiguration hardware configuration of the brick
     * @param phrases to generate the code from
     */
    public static String generate(
        String programName,
        EV3BrickConfiguration brickConfiguration,
        ArrayList<ArrayList<Phrase<Void>>> phrasesSet,
        boolean withWrapping) //
    {
        Assert.notNull(programName);
        Assert.notNull(brickConfiguration);
        Assert.isTrue(phrasesSet.size() >= 1);

        Set<EV3Sensors> usedSensors = HardwareCheckVisitor.check(phrasesSet);
        AstToLejosJavaVisitor astVisitor = new AstToLejosJavaVisitor(programName, brickConfiguration, usedSensors, withWrapping ? 2 : 0);
        astVisitor.generatePrefix(withWrapping);

        boolean mainBlock = false;
        for ( ArrayList<Phrase<Void>> phrases : phrasesSet ) {
            for ( Phrase<Void> phrase : phrases ) {
                //TODO this must be improved (the problem is with the old XML test resources !!!!!!!!
                if ( phrase.getProperty().isDisabled() ) {
                    continue;
                }
                if ( phrase.getProperty().isInTask() != null ) {
                    if ( phrase.getProperty().isInTask() == false ) {
                        continue;
                    }
                }
                if ( phrase.getKind().getCategory() != Category.TASK ) {
                    astVisitor.nlIndent();
                } else {
                    mainBlock = true;
                }
                phrase.visit(astVisitor);
            }
            if ( mainBlock ) {
                astVisitor.sb.append("\n").append(INDENT).append("}");
                mainBlock = false;
            }
        }
        if ( withWrapping ) {
            astVisitor.sb.append("\n}\n");
        }
        return astVisitor.sb.toString();
    }

    /**
     * Get the current indentation of the visitor. Meaningful for tests only.
     *
     * @return indentation value of the visitor.
     */
    int getIndentation() {
        return this.indentation;
    }

    /**
     * Get the string builder of the visitor. Meaningful for tests only.
     *
     * @return (current state of) the string builder
     */
    public StringBuilder getSb() {
        return this.sb;
    }

    @Override
    public Void visitNumConst(NumConst<Void> numConst) {
        if ( numConst.getProperty().isDisabled() ) {
            return null;
        }
        if ( isInteger(numConst.getValue()) ) {
            this.sb.append(numConst.getValue());
        } else {
            this.sb.append("((float) ");
            this.sb.append(numConst.getValue());
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitBoolConst(BoolConst<Void> boolConst) {
        if ( boolConst.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append(boolConst.isValue());
        return null;
    };

    @Override
    public Void visitMathConst(MathConst<Void> mathConst) {
        if ( mathConst.getProperty().isDisabled() ) {
            return null;
        }
        switch ( mathConst.getMathConst() ) {
            case PI:
                this.sb.append("Math.PI");
                break;
            case E:
                this.sb.append("Math.E");
                break;
            case GOLDEN_RATIO:
                this.sb.append("BlocklyMethods.GOLDEN_RATIO");
                break;
            case SQRT2:
                this.sb.append("Math.sqrt(2)");
                break;
            case SQRT1_2:
                this.sb.append("Math.sqrt(1.0/2.0)");
                break;
            case INFINITY:
                this.sb.append("Double.POSITIVE_INFINITY");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        if ( colorConst.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append(colorConst.getValue().getJavaCode());
        return null;
    }

    @Override
    public Void visitStringConst(StringConst<Void> stringConst) {
        if ( stringConst.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("\"").append(StringEscapeUtils.escapeJava(stringConst.getValue())).append("\"");
        return null;
    }

    @Override
    public Void visitNullConst(NullConst<Void> nullConst) {
        if ( nullConst.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("null");
        return null;
    }

    @Override
    public Void visitVar(Var<Void> var) {
        if ( var.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append(var.getValue());
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        if ( var.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append(var.getTypeVar().getJavaCode()).append(" ");
        this.sb.append(var.getName());
        if ( var.getValue().getKind() != Phrase.Kind.EMPTY_EXPR ) {
            this.sb.append(" = ");
            var.getValue().visit(this);
        }
        return null;
    }

    @Override
    public Void visitUnary(Unary<Void> unary) {
        if ( unary.getProperty().isDisabled() ) {
            return null;
        }
        if ( unary.getOp() == Unary.Op.POSTFIX_INCREMENTS ) {
            generateExprCode(unary, this.sb);
            this.sb.append(unary.getOp().getOpSymbol());
        } else {
            this.sb.append(unary.getOp().getOpSymbol());
            generateExprCode(unary, this.sb);
        }
        return null;
    }

    @Override
    public Void visitBinary(Binary<Void> binary) {
        if ( binary.getProperty().isDisabled() ) {
            return null;
        }
        generateSubExpr(this.sb, false, binary.getLeft(), binary);
        this.sb.append(whitespace() + binary.getOp().getOpSymbol() + whitespace());
        if ( binary.getOp() == Op.TEXT_APPEND ) {
            this.sb.append("String.valueOf(");
            generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
            this.sb.append(")");
        } else {
            generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
        }
        if ( binary.getOp() == Op.MATH_CHANGE ) {
            this.sb.append(";");
        }
        return null;
    }

    @Override
    public Void visitActionExpr(ActionExpr<Void> actionExpr) {
        if ( actionExpr.getProperty().isDisabled() ) {
            return null;
        }
        actionExpr.getAction().visit(this);
        return null;
    }

    @Override
    public Void visitSensorExpr(SensorExpr<Void> sensorExpr) {
        if ( sensorExpr.getProperty().isDisabled() ) {
            return null;
        }
        sensorExpr.getSens().visit(this);
        return null;
    }

    @Override
    public Void visitMethodExpr(MethodExpr<Void> methodExpr) {
        if ( methodExpr.getProperty().isDisabled() ) {
            return null;
        }
        methodExpr.getMethod().visit(this);
        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr<Void> emptyExpr) {
        if ( emptyExpr.getProperty().isDisabled() ) {
            return null;
        }
        switch ( emptyExpr.getDefVal().getName() ) {
            case "java.lang.String":
                this.sb.append("\"\"");
                break;
            case "java.lang.Boolean":
                this.sb.append("true");
                break;
            case "java.lang.Integer":
                this.sb.append("0");
                break;
            case "java.util.ArrayList":
                break;
            case "de.fhg.iais.roberta.ast.syntax.expr.NullConst":
                break;
            default:
                this.sb.append("[[EmptyExpr [defVal=" + emptyExpr.getDefVal() + "]]]");
                break;
        }
        return null;
    }

    @Override
    public Void visitExprList(ExprList<Void> exprList) {
        if ( exprList.getProperty().isDisabled() ) {
            return null;
        }
        boolean first = true;
        for ( Expr<Void> expr : exprList.get() ) {
            if ( expr.getKind() != Phrase.Kind.EMPTY_EXPR ) {
                if ( first ) {
                    first = false;
                } else {
                    if ( expr.getKind() == Kind.BINARY || expr.getKind() == Kind.UNARY ) {
                        this.sb.append("; ");
                    } else {
                        this.sb.append(", ");
                    }
                }
                expr.visit(this);
            }
        }
        return null;
    }

    @Override
    public Void visitFunc(MathPowerFunct<Void> funct) {
        if ( funct.getProperty().isDisabled() ) {
            return null;
        }
        //        switch ( funct.getFunctName() ) {
        //            case PRINT:
        //                this.sb.append("System.out.println(");
        //                funct.getParam().get(0).visit(this);
        //                this.sb.append(")");
        //                break;
        //            default:
        //                break;
        //        }
        return null;
    }

    @Override
    public Void visitActionStmt(ActionStmt<Void> actionStmt) {
        if ( actionStmt.getProperty().isDisabled() ) {
            return null;
        }
        actionStmt.getAction().visit(this);
        return null;
    }

    @Override
    public Void visitAssignStmt(AssignStmt<Void> assignStmt) {
        if ( assignStmt.getProperty().isDisabled() ) {
            return null;
        }
        assignStmt.getName().visit(this);
        this.sb.append(" = ");
        assignStmt.getExpr().visit(this);
        this.sb.append(";");
        return null;
    }

    @Override
    public Void visitExprStmt(ExprStmt<Void> exprStmt) {
        if ( exprStmt.getProperty().isDisabled() ) {
            return null;
        }
        exprStmt.getExpr().visit(this);
        this.sb.append(";");
        return null;
    }

    @Override
    public Void visitIfStmt(IfStmt<Void> ifStmt) {
        if ( ifStmt.getProperty().isDisabled() ) {
            return null;
        }
        if ( ifStmt.isTernary() ) {
            generateCodeFromTernary(ifStmt);
        } else {
            generateCodeFromIfElse(ifStmt);
            generateCodeFromElse(ifStmt);
        }
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        if ( repeatStmt.getProperty().isDisabled() ) {
            return null;
        }
        boolean additionalClosingBracket = false;
        switch ( repeatStmt.getMode() ) {
            case UNTIL:
            case WHILE:
            case FOREVER:
                this.sb.append("if ( TRUE ) {");
                incrIndentation();
                nlIndent();
                generateCodeFromStmtCondition("while", repeatStmt.getExpr());
                additionalClosingBracket = true;
                break;
            case TIMES:
            case FOR:
                generateCodeFromStmtCondition("for", repeatStmt.getExpr());
                break;
            case WAIT:
                generateCodeFromStmtCondition("if", repeatStmt.getExpr());
                break;
            case FOR_EACH:
                generateCodeFromStmtCondition("for", repeatStmt.getExpr());
                break;
            default:
                break;
        }
        incrIndentation();
        repeatStmt.getList().visit(this);
        appendBreakStmt(repeatStmt);
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        if ( additionalClosingBracket ) {
            decrIndentation();
            nlIndent();
            this.sb.append("}");
        }
        return null;
    }

    @Override
    public Void visitSensorStmt(SensorStmt<Void> sensorStmt) {
        if ( sensorStmt.getProperty().isDisabled() ) {
            return null;
        }
        sensorStmt.getSensor().visit(this);
        return null;
    }

    @Override
    public Void visitStmtFlowCon(StmtFlowCon<Void> stmtFlowCon) {
        if ( stmtFlowCon.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append(stmtFlowCon.getFlow().toString().toLowerCase() + ";");
        return null;
    }

    @Override
    public Void visitStmtList(StmtList<Void> stmtList) {
        if ( stmtList.getProperty().isDisabled() ) {
            return null;
        }
        for ( Stmt<Void> stmt : stmtList.get() ) {
            nlIndent();
            stmt.visit(this);
        }
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        if ( waitStmt.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("if ( TRUE ) {");
        incrIndentation();
        nlIndent();
        this.sb.append("while ( true ) {");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        this.sb.append("hal.waitFor(15);");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        if ( waitTimeStmt.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("hal.waitFor(");
        waitTimeStmt.getTime().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        if ( clearDisplayAction.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("hal.clearDisplay();");
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        if ( volumeAction.getProperty().isDisabled() ) {
            return null;
        }
        switch ( volumeAction.getMode() ) {
            case SET:
                this.sb.append("hal.setVolume(");
                volumeAction.getVolume().visit(this);
                this.sb.append(");");
                break;
            case GET:
                this.sb.append("hal.getVolume()");
                break;
            default:
                throw new DbcException("Invalid volume action mode!");
        }
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        if ( lightAction.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("hal.ledOn(" + lightAction.getColor().getJavaCode() + ", " + lightAction.getBlinkMode().getJavaCode() + ");");
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        if ( lightStatusAction.getProperty().isDisabled() ) {
            return null;
        }
        switch ( lightStatusAction.getStatus() ) {
            case OFF:
                this.sb.append("hal.ledOff();");
                break;
            case RESET:
                this.sb.append("hal.resetLED();");
                break;
            default:
                throw new DbcException("Invalid LED status mode!");
        }
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        if ( playFileAction.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("hal.playFile(" + playFileAction.getFileName() + ");");
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        if ( showPictureAction.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("hal.drawPicture(" + showPictureAction.getPicture().getJavaCode() + ", ");
        showPictureAction.getX().visit(this);
        this.sb.append(", ");
        showPictureAction.getY().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        if ( showTextAction.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("hal.drawText(");
        if ( showTextAction.getMsg().getKind() != Phrase.Kind.STRING_CONST ) {
            this.sb.append("String.valueOf(");
            showTextAction.getMsg().visit(this);
            this.sb.append(")");
        } else {
            showTextAction.getMsg().visit(this);
        }
        this.sb.append(", ");
        showTextAction.getX().visit(this);
        this.sb.append(", ");
        showTextAction.getY().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        if ( toneAction.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("hal.playTone(");
        toneAction.getFrequency().visit(this);
        this.sb.append(", ");
        toneAction.getDuration().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        if ( motorOnAction.getProperty().isDisabled() ) {
            return null;
        }
        String methodName;
        boolean isRegulated = this.brickConfiguration.isMotorRegulated(motorOnAction.getPort());
        boolean duration = motorOnAction.getParam().getDuration() != null;
        if ( duration ) {
            methodName = isRegulated ? "hal.rotateRegulatedMotor(" : "hal.rotateUnregulatedMotor(";
        } else {
            methodName = isRegulated ? "hal.turnOnRegulatedMotor(" : "hal.turnOnUnregulatedMotor(";
        }
        this.sb.append(methodName + motorOnAction.getPort().getJavaCode() + ", ");
        motorOnAction.getParam().getSpeed().visit(this);
        if ( duration ) {
            this.sb.append(", " + motorOnAction.getDurationMode().getJavaCode());
            this.sb.append(", ");
            motorOnAction.getDurationValue().visit(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        if ( motorSetPowerAction.getProperty().isDisabled() ) {
            return null;
        }
        boolean isRegulated = this.brickConfiguration.isMotorRegulated(motorSetPowerAction.getPort());
        String methodName = isRegulated ? "hal.setRegulatedMotorSpeed(" : "hal.setUnregulatedMotorSpeed(";
        this.sb.append(methodName + motorSetPowerAction.getPort().getJavaCode() + ", ");
        motorSetPowerAction.getPower().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        if ( motorGetPowerAction.getProperty().isDisabled() ) {
            return null;
        }
        boolean isRegulated = this.brickConfiguration.isMotorRegulated(motorGetPowerAction.getPort());
        String methodName = isRegulated ? "hal.getRegulatedMotorSpeed(" : "hal.getUnregulatedMotorSpeed(";
        this.sb.append(methodName + motorGetPowerAction.getPort().getJavaCode() + ")");
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        if ( motorStopAction.getProperty().isDisabled() ) {
            return null;
        }
        boolean isRegulated = this.brickConfiguration.isMotorRegulated(motorStopAction.getPort());
        String methodName = isRegulated ? "hal.stopRegulatedMotor(" : "hal.stopUnregulatedMotor(";
        this.sb.append(methodName + motorStopAction.getPort().getJavaCode() + ", " + motorStopAction.getMode().getJavaCode() + ");");
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        if ( driveAction.getProperty().isDisabled() ) {
            return null;
        }
        boolean isDuration = driveAction.getParam().getDuration() != null;
        String methodName = isDuration ? "hal.driveDistance(" : "hal.regulatedDrive(";
        this.sb.append(methodName);
        this.sb.append(this.brickConfiguration.getLeftMotorPort().getJavaCode() + ", ");
        this.sb.append(this.brickConfiguration.getRightMotorPort().getJavaCode() + ", false, ");
        this.sb.append(driveAction.getDirection().getJavaCode() + ", ");
        driveAction.getParam().getSpeed().visit(this);
        if ( isDuration ) {
            this.sb.append(", ");
            driveAction.getParam().getDuration().getValue().visit(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        if ( turnAction.getProperty().isDisabled() ) {
            return null;
        }
        boolean isDuration = turnAction.getParam().getDuration() != null;
        boolean isRegulated = this.brickConfiguration.getActorOnPort(this.brickConfiguration.getLeftMotorPort()).isRegulated();
        String methodName = "hal.rotateDirection" + (isDuration ? "Angle" : isRegulated ? "Regulated" : "Unregulated") + "(";
        this.sb.append(methodName);
        this.sb.append(this.brickConfiguration.getLeftMotorPort().getJavaCode() + ", ");
        this.sb.append(this.brickConfiguration.getRightMotorPort().getJavaCode() + ", false, ");
        this.sb.append(turnAction.getDirection().getJavaCode() + ", ");
        turnAction.getParam().getSpeed().visit(this);
        if ( isDuration ) {
            this.sb.append(", ");
            turnAction.getParam().getDuration().getValue().visit(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        if ( stopAction.getProperty().isDisabled() ) {
            return null;
        }
        boolean isRegulated = true;
        String methodName = isRegulated ? "hal.stopRegulatedDrive(" : "hal.stopUnregulatedDrive(";
        this.sb.append(methodName);
        this.sb.append(this.brickConfiguration.getLeftMotorPort().getJavaCode() + ", ");
        this.sb.append(this.brickConfiguration.getRightMotorPort().getJavaCode() + ");");
        return null;
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        if ( brickSensor.getProperty().isDisabled() ) {
            return null;
        }
        switch ( brickSensor.getMode() ) {
            case IS_PRESSED:
                this.sb.append("hal.isPressed(" + brickSensor.getKey().getJavaCode() + ")");
                break;
            case WAIT_FOR_PRESS_AND_RELEASE:
                this.sb.append("hal.isPressedAndReleased(" + brickSensor.getKey().getJavaCode() + ")");
                break;
            default:
                throw new DbcException("Invalide mode for BrickSensor!");
        }
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        if ( colorSensor.getProperty().isDisabled() ) {
            return null;
        }
        switch ( colorSensor.getMode() ) {
            case AMBIENTLIGHT:
                this.sb.append("hal.getColorSensorAmbient(" + colorSensor.getPort().getJavaCode() + ")");
                break;
            case COLOUR:
                this.sb.append("hal.getColorSensorColour(" + colorSensor.getPort().getJavaCode() + ")");
                break;
            case RED:
                this.sb.append("hal.getColorSensorRed(" + colorSensor.getPort().getJavaCode() + ")");
                break;
            case RGB:
                this.sb.append("hal.getColorSensorRgb(" + colorSensor.getPort().getJavaCode() + ")");
                break;
            default:
                throw new DbcException("Invalide mode for Color Sensor!");
        }
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        if ( encoderSensor.getProperty().isDisabled() ) {
            return null;
        }
        boolean isRegulated = this.brickConfiguration.isMotorRegulated(encoderSensor.getMotor());
        if ( encoderSensor.getMode() == MotorTachoMode.RESET ) {
            String methodName = isRegulated ? "hal.resetRegulatedMotorTacho(" : "hal.resetUnregulatedMotorTacho(";
            this.sb.append(methodName + encoderSensor.getMotor().getJavaCode() + ");");
        } else {
            String methodName = isRegulated ? "hal.getRegulatedMotorTachoValue(" : "hal.getUnregulatedMotorTachoValue(";
            this.sb.append(methodName + encoderSensor.getMotor().getJavaCode() + ", " + encoderSensor.getMode().getJavaCode() + ")");
        }
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        if ( gyroSensor.getProperty().isDisabled() ) {
            return null;
        }
        if ( gyroSensor.getMode() == GyroSensorMode.RESET ) {
            this.sb.append("hal.resetGyroSensor(" + gyroSensor.getPort().getJavaCode() + ");");
        } else {
            this.sb.append("hal.getGyroSensorValue(" + gyroSensor.getPort().getJavaCode() + ", " + gyroSensor.getMode().getJavaCode() + ")");
        }
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        if ( infraredSensor.getProperty().isDisabled() ) {
            return null;
        }
        switch ( infraredSensor.getMode() ) {
            case DISTANCE:
                this.sb.append("hal.getInfraredSensorDistance(" + infraredSensor.getPort().getJavaCode() + ")");
                break;
            case SEEK:
                this.sb.append("hal.getInfraredSensorSeek(" + infraredSensor.getPort().getJavaCode() + ")");
                break;
            default:
                throw new DbcException("Invalid Infrared Sensor Mode!");
        }

        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        if ( timerSensor.getProperty().isDisabled() ) {
            return null;
        }
        switch ( timerSensor.getMode() ) {
            case GET_SAMPLE:
                this.sb.append("hal.getTimerValue(" + timerSensor.getTimer() + ")");
                break;
            case RESET:
                this.sb.append("hal.resetTimer(" + timerSensor.getTimer() + ");");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        if ( touchSensor.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("hal.isPressed(" + touchSensor.getPort().getJavaCode() + ")");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        if ( ultrasonicSensor.getProperty().isDisabled() ) {
            return null;
        }
        if ( ultrasonicSensor.getMode() == UltrasonicSensorMode.DISTANCE ) {
            this.sb.append("hal.getUltraSonicSensorDistance(" + ultrasonicSensor.getPort().getJavaCode() + ")");
        } else {
            this.sb.append("hal.getUltraSonicSensorPresence(" + ultrasonicSensor.getPort().getJavaCode() + ")");
        }
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        if ( mainTask.getProperty().isDisabled() ) {
            return null;
        }
        mainTask.getVariables().visit(this);
        this.sb.append("\n\n").append(INDENT).append("public void run() {\n");
        //        this.sb.append(INDENT).append(INDENT).append("hal = new Hal(brickConfiguration, usedSensors);\n");
        return null;
    }

    @Override
    public Void visitActivityTask(ActivityTask<Void> activityTask) {
        if ( activityTask.getProperty().isDisabled() ) {
            return null;
        }
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitStartActivityTask(StartActivityTask<Void> startActivityTask) {
        if ( startActivityTask.getProperty().isDisabled() ) {
            return null;
        }
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitLocation(Location<Void> location) {
        return null;
    }

    @Override
    public Void visitGetSampleSensor(GetSampleSensor<Void> sensorGetSample) {
        if ( sensorGetSample.getProperty().isDisabled() ) {
            return null;
        }
        return sensorGetSample.getSensor().visit(this);
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct<Void> textPrintFunct) {
        if ( textPrintFunct.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("System.out.println(");
        textPrintFunct.getParam().get(0).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitFunctionStmt(FunctionStmt<Void> functionStmt) {
        if ( functionStmt.getProperty().isDisabled() ) {
            return null;
        }
        functionStmt.getFunction().visit(this);
        this.sb.append(";");
        return null;
    }

    @Override
    public Void visitFunctionExpr(FunctionExpr<Void> functionExpr) {
        if ( functionExpr.getProperty().isDisabled() ) {
            return null;
        }
        functionExpr.getFunction().visit(this);
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        if ( getSubFunct.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("BlocklyMethods.listsGetSubList( ");
        getSubFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        IndexLocation where1 = IndexLocation.get(getSubFunct.getStrParam().get(0));
        this.sb.append(where1.getJavaCode());
        if ( where1 == IndexLocation.FROM_START || where1 == IndexLocation.FROM_END ) {
            this.sb.append(", ");
            getSubFunct.getParam().get(1).visit(this);
        }
        this.sb.append(", ");
        IndexLocation where2 = IndexLocation.get(getSubFunct.getStrParam().get(1));
        this.sb.append(where2.getJavaCode());
        if ( where2 == IndexLocation.FROM_START || where2 == IndexLocation.FROM_END ) {
            this.sb.append(", ");
            if ( getSubFunct.getParam().size() == 3 ) {
                getSubFunct.getParam().get(2).visit(this);
            } else {
                getSubFunct.getParam().get(1).visit(this);
            }
        }
        this.sb.append(")");
        return null;

    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        if ( indexOfFunct.getProperty().isDisabled() ) {
            return null;
        }
        switch ( indexOfFunct.getLocation() ) {
            case FIRST:
                this.sb.append("BlocklyMethods.findFirst( ");
                indexOfFunct.getParam().get(0).visit(this);
                this.sb.append(", ");
                indexOfFunct.getParam().get(1).visit(this);
                this.sb.append(")");
                break;
            case LAST:
                this.sb.append("BlocklyMethods.findLast( ");
                indexOfFunct.getParam().get(0).visit(this);
                this.sb.append(", ");
                indexOfFunct.getParam().get(1).visit(this);
                this.sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitLenghtOfIsEmptyFunct(LenghtOfIsEmptyFunct<Void> lenghtOfIsEmptyFunct) {
        if ( lenghtOfIsEmptyFunct.getProperty().isDisabled() ) {
            return null;
        }
        switch ( lenghtOfIsEmptyFunct.getFunctName() ) {
            case LISTS_LENGTH:
                this.sb.append("BlocklyMethods.lenght( ");
                lenghtOfIsEmptyFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;

            case LIST_IS_EMPTY:
                this.sb.append("BlocklyMethods.isEmpty( ");
                lenghtOfIsEmptyFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList<Void> emptyList) {
        if ( emptyList.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("new ArrayList<"
            + emptyList.getTypeVar().getJavaCode().substring(0, 1).toUpperCase()
            + emptyList.getTypeVar().getJavaCode().substring(1).toLowerCase()
            + ">()");
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        if ( listCreate.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("BlocklyMethods.createListWith(");
        listCreate.getValue().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        if ( listRepeat.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("BlocklyMethods.createListWithItem(");
        listRepeat.getParam().get(0).visit(this);
        this.sb.append(", ");
        listRepeat.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        if ( listGetIndex.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("BlocklyMethods.listsIndex(");
        listGetIndex.getParam().get(0).visit(this);
        this.sb.append(", ");
        this.sb.append(listGetIndex.getElementOperation().getJavaCode());
        this.sb.append(", ");
        this.sb.append(listGetIndex.getLocation().getJavaCode());
        if ( listGetIndex.getParam().size() == 2 ) {
            this.sb.append(", ");
            listGetIndex.getParam().get(1).visit(this);
        }
        this.sb.append(")");
        if ( listGetIndex.getElementOperation().isStatment() ) {
            this.sb.append(";");
        }
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        if ( listSetIndex.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("BlocklyMethods.listsIndex(");
        listSetIndex.getParam().get(0).visit(this);
        this.sb.append(", ");
        this.sb.append(listSetIndex.getElementOperation().getJavaCode());
        this.sb.append(", ");
        listSetIndex.getParam().get(1).visit(this);
        this.sb.append(", ");
        this.sb.append(listSetIndex.getLocation().getJavaCode());
        if ( listSetIndex.getParam().size() == 3 ) {
            this.sb.append(", ");
            listSetIndex.getParam().get(2).visit(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        if ( mathConstrainFunct.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("BlocklyMethods.clamp(");
        mathConstrainFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathConstrainFunct.getParam().get(1).visit(this);
        this.sb.append(", ");
        mathConstrainFunct.getParam().get(2).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        if ( mathNumPropFunct.getProperty().isDisabled() ) {
            return null;
        }
        switch ( mathNumPropFunct.getFunctName() ) {
            case EVEN:
                this.sb.append("BlocklyMethods.isEven(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case ODD:
                this.sb.append("BlocklyMethods.isOdd(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case PRIME:
                this.sb.append("BlocklyMethods.isPrime(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case WHOLE:
                this.sb.append("BlocklyMethods.isWhole(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case POSITIVE:
                this.sb.append("BlocklyMethods.isPositive(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case NEGATIVE:
                this.sb.append("BlocklyMethods.isNegative(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case DIVISIBLE_BY:
                this.sb.append("BlocklyMethods.isDivisibleBy(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(", ");
                mathNumPropFunct.getParam().get(1).visit(this);
                this.sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        if ( mathOnListFunct.getProperty().isDisabled() ) {
            return null;
        }
        switch ( mathOnListFunct.getFunctName() ) {
            case SUM:
                this.sb.append("BlocklyMethods.sumOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MIN:
                this.sb.append("BlocklyMethods.minOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MAX:
                this.sb.append("BlocklyMethods.maxOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case AVERAGE:
                this.sb.append("BlocklyMethods.averageOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MEDIAN:
                this.sb.append("BlocklyMethods.medianOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case STD_DEV:
                this.sb.append("BlocklyMethods.standardDeviatioin(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case RANDOM:
                this.sb.append("BlocklyMethods.randOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MODE:
                this.sb.append("BlocklyMethods.modeOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            default:
                break;
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        if ( mathRandomFloatFunct.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("BlocklyMethods.randDouble()");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        if ( mathRandomIntFunct.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("BlocklyMethods.randInt(");
        mathRandomIntFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathRandomIntFunct.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        if ( mathSingleFunct.getProperty().isDisabled() ) {
            return null;
        }
        switch ( mathSingleFunct.getFunctName() ) {
            case ROOT:
                this.sb.append("Math.sqrt(");
                break;
            case ABS:
                this.sb.append("Math.abs(");
                break;
            case LN:
                this.sb.append("Math.log(");
                break;
            case LOG10:
                this.sb.append("Math.log10(");
                break;
            case EXP:
                this.sb.append("Math.exp(");
                break;
            case POW10:
                this.sb.append("Math.pow(10, ");
                break;
            case SIN:
                this.sb.append("Math.sin(");
                break;
            case COS:
                this.sb.append("Math.cos(");
                break;
            case TAN:
                this.sb.append("Math.tan(");
                break;
            case ASIN:
                this.sb.append("Math.asin(");
                break;
            case ATAN:
                this.sb.append("Math.atan(");
                break;
            case ACOS:
                this.sb.append("Math.acos(");
                break;
            case ROUND:
                this.sb.append("Math.round(");
                break;
            case ROUNDUP:
                this.sb.append("Math.ceil(");
                break;
            case ROUNDDOWN:
                this.sb.append("Math.floor(");
                break;
            default:
                break;
        }
        mathSingleFunct.getParam().get(0).visit(this);
        this.sb.append(")");

        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        if ( textJoinFunct.getProperty().isDisabled() ) {
            return null;
        }
        boolean isFirst = true;
        List<Expr<Void>> params = textJoinFunct.getParam();
        this.sb.append("BlocklyMethods.textJoin(");
        for ( Expr<Void> expr : params ) {
            if ( isFirst ) {
                isFirst = false;
            } else {
                this.sb.append(", ");
            }
            expr.visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid<Void> methodVoid) {
        if ( methodVoid.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("\n").append(INDENT).append("private void ");
        this.sb.append(methodVoid.getMethodName() + "(");
        methodVoid.getParameters().visit(this);
        this.sb.append(") {");
        methodVoid.getBody().visit(this);
        this.sb.append("\n").append(INDENT).append("}");
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn<Void> methodReturn) {
        if ( methodReturn.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("\n").append(INDENT).append("private " + methodReturn.getReturnType().getJavaCode());
        this.sb.append(" " + methodReturn.getMethodName() + "(");
        methodReturn.getParameters().visit(this);
        this.sb.append(") {");
        methodReturn.getBody().visit(this);
        this.nlIndent();
        this.sb.append("return ");
        methodReturn.getReturnValue().visit(this);
        this.sb.append(";\n").append(INDENT).append("}");
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn<Void> methodIfReturn) {
        if ( methodIfReturn.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("if (");
        methodIfReturn.getCondition().visit(this);
        this.sb.append(") ");
        this.sb.append("return ");
        methodIfReturn.getReturnValue().visit(this);
        return null;
    }

    @Override
    public Void visitMethodStmt(MethodStmt<Void> methodStmt) {
        if ( methodStmt.getProperty().isDisabled() ) {
            return null;
        }
        methodStmt.getMethod().visit(this);
        this.sb.append(";");
        return null;
    }

    @Override
    public Void visitMethodCall(MethodCall<Void> methodCall) {
        if ( methodCall.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append(methodCall.getMethodName() + "(");
        methodCall.getParametersValues().visit(this);
        this.sb.append(")");
        if ( methodCall.getReturnType() == null ) {
            this.sb.append(";");
        }
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> clearDisplayAction) {
        if ( clearDisplayAction.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("hal.readMessage()");
        return null;
    }

    @Override
    public Void visitBluetoothConnectAction(BluetoothConnectAction<Void> bluetoothConnectAction) {
        if ( bluetoothConnectAction.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("hal.establishConnectionTo(");
        if ( bluetoothConnectAction.get_address().getKind() != Phrase.Kind.STRING_CONST ) {
            this.sb.append("String.valueOf(");
            bluetoothConnectAction.get_address().visit(this);
            this.sb.append(")");
        } else {
            bluetoothConnectAction.get_address().visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
        if ( bluetoothSendAction.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("hal.sendMessage(");
        if ( bluetoothSendAction.get_msg().getKind() != Phrase.Kind.STRING_CONST ) {
            this.sb.append("String.valueOf(");
            bluetoothSendAction.get_msg().visit(this);
            this.sb.append(")");
        } else {
            bluetoothSendAction.get_msg().visit(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<Void> bluetoothWaitForConnection) {
        if ( bluetoothWaitForConnection.getProperty().isDisabled() ) {
            return null;
        }
        this.sb.append("hal.waitForConnection()");
        return null;
    }

    private void incrIndentation() {
        this.indentation += 1;
    }

    private void decrIndentation() {
        this.indentation -= 1;
    }

    private void indent() {
        if ( this.indentation <= 0 ) {
            return;
        } else {
            for ( int i = 0; i < this.indentation; i++ ) {
                this.sb.append(INDENT);
            }
        }
    }

    private void nlIndent() {
        this.sb.append("\n");
        indent();
    }

    private String whitespace() {
        return " ";
    }

    private boolean parenthesesCheck(Binary<Void> binary) {
        return binary.getOp() == Op.MINUS && binary.getRight().getKind() == Kind.BINARY && binary.getRight().getPrecedence() <= binary.getPrecedence();
    }

    private void generateSubExpr(StringBuilder sb, boolean minusAdaption, Expr<Void> expr, Binary<Void> binary) {
        if ( expr.getPrecedence() >= binary.getPrecedence() && !minusAdaption ) {
            // parentheses are omitted
            expr.visit(this);
        } else {
            sb.append("(" + whitespace());
            expr.visit(this);
            sb.append(whitespace() + ")");
        }
    }

    private void generateExprCode(Unary<Void> unary, StringBuilder sb) {
        if ( unary.getExpr().getPrecedence() < unary.getPrecedence() ) {
            sb.append("(");
            unary.getExpr().visit(this);
            sb.append(")");
        } else {
            unary.getExpr().visit(this);
        }
    }

    private void generateCodeFromTernary(IfStmt<Void> ifStmt) {
        this.sb.append("(" + whitespace());
        ifStmt.getExpr().get(0).visit(this);
        this.sb.append(whitespace() + ")" + whitespace() + "?" + whitespace());
        ((ExprStmt<Void>) ifStmt.getThenList().get(0).get().get(0)).getExpr().visit(this);
        this.sb.append(whitespace() + ":" + whitespace());
        ((ExprStmt<Void>) ifStmt.getElseList().get().get(0)).getExpr().visit(this);
    }

    private void generateCodeFromIfElse(IfStmt<Void> ifStmt) {
        for ( int i = 0; i < ifStmt.getExpr().size(); i++ ) {
            if ( i == 0 ) {
                generateCodeFromStmtCondition("if", ifStmt.getExpr().get(i));
            } else {
                generateCodeFromStmtCondition("else if", ifStmt.getExpr().get(i));
            }
            incrIndentation();
            ifStmt.getThenList().get(i).visit(this);
            decrIndentation();
            if ( i + 1 < ifStmt.getExpr().size() ) {
                nlIndent();
                this.sb.append("}").append(whitespace());
            }
        }
    }

    private void generateCodeFromElse(IfStmt<Void> ifStmt) {
        if ( ifStmt.getElseList().get().size() != 0 ) {
            nlIndent();
            this.sb.append("}").append(whitespace()).append("else").append(whitespace() + "{");
            incrIndentation();
            ifStmt.getElseList().visit(this);
            decrIndentation();
        }
        nlIndent();
        this.sb.append("}");
    }

    private void generateCodeFromStmtCondition(String stmtType, Expr<Void> expr) {
        this.sb.append(stmtType + whitespace() + "(" + whitespace());
        expr.visit(this);
        this.sb.append(whitespace() + ")" + whitespace() + "{");
    }

    private void appendBreakStmt(RepeatStmt<Void> repeatStmt) {
        if ( repeatStmt.getMode() == Mode.WAIT ) {
            nlIndent();
            this.sb.append("break;");
        }
    }

    private void generatePrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.sb.append("package generated.main;\n\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.*;\n");
        this.sb.append("import de.fhg.iais.roberta.codegen.lejos.BlocklyMethods;\n");
        this.sb.append("import de.fhg.iais.roberta.codegen.lejos.Hal;\n\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.action.*;\n");
        this.sb.append("import lejos.remote.nxt.NXTConnection;\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.sensor.*;\n");
        this.sb.append("import de.fhg.iais.roberta.ev3.*;\n");
        this.sb.append("import de.fhg.iais.roberta.ev3.components.*;\n\n");
        this.sb.append("import java.util.LinkedHashSet;\n");
        this.sb.append("import java.util.Set;\n");
        this.sb.append("import java.util.List;\n");
        this.sb.append("import java.util.ArrayList;\n");
        this.sb.append("import java.util.Arrays;\n\n");

        this.sb.append("public class " + this.programName + " {\n");
        this.sb.append(INDENT).append("private static final boolean TRUE = true;\n");
        this.sb.append(INDENT).append(this.brickConfiguration.generateRegenerate()).append("\n\n");
        this.sb.append(INDENT).append(generateRegenerateUsedSensors()).append("\n\n");
        //        this.sb.append(INDENT).append("private Hal hal;\n\n");
        this.sb.append(INDENT).append("private Hal hal = new Hal(brickConfiguration, usedSensors);\n\n");
        this.sb.append(INDENT).append("public static void main(String[] args) {\n");
        this.sb.append(INDENT).append(INDENT).append("try {\n");
        this.sb.append(INDENT).append(INDENT).append(INDENT).append("new ").append(this.programName).append("().run();\n");
        this.sb.append(INDENT).append(INDENT).append("} catch ( Exception e ) {\n");
        this.sb.append(INDENT).append(INDENT).append(INDENT).append("lejos.hardware.lcd.TextLCD lcd = lejos.hardware.ev3.LocalEV3.get().getTextLCD();\n");
        this.sb.append(INDENT).append(INDENT).append(INDENT).append("lcd.clear();\n");
        this.sb.append(INDENT).append(INDENT).append(INDENT).append("lcd.drawString(\"Fehler im EV3\", 0, 2);\n");

        this.sb.append(INDENT).append(INDENT).append(INDENT).append("if (e.getMessage() != null) {\n");
        this.sb.append(INDENT).append(INDENT).append(INDENT).append(INDENT).append("lcd.drawString(\"Fehlermeldung\", 0, 4);\n");
        this.sb.append(INDENT).append(INDENT).append(INDENT).append(INDENT).append("lcd.drawString(e.getMessage(), 0, 5);\n");
        this.sb.append(INDENT).append(INDENT).append(INDENT).append("}\n");

        this.sb.append(INDENT).append(INDENT).append(INDENT).append("lcd.drawString(\"Press any key\", 0, 7);\n");
        this.sb.append(INDENT).append(INDENT).append(INDENT).append("lejos.hardware.Button.waitForAnyPress();\n");
        this.sb.append(INDENT).append(INDENT).append("}\n");
        this.sb.append(INDENT).append("}\n");

        //        this.sb.append(INDENT).append("public void run() {\n");
        //        this.sb.append(INDENT).append(INDENT).append("Hal hal = new Hal(brickConfiguration, usedSensors);");
    }

    private String generateRegenerateUsedSensors() {
        StringBuilder sb = new StringBuilder();
        String arrayOfSensors = "";
        for ( EV3Sensors usedSensor : this.usedSensors ) {
            arrayOfSensors += usedSensor.getJavaCode();
            arrayOfSensors += ",";
        }
        sb.append("private Set<EV3Sensors> usedSensors = " + "new LinkedHashSet<EV3Sensors>(");
        if ( this.usedSensors.size() > 0 ) {
            sb.append("Arrays.asList(" + arrayOfSensors.substring(0, arrayOfSensors.length() - 1) + ")");
        }
        sb.append(");");
        return sb.toString();
    }

    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch ( NumberFormatException e ) {
            return false;
        }
    }

}
