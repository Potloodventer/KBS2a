import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BPPResultaatGUI extends JFrame implements ActionListener {

    private HMIStatusGUI hmiStatusGUI;
    TekenPanelBPPResultaat tekenPanelBPPResultaat;

    private JLabel jlRood, jlGroen, jlGeel;
    private JButton jbPakbon;

    private int aantalBins = 0; // int om bij te houden wanneer de bins een plek op moeten schuiven

    public BPPResultaatGUI(HMIStatusGUI hmiStatusGUI) {
        this.hmiStatusGUI = hmiStatusGUI;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(HoofdschermGUI.getSchermBreedte(), HoofdschermGUI.getSchermHoogte());
        setTitle("BPP Resultaat");

        tekenPanelBPPResultaat = new TekenPanelBPPResultaat(this);

        jbPakbon = new JButton("Pakbon");
        jbPakbon.addActionListener(this);

        // kleuren met het aantal erachter
        jlRood = new JLabel("Rood: " + hmiStatusGUI.getAantal("rood"));
        jlGroen = new JLabel("Groen: " + hmiStatusGUI.getAantal("groen"));
        jlGeel = new JLabel("Geel: " + hmiStatusGUI.getAantal("geel"));


        // voeg dit allemaal toe op het scherm
        add(jbPakbon);

        add(jlRood); // rood met het aantal blokjes als eerst
        // hier moeten de bakken komen voor rood (naast jlRood)
        add(jlGroen); // middenin komt jlGroen met het aantal blokjes
        // hier moeten de bakken komen voor alle groene blokjes ( rechts van jlGroen )
        add(jlGeel); //

        add(tekenPanelBPPResultaat);


        setVisible(false);
    }


    public void drawBins(Graphics g) {

        for (int j = 2; j < 9; j += 2) {

            for (int i = 1; i < 6; i++) {
                int x1 = 100 * i;
                int x2 = 100 * i; // als de dozen skeer gaan verderop dit aanpassen

                int y1 = 50 * j;
                int y2 = 100 * j;

                g.drawLine(x1, y1, x2, y2); // linker lijn
                g.drawLine(x1, y2, x1 + 80, y2); // onderste lijn
                g.drawLine(x1 + 80, y1, x1 + 80, y2); // rechter lijn
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbPakbon) {
            this.dispose();
            new PakbonGUI().setVisible(true);
        }
    }
}
