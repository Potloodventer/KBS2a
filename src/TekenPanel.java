import javax.swing.*;
import java.awt.*;

public class TekenPanel extends JPanel {

    private HMIStatusGUI hmiStatusGUI;

    public TekenPanel(HMIStatusGUI hmiStatusGUI) {
        this.hmiStatusGUI = hmiStatusGUI;

        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(800,300));
        setBackground(Color.DARK_GRAY);

        setVisible(true);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        hmiStatusGUI.TekenBlueprint(g);

    }
}
