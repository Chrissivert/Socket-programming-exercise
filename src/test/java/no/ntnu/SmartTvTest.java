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
    public void testTvTurnedOffByDefault(){
            SmartTv tv = new SmartTv(10);
            assertEquals("no", tv.isOn());
    }
}
