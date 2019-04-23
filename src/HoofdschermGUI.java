import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HoofdschermGUI extends JFrame implements ActionListener {

    private static int schermBreedte = 900, schermHoogte = 800;
    private int breedteMidden = schermBreedte / 2;
    private int hoogteMidden = schermHoogte /15;

    public static boolean open = true;

    JButton orderInladenJB;
    JButton robotStatusJB;
    JButton alleProductenJB;
    JButton startRobotJB;
    JButton stopRobotJB;

    HMIStatusGUI hmiStatusGUI;

    public HoofdschermGUI() {

        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

        add(Box.createRigidArea(new Dimension(breedteMidden, 15)));  // lege Box voor de indeling
        add(orderInladenJB);
        add(Box.createRigidArea(new Dimension(breedteMidden, 30)));
        add(robotStatusJB);
        add(Box.createRigidArea(new Dimension(breedteMidden, 30)));
        add(alleProductenJB);
        add(Box.createRigidArea(new Dimension(breedteMidden, 30)));
        add(startRobotJB);
        add(Box.createRigidArea(new Dimension(breedteMidden, 30)));
        add(stopRobotJB);

        setVisible(open);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == robotStatusJB) {
            this.setVisible(false);
            hmiStatusGUI = new HMIStatusGUI();
            hmiStatusGUI.setVisible(true);
        }
//kreng
    }


    public static int getSchermBreedte() {
        return schermBreedte;
    }
    public static int getSchermHoogte() {
        return schermHoogte;
    }

    public static void setOpen(boolean f) {
        open = f;
    }
}
