package de.fhg.iais.roberta.ev3;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.MotorSide;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.brickconfiguration.BrickConfiguration;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;
import de.fhg.iais.roberta.ev3.components.EV3Actor;
import de.fhg.iais.roberta.ev3.components.EV3Sensor;
import de.fhg.iais.roberta.hardwarecomponents.HardwareComponentType;
import de.fhg.iais.roberta.util.Formatter;
import de.fhg.iais.roberta.util.Pair;

/**
 * This class represents model of the hardware configuration of the brick. It is used in the code generation. <br>
 * <br>
 * The {@link EV3BrickConfiguration} contains four sensor ports and four actor ports. Client cannot connect more than that.
 */
public class EV3BrickConfiguration extends BrickConfiguration {
    private final Map<ActorPort, EV3Actor> actors;
    private final Map<SensorPort, EV3Sensor> sensors;

    private final double wheelDiameterCM;
    private final double trackWidthCM;

    /**
     * This constructor sets the each actor connected to given port, each sensor to given port, wheel diameter and track width. <br>
     * Client must provide <b>{@code Map<ActorPort, EV3Actor>}</b> where each {@link EV3Actor} is connected to {@link ActorPort} and
     * {@code Map<SensorPort, EV3Sensor>} where each {@link EV3Sensor} is connected to {@link SensorPort}.
     *
     * @param actors connected to the brick
     * @param sensors connected to the brick
     * @param wheelDiameterCM of the brick wheels
     * @param trackWidthCM of the brick
     */
    public EV3BrickConfiguration(Map<ActorPort, EV3Actor> actors, Map<SensorPort, EV3Sensor> sensors, double wheelDiameterCM, double trackWidthCM) {
        super();
        this.actors = actors;
        this.sensors = sensors;
        this.wheelDiameterCM = wheelDiameterCM;
        this.trackWidthCM = trackWidthCM;
    }

    @Override
    public String generateRegenerate() {
        StringBuilder sb = new StringBuilder();
        sb.append("private EV3BrickConfiguration brickConfiguration = new EV3BrickConfiguration.Builder()\n");
        sb.append("    .setWheelDiameter(" + this.wheelDiameterCM + ")\n");
        sb.append("    .setTrackWidth(" + this.trackWidthCM + ")\n");
        appendActors(sb);
        appendSensors(sb);
        sb.append("    .build();");
        return sb.toString();
    }

