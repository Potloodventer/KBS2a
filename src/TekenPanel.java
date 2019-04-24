import javax.swing.*;
import java.awt.*;

public class TekenPanel extends JPanel {

    private HMIStatusGUI hmiStatusGUI;
    private int tekenPanelBreedte = HoofdschermGUI.getSchermBreedte() - 200;
    private int tekenPanelHoogte = HoofdschermGUI.getSchermHoogte() - 200;

    public TekenPanel(HMIStatusGUI hmiStatusGUI) {
        this.hmiStatusGUI = hmiStatusGUI;

        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(tekenPanelBreedte, tekenPanelHoogte));
        setBackground(Color.DARK_GRAY);

        setVisible(true);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        hmiStatusGUI.beweegPijlen(g, null); // null wordt: hmiStatusGUI.getKleur();

    }


}
