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
