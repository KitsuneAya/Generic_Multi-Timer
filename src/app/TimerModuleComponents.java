package app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

import static app.GlobalConstants.*;
import static app.GlobalVariables.rowCount;

/**
 * @author <a href="https://steamcommunity.com/id/KitsuneAya/">Ayaya</a>
 * @apiNote Methods for instantiating new components for timer rows.
 */
public final class TimerModuleComponents {

    /**
     * A method for instantiating a new JLabel of the timer row's numerical position.
     * Uses the rowCount variable from GlobalVariables to get the next row number
     * to be added.
     *
     * @return A JLabel of the next new row's number.
     */
    public static JLabel getRowNumberLabel() {
        JLabel rowNumberLabel = new JLabel(String.valueOf(rowCount));                       // Label with row number
        rowNumberLabel.setPreferredSize(new Dimension(ROW_NUMBER_WIDTH, ROW_HEIGHT));       // Set the size of the label
        rowNumberLabel.setHorizontalAlignment(JLabel.HORIZONTAL);                           // Center align the label's text

        rowNumberLabel.setToolTipText("Double click to delete this timer");                 // Set the tool tip text

        if (rowCount % 2 == 1) {                                                            // Change the font color depending
            rowNumberLabel.setForeground(Color.WHITE);                                      // on if the row # is even or odd
        }

        return rowNumberLabel;
    }

    /**
     * A method for instantiating a new JTextField that can be used to name the
     * row timer. Initializes with a default name of "Timer Name (Row Number)".
     * Currently isn't used in any methods and is just for the user to make note of
     * what the timer is for.
     *
     * @return A JTextField with a default timer name that can be changed by the user.
     */
    public static JTextField getTimerNameField() {
        // A text field that the user can use to name a timer
        // Doesn't do anything inside the code

        JTextField timerNameField = new JTextField("Timer Name " + rowCount);
        timerNameField.setPreferredSize(new Dimension(TIMER_NAME_WIDTH, ROW_HEIGHT));

        return timerNameField;
    }

    /**
     * A method for instantiating a new JButton for multifunction use. Currently for use as
     * a "Start", "Pause", and "Resume" button. Initially set to be a "Start" button.
     *
     * @return A JButton to "Start", "Pause", or "Resume" the timer.
     */
    public static JButton getMultiActionButton() {
        JButton multiActionButton = new JButton("Start");
        multiActionButton.setBackground(GREEN);
        multiActionButton.setMargin(new Insets(0, 0, 0, 0));
        multiActionButton.setBorder(null);

        multiActionButton.setPreferredSize(new Dimension(START_PAUSE_BUTTON_WIDTH, ROW_HEIGHT));

        return multiActionButton;
    }

    /**
     * A method for instantiating a new JButton for stopping a timer or resetting it
     * should the timer complete and not be on loop.
     *
     * @return A JButton to either "Stop" or "Reset" the timer.
     */
    public static JButton getStopButton() {
        JButton stopButton = new JButton("Stop");
        stopButton.setBackground(RED);
        stopButton.setMargin(new Insets(0, 0, 0, 0));
        stopButton.setBorder(null);

        stopButton.setPreferredSize(new Dimension(STOP_BUTTON_WIDTH, ROW_HEIGHT));

        return stopButton;
    }

    /**
     * A method for instantiating a new JComboBox for selecting the type of timer
     * the row will use. The options available in this drop down menu are determined by
     * what's in the TIMER_TYPE_OPTIONS String array in GlobalConstants.
     *
     * @return A JComboBox with all available timer types that can be selected.
     */
    public static JComboBox<String> getTimerOptions() {
        JComboBox<String> timerTypeComboBox = new JComboBox<>(TIMER_TYPE_OPTIONS);
        timerTypeComboBox.setBorder(null);

        timerTypeComboBox.setPreferredSize(new Dimension(TIMER_TYPE_SELECTOR_WIDTH, ROW_HEIGHT));

        return timerTypeComboBox;
    }