    @Override
    public String generateText(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("ev3 ").append(name).append(" {\n");
        sb.append("  wheel diameter ").append(Formatter.d2s(this.wheelDiameterCM)).append(" cm\n");
        sb.append("  track width    ").append(Formatter.d2s(this.trackWidthCM)).append(" cm\n");
        for ( SensorPort port : this.sensors.keySet() ) {
            sb.append("    sensor port ").append(port.getPortNumber()).append(" ");
            HardwareComponentType hc = this.sensors.get(port).getComponentType();
            sb.append(hc.getShortName()).append("\n");
        }
        for ( ActorPort port : this.actors.keySet() ) {
            sb.append("    actor port ").append(port.name()).append(" ");
            EV3Actor actor = this.actors.get(port);
            HardwareComponentType hc = actor.getComponentType();
            if ( hc.equals(EV3Actors.EV3_LARGE_MOTOR) ) {
                sb.append("large");
            } else if ( hc.equals(EV3Actors.EV3_MEDIUM_MOTOR) ) {
                sb.append("middle");
            } else {
                throw new RuntimeException("Key.E3");
            }
            sb.append(" motor { ");
            if ( actor.isRegulated() ) {
                sb.append("regulated ");
            } else {
                sb.append("unregulated ");
            }
            sb.append(actor.getRotationDirection().toString().toLowerCase()).append(" ");
            sb.append(actor.getMotorSide().getText()).append(" ");
            sb.append("}\n");
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Returns the sensor connected on given port.
     *
     * @param sensorPort
     * @return connected sensor on given port
     */
    public EV3Sensor getSensorOnPort(SensorPort sensorPort) {
        EV3Sensor sensor = this.sensors.get(sensorPort);
        return sensor;
    }

    /**
     * Returns the actor connected on given port.
     *
     * @param actorPort
     * @return connected actor on given port
     */
    public EV3Actor getActorOnPort(ActorPort actorPort) {
        EV3Actor actor = this.actors.get(actorPort);
        return actor;
    }

    /**
     * Get all the actors connected to the brick
     *
     * @return the actors
     */
    public Map<ActorPort, EV3Actor> getActors() {
        return this.actors;
    }

    /**
     * All the sensors connected to the brick
     *
     * @return the sensors
     */
    public Map<SensorPort, EV3Sensor> getSensors() {
        return this.sensors;
    }

    /**
     * @return the wheelDiameterCM
     */
    public double getWheelDiameterCM() {
        return this.wheelDiameterCM;
    }

    /**
     * @return the trackWidthCM
     */
    public double getTrackWidthCM() {
        return this.trackWidthCM;
    }

    /**
     * Check if the motor is regulated. Client must provide valid {@link ActorPort}.
     *
     * @param port on which the motor is connected
     * @return if the motor is regulated
     */
    public boolean isMotorRegulated(ActorPort port) {
        EV3Actor actor = this.actors.get(port);
        Assert.isTrue(actor != null, "No actor connected to the port " + port);
        return actor.isRegulated();
    }

    /**
     * This method returns the port on which the left motor is connected. If there is no left motor connected throws and {@link DbcException} exception.
     *
     * @return port on which the left motor is connected
     */
    public ActorPort getLeftMotorPort() {
        Assert.isTrue(this.actors != null, "There is no actors set to the configuration!");
        for ( Map.Entry<ActorPort, EV3Actor> entry : this.actors.entrySet() ) {
            if ( entry.getValue().getMotorSide() == MotorSide.LEFT ) {
                return entry.getKey();
            }
        }
        throw new DbcException("No left motor defined!");
    }

    /**
     * This method returns the port on which the right motor is connected. If there is no right motor connected throws and {@link DbcException} exception.
     *
     * @return port on which the left motor is connected
     */
    public ActorPort getRightMotorPort() {
        Assert.isTrue(this.actors != null, "There is no actors set to the configuration!");
        for ( Map.Entry<ActorPort, EV3Actor> entry : this.actors.entrySet() ) {
            if ( entry.getValue().getMotorSide() == MotorSide.RIGHT ) {
                return entry.getKey();
            }
        }
        throw new DbcException("No right motor defined!");
    }

    @Override
    public int hashCode() {
        // trackWidthCM and wheelDiameterCM restricted to 1 fraction digit
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.actors == null) ? 0 : this.actors.hashCode());
        result = prime * result + ((this.sensors == null) ? 0 : this.sensors.hashCode());
        long temp;
        temp = (long) (this.trackWidthCM * 10);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = (long) (this.wheelDiameterCM);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        // trackWidthCM and wheelDiameterCM restricted to 1 fraction digit
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        EV3BrickConfiguration other = (EV3BrickConfiguration) obj;
        if ( this.actors == null ) {
            if ( other.actors != null ) {
                return false;
            }
        } else if ( !this.actors.equals(other.actors) ) {
            return false;
        }
        if ( this.sensors == null ) {
            if ( other.sensors != null ) {
                return false;
            }
        } else if ( !this.sensors.equals(other.sensors) ) {
            return false;
        }
        if ( (long) (this.trackWidthCM * 10) != (long) (other.trackWidthCM * 10) ) {
            return false;
        }
        if ( (long) (this.wheelDiameterCM * 10) != (long) (other.wheelDiameterCM * 10) ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EV3BrickConfiguration [actors="
            + this.actors
            + ", sensors="
            + this.sensors
            + ", wheelDiameterCM="
            + this.wheelDiameterCM
            + ", trackWidthCM="
            + this.trackWidthCM
            + "]";
    }

    private void appendSensors(StringBuilder sb) {
        for ( Map.Entry<SensorPort, EV3Sensor> entry : this.sensors.entrySet() ) {
            appendOptional(sb, "Sensor", entry.getKey().getJavaCode(), entry.getValue());
        }
    }

    private void appendActors(StringBuilder sb) {
        for ( Map.Entry<ActorPort, EV3Actor> entry : this.actors.entrySet() ) {
            appendOptional(sb, "Actor", entry.getKey().getJavaCode(), entry.getValue());
        }
    }

    /**
     * This class is a builder of {@link EV3BrickConfiguration}
     */
    public static class Builder {
        private final Map<ActorPort, EV3Actor> actorMapping = new TreeMap<>();
        private final Map<SensorPort, EV3Sensor> sensorMapping = new TreeMap<>();

        private double wheelDiameter;
        private double trackWidth;

        /**
         * Add actor to the {@link EV3BrickConfiguration}
         *
         * @param port on which the component is connected
         * @param component we want to connect
         * @return
         */
        public Builder addActor(ActorPort port, EV3Actor component) {
            this.actorMapping.put(port, component);
            return this;
        }

        /**
         * Client must provide list of {@link Pair} ({@link ActorPort} and {@link EV3Actor})
         *
         * @param actors we want to connect to the brick configuration
         * @return
         */
        public Builder addActors(List<Pair<ActorPort, EV3Actor>> actors) {
            for ( Pair<ActorPort, EV3Actor> pair : actors ) {
                this.actorMapping.put(pair.getFirst(), pair.getSecond());
            }
            return this;
        }

        /**
         * Add sensor to the {@link EV3BrickConfiguration}
         *
         * @param port on which the component is connected
         * @param component we want to connect
         * @return
         */
        public Builder addSensor(SensorPort port, EV3Sensor component) {
            this.sensorMapping.put(port, component);
            return this;
        }

        /**
         * Client must provide list of {@link Pair} ({@link SensorPort} and {@link EV3Sensor})
         *
         * @param sensors we want to connect to the brick configuration
         * @return
         */
        public Builder addSensors(List<Pair<SensorPort, EV3Sensor>> sensors) {
            for ( Pair<SensorPort, EV3Sensor> pair : sensors ) {
                this.sensorMapping.put(pair.getFirst(), pair.getSecond());
            }
            return this;
        }

        /**
         * Set the wheel diameter
         *
         * @param wheelDiameter in cm
         * @return
         */
        public Builder setWheelDiameter(double wheelDiameter) {
            this.wheelDiameter = wheelDiameter;
            return this;
        }

        /**
         * Set the track width
         *
         * @param trackWidth in cm
         * @return
         */
        public Builder setTrackWidth(double trackWidth) {
            this.trackWidth = trackWidth;
            return this;
        }

        public EV3BrickConfiguration build() {
            return new EV3BrickConfiguration(this.actorMapping, this.sensorMapping, this.wheelDiameter, this.trackWidth);
        }

        @Override
        public String toString() {
            return "Builder [actorMapping="
                + this.actorMapping
                + ", sensorMapping="
                + this.sensorMapping
                + ", wheelDiameter="
                + this.wheelDiameter
                + ", trackWidth="
                + this.trackWidth
                + "]";
        }
    }
}
