package io.dreistein.rigol.util;

import java.util.Vector;

/**
 * Created by Dreistein
 * 16.11.15
 */
public class Chalk {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_UNDERSCORE = "\u001B[4m";

    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_MAGENTA = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BG_BLACK = "\u001B[40m";
    public static final String ANSI_BG_RED = "\u001B[41m";
    public static final String ANSI_BG_GREEN = "\u001B[42m";
    public static final String ANSI_BG_YELLOW = "\u001B[43m";
    public static final String ANSI_BG_BLUE = "\u001B[44m";
    public static final String ANSI_BG_MAGENTA = "\u001B[45m";
    public static final String ANSI_BG_CYAN = "\u001B[46m";
    public static final String ANSI_BG_WHITE = "\u001B[47m";

    protected Vector<String> styles;
    protected String msg;

    public Chalk(String s) {
        this.msg = s;
        styles = new Vector<>();
    }

    protected Chalk(Chalk c, String style) {
        this.msg = c.msg;
        styles = new Vector<>(c.styles);
        styles.add(style);
    }

    public Chalk style(String style) {
        return new Chalk(this, style);
    }
    public Chalk reset() {
        styles.clear();
        return this;
    }
    public Chalk bold() {
        return style(ANSI_BOLD);
    }
    public Chalk underscore() {
        return style(ANSI_UNDERSCORE);
    }
    public Chalk black() {
        return style(ANSI_BLACK);
    }
    public Chalk red() {
        return style(ANSI_RED);
    }
    public Chalk green() {
        return style(ANSI_GREEN);
    }
    public Chalk yellow() {
        return style(ANSI_YELLOW);
    }
    public Chalk blue() {
        return style(ANSI_BLUE);
    }
    public Chalk magenta() {
        return style(ANSI_MAGENTA);
    }
    public Chalk cyan() {
        return style(ANSI_CYAN);
    }
    public Chalk white() {
        return style(ANSI_WHITE);
    }    
    public Chalk bgBlack() {
        return style(ANSI_BG_BLACK);
    }
    public Chalk bgRed() {
        return style(ANSI_BG_RED);
    }
    public Chalk bgGreen() {
        return style(ANSI_BG_GREEN);
    }
    public Chalk bgYellow() {
        return style(ANSI_BG_YELLOW);
    }
    public Chalk bgBlue() {
        return style(ANSI_BG_BLUE);
    }
    public Chalk bgMagenta() {
        return style(ANSI_BG_MAGENTA);
    }
    public Chalk bgCyan() {
        return style(ANSI_BG_CYAN);
    }
    public Chalk bgWhite() {
        return style(ANSI_BG_WHITE);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(ANSI_RESET);
        for (String style : styles) {
            builder.append(style);
        }
        builder.append(msg);
        return builder.append(ANSI_RESET).toString();
    }
}
