package app;

import javax.swing.*;
import java.awt.*;

import static app.GlobalVariables.*;

/**
 * @author <a href="https://steamcommunity.com/id/KitsuneAya/">Ayaya</a>
 * @apiNote The main window of the timer app
 */
public class MainWindow extends JFrame {

    private JScrollPane scrollPane;

    public MainWindow() {

        // Set up main window properties
        this.setTitle("Simple Timer");
        this.setMinimumSize(new Dimension(164, 200));
        this.setSize(new Dimension(690, 200));

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);


        // Instantiate the JScrollPane
        this.scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.getContentPane().add(this.scrollPane); // Add the JScrollPane to the MainWindow's ContentPane


        // Create a JPanel to throw into the JScrollPane
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.GRAY);

        this.scrollPane.setViewportView(contentPanel);


        // Initialize constraints
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.weighty = 0;

        // Add the column headers
        scrollPane.setColumnHeaderView(ColumnHeaders.getHeader());

        // Add 10 rows

        for (int i = 0; i < 10; i++) {
            constraints.gridy = rowCount;
            contentPanel.add(new RowTimer(), constraints);
        }
    }
}
