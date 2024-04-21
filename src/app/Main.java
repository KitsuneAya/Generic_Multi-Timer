package app;

import static app.GlobalConstants.*;

public class Main {

    public static void main(String[] args) {

        // Sum of component widths
        System.out.println(
            ROW_NUMBER_WIDTH +
                    TIMER_NAME_WIDTH +
                    START_PAUSE_BUTTON_WIDTH +
                    STOP_BUTTON_WIDTH +
                    TIMER_TYPE_SELECTOR_WIDTH +
                    CARD_PANEL_WIDTH
        );

        new MainWindow().setVisible(true);

    }
}