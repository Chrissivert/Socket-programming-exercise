package no.ntnu;

import org.junit.Test;
import static org.junit.Assert.*;

public class SmartTvTest {

    @Test(expected = IllegalArgumentException.class)
    public void testSmartTvConstructorWithZeroChannels() {
        new SmartTv(0);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testSmartTvConstructorWithNegativeChannels() {
        new SmartTv(-1);
    }

    @Test
    public void testTVTurnedOffByDefault() {
        SmartTv tv = new SmartTv(10);
        assertEquals("no", tv.isOn());
    }

    @Test
    public void cantGetChannelListWhenTVIsOff() {
        SmartTv tv = new SmartTv(10);
        assertEquals("Must turn the TV on first", tv.getAmountOfChannels());
    }

    @Test
    public void canTurnOnTV() {
        SmartTv tv = new SmartTv(10);
        assertFalse(tv.isTvOn);
        tv.handleTurnOnCommand();
        assertTrue(tv.isTvOn);
    }
    @Test
    public void canChangeChannel() {
        SmartTv tv = new SmartTv(10);
        tv.handleTurnOnCommand();
        int response = tv.setChannel("14");
        assertEquals(response, tv.currentChannel);
    }

//    @Test
//    public void cantChangeToNegativeChannel(){
//        SmartTv tv = new SmartTv(10);
//        tv.handleTurnOnCommand();
//        int response = tv.setChannel("-14");
//        assertEquals(response, tv.);
    }
}
