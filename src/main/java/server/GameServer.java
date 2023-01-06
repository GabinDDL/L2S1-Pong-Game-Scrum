package server;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import model.interfaces.InterfaceRacketController.State;

/**
 * This class is the server side of the game. It is responsible for creating the
 * server socket and for creating a new thread for each client that connects to
 * the server.
 */
public class GameServer implements Runnable {

    // Server port
    public static final int PORT = 5555;
    private String ip = "";

    // Server information
    private ServerSocket serverSocket;
    private int nbPlayers;
    private int nbPlayersConnected;
    private boolean isServerOn;// True if the server is on

    // Server connections
    private ServerSideConnection[] serverSideConnections;

    // Game information
    private CourtModel court;
    private boolean isGameOn; // True if the game is on (not paused)

    static final double WIDTH = 1000;
    static final double HEIGHT = 600;

    public static final int FPS = 50;

    // Constructors

    /**
     * The constructor of the GameServer class. It creates a new server socket on
     * port {@code PORT}, sets the number of players to the number of players passed
     * as argument, sets the number of players connected to 0 and creates an array
     * of {@code ServerSideConnection} of size {@code nbPlayers}.
     * 
     * @param nbPlayers number of players on the server
     */
    private GameServer(int nbPlayers) {
        System.out.println("Starting server...");

        try {
            serverSocket = new ServerSocket(PORT);
            this.nbPlayers = nbPlayers;
        } catch (IOException e) {
            System.out.println("IOException from GameServer constructor");
            e.printStackTrace();
        }

        try {
            updateIp();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not get ip address");
        }
        isServerOn = true;

        System.out.println("------------------------------------------------------");
        System.out.println("Server started on:\nip: " + ip + "\nport: " + PORT);
        System.out.println("------------------------------------------------------");
    }

    /**
     * The constructor of the GameServer class. It creates a new server socket on
     * port {@code PORT}, sets the number of players to 2, sets the number of
     * players connected to 0 and creates an array of {@code ServerSideConnection}
     * of size {@code nbPlayers}.
     * 
     * @param nbPlayers number of players on the server
     */
    public GameServer() {
        this(2);
    }

    // Getter

    public String getIp() {
        return ip;
    }

    // Methods

