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
        TvLogic tvLogic = new TvLogic(10);
        TvServer tvServer = new TvServer(tvLogic);
        assertEquals(false,  tvServer.getLogic().isTvOn());
    }

    @Test
    public void cantGetChannelListWhenTVIsOff() {
        TvLogic tvLogic = new TvLogic(10);
        TvServer tvServer = new TvServer(tvLogic);
        assertEquals("Must turn the TV on first", tvServer.handleClientRequest("c"));
    }

    @Test
    public void canTurnOnTV() {
        TvLogic tvLogic = new TvLogic(10);
        TvServer tvServer = new TvServer(tvLogic);
        assertFalse(tvServer.getLogic().isTvOn());
        tvServer.getLogic().turnOn();
        assertTrue(tvServer.getLogic().isTvOn());
    }
    @Test
    public void canChangeChannel() {
        TvLogic tvLogic = new TvLogic(10);
        TvServer tvServer = new TvServer(tvLogic);
        tvServer.getLogic().turnOn();
        int initialChannel = tvServer.getLogic().getCurrentChannel();
        String response = tvServer.handleClientRequest("Increase channel");
        assertEquals(Integer.parseInt(response), (initialChannel+1));
    }

    @Test
    public void cantChangeToNegativeChannel() {
        TvLogic tvLogic = new TvLogic(10);
        TvServer tvServer = new TvServer(tvLogic);
        tvServer.getLogic().turnOn();
        int initialChannel = tvServer.getLogic().getCurrentChannel();
        String response = tvServer.handleClientRequest("Decrease channel");
        assertEquals(Integer.parseInt(response), (initialChannel));
    }

//    @Test
//    public void cantChangeToNegativeChannel(){
//        SmartTv tv = new SmartTv(10);
//        tv.handleTurnOnCommand();
//        int response = tv.setChannel("-14");
//        assertEquals(response, tv.);
//    }
}
