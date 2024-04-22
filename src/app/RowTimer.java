package app;

import javazoom.jl.converter.Converter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static app.GlobalVariables.*;
import static app.GlobalConstants.*;

/**
 * @author <a href="https://steamcommunity.com/id/KitsuneAya/">Ayaya</a>
 * @apiNote A JPanel which has a timer and java swing components for controlling it.
 */
public class RowTimer extends JPanel implements ActionListener {

    // Timer variable
    private int initialTime, remainingTime;
    private Timer timer;
    private Audio timerCompletedSound;


    // Component variables
    private JLabel rowNumberLabel;
    private JTextField timerNameField;
    private JButton multiActionButton;
    private JButton stopButton;
    private JComboBox<String> timerTypeComboBox;
    private JComboBox<String> alarmOptions;
    private JPanel clockPanel;
    private CardLayout clockCardLayout;
    private JSpinner simpleSpinner;
    private JSpinner relativeSpinner;
    private JLabel timeRemaining;
    private JToggleButton repeatTimerToggle;


    public RowTimer() {

        // Get the next available row number
        rowCount++;

        // Specify that the TimerRow will use a GridBagLayout
        this.setLayout(new GridBagLayout());

        // Pad out the row above and below its components
        int leftPadding = 0;
        int rightPadding = 0;
        int topPadding = 6;
        int bottomPadding = 6;

        this.setBorder(new EmptyBorder(topPadding, leftPadding, bottomPadding, rightPadding));

        // Color the row depending on if it's an even or odd row number
        if (rowCount % 2 == 0) { this.setBackground(Color.LIGHT_GRAY); }        // Even row color
        else { this.setBackground(Color.DARK_GRAY); }                           // Odd row color

        // A variable that will add components from left to right
        // in a first-come first-served manner
        int xPos = 0;

        // Instantiate a constraints object for adding the components to the TimerRow
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        // Row number - Label to show the vertical position of the row
        this.rowNumberLabel = RowComponents.getRowNumberLabel();

        constraints.gridx = xPos++;
        constraints.weightx = 0;
        this.add(this.rowNumberLabel, constraints);

        // Timer Name - Optional name chosen by the user for the Timer
        this.timerNameField = RowComponents.getTimerNameField();

        constraints.gridx = xPos++;
        constraints.weightx = 1;
        this.add(this.timerNameField, constraints);

        // Multi-action Button - Starts/Pauses/Resumes the Timer
        this.multiActionButton = RowComponents.getMultiActionButton();
        this.multiActionButton.addActionListener(this);

        constraints.gridx = xPos++;
        constraints.weightx = 0;
        this.add(this.multiActionButton, constraints);

        // Stop button - Stops and resets the Timer
        this.stopButton = RowComponents.getStopButton();
        this.stopButton.addActionListener(this);

        constraints.gridx = xPos++;
        constraints.weightx = 0;
        this.add(this.stopButton, constraints);

        // Timer type dropdown list - Changes how the Timer works
        this.timerTypeComboBox = RowComponents.getTimerTypeComboBox();
        this.timerTypeComboBox.addActionListener(this);
        this.timerTypeComboBox.setActionCommand("Timer Type");

        constraints.gridx = xPos++;
        constraints.weightx = 0;
        this.add(this.timerTypeComboBox, constraints);

        // Alarm sound dropdown list - Determines what sound plays when the timer completes
        this.alarmOptions = RowComponents.getAlarmOptions();
        this.alarmOptions.addActionListener(this);
        this.alarmOptions.setActionCommand("Alarm Sound");

        constraints.gridx = xPos++;
        constraints.weightx = 0;
        this.add(this.alarmOptions, constraints);

        // Clock panel - Changes time shown depending on Timer type and state
        this.clockPanel = RowComponents.getClockPanel();
        this.clockCardLayout = (CardLayout) this.clockPanel.getLayout();

        constraints.gridx = xPos++;
        constraints.weightx = 0;
        this.add(this.clockPanel, constraints);

        // Simple countdown input
        this.simpleSpinner = RowComponents.getSimpleDateSpinner();
        this.simpleSpinner.setName("Simple");
        this.clockPanel.add(this.simpleSpinner, OPTION_1);

        // Minutes past the hour input
        this.relativeSpinner = RowComponents.getRelativeDateSpinner();
        this.relativeSpinner.setName("Relative");
        this.clockPanel.add(this.relativeSpinner, OPTION_2);

        // Time remaining card
        this.timeRemaining = RowComponents.getTimeRemainingLabel();
        this.clockPanel.add(this.timeRemaining, COUNTDOWN);

        // Timer Repeat Option Toggle
        this.initializeTimerRepeatToggle(xPos++);

    }

