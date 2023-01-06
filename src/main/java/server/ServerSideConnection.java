package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.Socket;

/**
 * This class takes care of the connection between the server and a client from
 * the server's side.
 * It is responsible for sending and receiving messages from the client.
 */
public class ServerSideConnection implements Runnable {

    // Server information
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    private boolean isConnectionOn; // True if the connection is on

    // Client information
    private int playerId;
    private int racketState;

    private boolean isGameOn; // True if the game is on (not paused)
    private boolean isPlayerReady; // True if the player is ready

    /**
     * The constructor of the ServerSideConnection class. It creates a new
     * DataInputStream and a new DataOutputStream from the socket passed as
     * argument, and it reads the player ID from the client.
     * 
     * @param socket   The socket of the connection
     * @param playerId The player ID
     */
    public ServerSideConnection(Socket socket, int playerId) {
        this.socket = socket;
        this.playerId = playerId;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("IOException from ServerSideConnection constructor");
            e.printStackTrace();
        }
        isConnectionOn = true;
        isPlayerReady = false;
    }

    // Getters
    public Socket getSocket() {
        return socket;
    }

    public int getRacketState() {
        return racketState;
    }

    public int getPlayerId() {
        return playerId;
    }

    public boolean isPlayerReady() {
        return isPlayerReady;
    }

    public boolean isConnectionOn() {
        return isConnectionOn;
    }

    // Methods

    public void sendGameOn() {
        try {
            isGameOn = true;
            dataOutputStream.writeBoolean(true);
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println("IOException from ServerSideConnection sendGameOn");
            System.out.println("Start information not sent");
            e.printStackTrace();
        }

    }

    private void mainLoop() {
        while (isConnectionOn)
            receiveRacketState();
    }

    /**
     * Reads the racket state from the client, computes the encoded info, and
     * sends it back to the client.
     */
    @Override
    public void run() {
        System.out.println("Starting thread for player " + playerId);

        try {
            dataOutputStream.writeInt(playerId);
            dataOutputStream.writeDouble(GameServer.WIDTH);
            dataOutputStream.writeDouble(GameServer.HEIGHT);

            dataOutputStream.flush();

            while (!isGameOn) {
                // Sleep until the game starts
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("InterruptedException from ServerSideConnection run");
                    e.printStackTrace();
                }
            }

            while (!isPlayerReady) {
                isPlayerReady = dataInputStream.readBoolean();
                // Sleep until the player is ready
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("InterruptedException from ServerSideConnection run");
                    e.printStackTrace();
                }
            }
            System.out.println("Player " + playerId + " is ready");

            mainLoop();

        } catch (IOException e) {
            System.out.println("IOException from ServerSideConnection run");
            e.printStackTrace();
        }
    }

    /**
     * Reads the racket state from the data input stream and stores it in the
     * racketState variable.
     */
    private void receiveRacketState() {
        try {
            racketState = dataInputStream.readInt();
        } catch (IOException e) {
            System.out.println("Connection interrupted");
            closeConnection();
        }
    }

    /**
     * Sends the encoded info to the client.
     */
    public void sendEncodedInfo(String encodedInfo) {
        try {
            dataOutputStream.writeUTF(encodedInfo);
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println("IOException from ServerSideConnection sendEncodedInfo");
            e.printStackTrace();
        }
    }

    /**
     * Closes the connection between the client and the server.
     */
    void closeConnection() {
        isConnectionOn = false;
        try {
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("IOException from ServerSideConnection close");
            e.printStackTrace();
        }
    }
}