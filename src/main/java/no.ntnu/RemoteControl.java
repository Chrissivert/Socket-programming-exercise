package no.ntnu;

import static no.ntnu.TvServer.PORT_NUMBER;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Remote control for a TV - a TCP client.
 */
public class RemoteControl {
  private Socket socket;
  private BufferedReader socketReader;
  private PrintWriter socketWriter;
  private Scanner scanner;

  public static void main(String[] args) {
    RemoteControl remoteControl = new RemoteControl();
    remoteControl.run();
  }

  private void run() {
    try {
      printFunctionInfoToUser();
      socket = new Socket("localhost", PORT_NUMBER);
      socketWriter = new PrintWriter(socket.getOutputStream(), true);
      socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      scanner = new Scanner(System.in);

      // Start a separate thread for receiving server responses.
      Thread responseThread = new Thread(this::receiveServerResponses);
      responseThread.start();

      // Read user input and send it to the server.

      boolean isRunning = true;
      while (isRunning) {
        String userInput = scanner.nextLine();
        if (userInput.toLowerCase() == "exit") {
          isRunning = false;
          System.out.println("Remote is disconnected.");
        } else {
          sendCommandToServer(userInput);
        }
      }

    } catch (IOException e) {
      System.err.println("Could not establish connection to the server: " + e.getMessage());
    }
  }

  private void sendCommandToServer(String command) throws IOException {
    socketWriter.println(command);
  }

  private void sendRandomMessages() throws IOException{
    sendCommandToServer("c");
    sendCommandToServer("0");
    sendCommandToServer("c");
    sendCommandToServer("1");
  }

  private void receiveServerResponses() {
    try {
      boolean serverRunning = true;
      while (serverRunning) {
        String serverResponse = socketReader.readLine();
        if (serverResponse == null) {
          serverRunning = false;
        }
        System.out.println("Server's response: " + serverResponse);
      }
    } catch (IOException e) {
      System.err.println("Error while receiving server responses: " + e.getMessage());
    }
  }

  private void printFunctionInfoToUser(){
    System.out.println("0 to turn off tv \n 1 to turn on \n 2 to check if tv is turned on \n " +
            "c to get amount of channels \n s to set channel \n  k to get current channel \n  a to increase channel by 1 \n  " +
            "b to decrease channel by 1");

  }
}
