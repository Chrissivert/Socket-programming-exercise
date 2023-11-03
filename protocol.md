# Protocol

Message are separated by newline.

The client always sends a request first, the server responds.
There can be several requests/responses during one connection.

The following requests could be sent from client to server:

* "1" turn on the TV
* "0" turn off the TV
* 2 to check if the TV is on
* "c" get the number of channels
* "k" get current channel
* "q" select a specific next channel
* "a" to increase channel by 1
* "b" to decrease channel by 1


public static final String OK_REPONSE = "ok";


Server can respond with:

* "ok"  - performed the necessary operation
* "c##"  - report the number of channels, where ### is the number of channels, as a string, 
  can be several bytes, until the newline
* "Enter the channel number" - request to enter the channel number after writing q
  to change channel
* "Channel changed to" - after writing the specified channel number