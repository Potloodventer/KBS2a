import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BPPResultaatGUI extends JFrame implements ActionListener {

    private HMIStatusGUI hmiStatusGUI;

    private JLabel jlRood, jlGroen, jlGeel;
    private JButton jbPakbon;

    public BPPResultaatGUI(HMIStatusGUI hmiStatusGUI) {
        this.hmiStatusGUI = hmiStatusGUI;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(HoofdschermGUI.getSchermBreedte(), HoofdschermGUI.getSchermHoogte());
        setTitle("BPP Resultaat");

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


        setVisible(false);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbPakbon) {
            this.dispose();
            new PakbonGUI().setVisible(true);
        }
    }
}
