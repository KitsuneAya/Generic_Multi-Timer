package app;

import javax.swing.*;
import java.awt.*;

import static app.GlobalConstants.*;

/**
 * @author <a href="https://steamcommunity.com/id/KitsuneAya/">Ayaya</a>
 * @apiNote A JPanel containing column labels for use as a header in a JScrollPane
 */
public final class ColumnHeaders {

    public static JPanel getHeader() {

        // A variable that will add components from left to right
        // in a first-come first-served manner
        int xPos = 0;

        // Instantiate the JPanel that everything in the row will be added into
        // Uses a GridBagLayout to control component width sizing
        JPanel headerPanel = new JPanel(new GridBagLayout());

        // Instantiate a constraints object for adding components to the row panel
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;


        // Row Number (Obvious, leave blank header. Also, not enough space)
        JLabel rowNumbersHeader = new JLabel(" ");
        rowNumbersHeader.setPreferredSize(new Dimension(ROW_NUMBER_WIDTH, ROW_HEIGHT));

        constraints.gridx = xPos++;
        constraints.weightx = 0;
        headerPanel.add(rowNumbersHeader, constraints);


        // Timer Names Header
        JLabel timerNamesHeader = new JLabel("Timer Name");
        timerNamesHeader.setPreferredSize(new Dimension(TIMER_NAME_WIDTH, ROW_HEIGHT));
        timerNamesHeader.setHorizontalAlignment(JLabel.CENTER);

        constraints.gridx = xPos++;
        constraints.weightx = 1;
        headerPanel.add(timerNamesHeader, constraints);


        // Controls Header
        JLabel controlsHeader = new JLabel("Controls");
        controlsHeader.setPreferredSize(new Dimension(START_PAUSE_BUTTON_WIDTH + STOP_BUTTON_WIDTH, ROW_HEIGHT));
        controlsHeader.setHorizontalAlignment(JLabel.CENTER);

        constraints.gridx = xPos++;
        constraints.weightx = 0;
        headerPanel.add(controlsHeader, constraints);


        // Timer Type Header
        JLabel timerTypeHeader = new JLabel("Timer Type");
        timerTypeHeader.setPreferredSize(new Dimension(TIMER_TYPE_SELECTOR_WIDTH, ROW_HEIGHT));
        timerTypeHeader.setHorizontalAlignment(JLabel.CENTER);

        constraints.gridx = xPos++;
        constraints.weightx = 0;
        headerPanel.add(timerTypeHeader, constraints);

        // Alarm Sound Header
        JLabel alarmSoundHeader = new JLabel("Alarm Sound");
        alarmSoundHeader.setPreferredSize(new Dimension(ALARM_OPTIONS_WIDTH, ROW_HEIGHT));
        alarmSoundHeader.setHorizontalAlignment(JLabel.CENTER);

        constraints.gridx = xPos++;
        constraints.weightx = 0;
        headerPanel.add(alarmSoundHeader, constraints);

        // Time Header
        JLabel timeHeader = new JLabel("Time");
        timeHeader.setPreferredSize(new Dimension(CARD_PANEL_WIDTH, ROW_HEIGHT));
        timeHeader.setHorizontalAlignment(JLabel.CENTER);

        constraints.gridx = xPos++;
        constraints.weightx = 0;
        headerPanel.add(timeHeader, constraints);


        // Timer Repeat Header
        JLabel repeatHeader = new JLabel(" ");
        repeatHeader.setPreferredSize(new Dimension(TIMER_REPEAT_TOGGLE_WIDTH, ROW_HEIGHT));
        repeatHeader.setHorizontalAlignment(JLabel.CENTER);

        constraints.gridx = xPos++;
        constraints.weightx = 0;
        headerPanel.add(repeatHeader, constraints);

        return headerPanel;
    }

}
