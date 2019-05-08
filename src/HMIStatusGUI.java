import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HMIStatusGUI extends JFrame implements ActionListener {

    private int aantalRood = 0, aantalGroen = 0, aantalGeel = 0;
    private int pijlX = 0;

    private int upp = 0; // puur voor testen van slagboom
    int y = (HoofdschermGUI.getSchermHoogte() - 200) / 2;
    int[] xpoints = new int[3];
    int[] ypoints = new int[3];

    private String kleur; // Hier moet de kleur inkomen die de sensor waarneemt

    private JButton jbHome, jbStart, jbStop, jbResultaat;
    private JLabel jlAantallen, jlRood, jlGroen, jlGeel;

    private TekenPanelHMIStatus tekenPanelHMIStatus;
    private Timer timer;

    private BufferedImage blauwdruk = null;

    public HMIStatusGUI() {

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(HoofdschermGUI.getSchermBreedte(), HoofdschermGUI.getSchermHoogte());
        setTitle("Status robots");

        //Kijken of plaatje bestaat anders wordt er een fout weergegeven
        try {
            blauwdruk = ImageIO.read(new File("src/Images/ModelRobot_2.0.png"));

        } catch (IOException IOex) {
            System.out.println("Plaatje Niet gevonden");
        }


        // timer met delay van 1 milliseconde
        timer = new Timer(1, this);

        tekenPanelHMIStatus = new TekenPanelHMIStatus(this);

        jlAantallen = new JLabel("De waargenomen aantallen per kleur: ");
        jlRood = new JLabel("Rood: " + aantalRood);
        jlGroen = new JLabel("Groen: " + aantalGroen);
        jlGeel = new JLabel("Geel: " + aantalGeel);


        tekenPanelHMIStatus = new TekenPanelHMIStatus(this);
        tekenPanelHMIStatus.add(Box.createRigidArea(new Dimension(900, 100)));
        tekenPanelHMIStatus.add(jlAantallen);
        tekenPanelHMIStatus.add(Box.createRigidArea(new Dimension(630, 5)));

        tekenPanelHMIStatus.add(jlRood);
        tekenPanelHMIStatus.add(Box.createRigidArea(new Dimension(800, 2)));

        tekenPanelHMIStatus.add(jlGroen);
        tekenPanelHMIStatus.add(Box.createRigidArea(new Dimension(800, 2)));
        tekenPanelHMIStatus.add(jlGeel);
        tekenPanelHMIStatus.add(Box.createRigidArea(new Dimension(800, 2)));

        //homeknop
        jbHome = new JButton("HOME");
        jbHome.addActionListener(this);
        jbHome.setPreferredSize(new Dimension(100, 30));

        //starknop
        jbStart = new JButton("START");
        jbStart.addActionListener(this);
        jbStart.setPreferredSize(new Dimension(100, 30));
        jbStart.setEnabled(true);

        //stopknop
        jbStop = new JButton("STOP");
        jbStop.addActionListener(this);
        jbStop.setPreferredSize(new Dimension(100, 30));
        jbStop.setEnabled(false);

        //resultaatknop
        jbResultaat = new JButton("RESULTAAT");
        jbResultaat.addActionListener(this);
        jbResultaat.setPreferredSize(new Dimension(150, 30));
        jbResultaat.setEnabled(false);

        // voeg dit allemaal toe
        add(Box.createRigidArea(new Dimension(45, 3)));
        add(jbHome);
        add(Box.createRigidArea(new Dimension(30, 3)));
        add(jbStart);
        add(Box.createRigidArea(new Dimension(30, 3)));
        add(jbStop);
        add(Box.createRigidArea(new Dimension(30, 3)));
        add(jbResultaat);
        add(Box.createRigidArea(new Dimension(HoofdschermGUI.getSchermBreedte() / 2, 20)));
        // tekenpanel
        add(Box.createRigidArea(new Dimension(800, 50)));
        add(tekenPanelHMIStatus);

        setVisible(false);
    }

    // puur voor testen van de posities van de armen
    public int getUpp() {
        return upp;
    }

    // getter voor de kleur ( rood, geel, groen of onbekend bij andere kleur )
    public String getKleur() {
        return kleur;
    }

    // getter voor de aantallen (kleur moet rood, geel of groen zijn)
    public int getAantal(String kleur) {
        if (kleur.equals("rood")) {
            return aantalRood;
        } else if (kleur.equals("groen")) {
            return aantalGroen;
        } else if (kleur == "geel") {
            return aantalGeel;
        } else {
            return 404;
        }
    }

    public void drawBlueprint(Graphics g) {
        //Overzicht van de robot tekenen
        g.drawImage(blauwdruk, 50, y - 165, null);
    }

    public void drawServoArm(Graphics g, int nummer1, int nummer2) {

        int[] xArm = new int[4];
        int[] yArm = new int[4];
        int[] xArm2 = new int[4];
        int[] yArm2 = new int[4];

        // 1e arm
        if (nummer1 == 1) { // 1 = blokje weggooien
            //Stand dicht van arm 1
            xArm[0] = 405;
            xArm[1] = 415;
            xArm[2] = 500;
            xArm[3] = 490;

            yArm[0] = 265;
            yArm[1] = 255;
            yArm[2] = 360;
            yArm[3] = 370;

        } else if (nummer1 == 2) { // 2 is blokje doorlaten
            //Stand open van arm 1
            xArm[0] = 405;
            xArm[1] = 500;
            xArm[2] = 500;
            xArm[3] = 405;

            yArm[0] = 260;
            yArm[1] = 270;
            yArm[2] = 280;
            yArm[3] = 270;
        }

        // 2e arm
        if (nummer2 == 1){
            //Stand Groen van arm 2
            xArm2[0] = 590;
            xArm2[1] = 600;
            xArm2[2] = 720;
            xArm2[3] = 710;

            yArm2[0] = 280;
            yArm2[1] = 270;
            yArm2[2] = 350;
            yArm2[3] = 360;

        } else if (nummer2 == 2) {
            //Stand Geel van arm 2
            xArm2[0] = 760;
            xArm2[1] = 770;
            xArm2[2] = 710;
            xArm2[3] = 700;

            yArm2[0] = 250;
            yArm2[1] = 260;
            yArm2[2] = 360;
            yArm2[3] = 350;

        } else if (nummer2 == 3) {
            //Stand Rood van arm 2
            xArm2[0] = 710; // links boven
            xArm2[1] = 840; // rechts boven
            xArm2[2] = 840; // rechts onder
            xArm2[3] = 710; // links onder

            yArm2[0] = 350;
            yArm2[1] = 350;
            yArm2[2] = 360;
            yArm2[3] = 360;
        }

        g.setColor(Color.MAGENTA);
        g.fillPolygon(xArm,yArm,4);
        g.setColor(Color.GREEN);
        g.fillPolygon(xArm2,yArm2,4);


    }

    // teken en beweeg de pijlen op de lopende band
    public void moveArrows(Graphics g, String kleur) {
        // stelt de kleur van de pijlen in
        if (kleur == null) {
            g.setColor(Color.BLACK);
        } else if (kleur == "groen") {
            g.setColor(Color.GREEN);
        } else if (kleur == "rood") {
            g.setColor(Color.RED);
        } else if (kleur == "geel") {
            g.setColor(Color.YELLOW);
        }

        // aanmaken arrays voor de X en Y punten van driehoek (pijlpunt)
        for (int i = 1; i < 7; i++) {
            // aantal blokjes, hun positie en bewegen met stappen van pijlX
            int x = (120 * i) + pijlX;

            // X punten van driehoek toevoegen aan array
            xpoints[0] = x + 31; // bovenste x punt driehoek
            xpoints[1] = x + 55; // middelste x punt driehoek
            xpoints[2] = x + 31; // onderste x punt driehoek

            // Y punten van driehoek toevoegen aan array
            ypoints[0] = y - 15; // bovenste y punt driehoek
            ypoints[1] = y + 10; // middelste y punt driehoek
            ypoints[2] = y + 35; // onderste y punt driehoek

            // teken pijlbody
            g.fillRect(x, y, 30, 20);
            // teken pijlpunt
            g.fillPolygon(xpoints, ypoints, 3);
        }

        // verplaats de pijlen met een verandering van 20 naar rechts
        pijlX += 3;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Met interval van de delay van de timer wordt dit gedaan:
        if (e.getSource() == timer) {
            if (pijlX > 120) { // zet de x terug naar begin en reset de posities van de pijlen elke keer
                pijlX = 0;
            }
        }

        // wanneer op de startknop wordt gedrukt gebeurd dit:
        if (e.getSource() == jbStart) {
            jbStart.setEnabled(false);
            jbStop.setEnabled(true);
            timer.start();
        }

        // wanneer op de stopknop wordt gedrukt gebeurd dit:
        if (e.getSource() == jbStop) {
            jbStart.setEnabled(true);
            jbStop.setEnabled(false);
            jbResultaat.setEnabled(true);
            timer.stop();

            // testen van arm(en)
            if (upp <= 3) {
                upp++;
            } else {
                upp = 1;
            }
        }

        // wanneer op de resultaatknop wordt gedrukt gebeurd dit:
        if (e.getSource() == jbResultaat) {
            new BPPResultaatGUI(this).setVisible(true);
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