package io.dreistein.rigol.terminal;

import io.dreistein.rigol.comm.Scope;
import io.dreistein.rigol.comm.UnsupportedDeviceException;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.*;

/**
 * Created by Dreistein
 * 15.11.15
 */
public class CSV {

    protected static String[] DS_CHAN = {"CHAN1", "CHAN2", "CHAN3", "CHAN4", "MATH"};

    public static void main(String[] args) {
        if (args.length < 1) {
            printUsage();
            System.exit(1);
        }

        Inet4Address addr;
        try {
            addr = (Inet4Address) InetAddress.getByName(args[0]);
            Scope scope = new Scope(addr, 5555);

            CSV csv = new CSV();

            scope.send(":WAV:XINC?");
            double xIncrement = Double.parseDouble(scope.receiveString());

            Vector<String> data = new Vector<>(1201);
            data.add("TIME");
            for (int i = 0; i < 1200; i++) {
                data.add(String.valueOf(xIncrement*i));
            }
            csv.addRow(data);

            for (String channel : DS_CHAN) {
                scope.send(":"+channel+":DISP?");
                if (scope.receiveBoolean()) {
                    data.clear();
                    data.add(channel);
                    try {
                        scope.send(":WAV:SOUR " + channel);
                        Thread.sleep(200);
                        scope.send(":WAV:MODE NORM");
                        Thread.sleep(200);
                        scope.send(":WAV:FORM ASC");
                        Thread.sleep(200);
                        scope.send(":WAV:DATA?");
                        String[] points = scope.receiveString().substring(11).split(",");
                        data.addAll(Arrays.asList(points));
                        csv.addRow(data);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            FileOutputStream os = new FileOutputStream("dat.csv");
            csv.print(os);
            os.close();

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
        String executableName = new java.io.File(CSV.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath())
                .getName();
        System.out.println("Description:\n\tDriverless utility function to get the current displayed traces" +
                " from Rigol DS1000Z and MSO1000Z series Oscilloscopes as a dat.csv file.");
        System.out.println("Usage:\n\tjava - jar "+executableName+" <IP>\n\tParameter:\tIP: Current IP Address of the scope.\n");
    }

    protected ArrayList<Vector> rows;

    protected CSV() {
        rows = new ArrayList<>();
    }

    public void addRow(Collection r) {
        Vector<Object> v = new Vector<>(r.size());
        v.addAll(r);
        rows.add(v);
    }

    public void print(OutputStream os) {
        if(rows.size() < 0)
            return;

        PrintWriter writer = new PrintWriter(os);

        int size = 0;
        for (Vector r : rows)
            size = Math.max(size, r.size());

        int r = 0;
        while(r < size) {
            for (int c = 0; c < rows.size(); c++) {
                if(c > 0)
                    writer.print(',');
                try {
                    writer.print(rows.get(c).get(r));
                } catch (Exception e) {
                    System.out.printf("error: %d, %d %n", r, c);
                }
            }
            if (++r < size)
                writer.print("\r\n");
        }
        writer.flush();
    }
}