    /**
     * @return A JComboBox with all known available alert sound options.
     */
    public static JComboBox<String> getAlertOptions() {
        JComboBox<String> alarmOptions = new JComboBox<>(ALARM_OPTIONS);
        alarmOptions.setBorder(null);

        alarmOptions.setPreferredSize(new Dimension(ALARM_OPTIONS_WIDTH, ROW_HEIGHT));

        return alarmOptions;
    }

    /**
     * A method for instantiating a new JPanel with a CardLayout that changes which components
     * are visible depending on the state of the timer. Does not come with components as those components
     * need be designated as class variables within the row timer for accessibility purposes.
     *
     * @return An empty JPanel with a CardLayout that needs components added to it.
     */
    public static JPanel getTimePanel() {
        CardLayout clockCardLayout = new CardLayout();
        JPanel clockPanel = new JPanel(clockCardLayout);

        // Set the preferred size of the clock panel
        clockPanel.setPreferredSize(new Dimension(CARD_PANEL_WIDTH, ROW_HEIGHT));

        return clockPanel;
    }

    /**
     * A method for instantiating a JSpinner that the user can specify a simple countdown
     * timer up to 24 hours, with 1 second being the smallest increment allowed.
     *
     * @return A JSpinner with a "HH:mm:ss" display set up with a date model.
     */
    public static JSpinner getSimpleDateSpinner() {

        return getDateSpinner("HH:mm:ss");
    }

    /**
     * A method for instantiating a JSpinner that the user can specify a time past the top of
     * the hour for the timer to finish at.
     *
     * @return A JSpinner with a "mm:ss" display and set up with a date model.
     */
    public static JSpinner getRelativeDateSpinner() {

        return getDateSpinner("mm:ss");
    }

    /**
     * A method for instantiating a JLabel that is used to display the time remaining
     * on the row timer. It is set up to match the digit positions of the JSpinners it shares
     * the CardLayout with.
     *
     * @return A JLabel used for displaying the time remaining on the timer.
     */
    public static JLabel getTimeRemainingLabel() {
        JLabel timeRemaining = new JLabel("00:00:00");
        timeRemaining.setHorizontalAlignment(JLabel.RIGHT);
        timeRemaining.setBorder(new EmptyBorder(0, 0, 0, 18));
        timeRemaining.setBackground(YELLOW);
        timeRemaining.setOpaque(true);

        return timeRemaining;
    }

    /**
     * A private method for creating a JSpinner with a date model that displays
     * the date in a specified format
     *
     * @param dateFormatPattern A format string defining what parts of the date and time are shown on the JSpinner
     * @return A JSpinner that displays only the components of time determined by the format pattern.
     */
    private static JSpinner getDateSpinner(String dateFormatPattern) {
        SpinnerDateModel dateModel = new SpinnerDateModel(getDateSetToMidnight(), null, null, Calendar.SECOND);
        JSpinner spinner = new JSpinner(dateModel);

        // Sets the spinner to display time in hours:minutes:seconds
        JSpinner.DateEditor simpleSpinnerDateDisplay = new JSpinner.DateEditor(spinner, dateFormatPattern);

        // Sets the spinner to right-aligned display text
        JTextField textField = simpleSpinnerDateDisplay.getTextField();
        textField.setHorizontalAlignment(JTextField.RIGHT);

        // Apply the time format and text alignment to the spinner
        spinner.setEditor(simpleSpinnerDateDisplay);
        return spinner;
    }

    /**
     * A private method that gets a date that will display as midnight
     * no matter the local time.
     *
     * @return A Date object set to the client's local midnight.
     */
    private static Date getDateSetToMidnight() {
        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.HOUR, 0);

        // If the current time is after noon, the displayed hours time will be 12.
        // This if statement corrects that so the hour is always set to display 0.
        if (midnight.getTime().getHours() != 0) {
            midnight.set(Calendar.HOUR, 12);
        }

        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        return midnight.getTime();
    }

    public static JToggleButton getLoopToggle() {

        JToggleButton loopToggle = new JToggleButton();

        loopToggle.setIcon(REPEAT_SYMBOL_OFF);
        loopToggle.setSelectedIcon(REPEAT_SYMBOL_ON);

        loopToggle.setPreferredSize(new Dimension(TIMER_REPEAT_TOGGLE_WIDTH, ROW_HEIGHT));

        return loopToggle;
    }

}
