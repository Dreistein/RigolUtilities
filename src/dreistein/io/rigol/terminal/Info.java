package dreistein.io.rigol.terminal;

import dreistein.io.rigol.com.Scope;
import dreistein.io.rigol.com.UnsupportedDeviceException;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;

/**
 * Created by Dreistein
 * 16.11.15
 */
public class Info {

    public static void main(String[] args) {
        if (args.length < 1) {
            printUsage();
            System.exit(1);
        }

        Inet4Address addr;
        try {
            addr = (Inet4Address) InetAddress.getByName(args[0]);
            Scope scope = new Scope(addr, 5555);

            System.out.printf("Model:\t%s%nSerial Number:\t%s%nFirmware Version:\t%s%n%n",
                    scope.getModel(),
                    scope.getSerialNumber(),
                    scope.getFirmware()
            );

            scope.send(":TRIG:STAT?");
            System.out.println("Scope status:\t" + scope.receiveString() + "\n");

            System.out.println("Active channel:");

            String[] channel = new String[] {"CHAN1", "CHAN2", "CHAN3", "CHAN4", "MATH"};
            for (String c : channel) {
                scope.send(":" + c + ":DISP?");
                if (scope.receiveBoolean())
                    System.out.println("\t- " + c);
            }

        } catch (IOException e) {
            System.err.println("Not a valid address!");
            printUsage();
            System.exit(1);
        } catch (UnsupportedDeviceException e) {
            System.err.println(e.getMessage());
            printUsage();
            System.exit(1);
        }
    }

    public static void printUsage() {
        String executableName = new java.io.File(Info.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath())
                .getName();
        System.out.println("Description:\n\tPrints usable info about" +
                " Rigol DS1000Z and MSO1000Z series Oscilloscopes on screen");
        System.out.println("Usage:\n\tjava -jar "+executableName+" <IP>\n\tParameter:\tIP: Current IP Address of the scope.\n");
    }
}
