package dreistein.io.rigol.com;

/**
 * Created by Dreistein
 * 14.11.15
 */
public class UnsupportedDeviceException extends Exception {

    protected String device;

    public UnsupportedDeviceException(String msg, String device) {
        super(msg + " " + device);
        this.device = device;
    }

    public String getDevice() {
        return device;
    }


}
