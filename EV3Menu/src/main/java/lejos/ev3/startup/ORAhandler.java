package lejos.ev3.startup;

/**
 * No Singleton Pattern but do not use more than one
 *
 * @author dpyka
 */
public class ORAhandler {

    private static boolean hasConnectionError = false;
    private static boolean isRegistered = false;
    private static boolean timeout = false;

    private ORApushCmd pushCmd;
    private Thread serverCommunicator;

    private static boolean restart = true;

    /**
     * Creates a control object for most of the Open Roberta Lab related
     * functionality.
     */
    public ORAhandler() {
        // default
    }

    private void resetState() {
        setRegistered(false);
        setConnectionError(false);
        setTimeout(false);
    }

    /**
     * Start the brick server "push" communication.
     *
     * @param serverBaseIP
     *        The base IP like 192.168.56.1:1999
     * @param token
     *        Token for client/ brick identification
     */
    public void startServerCommunicator(String serverBaseIP, String token) {
        resetState();
        this.pushCmd = new ORApushCmd(serverBaseIP, token);
        this.serverCommunicator = new Thread(this.pushCmd);
        this.serverCommunicator.start();
    }

    /**
     * Disconnect the http connection to ORA server.
     */
    public void disconnect() {
        setRegistered(false);
        if ( this.pushCmd.getHttpConnection() != null ) {
            this.pushCmd.getHttpConnection().disconnect();
        }
    }

    public static boolean isRegistered() {
        return ORAhandler.isRegistered;
    }

    public static void setRegistered(boolean isRegistered) {
        ORAhandler.isRegistered = isRegistered;
    }

    public static boolean hasConnectionError() {
        return hasConnectionError;
    }

    public static void setConnectionError(boolean hasConnectionError) {
        ORAhandler.hasConnectionError = hasConnectionError;
    }

    public static boolean hasTimeout() {
        return timeout;
    }

    public static void setTimeout(boolean timeout) {
        ORAhandler.timeout = timeout;
    }

    public static boolean isRestarted() {
        return restart;
    }

    public static void setRestarted(boolean restart) {
        ORAhandler.restart = restart;
    }
}