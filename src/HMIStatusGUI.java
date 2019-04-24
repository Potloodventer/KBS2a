import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HMIStatusGUI extends JFrame implements ActionListener {

    private int roodAantal = 0, groenAantal = 0, blauwAantal = 0, pijlX = 0;

    private JButton jbHome, jbStart, jbStop, jbResultaat;
    private JLabel jlRood, jlGroen, jlBlauw;

    private TekenPanel tekenPanel;

    private Timer timer;

    public HMIStatusGUI() {

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(HoofdschermGUI.getSchermBreedte(), HoofdschermGUI.getSchermHoogte());
        setTitle("Status robots");

        // timer met delay van 1 seconde
        timer = new Timer(500, this);

        tekenPanel = new TekenPanel(this);

        //homeknop
        jbHome = new JButton("HOME");
        jbHome.addActionListener(this);
        jbHome.setPreferredSize(new Dimension(100, 30));

        //starknop
        jbStart = new JButton("START");
        jbStart.addActionListener(this);
        jbStart.setPreferredSize(new Dimension(100, 30));

        //stopknop
        jbStop = new JButton("STOP");
        jbStop.addActionListener(this);
        jbStop.setPreferredSize(new Dimension(100, 30));

        // voeg dit allemaal toe
        add(Box.createRigidArea(new Dimension(45, 3)));
        add(jbHome);
        add(Box.createRigidArea(new Dimension(30, 3)));
        add(jbStart);
        add(Box.createRigidArea(new Dimension(30, 3)));
        add(jbStop);
        add(Box.createRigidArea(new Dimension(HoofdschermGUI.getSchermBreedte() /2, 20)));
        // tekenpanel
        add(Box.createRigidArea(new Dimension(800, 50)));
        add(tekenPanel);

        setVisible(false);
    }


        // teken en beweeg de pijlen op de lopende band
    public void beweegPijlen(Graphics g){

        g.setColor(Color.GREEN);

        int y = HoofdschermGUI.getSchermHoogte() / 2;

        for (int i = 1; i < 8; i++) {
            int x = (50 * i) + pijlX;

            System.out.println(x * i + " " + y + " " + i);
            System.out.println(timer);
            g.fillRect(x, y, 20, 20);

        }
        pijlX += 20;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

            // Met interval van de delay van de timer wordt dit gedaan:
        if (e.getSource() == timer) {
            if (pijlX == 120) {
                pijlX = 0;
            }
        }

        // wanneer op de startknop wordt gedrukt gebeurd dit:
        if (e.getSource() == jbStart) {
            timer.start();
        }
        // wanneer op de stopknop wordt gedrukt gebeurd dit:
        if (e.getSource() == jbStop) {
            timer.stop();
        }

        // Als op de homeknop gedrukt wordt het scherm gesloten en opent het homescherm
        if (e.getSource() == jbHome) {
            this.dispose();
            new HoofdschermGUI().setVisible(true);
            timer.stop();
        }
        repaint();
    }
}
