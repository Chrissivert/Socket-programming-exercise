package no.ntnu;

/**
 * Turn on
 * Turn off
 * Number of channels
 * isOn
 * getNumberOfChannels
 * changeChannel
 */
public class TvLogic {
    private boolean isTvOn;
    private final int numberOfChannels;
    private int currentChannel;

    public TvLogic(int numberOfChannels) {
        if (numberOfChannels < 1) {
            throw new IllegalArgumentException("Amount of channels must be 1 or higher.");
        }
        this.numberOfChannels = numberOfChannels;
        this.isTvOn = false;
        this.currentChannel = 1;

    }

    public boolean turnOn() {
        this.isTvOn = true;
        return this.isTvOn();
    }

    public boolean turnOff() {
        this.isTvOn = false;
        return this.isTvOn();
    }

    public int setChannel(int channel) {
        if (channel > 0) {
            this.currentChannel = channel;
        }
        return this.currentChannel;
    }

    public int getNumberOfChannels() {
        return this.numberOfChannels;
    }

    public int getCurrentChannel() {
        return this.currentChannel;
    }

    public boolean isTvOn() {
        return this.isTvOn;
    }
}
