package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static app.GlobalVariables.*;

/**
 * @author <a href="https://steamcommunity.com/id/KitsuneAya/">Ayaya</a>
 * @apiNote The main window of the timer app
 */
public class MainWindow extends JFrame {

    private final JScrollPane SCROLL_PANE;

    public MainWindow() {

        // Set up main window properties
        this.setTitle("Simple Timer");
        this.setMinimumSize(new Dimension(164, 200));
        this.setSize(new Dimension(690, 200));

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);



        // Instantiate the JScrollPane
        SCROLL_PANE = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.getContentPane().add(SCROLL_PANE); // Add the JScrollPane to the MainWindow's ContentPane

        // Set the scroll pane's column header
        SCROLL_PANE.setColumnHeaderView(new ContentHeader());



        // Initialize constraints
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.weighty = 0;



        // Create a JPanel to hold content as the JScrollPane's client
        JPanel contentPanel;
        contentPanel = new JPanel(new GridBagLayout());
        SCROLL_PANE.setViewportView(contentPanel);



        // Create a JPanel to act as an upper section within contentPanel
        GlobalVariables.upperContentPanel = new JPanel(new GridBagLayout());

        constraints.gridy = 0;
        contentPanel.add(GlobalVariables.upperContentPanel, constraints);



        // Create a JPanel to act as a lower section within contentPanel
        JPanel lowerContentPanel = new JPanel(new GridBagLayout());

        constraints.gridy = 1;
        contentPanel.add(lowerContentPanel, constraints);



        // Add 3 timers
        for (int i = 0; i < 3; i++) {
            this.addTimer();
        }


        // Add new row button
        JButton addTimerModuleButton = new JButton("⌄ ⌄ ⌄  Add Another Timer  ⌄ ⌄ ⌄");
        addTimerModuleButton.setBackground(Color.DARK_GRAY.darker());
        addTimerModuleButton.setForeground(Color.WHITE);
        addTimerModuleButton.addActionListener(e -> this.addTimer());
        lowerContentPanel.add(addTimerModuleButton, constraints);
    }

    private void addTimer() {

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

        constraints.gridy = rowCount;
        upperContentPanel.add(new TimerModule(), constraints);

        SCROLL_PANE.revalidate();
    }
}
