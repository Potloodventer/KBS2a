import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PakbonGUI extends JFrame implements ActionListener {


    public PakbonGUI() {

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(HoofdschermGUI.getSchermBreedte(), HoofdschermGUI.getSchermHoogte());
        setTitle("Pakbon");



        setVisible(false);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
