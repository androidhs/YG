package android.eq366pt.zxtnetwork.com.yg;

import java.util.ArrayList;

/**
 * Created by lbk on 2018/2/6.
 */

public class LED {
    private keyLED keyLED;
    private ArrayList<LEDOPTION> options;

    public LED(android.eq366pt.zxtnetwork.com.yg.keyLED keyLED, ArrayList<LEDOPTION> options) {
        this.keyLED = keyLED;
        this.options = options;
    }

    public android.eq366pt.zxtnetwork.com.yg.keyLED getKeyLED() {
        return keyLED;
    }

    public void setKeyLED(android.eq366pt.zxtnetwork.com.yg.keyLED keyLED) {
        this.keyLED = keyLED;
    }

    public ArrayList<LEDOPTION> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<LEDOPTION> options) {
        this.options = options;
    }
}
