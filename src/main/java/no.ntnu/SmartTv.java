package no.ntnu;

import org.w3c.dom.ls.LSOutput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Smart TV - TCP server.
 */
public class SmartTv {
  public static final int PORT_NUMBER = 10025;
  public static final String CHANNEL_COUNT_COMMAND = "c";
  public static final String TURN_ON_COMMAND = "1";

  public static final String IS_ON = "2";
  public static final String TURN_OFF_COMMAND = "0";
  public static final String OK_REPONSE = "ok";

  public static final String CURRENT_CHANNEL = "k";

  public static final String SELECT_CHANNEL ="q";
  boolean isTvOn;
  final int numberOfChannels;
  int currentChannel;
  boolean isTcpServerRunning;
  private BufferedReader socketReader;
  private PrintWriter socketWriter;

  /**
   * Create a new Smart TV.
   *
   * @param numberOfChannels The total number of channels the TV has
   */
  public SmartTv(int numberOfChannels) {
    if (numberOfChannels < 1) {
      throw new IllegalArgumentException("Number of channels must be a positive number");
    }

    this.numberOfChannels = numberOfChannels;
    isTvOn = false;
    currentChannel = 1;
  }

  public static void main(String[] args) {
    SmartTv tv = new SmartTv(13);
    tv.startServer();
  }


  /**
   * Start TCP server for this TV.
   */
  private void startServer() {
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


  private String handleClientRequest(String clientRequest) {
    String response = null;

    if (clientRequest != null) {
      if (clientRequest.equals(CHANNEL_COUNT_COMMAND)) {
        response = handleChannelCountCommand();
      } else if (clientRequest.equals(TURN_ON_COMMAND)) {
        response = handleTurnOnCommand();
      } else if (clientRequest.equals(TURN_OFF_COMMAND)) {
        response = handleTurnOffCommand();
      } else if (clientRequest.equals(IS_ON)) {
        response = isOn();
      } else if (clientRequest.equals(CURRENT_CHANNEL)) {
        response = getCurrentChannel();
    } else if (clientRequest.equals(SELECT_CHANNEL))
      response = selectChannel();
    }
      return response;
  }

  private String selectChannel() {
    try {
      sendResponseToClient("Enter the channel number: ");
      String userInput = readClientRequest();
      int channelNumber = Integer.parseInt(userInput);

      if (channelNumber >= 1 && channelNumber <= numberOfChannels) {

        currentChannel = channelNumber;
        return "Channel changed to " + currentChannel;
      } else {
        return "Invalid channel number. Please enter a number between 1 and " + numberOfChannels;
      }
    } catch (NumberFormatException e) {
      return "Invalid input. Please enter a valid number.";
    }
  }

  private String handleTurnOnCommand() {
    isTvOn = true;
    return OK_REPONSE;
  }

  private String handleTurnOffCommand(){
    isTvOn = false;
    return OK_REPONSE;
  }

  private String getCurrentChannel(){
      return Integer.toString(currentChannel);
  }

  public String isOn() {
    return isTvOn ? "yes" : "no";
  }


  private String handleChannelCountCommand() {
    String response;
    if (isTvOn) {
      response = "c" + numberOfChannels;
    } else {
      response = "Must turn the TV on first";
    }
    return response;
  }

  /**
   * Send a response from the server to the client, over the TCP socket.
   *
   * @param response The response to send to the client, NOT including the newline
   */
  private void sendResponseToClient(String response) {
    socketWriter.println(response);
  }
}
