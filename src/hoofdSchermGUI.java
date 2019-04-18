import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class hoofdSchermGUI extends JFrame implements ActionListener {

    JButton orderInladenJB;
    JButton robotStatusJB;
    JButton alleProductenJB;
    JButton startRobotJB;
    JButton stopRobotJB;

    public hoofdSchermGUI()
    {
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
        setTitle("HMI Hoofdscherm");

        orderInladenJB = new JButton("Order inladen");
        robotStatusJB = new JButton("Visuele weergave");
        alleProductenJB = new JButton("Pas voorraad aan");
        startRobotJB = new JButton("Start robots");
        stopRobotJB = new JButton("Stop robots");

        orderInladenJB.setPreferredSize(new Dimension(160, 30));
        robotStatusJB.setPreferredSize(new Dimension(160, 30));
        alleProductenJB.setPreferredSize(new Dimension(160, 30));
        startRobotJB.setPreferredSize(new Dimension(160, 30));
        stopRobotJB.setPreferredSize(new Dimension(160, 30));

        add(Box.createRigidArea(new Dimension(200, 3)));
        add(orderInladenJB);
        add(Box.createRigidArea(new Dimension(200, 3)));
        add(robotStatusJB);
        add(Box.createRigidArea(new Dimension(200, 3)));
        add(alleProductenJB);
        add(Box.createRigidArea(new Dimension(200, 3)));
        add(startRobotJB);
        add(Box.createRigidArea(new Dimension(200, 3)));
        add(stopRobotJB);
        add(Box.createRigidArea(new Dimension(200, 3)));

        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
