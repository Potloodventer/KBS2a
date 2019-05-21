import javax.swing.*;
import java.awt.*;

public class TekenPanelHMIStatus extends JPanel {

    private HMIStatusGUI hmiStatusGUI;
    private int tekenPanelBreedte = HoofdschermGUI.getSchermBreedte() - 200;
    private int tekenPanelHoogte = HoofdschermGUI.getSchermHoogte() - 200;

    public TekenPanelHMIStatus(HMIStatusGUI hmiStatusGUI) {
        this.hmiStatusGUI = hmiStatusGUI;
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(tekenPanelBreedte, tekenPanelHoogte));
        setBackground(Color.white);
        setVisible(true);
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);

        hmiStatusGUI.drawBlueprint(g);
        hmiStatusGUI.moveArrows(g, hmiStatusGUI.getKleur()); // null wordt: hmiStatusGUI.getKleur();
        hmiStatusGUI.drawRGBSensor(g, hmiStatusGUI.getKleur());
        hmiStatusGUI.drawTelSensor(g, hmiStatusGUI.getTelSensorKleur());
        hmiStatusGUI.drawServoArm(g, hmiStatusGUI.getNummer1(), hmiStatusGUI.getNummer2());

    }



}
