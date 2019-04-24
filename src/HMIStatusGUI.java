import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HMIStatusGUI extends JFrame implements ActionListener {

    private int roodAantal = 0, groenAantal = 0, blauwAantal = 0, pijlX = 0;

    private String kleur; // Hier moet de kleur inkomen die de sensor waarneemt

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
        timer = new Timer(150, this);

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
    public void beweegPijlen(Graphics g, String kleur){
        // stelt de kleur van de pijlen in
        if (kleur == null) {
            g.setColor(Color.GREEN);
        } else if (kleur == "blauw") {
            g.setColor(Color.cyan);
        } else if (kleur == "rood") {
            g.setColor(Color.RED);
        } else if (kleur == "geel") {
            g.setColor(Color.YELLOW);
        }

        int y = (HoofdschermGUI.getSchermHoogte() - 200) / 2;
        // aanmaken arrays voor de X en Y punten van driehoek (pijlpunt)
        int[] xpoints = new int[3];
        int[] ypoints = new int[3];

        for (int i = 1; i < 7; i++) {
            // aantal blokjes, hun positie en bewegen met stappen van pijlX
            int x = (90 * i) + pijlX;

            // X punten van driehoek toevoegen aan array
            xpoints[0] = x + 31;
            xpoints[1] = x + 55;
            xpoints[2] = x + 31;
            // Y punten van driehoek toevoegen aan array
            ypoints[0] = y - 15;
            ypoints[1] = y + 10;
            ypoints[2] = y + 35;

            //print de x en y positie van de blokjes en de i van de loop
            System.out.println(x * i + " " + y + " " + i);
            System.out.println(timer);

            // teken pijlbody
            g.fillRect(x, y, 30, 20);
            // teken pijlpunt
            g.fillPolygon(xpoints, ypoints, 3);

        }
        // verplaats de pijlen met een verandering van 20 naar rechts
        pijlX += 20;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Met interval van de delay van de timer wordt dit gedaan:
        if (e.getSource() == timer) {
            if (pijlX == 120) { // zet de x terug naar begin en reset de posities van de pijlen elke keer
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