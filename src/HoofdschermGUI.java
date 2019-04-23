import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HoofdschermGUI extends JFrame implements ActionListener {

    private int schermBreedte = 1000, schermHoogte = 1000;

    JButton orderInladenJB;
    JButton robotStatusJB;
    JButton alleProductenJB;
    JButton startRobotJB;
    JButton stopRobotJB;

    HMIStatusGUI hmiStatusGUI;

    public HoofdschermGUI() {

        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
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

        add(Box.createRigidArea(new Dimension(200, 3)));
        orderInladenJB.addActionListener(this);
        add(orderInladenJB);

        add(Box.createRigidArea(new Dimension(200, 3)));
        robotStatusJB.addActionListener(this);
        add(robotStatusJB);

        add(Box.createRigidArea(new Dimension(200, 3)));
        alleProductenJB.addActionListener(this);
        add(alleProductenJB);

        add(Box.createRigidArea(new Dimension(200, 3)));
        startRobotJB.addActionListener(this);
        add(startRobotJB);

        add(Box.createRigidArea(new Dimension(200, 3)));
        stopRobotJB.addActionListener(this);
        add(stopRobotJB);

        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == robotStatusJB) {
            hmiStatusGUI = new HMIStatusGUI();
            hmiStatusGUI.setVisible(true);
        }
    }

    public int getSchermBreedte() {
        return schermBreedte;
    }
    public int getSchermHoogte() {
        return schermHoogte;
    }
}
