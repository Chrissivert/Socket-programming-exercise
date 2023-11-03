package no.ntnu;

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
  public static final String ONE_CHANNEL_UP = "a";
  public static final String ONE_CHANNEL_DOWN = "b";

  boolean isTvOn;
  int numberOfChannels;
  int currentChannel;
  boolean isTcpServerRunning;
  private BufferedReader socketReader;
  private PrintWriter socketWriter;

  private TvLogic logic;
  private TvServer tvServer;

  /**
   * Create a new Smart TV.
   *
   * @param numberOfChannels The total number of channels the TV has
   */
  public SmartTv(int numberOfChannels) {

    this.logic = new TvLogic(numberOfChannels);
    this.tvServer = new TvServer(this.logic);
    tvServer.startServer();
  }

  public static void main(String[] args) {
    SmartTv tv = new SmartTv(13);
  }
}