    /**
     * Updates the ip address of the server by getting the first non-loopback
     * and non-link-local address of the first network interface.
     * 
     * @throws SocketException if there is an error getting the network interfaces
     *                         of the computer
     */
    private void updateIp() throws SocketException {
        InetAddress ipAddress = null;
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();
            Enumeration<InetAddress> addresses = ni.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if (addr.isSiteLocalAddress() && !addr.isLinkLocalAddress() && !addr.isLoopbackAddress()
                        && addr instanceof Inet4Address) {
                    ipAddress = addr;
                    break;
                }
            }
        }
        this.ip = ipAddress.getHostAddress();
    }

    // Control methods

    /**
     * This is the main loop of the server. It waits for connections from clients,
     * and when all the clients are connected, it starts the game loop.
     * 
     * If the game has ended it waits for another game to start)
     */
    @Override
    public void run() {
        while (isServerOn) {
            startGameRoutine();
        }
        closeServer();
    }

    /**
     * This method takes care of the game routine. It waits for connections from
     * clients, and when all the clients are connected, it starts the game loop.
     */
    private void startGameRoutine() {
        try {
            acceptConnections();
            gameLoop();
        } catch (Exception e) {
            System.out.println("Connection interrupted");
            closeConnections();
        }
    }

    /**
     * It waits for connections from clients, and when a client connects, it creates
     * a new ServerSideConnection object and starts a new thread for it.
     */
    public void acceptConnections() {
        System.out.println("Waiting for connections...");

        nbPlayersConnected = 0;
        serverSideConnections = new ServerSideConnection[nbPlayers];

        try {
            while (nbPlayersConnected < nbPlayers) {
                Socket socket = serverSocket.accept();

                nbPlayersConnected++;
                System.out.println("Connection accepted from " + socket.getInetAddress());
                System.out.println("Player " + nbPlayersConnected + " connected");

                serverSideConnections[nbPlayersConnected - 1] = new ServerSideConnection(socket,
                        nbPlayersConnected - 1);

                Thread thread = new Thread(serverSideConnections[nbPlayersConnected - 1]);
                thread.start();
            }

            System.out.println("All players connected. No more connections accepted.");
        } catch (IOException e) {
            closeConnections();
        }

        try {
            Thread.sleep(1000);// Sleep for 1 second to let the clients initialize
        } catch (InterruptedException e) {
        }

    }

    /**
     * Game loop. It receives the racket states from the clients and updates
     * the court model. It then sends the updated court model to all the clients.
     */
    public void gameLoop() {

        System.out.println("Starting server loop...");
        notifyGameOn();
        System.out.println("Initializing court...");

        court = new CourtModel(1000, 600, 7);

        System.out.println("Court initialized");

        isGameOn = false;

        waitForPlayers();

        // We wait for 1 second to let the clients initialize
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Game started");

        long last = System.currentTimeMillis();

        while (isGameOn) {

            for (ServerSideConnection ssc : serverSideConnections) {
                if (!ssc.isConnectionOn()) {
                    closeConnections();
                    isGameOn = false;
                    break;
                }
            }

            if (!isGameOn) {
                break;
            }

            long now = System.currentTimeMillis();

            if (last - now != 0) {
                State[] states = new State[nbPlayersConnected];

                for (int i = 0; i < nbPlayersConnected; i++) {
                    states[i] = Conversion.intToState(serverSideConnections[i].getRacketState());

                }

                court.update((now - last) / 1000., states);

                sendUpdate();
            }

            last = now;

            try {
                Thread.sleep(1000 / FPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Senders

    /**
     * Notify the players that the game can start (everyone is connected).
     */
    private void notifyGameOn() {
        isGameOn = true;

        for (ServerSideConnection ssc : serverSideConnections) {
            System.out.println("Sending game on to player " + ssc.getPlayerId());
            ssc.sendGameOn();
        }
    }

    /**
     * Waits for the clients to notify the server that their game can start.
     */
    private void waitForPlayers() {
        boolean[] ready;
        while (!isGameOn) {
            ready = new boolean[nbPlayers];

            for (int i = 0; i < nbPlayers; i++) {
                ready[i] = serverSideConnections[i].isPlayerReady();
            }

            if (ready[0] && ready[1]) {
                isGameOn = true;
            }
        }
    }

    /**
     * Send the court state to all the clients.
     */
    public void sendUpdate() {
        if (!isGameOn)
            return;

        StringBuilder sb = new StringBuilder();
        sb.append(court.encode());
        sb.append("game");
        sb.append(";");
        sb.append(isGameOn ? 1 : 0);
        String encodedInfo = sb.toString();

        for (ServerSideConnection serverSideConnection : serverSideConnections) {
            try {
                serverSideConnection.sendEncodedInfo(encodedInfo);
            } catch (Exception e) {
                closeConnections();
                isGameOn = false;
                break;
            }
        }
    }

    // Closing

    /**
     * Closes the server socket and all the serverSide sockets.
     */
    public void closeServer() {
        if (!isServerOn)
            return;

        System.out.println("Closing server...");

        isServerOn = false;

        closeConnections();

        try {
            serverSocket.close();
            System.out.println("Server closed");
        } catch (IOException e) {
            System.out.println("IOException from closeServer");
            e.printStackTrace();
        }
    }

    private void closeConnections() {
        System.out.println("Closing connections...");
        for (ServerSideConnection ssc : serverSideConnections) {
            try {
                ssc.closeConnection();
            } catch (NullPointerException e) {
            } catch (Exception e) {
                System.out.println("Exception raised while closing " + ssc.getPlayerId() + " connection");
                System.out.println(e.getMessage());
            }
        }
    }

}
