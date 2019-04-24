import javax.swing.*;
import java.awt.*;

public class TekenPanel extends JPanel {

    private HMIStatusGUI hmiStatusGUI;
    private int tekenPanelBreedte = HoofdschermGUI.getSchermBreedte() - 200;
    private int tekenPanelHoogte = HoofdschermGUI.getSchermBreedte() - 200;

    public TekenPanel(HMIStatusGUI hmiStatusGUI) {
        this.hmiStatusGUI = hmiStatusGUI;

        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(tekenPanelBreedte,tekenPanelHoogte));
        setBackground(Color.DARK_GRAY);

        setVisible(true);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        hmiStatusGUI.beweegPijlen(g);

        int[] xpoints = new int[2];
        int[] ypoints = new int[2];

        xpoints[0] = 50;
        xpoints[1] = 100;
        xpoints[2] = 75;

        ypoints[0] = 200;
        ypoints[1] = 250;
        ypoints[2] = 300;

        g.setColor(Color.cyan);
        //g.fillRect(50, 200, 50, 50);
        g.fillPolygon(xpoints, ypoints, 5);
    }


}
