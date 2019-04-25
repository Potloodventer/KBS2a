import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HoofdschermGUI extends JFrame implements ActionListener {

    private static int schermBreedte = 1100, schermHoogte = 800;

    JButton orderInladenJB;
    JButton robotStatusJB;
    JButton alleProductenJB;
    JButton startRobotJB;
    JButton stopRobotJB;

    HMIStatusGUI hmiStatusGUI;
    VoorraadGUI voorraadGUI;
    OrderInladenGUI orderInladenGUI;

    public HoofdschermGUI() {

        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(schermBreedte /2, schermHoogte /2);
        setTitle("HMI Hoofdscherm");

        orderInladenJB = new JButton("Order inladen");
        robotStatusJB = new JButton("Status robots");
        alleProductenJB = new JButton("Pas voorraad aan");
        startRobotJB = new JButton("Start robots");
        stopRobotJB = new JButton("Stop robots");

        orderInladenJB.setPreferredSize(new Dimension(160, 30));
        robotStatusJB.setPreferredSize(new Dimension(160, 30));
        alleProductenJB.setPreferredSize(new Dimension(160, 30));
        startRobotJB.setPreferredSize(new Dimension(160, 30));
        stopRobotJB.setPreferredSize(new Dimension(160, 30));

        orderInladenJB.addActionListener(this);
        robotStatusJB.addActionListener(this);
        alleProductenJB.addActionListener(this);
        startRobotJB.addActionListener(this);
        stopRobotJB.addActionListener(this);

        add(Box.createRigidArea(new Dimension(schermBreedte / 2, 35)));  // lege Box voor de indeling
        add(orderInladenJB);
        add(Box.createRigidArea(new Dimension(schermBreedte / 2, 20)));
        add(robotStatusJB);
        add(Box.createRigidArea(new Dimension(schermBreedte / 2, 20)));
        add(alleProductenJB);
        add(Box.createRigidArea(new Dimension(schermBreedte / 2, 20)));
        add(startRobotJB);
        add(Box.createRigidArea(new Dimension(schermBreedte / 2, 20)));
        add(stopRobotJB);

        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == orderInladenJB) {
            // laad de order?
            this.setVisible(false);
            orderInladenGUI = new OrderInladenGUI();
            orderInladenGUI.setVisible(true);

        }

            // laat de visuele weergave van de robot zien
            // sluit het homescherm
        if (e.getSource() == robotStatusJB) {
            this.setVisible(false);
            hmiStatusGUI = new HMIStatusGUI();
            hmiStatusGUI.setVisible(true);
        }

            // Laat pagina met alle producten zien
        if (e.getSource() == alleProductenJB) {
            this.setVisible(false);
            voorraadGUI = new VoorraadGUI();
            voorraadGUI.setVisible(true);
        }

            // Start de robots
        if (e.getSource() == startRobotJB) {
            // start de robots
        }

            // Stop de robots
        if (e.getSource() == stopRobotJB) {
            // stop beide robots
        }
    }


    public static int getSchermBreedte() {
        return schermBreedte;
    }
    public static int getSchermHoogte() {
        return schermHoogte;
    }
}
