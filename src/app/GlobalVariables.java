package app;

import javax.swing.*;
import java.util.ArrayList;

/**
 * @author <a href="https://steamcommunity.com/id/KitsuneAya/">Ayaya</a>
 * @apiNote Globally accessible variables for use within many classes.
 */
public final class GlobalVariables {

    // Number of Rows in the Main Window
    public static int rowCount = 0;

    // The upper content panel containing the TimerModules
    public static JPanel upperContentPanel;

    // ArrayList of TimerModules
    public static ArrayList<TimerModule> timerModules = new ArrayList<>();

}
