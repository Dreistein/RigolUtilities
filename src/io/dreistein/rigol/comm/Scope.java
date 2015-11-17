package io.dreistein.rigol.comm;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Dreistein
 * 14.11.15
 */
public class Scope {

    protected final Pattern SUPPORTED_DEVICES = Pattern.compile("^(MSO|DS)1\\d{2}4Z(-S)?$");
    protected final String MANUFACTURER_ID = "RIGOL TECHNOLOGIES";

    protected final int DEFAULT_TIMEOUT = 1000;
    protected final Pattern END = Pattern.compile("$", Pattern.MULTILINE);

    protected String model;
    protected boolean isMixedSignal;
    protected String serialNumber;
    protected String firmware;

    protected TelnetClient client;
    protected PrintWriter out;
    protected Scanner in;

    public Scope(Inet4Address addr, int port)
            throws IOException, UnsupportedDeviceException {
        client = new TelnetClient();
        client.connect(addr, port);
        client.setSoTimeout(DEFAULT_TIMEOUT);

        out = new PrintWriter(client.getOutputStream());
        in = new Scanner(client.getInputStream());

        send("*idn?");
        String IDString = receiveString();
        String[] IDParts = IDString.split(",");

        if (!MANUFACTURER_ID.equals(IDParts[0].toUpperCase()))
            throw new UnsupportedDeviceException("Not a Rigol Oscilloscope!", IDString);

        Matcher m = SUPPORTED_DEVICES.matcher(IDParts[1]);
        if (!m.matches())
            throw new UnsupportedDeviceException("Oscilloscope Model not supported!", IDParts[1]);

        model = IDParts[1];
        isMixedSignal = "MSO".equals(m.group(1));
        serialNumber = IDParts[2];
        firmware = IDParts[3];
    }

//getter setter

    public String getModel() {
        return model;
    }

    public boolean isMixedSignal() {
        return isMixedSignal;
    }

    public String getFirmware() {
        return firmware;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setDefaultTimeout(int timeout) {
        try {
            client.setSoTimeout(timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//comm methods

    public void send(String command) {
        out.print(command);
        out.flush();
    }

    public String receiveString() {
        return in.nextLine();
    }

    public boolean receiveBoolean() {
        return receiveString().equals("1");
    }
}
