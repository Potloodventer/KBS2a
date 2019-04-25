import javax.swing.*;
import javax.xml.transform.Result;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class OrderInladenDialog extends JDialog implements ActionListener {

    private JLabel jLabel;
    private JButton jbLaadIn;
    private String[] kleuren = {"Rood", "Groen", "Geel"};
    private JComboBox jComboBox;
    private int orderNummer;
    private int aantalProducten;
    private DatabaseHelper databaseHelper;

    public OrderInladenDialog(int orderNummer, int aantalProducten)
    {
        this.orderNummer = orderNummer;
        this.aantalProducten = aantalProducten;
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 300);
        setTitle("Order inladen");

        jLabel = new JLabel("Order " + orderNummer + " met  " + aantalProducten + " producten inladen met kleur:");
        add(jLabel);
        add(Box.createRigidArea(new Dimension(HoofdschermGUI.getSchermBreedte() / 2, 20)));

        jComboBox = new JComboBox(kleuren);
        add(jComboBox);
        add(Box.createRigidArea(new Dimension(HoofdschermGUI.getSchermBreedte() / 2, 20)));

        jbLaadIn = new JButton("Inladen");
        jbLaadIn.addActionListener(this);
        add(jbLaadIn);

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == jbLaadIn)
        {
            databaseHelper = new DatabaseHelper();
            databaseHelper.openConnection();
            String selectedKleur = String.valueOf(jComboBox.getSelectedItem());
            System.out.println(selectedKleur);
            String SQL = String.format("INSERT INTO temporders (orderkleur, aantalblokjes, orderid) VALUES ('%s', %S, %S)", selectedKleur, aantalProducten, orderNummer);

            int querySuccess = databaseHelper.executeUpdateQuery(SQL);

            if(querySuccess == 1)
            {
                JOptionPane.showMessageDialog(this, "Order " + orderNummer + " ingeladen met kleur " + selectedKleur);
                this.dispose();
            }

        }


    }
}
