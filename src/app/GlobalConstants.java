package app;

import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * @author <a href="https://steamcommunity.com/id/KitsuneAya/">Ayaya</a>
 * @apiNote Globally accessible constants for use within many classes.
 */
public final class GlobalConstants {

    // Row Height
    public static final int ROW_HEIGHT = 24;


    // Component Widths (in order of their position from left to right)
    public static final int ROW_NUMBER_WIDTH = 20;
    public static final int TIMER_NAME_WIDTH = 0;
    public static final int START_PAUSE_BUTTON_WIDTH = 60;
    public static final int STOP_BUTTON_WIDTH = 50;
    public static final int TIMER_TYPE_SELECTOR_WIDTH = 185;
    public static final int ALARM_OPTIONS_WIDTH = 160;
    public static final int CARD_PANEL_WIDTH = 80;
    public static final int TIMER_REPEAT_TOGGLE_WIDTH = ROW_HEIGHT; // Equals row height to make it a square


    // Card Panel Layers
    // Dropdown Menu Options
    public static final String OPTION_1 = "Countdown (hh:mm:ss)";
    public static final String OPTION_2 = "Time past the hour (mm:ss)";
    public static final String[] TIMER_TYPE_OPTIONS = {OPTION_1, OPTION_2};

    // Countdown Layer
    public static final String COUNTDOWN = "Display Time Remaining";


    // ImageIcons
    public static final ImageIcon REPEAT_SYMBOL_OFF = new RepeatSymbol(Color.WHITE, Color.BLACK);
    public static final ImageIcon REPEAT_SYMBOL_ON = new RepeatSymbol(Color.DARK_GRAY, Color.CYAN);


    // Colors

    public static final Color RED = new Color(255, 107, 107);
    public static final Color YELLOW = new Color(255, 212, 70);
    public static final Color GREEN = new Color(109, 232, 100);
    public static final Color BLUE = new Color(99, 185, 255);


    // Alarm Sound Options

    public static final String[] ALARM_OPTIONS;

    static {
        File soundFolder = new File("sounds");
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".mp3") || name.endsWith(".wav") || name.endsWith(".ogg");
            }
        };
        ALARM_OPTIONS = soundFolder.list(filter);
    }

    // TimerModule Component Names

    public static final String ROW_NUMBER = "Row Number";

}
