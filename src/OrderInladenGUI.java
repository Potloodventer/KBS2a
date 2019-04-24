import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderInladenGUI extends JFrame implements ActionListener {

    private JButton jbHome;

    public OrderInladenGUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(HoofdschermGUI.getSchermBreedte(), HoofdschermGUI.getSchermHoogte());
        setTitle("Voorraad");

        jbHome = new JButton("HOME");
        jbHome.addActionListener(this);
        jbHome.setPreferredSize(new Dimension(100, 30));

        add(jbHome);

        setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Als op de homeknop gedrukt wordt sluit het scherm en opent het homescherm
        if (e.getSource() == jbHome) {
            this.dispose();
            new HoofdschermGUI().setVisible(true);
        }
    }

}
