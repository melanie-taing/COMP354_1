
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainView
{
	// View uses Swing framework to display UI to user

	public MainView(String title, IViewGUI accountView, IViewGUI transactionView)
    {
        // Create main frame container
        JFrame mainFrame = new JFrame(title);
        mainFrame.setSize(1050, 480);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation((JFrame.DISPOSE_ON_CLOSE));

        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Attach account panel
        JPanel accountPanel = accountView.getPanel();
        accountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(accountPanel);

        // Attach transaction panel
        JPanel transactionPanel = transactionView.getPanel();
        transactionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(transactionPanel);

        // Attack main panel to frame
        mainFrame.add(mainPanel);

        mainFrame.validate();
        mainFrame.repaint();
	}
}
