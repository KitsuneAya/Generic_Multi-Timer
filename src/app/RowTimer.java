package app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.Timer;

import static app.GlobalVariables.*;
import static app.GlobalConstants.*;

/**
 * @author <a href="https://steamcommunity.com/id/KitsuneAya/">Ayaya</a>
 * @apiNote A JPanel which has a timer and java swing components for controlling it.
 */
public class RowTimer extends JPanel implements ActionListener {

    // Timer variable
    private int time;
    private Timer timer, ticker;
    private Audio alertSound;
    private boolean isTimerRunning = false, isTickerRunning = false;


    // Component variables
    private final JLabel ROW_NUMBER_LABEL;
    private final JTextField TIMER_NAME_FIELD;
    private final JButton MULTI_ACTION_BUTTON;
    private final JButton STOP_BUTTON;
    private final JComboBox<String> TIMER_OPTIONS;
    private final JComboBox<String> ALERT_OPTIONS;
    private final JPanel TIME_PANEL;
    private final CardLayout TIME_PANEL_LAYOUT;
    private final JSpinner SIMPLE_SPINNER;
    private final JSpinner RELATIVE_SPINNER;
    private final JLabel TIME_REMAINING_LABEL;
    private final JToggleButton LOOP_TOGGLE;


    /**
     * Default constructor that instantiates a new RowTimer object.
     */
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

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Row number - Label to show the vertical position of the row
        this.ROW_NUMBER_LABEL = RowComponents.getRowNumberLabel();

        constraints.gridx = xPos++;
        constraints.weightx = 0;
        this.add(this.ROW_NUMBER_LABEL, constraints);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Timer Name - Optional name chosen by the user for the Timer
        this.TIMER_NAME_FIELD = RowComponents.getTimerNameField();

        constraints.gridx = xPos++;
        constraints.weightx = 1;
        this.add(this.TIMER_NAME_FIELD, constraints);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Multi-action Button - Starts/Pauses/Resumes the Timer
        this.MULTI_ACTION_BUTTON = RowComponents.getMultiActionButton();
        this.MULTI_ACTION_BUTTON.addActionListener(this);

        constraints.gridx = xPos++;
        constraints.weightx = 0;
        this.add(this.MULTI_ACTION_BUTTON, constraints);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Stop button - Stops and resets the Timer
        this.STOP_BUTTON = RowComponents.getStopButton();
        this.STOP_BUTTON.addActionListener(this);

        constraints.gridx = xPos++;
        constraints.weightx = 0;
        this.add(this.STOP_BUTTON, constraints);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Timer type dropdown list - Changes how the Timer works
        this.TIMER_OPTIONS = RowComponents.getTimerOptions();
        this.TIMER_OPTIONS.addActionListener(this);
        this.TIMER_OPTIONS.setActionCommand("Timer Type");

        constraints.gridx = xPos++;
        constraints.weightx = 0;
        this.add(this.TIMER_OPTIONS, constraints);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Alarm sound dropdown list - Determines what sound plays when the timer completes
        this.ALERT_OPTIONS = RowComponents.getAlertOptions();
        this.ALERT_OPTIONS.addActionListener(this);
        this.ALERT_OPTIONS.setActionCommand("Alarm Sound");

        constraints.gridx = xPos++;
        constraints.weightx = 0;
        this.add(this.ALERT_OPTIONS, constraints);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Clock panel - Changes time shown depending on Timer type and state
        this.TIME_PANEL = RowComponents.getTimePanel();
        this.TIME_PANEL_LAYOUT = (CardLayout) this.TIME_PANEL.getLayout();

        constraints.gridx = xPos++;
        constraints.weightx = 0;
        this.add(this.TIME_PANEL, constraints);

        // Simple countdown input
        this.SIMPLE_SPINNER = RowComponents.getSimpleDateSpinner();
        this.SIMPLE_SPINNER.setName("Simple");
        this.TIME_PANEL.add(this.SIMPLE_SPINNER, OPTION_1);

        // Minutes past the hour input
        this.RELATIVE_SPINNER = RowComponents.getRelativeDateSpinner();
        this.RELATIVE_SPINNER.setName("Relative");
        this.TIME_PANEL.add(this.RELATIVE_SPINNER, OPTION_2);

        // Time remaining card
        this.TIME_REMAINING_LABEL = RowComponents.getTimeRemainingLabel();
        this.TIME_PANEL.add(this.TIME_REMAINING_LABEL, COUNTDOWN);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Timer Repeat Option Toggle
        this.LOOP_TOGGLE = RowComponents.getLoopToggle();

