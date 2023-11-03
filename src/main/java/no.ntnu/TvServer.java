package no.ntnu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TvServer {
    public static final int PORT_NUMBER = 10025;
    public static final String CHANNEL_COUNT_COMMAND = "c";
    public static final String TURN_ON_COMMAND = "1";
    public static final String IS_ON = "2";
    public static final String TURN_OFF_COMMAND = "0";
    public static final String CURRENT_CHANNEL = "k";
    public static final String SET_CHANNEL = "s";
    public static final String ONE_CHANNEL_UP = "a";
    public static final String ONE_CHANNEL_DOWN = "b";
    boolean isTcpServerRunning;
    private BufferedReader socketReader;
    private PrintWriter socketWriter;

    private TvLogic logic;

    public TvServer(TvLogic logic) {
        this.logic = logic;
    }

    public void startServer() {
        ServerSocket listeningSocket = openListeningSocket();
        System.out.println("Server listening on port " + PORT_NUMBER);
        if (listeningSocket != null) {
            isTcpServerRunning = true;
            while (isTcpServerRunning) {
                Socket clientSocket = acceptNextClientConnection(listeningSocket);
                if (clientSocket != null) {
                    System.out.println("New client connected from " + clientSocket.getRemoteSocketAddress());
                    handleClient(clientSocket);
                }
            }
        }
    }

    private ServerSocket openListeningSocket() {
        ServerSocket listeningSocket = null;
        try {
            listeningSocket = new ServerSocket(PORT_NUMBER);
        } catch (IOException e) {
            System.err.println("Could not open server socket: " + e.getMessage());
        }
        return listeningSocket;
    }

    private Socket acceptNextClientConnection(ServerSocket listeningSocket) {
        Socket clientSocket = null;
        try {
            clientSocket = listeningSocket.accept();
            socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            socketWriter = new PrintWriter(clientSocket.getOutputStream(), true);

        } catch (IOException e) {
            System.err.println("Could not accept client connection: " + e.getMessage());
        }
        return clientSocket;
    }

    private void handleClient(Socket clientSocket) {
        String response;
        do {
            String clientRequest = readClientRequest();
            System.out.println("Received from client: " + clientRequest);
            response = handleClientRequest(clientRequest);
            if (response != null) {
                sendResponseToClient(response);
            }
        } while (response != null);
    }

    /**
     * Read one message from the TCP socket - from the client.
     *
     * @return The received client message, or null on error
     */
    private String readClientRequest() {
        String clientRequest = null;
        try {
            clientRequest = socketReader.readLine();
        } catch (IOException e) {
            System.err.println("Could not receive client request: " + e.getMessage());
        }
        return clientRequest;
    }

    public String handleClientRequest(String clientRequest) {
        String response = null;

        switch(clientRequest) {
            case(CHANNEL_COUNT_COMMAND): {
                if (this.logic.isTvOn() == true) {
                    response = Integer.toString(this.logic.getNumberOfChannels());
                } else {
                    response = "Must turn the TV on first";
                }
                break;
            }
            case(TURN_ON_COMMAND): {
                response = Boolean.toString(this.logic.turnOn());
                break;
            }
            case(TURN_OFF_COMMAND): {
                response = Boolean.toString(this.logic.turnOff());
                break;
            }
            case(IS_ON): {
                response = this.logic.isTvOn() ? "yes" : "no";
                break;
            }
            case(CURRENT_CHANNEL): {
                if (this.logic.isTvOn()){
                    response = Integer.toString(this.logic.getCurrentChannel());
                } else {
                    response = "Must turn the TV on first";
                }
                break;
            }
            case(SET_CHANNEL): {
                if (this.logic.isTvOn()){
                    this.logic.selectChannel(clientRequest);
                    response = "Channel is set to " + this.logic.getCurrentChannel();
                } else {
                    response = "Must turn the TV on first";
                }

                break;
            }
            case(ONE_CHANNEL_UP): {
                this.logic.setChannel(this.logic.getCurrentChannel() + 1);
                response = Integer.toString(this.getLogic().getCurrentChannel());
                break;
            }
            case(ONE_CHANNEL_DOWN): {
                if(this.getLogic().getCurrentChannel() != 1) {
                    this.logic.setChannel(this.logic.getCurrentChannel() - 1);
                }
                response = Integer.toString(this.getLogic().getCurrentChannel());
                break;
            }
            default: {
                response = "Not a valid command. See protocol.md.";
            }
        }
        return response;
    }
    public String handleChangeChannel() {
        try {
            sendResponseToClient("Enter the channel number: ");
            String userInput = readClientRequest();
            int channelNumber = this.logic.setChannel(Integer.parseInt(userInput));

            if (channelNumber >= 1 && channelNumber <= this.logic.getNumberOfChannels()) {

                return "Channel changed to " + this.logic.getCurrentChannel();
            } else {
                return "Invalid channel number. Please enter a number between 1 and " + this.logic.getNumberOfChannels();
            }
        } catch (NumberFormatException e) {
            return "Invalid input. Please enter a valid number.";
        }
    }

//    public int setChannel(String userInput){
//        this.logic.setChannel(Integer.parseInt(userInput));
//        return Integer.parseInt(userInput);
//    }



    /**
     * Send a response from the server to the client, over the TCP socket.
     *
     * @param response The response to send to the client, NOT including the newline
     */
    private void sendResponseToClient(String response) {
        socketWriter.println(response);
    }

    public TvLogic getLogic() {
        return this.logic;
    }
}