    private void initializeTimerRepeatToggle(int xPos) {

        this.repeatTimerToggle = new JToggleButton();

        this.repeatTimerToggle.setIcon(REPEAT_SYMBOL_OFF);
        this.repeatTimerToggle.setSelectedIcon(REPEAT_SYMBOL_ON);

        this.repeatTimerToggle.setPreferredSize(new Dimension(TIMER_REPEAT_TOGGLE_WIDTH, ROW_HEIGHT));

        // Instantiate a constraints object for adding the component to the TimerRow
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = xPos;
        constraints.weightx = 0;
        this.add(this.repeatTimerToggle, constraints);
    }

    private void getInitialTime() {

        String selection = (String) this.timerTypeComboBox.getSelectedItem();

        this.initialTime = 0;

        assert selection != null;
        if (selection.equals(OPTION_1)) {

            Calendar endTime = Calendar.getInstance();
            endTime.setTime((Date) simpleSpinner.getValue());

            this.initialTime += UnitConverter.hoursToMilliseconds(endTime.get(Calendar.HOUR));
            this.initialTime += UnitConverter.minutesToMilliseconds(endTime.get(Calendar.MINUTE));
            this.initialTime += UnitConverter.secondsToMilliseconds(endTime.get(Calendar.SECOND));

        } else if (this.timerTypeComboBox.getSelectedItem().equals(OPTION_2)) {

            // Start time is the current time in ms past the hour
            Calendar calendar = Calendar.getInstance();
            int startTime = calendar.get(Calendar.MILLISECOND);
            startTime += UnitConverter.secondsToMilliseconds(calendar.get(Calendar.SECOND));
            startTime += UnitConverter.minutesToMilliseconds(calendar.get(Calendar.MINUTE));

            // End time is the time after the hour in ms that the timer should finish at
            calendar.setTime((Date) relativeSpinner.getValue());
            int endTime = calendar.get(Calendar.MILLISECOND);
            endTime += UnitConverter.secondsToMilliseconds(calendar.get(Calendar.SECOND));
            endTime += UnitConverter.minutesToMilliseconds(calendar.get(Calendar.MINUTE));

            // A negative time difference indicates the time now is past current hour's end time
            int timeDifference = endTime - startTime;

            // If end time is after start time by at least 5 seconds
            if (timeDifference > 5000) {
                // Time to Alert = Targeted time past the hour - time which has already past
                this.initialTime = endTime - startTime;
            }
            else { // If start time is after end time...
                // Time to Alert = Time remaining on the current hour + targeted time past the next hour
                this.initialTime = (UnitConverter.hoursToMilliseconds(1) - startTime) + endTime;
            }
        }

        this.remainingTime = this.initialTime;                                  // Assign the initial time to the remaining time

        System.out.println("Timer countdown initialized to: " + this.initialTime);

    }

    /**
     * This method updates the time remaining int class variable, the time remaining JLabel,
     * and stops the timer if the time remaining has reached 0.
     */
    private void updateTimeRemaining() {

        int millisecondsPerHour = 3600000;
        int millisecondsPerMinute = 60000;
        int millisecondsPerSecond = 1000;

        int hours = this.remainingTime / millisecondsPerHour;
        int minutes = this.remainingTime % millisecondsPerHour / millisecondsPerMinute;
        int seconds = this.remainingTime % millisecondsPerMinute / millisecondsPerSecond;

        String timeText = "";

        if (this.initialTime / millisecondsPerHour != 0) {
            timeText = String.format("%02d", hours) + ':';
        }
        timeText += String.format("%02d", minutes) + ':';
        timeText += String.format("%02d", seconds);

        this.timeRemaining.setText(timeText);

        // Take action if there is no remaining time
        if (hours <= 0 && minutes <= 0 && seconds <= 0) {

            // Play the selected alarm sound
            System.out.println("Check 1");
            this.timer.cancel();
            this.timerCompletedSound.play();

            // Do different things depending on if the timer repeat button is selected
            if (this.repeatTimerToggle.isSelected()) {

                System.out.println("Repeat is ON. Looping...");

                this.getInitialTime();
                this.setAlertSound();

                boolean isDaemon = false;                                                   // Make the timer thread non-daemon so it closes on exit
                this.timer = new Timer(isDaemon);                                           // Set up the timer with this as the action listener
                this.timer.scheduleAtFixedRate(getTimerTask(), 0, 1000);     // Start the timer with one second parameters

                System.out.println("Check 2");

            }
            else { // If timer repeat is off

                System.out.println("Repeat is OFF. Stopping...");

                this.multiActionButton.setEnabled(false);               // Disable the multi-action button until "Reset" has been pressed to prevent hearing noises from another dimension
                this.multiActionButton.setText("Start");                // Revert the multi-action button back to Start
                this.multiActionButton.setBackground(GREEN.darker());   // Set "Start" to have a green background

                this.stopButton.setText("Reset");                       // Set the stop button to "Reset"
            }
        }
    }

