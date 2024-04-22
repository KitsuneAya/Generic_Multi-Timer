package app;

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
    private int time;
    private int minTime = 5000; // Minimum timer duration in milliseconds
    private Timer timer, ticker;
    private Audio alertSound;
    private boolean isTimerRunning = false, isTickerRunning = false;


    // Component variables
    private JLabel rowNumberLabel;
    private JTextField timerNameField;
    private JButton multiActionButton;
    private JButton stopButton;
    private JComboBox<String> timerTypeComboBox;
    private JComboBox<String> alertOptions;
    private JPanel clockPanel;
    private CardLayout clockCardLayout;
    private JSpinner simpleSpinner;
    private JSpinner relativeSpinner;
    private JLabel timeRemaining;
    private JToggleButton loopToggle;


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
        this.alertOptions = RowComponents.getAlarmOptions();
        this.alertOptions.addActionListener(this);
        this.alertOptions.setActionCommand("Alarm Sound");

        constraints.gridx = xPos++;
        constraints.weightx = 0;
        this.add(this.alertOptions, constraints);

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

        this.loopToggle = new JToggleButton();

        this.loopToggle.setIcon(REPEAT_SYMBOL_OFF);
        this.loopToggle.setSelectedIcon(REPEAT_SYMBOL_ON);

        this.loopToggle.setPreferredSize(new Dimension(TIMER_REPEAT_TOGGLE_WIDTH, ROW_HEIGHT));

        // Instantiate a constraints object for adding the component to the TimerRow
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = xPos;
        constraints.weightx = 0;
        this.add(this.loopToggle, constraints);
    }

    private void calculateTime() {

        String selection = (String) this.timerTypeComboBox.getSelectedItem();

        this.time = 0;

        assert selection != null;
        if (selection.equals(OPTION_1)) {

            Calendar endTime = Calendar.getInstance();
            endTime.setTime((Date) this.simpleSpinner.getValue());

            this.time += UnitConverter.hoursToMilliseconds(endTime.get(Calendar.HOUR));
            this.time += UnitConverter.minutesToMilliseconds(endTime.get(Calendar.MINUTE));
            this.time += UnitConverter.secondsToMilliseconds(endTime.get(Calendar.SECOND));

        } else if (this.timerTypeComboBox.getSelectedItem().equals(OPTION_2)) {

            // Start time is the current time in ms past the hour
            Calendar calendar = Calendar.getInstance();
            int startTime = calendar.get(Calendar.MILLISECOND);
            startTime += UnitConverter.secondsToMilliseconds(calendar.get(Calendar.SECOND));
            startTime += UnitConverter.minutesToMilliseconds(calendar.get(Calendar.MINUTE));

            // End time is the time after the hour in ms that the timer should finish at
            calendar.setTime((Date) this.relativeSpinner.getValue());
            int endTime = calendar.get(Calendar.MILLISECOND);
            endTime += UnitConverter.secondsToMilliseconds(calendar.get(Calendar.SECOND));
            endTime += UnitConverter.minutesToMilliseconds(calendar.get(Calendar.MINUTE));

            // A negative time difference indicates the time now is past current hour's end time
            int timeDifference = endTime - startTime;

            // If end time is after start time by at least 5 seconds
            if (timeDifference > 5000) {
                // Time to Alert = Targeted time past the hour - time which has already past
                this.time = endTime - startTime;
            }
            else { // If start time is after end time...
                // Time to Alert = Time remaining on the current hour + targeted time past the next hour
                this.time = (UnitConverter.hoursToMilliseconds(1) - startTime) + endTime;
            }
        }
        System.out.println("Timer countdown initialized to: " + this.time + " ms");
    }

    /**
     * This method updates the time remaining int class variable, the time remaining JLabel,
     * and stops the timer if the time remaining has reached 0.
     */
    private void updateTime() {

        int hours = (int) UnitConverter.millisecondsToHours(this.time);
        int minutes = (int) UnitConverter.millisecondsToMinutes(this.time) - UnitConverter.hoursToMinutes(hours);
        int seconds = (int) UnitConverter.millisecondsToSeconds(this.time) - UnitConverter.hoursToSeconds(hours) - UnitConverter.minutesToSeconds(minutes);

        if (hours >= 0 && minutes >= 0 && seconds >= 0) {

            String timeText = "";

            if (timerTypeComboBox.getSelectedItem().equals(OPTION_1)) {
                timeText = String.format("%02d", hours) + ':';
            }
            timeText += String.format("%02d", minutes) + ':';
            timeText += String.format("%02d", seconds);

            this.timeRemaining.setText(timeText);
        }
        else {
            System.out.println("Warning: time is being updated after the timer has finished");
        }
    }

    /**
     * @return The task that needs to be performed at the end of the timer.
     */
    private TimerTask timerTask() {
        return new TimerTask() {
            @Override
            public void run() {

                alertSound.play();                                      // Play the alert sound to indicate the timer is done

                if (!loopToggle.isSelected()) {                         // If the timer has not been set to loop...

                    stopTicker();                                       // Cancel the ticker
                    stopTimer();                                        // Cancel the timer

                    multiActionButton.setEnabled(false);                // Disable the multi-action button until "Reset" has been pressed to prevent hearing noises from another dimension
                    multiActionButton.setText("Start");                 // Revert the multi-action button back to Start
                    multiActionButton.setBackground(GREEN.darker());    // Set "Start" to have a green background

                    stopButton.setText("Reset");                        // Set the stop button to "Reset" mode
                }
                else {                                                  // If the timer is set to loop...
                    setAlertSound();                                    // Reset alert sound
                    startTimer();                                       // Restart the timer
                }
            }
        };
    }

    /**
     *
     * @return The task that needs to be carried out every second while the timer is running.
     */
    private TimerTask tickerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                time -= 1000;
                updateTime();
            }
        };
    }

    private void startTimer() {
        this.calculateTime();                                                   // Calculate the timer's lifespan
        if (this.time < minTime) {                                              // If the time's lifespan is below the minimum...
            throw new IllegalArgumentException("Timer is too short!");          // Throw an exception
        }
        if (!isTimerRunning) {
            boolean isDaemon = false;                                           // Set timer to be a background thread
            this.timer = new Timer(isDaemon);                                   // Start a new timer
            this.isTimerRunning = true;                                         // Indicate timer is running
        }
        if (!isTickerRunning) {                                                 // If the ticker is no longer running...
            startTicker();                                                      // Schedule a new ticker with its task
        } else {
            time += 1000;                                                       // Correct ticker by one second if continuing to run
        }
        this.timer.schedule(this.timerTask(), time);                            // Schedule a new timer with its task
    }

    private void startTicker() {
        int period = 1000;                                                      // Ticker operates on a per-second basis
        int delay = time % period + period;                                     // Delay to line up ticker with end time
        System.out.println("Ticker start delay of " + delay + " ms");           // Console print statement about delay time
        boolean isDaemon = false;                                               // Set ticker to be a background thread
        this.ticker = new Timer(isDaemon);                                      // Start a new timer for ticker
        this.ticker.scheduleAtFixedRate(this.tickerTask(), delay, period);      // Schedule the ticker's task
        this.isTickerRunning = true;                                            // Indicate ticker is running
    }

    private void stopTimer() {
        this.timer.cancel();
        this.isTimerRunning = false;
    }

    private void stopTicker() {
        this.ticker.cancel();
        this.isTickerRunning = false;
    }

    private void setAlertSound() {
        System.out.println("Setting alert sound...");
        String soundFileName = (String) this.alertOptions.getSelectedItem();
        this.alertSound = new Audio("sounds/" + soundFileName);
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

                    if (this.alertSound != null) {
                        this.alertSound.stop();
                    }

                    this.setAlertSound();
                    this.alertSound.play();
                    break;
            }

        }

        // ActionEvents for JButtons
        if (e.getSource() instanceof JButton) {

            // What code is run depends on the display text of the JButton
            switch (((JButton) e.getSource()).getText()) {

                case "Start":
                case "Resume":

                    startTimer();                                           // Start the timer
                    updateTime();                                           // Update the JLabel with the initial countdown time

                    this.setAlertSound();                                   // Get and set the currently selected alert sound

                    this.multiActionButton.setText("Pause");                // Set the start button's next option as "Pause"
                    this.multiActionButton.setBackground(YELLOW);           // Set "Pause" to have a yellow background
                    this.timerTypeComboBox.setEnabled(false);               // Disable the ComboBox so the user can't change it while the timer is running
                    this.clockCardLayout.show(this.clockPanel, COUNTDOWN);  // Replace the Spinner with a Label showing the timer's remaining time

                    break;

                case "Pause":

                    this.stopTicker();                                      // Cancel the ticker
                    this.stopTimer();                                       // Cancel the timer

                    this.multiActionButton.setText("Resume");               // Set the start button's next option as "Resume"
                    this.multiActionButton.setBackground(BLUE);             // Set "Resume" to have a blue background

                    break;

                case "Reset":

                    this.stopButton.setText("Stop");                        // Revert the stop button back to "Stop"
                    this.alertSound.stop();                                 // Stops the alarm if still going

                    // Reset method continues below in the "Stop" method

                case "Stop":

                    this.stopTicker();                                      // Cancel the ticker
                    this.stopTimer();                                       // Cancel the timer

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