        constraints.gridx = xPos;
        constraints.weightx = 0;
        this.add(this.LOOP_TOGGLE, constraints);

    }

    /**
     * This method is used for calculating the time delay to trigger the final timer event.
     * For simple timers, it converts the date the user has entered on the spinner directly into milliseconds.
     * For relative timers, it compares the minutes and seconds on the spinner to the current time past the hour,
     * and then based off that calculates the milliseconds to the next user defined time past the hour.
     */
    private void calculateTime() {

        String selection = (String) this.TIMER_OPTIONS.getSelectedItem();

        this.time = 0;

        assert selection != null;
        if (selection.equals(OPTION_1)) {

            Calendar endTime = Calendar.getInstance();
            endTime.setTime((Date) this.SIMPLE_SPINNER.getValue());

            this.time += UnitConverter.hoursToMilliseconds(endTime.get(Calendar.HOUR));
            this.time += UnitConverter.minutesToMilliseconds(endTime.get(Calendar.MINUTE));
            this.time += UnitConverter.secondsToMilliseconds(endTime.get(Calendar.SECOND));

        } else if (Objects.equals(this.TIMER_OPTIONS.getSelectedItem(), OPTION_2)) {

            // Start time is the current time in ms past the hour
            Calendar calendar = Calendar.getInstance();
            int startTime = calendar.get(Calendar.MILLISECOND);
            startTime += UnitConverter.secondsToMilliseconds(calendar.get(Calendar.SECOND));
            startTime += UnitConverter.minutesToMilliseconds(calendar.get(Calendar.MINUTE));

            // End time is the time after the hour in ms that the timer should finish at
            calendar.setTime((Date) this.RELATIVE_SPINNER.getValue());
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
     * This method updates the time class variable and its associated JLabel. Will also
     * print a warning in the terminal if this method is being called after the timer should
     * have stopped in the case the ticker has continued to function.
     */
    private void updateTime() {

        int hours = (int) UnitConverter.millisecondsToHours(this.time);
        int minutes = (int) UnitConverter.millisecondsToMinutes(this.time) - UnitConverter.hoursToMinutes(hours);
        int seconds = (int) UnitConverter.millisecondsToSeconds(this.time) - UnitConverter.hoursToSeconds(hours) - UnitConverter.minutesToSeconds(minutes);

        if (hours >= 0 && minutes >= 0 && seconds >= 0) {

            String timeText = "";

            if (Objects.equals(TIMER_OPTIONS.getSelectedItem(), OPTION_1)) {
                timeText = String.format("%02d", hours) + ':';
            }
            timeText += String.format("%02d", minutes) + ':';
            timeText += String.format("%02d", seconds);

            this.TIME_REMAINING_LABEL.setText(timeText);
        }
        else {
            System.out.println("Warning: time is being updated after the timer has finished");
        }
    }

    /**
     * This method returns a TimerTask with the instructions to be carried out at the completion of the timer.
     * If the timer has not been set to loop, the ticker and timer are cancelled, and the stop button is set to become a reset button.
     * If the timer is set to loop, the alert sound is reset and the timer restarted.
     *
     * @return The task that needs to be performed at the end of the timer.
     */
    private TimerTask timerTask() {
        return new TimerTask() {
            @Override
            public void run() {

                alertSound.play();                                      // Play the alert sound to indicate the timer is done

                if (!LOOP_TOGGLE.isSelected()) {                         // If the timer has not been set to loop...

                    stopTicker();                                       // Cancel the ticker
                    stopTimer();                                        // Cancel the timer

                    MULTI_ACTION_BUTTON.setEnabled(false);                // Disable the multi-action button until "Reset" has been pressed to prevent hearing noises from another dimension
                    MULTI_ACTION_BUTTON.setText("Start");                 // Revert the multi-action button back to Start
                    MULTI_ACTION_BUTTON.setBackground(GREEN.darker());    // Set "Start" to have a green background

                    STOP_BUTTON.setText("Reset");                        // Set the stop button to "Reset" mode
                }
                else {                                                  // If the timer is set to loop...
                    setAlertSound();                                    // Reset alert sound
                    startTimer();                                       // Restart the timer
                }
            }
        };
    }

    /**
     * This method returns a TimerTask with the instructions to be carried out with each 'tick' of the ticker.
     * Updates time remaining information for the user.
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

    /**
     * This method starts a new timer, as well as starting the ticker if necessary.
     * Also updates the boolean variable which states whether the timer is running.
     */
    private void startTimer() {
        this.calculateTime();                                                   // Calculate the timer's lifespan
        int minTime = 5000;                                                     // Minimum timer duration in milliseconds
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

    /**
     * This method starts a new ticker, and will align itself with system time for relative timers.
     * Its name also has the ability to trick your brain into thinking it says 'kickStarter' when
     * not looked at directly.
     */
    private void startTicker() {
        int period = 1000;                                                      // Ticker operates on a per-second basis
        int delay = time % period + period;                                     // Delay to line up ticker with end time
        System.out.println("Ticker start delay of " + delay + " ms");           // Console print statement about delay time
        boolean isDaemon = false;                                               // Set ticker to be a background thread
        this.ticker = new Timer(isDaemon);                                      // Start a new timer for ticker
        this.ticker.scheduleAtFixedRate(this.tickerTask(), delay, period);      // Schedule the ticker's task
        this.isTickerRunning = true;                                            // Indicate ticker is running
    }

    /**
     * This method stops the timer and updates the associated is running boolean.
     */
    private void stopTimer() {
        this.timer.cancel();
        this.isTimerRunning = false;
    }

    /**
     * This method stops the ticker and updates the associated is running boolean.
     */
    private void stopTicker() {
        this.ticker.cancel();
        this.isTickerRunning = false;
    }

    /**
     * This method sets the Audio object to play the sound that has been selected in the
     * available sounds drown down list.
     */
    private void setAlertSound() {
        System.out.println("Setting alert sound...");
        String soundFileName = (String) this.ALERT_OPTIONS.getSelectedItem();
        this.alertSound = new Audio("sounds/" + soundFileName);
    }

    public void setRowLabelText(int rowNum) {
        this.ROW_NUMBER_LABEL.setText(String.valueOf(rowNum));
    }

    public void setNameFieldText(String text) {
        this.TIMER_NAME_FIELD.setText(text);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        String selectedTimerType = (String) this.TIMER_OPTIONS.getSelectedItem();

        // ActionEvents for the JComboBoxes
        if (e.getSource() instanceof JComboBox) {

            switch (e.getActionCommand()) {
                case "Timer Type":
                    // Changes what the card panel is displaying for the row
                    this.TIME_PANEL_LAYOUT.show(this.TIME_PANEL, selectedTimerType);
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

                    this.MULTI_ACTION_BUTTON.setText("Pause");                // Set the start button's next option as "Pause"
                    this.MULTI_ACTION_BUTTON.setBackground(YELLOW);           // Set "Pause" to have a yellow background
                    this.TIMER_OPTIONS.setEnabled(false);               // Disable the ComboBox so the user can't change it while the timer is running
                    this.TIME_PANEL_LAYOUT.show(this.TIME_PANEL, COUNTDOWN);  // Replace the Spinner with a Label showing the timer's remaining time

                    break;

                case "Pause":

                    this.stopTicker();                                      // Cancel the ticker
                    this.stopTimer();                                       // Cancel the timer

                    this.MULTI_ACTION_BUTTON.setText("Resume");               // Set the start button's next option as "Resume"
                    this.MULTI_ACTION_BUTTON.setBackground(BLUE);             // Set "Resume" to have a blue background

                    break;

                case "Reset":

                    this.STOP_BUTTON.setText("Stop");                        // Revert the stop button back to "Stop"
                    this.alertSound.stop();                                 // Stops the alarm if still going

                    // Reset method continues below in the "Stop" method

                case "Stop":

                    this.stopTicker();                                      // Cancel the ticker
                    this.stopTimer();                                       // Cancel the timer

                    this.MULTI_ACTION_BUTTON.setText("Start");                // Revert the multi-action button back to "Start"
                    this.MULTI_ACTION_BUTTON.setBackground(GREEN);            // Set "Start" to have a green background

                    // Display the associated spinner for the selected timer type in the ComboBox
                    this.TIME_PANEL_LAYOUT.show(this.TIME_PANEL, selectedTimerType);

                    this.TIMER_OPTIONS.setEnabled(true);                // Re-enable the ComboBox
                    this.MULTI_ACTION_BUTTON.setEnabled(true);                // Re-enable the Multi-action Button

                    break;
            }
        }
    }
}