    private TimerTask getTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                remainingTime -= 1000;
                updateTimeRemaining();
            }
        };
    }

    private void setAlertSound() {
        System.out.println("Setting alert sound...");
        String soundFileName = (String) this.alarmOptions.getSelectedItem();
        this.timerCompletedSound = new Audio("sounds/" + soundFileName);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        String selectedTimerType = (String) this.timerTypeComboBox.getSelectedItem();

        // ActionEvents for the JComboBoxes
        if (e.getSource() instanceof JComboBox) {

            switch (e.getActionCommand()) {
                case "Timer Type":
                    // Changes what the card panel is displaying for the row
                    this.clockCardLayout.show(this.clockPanel, selectedTimerType);
                    break;

                case "Alarm Sound":

                    if (this.timerCompletedSound != null) {
                        this.timerCompletedSound.stop();
                    }

                    this.setAlertSound();
                    this.timerCompletedSound.play();
                    break;
            }

        }

        // ActionEvents for JButtons
        if (e.getSource() instanceof JButton) {

            // What code is run depends on the display text of the JButton
            switch (((JButton) e.getSource()).getText()) {

                case "Start":

                    this.getInitialTime();  // Get the time from the spinner

                    // Prevent the timer from starting if no time has been set.
                    if (this.remainingTime == 0) {
                        break;
                    }
                    updateTimeRemaining();  // Update the JLabel with the countdown time

                    // Start method continues below in the "Resume" method

                case "Resume":

                    // If resuming a relative timer, recalculate the time remaining.
                    if (this.timerTypeComboBox.getSelectedItem().equals(OPTION_2)) {
                        this.getInitialTime();  // Get the time from the spinner
                        updateTimeRemaining();  // Update the JLabel with the countdown time
                    }

                    this.setAlertSound();                                   // Get and set the currently selected alert sound

                    boolean isDaemon = false;                               // Make the timer thread non-daemon so it closes on exit
                    this.timer = new Timer(isDaemon);                       // Set up the timer with this as the action listener
                    this.timer.scheduleAtFixedRate(this.getTimerTask(), 0, 1000);

                    this.multiActionButton.setText("Pause");                // Set the start button's next option as "Pause"
                    this.multiActionButton.setBackground(YELLOW);           // Set "Pause" to have a yellow background
                    this.timerTypeComboBox.setEnabled(false);               // Disable the ComboBox so the user can't change it while the timer is running
                    this.clockCardLayout.show(this.clockPanel, COUNTDOWN);  // Replace the Spinner with a Label showing the timer's remaining time

                    break;

                case "Pause":

                    this.timer.cancel();

                    this.multiActionButton.setText("Resume");               // Set the start button's next option as "Resume"
                    this.multiActionButton.setBackground(BLUE);             // Set "Resume" to have a blue background

                    break;

                case "Reset":

                    this.stopButton.setText("Stop");                        // Revert the stop button back to "Stop"
                    this.timerCompletedSound.stop();                        // Stops the alarm if still going

                    // Reset method continues below in the "Stop" method

                case "Stop":

                    this.timer.cancel();                                    // Cancel the countdown

                    this.multiActionButton.setText("Start");                // Revert the multi-action button back to "Start"
                    this.multiActionButton.setBackground(GREEN);            // Set "Start" to have a green background

                    // Display the associated spinner for the selected timer type in the ComboBox
                    this.clockCardLayout.show(this.clockPanel, selectedTimerType);

                    this.timerTypeComboBox.setEnabled(true);                // Re-enable the ComboBox
                    this.multiActionButton.setEnabled(true);                // Re-enable the Multi-action Button

                    break;

            }
        }
    }
}