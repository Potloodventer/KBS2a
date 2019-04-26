import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PakbonGUI extends JFrame implements ActionListener {

    private int aantalRood, aantalGroen, aantalGeel;
    private JLabel jlRood, jlGroen, jlGeel;

    private HMIStatusGUI hmiStatusGUI;

    public PakbonGUI(HMIStatusGUI hmiStatusGUI) {
        this.hmiStatusGUI = hmiStatusGUI;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(HoofdschermGUI.getSchermBreedte(), HoofdschermGUI.getSchermHoogte());
        setTitle("Pakbon");

        // kleuren met het aantal erachter
        jlRood = new JLabel("Rood: " + hmiStatusGUI.getAantal("rood"));
        jlGroen = new JLabel("Groen: " + hmiStatusGUI.getAantal("groen"));
        jlGeel = new JLabel("Geel: " + hmiStatusGUI.getAantal("geel"));


        // voeg dit allemaal toe op het scherm
        add(jlRood);
        add(jlGroen);
        add(jlGeel);

        setVisible(false);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
