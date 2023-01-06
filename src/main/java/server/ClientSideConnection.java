package server;

import model.interfaces.InterfaceRacketController.State;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.Socket;

/**
 * This class takes care of the connection between the server and a client from
 * the client's side.
 * It is responsible for sending and receiving messages from the server.
 */
public class ClientSideConnection implements Runnable {

    // Server information
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    // Client information
    private int playerID;
    private State racketStatus;
    private String encodedInfo;

    private boolean isGameOn = false; // True if the game is on (not paused)
    private boolean isConnectionOn = true;

    private double width;
    private double height;

    public ClientSideConnection(String serverAddress, int port) throws IOException {
        System.out.println("Starting client side connection...");

        this.socket = new Socket(serverAddress, port);
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());

        playerID = dataInputStream.readInt();
        width = dataInputStream.readDouble();
        height = dataInputStream.readDouble();

        System.out.println("Player ID: " + playerID);

    }

    public ClientSideConnection(String serverAddress) throws IOException {
        this(serverAddress, GameServer.PORT);
    }

    public ClientSideConnection() throws IOException {
        this("localhost", GameServer.PORT);
    }

    // Getters
    public String getEncodedInfo() {
        return encodedInfo;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public boolean getIsGameOn() {
        return isGameOn;
    }

    public int getPlayerID() {
        return playerID;
    }

    // Setters
    void setEncodedInfo(String encodedInfo) {
        this.encodedInfo = encodedInfo;
    }

    public void setRacketStatus(State racketStatus) {
        this.racketStatus = racketStatus;
    }

    public void setIsGameOn(boolean isGameOn) {
        this.isGameOn = isGameOn;
    }

    // Methods

    /**
     * Reads the player number from the server, sends it back to the server, starts
     * a thread to receive messages from the server, and then sends racket status to
     * the server.
     */
    @Override
    public void run() {
        System.out.println("Starting client side connection thread...");
        try {
            System.out.println("Waiting for game to start...");

            isGameOn = dataInputStream.readBoolean();

            if (isGameOn)
                System.out.println("All players are connected");

            startReceiving();
        } catch (IOException e) {
            System.out.println("IOException from ClientSideConnection run");
            e.printStackTrace();

        }
    }

    /**
     * Sends the racket status to the server.
     */
    public void sendRacketStatus() {
        if (!isConnectionOn)
            return;

        try {
            dataOutputStream.writeInt(Conversion.stateToInt(racketStatus));
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println("Connection interrupted");
            closeConnection();
        }
    }

    /**
     * Notifies the server that the player is ready.
     */
    public void sendReady() {
        if (!isConnectionOn)
            return;

        try {
            dataOutputStream.writeBoolean(true);
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println("IOException from ClientSideConnection sendReady");
            e.printStackTrace();
        }
    }

    /**
     * Reads a string from the socket and returns it.
     * 
     * @return A string
     */
    public String receiveString() {
        if (!isConnectionOn)
            return "";

        String s = "";
        try {
            s = dataInputStream.readUTF();
        } catch (IOException e) {
            System.out.println("Connection interrupted");
            closeConnection();
        }
        return s;
    }

    /**
     * Starts a new thread that will receive information from the server and update
     * the court accordingly.
     */
    public void startReceiving() {
        while (isGameOn && isConnectionOn) {
            String s = receiveString();
            if (!s.equals("")) {
                setEncodedInfo(s);
            }
        }
    }

    /**
     * Closes the connection between the client and the server.
     */
    public void closeConnection() {
        System.out.println("Closing connection...");
        isGameOn = false;
        isConnectionOn = false;
        try {
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("IOException from ClientSideConnection closeConnection");
            e.printStackTrace();
        }
    }
}