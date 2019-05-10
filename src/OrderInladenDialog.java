import javax.swing.*;
import javax.xml.transform.Result;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class OrderInladenDialog extends JDialog implements ActionListener {

    private JLabel jLabel;
    private JButton jbLaadIn;

    private String[] kleuren = {"rood", "groen", "geel"};
    private JComboBox jComboBox;

    private int orderNummer;
    private int aantalProducten;

    private DatabaseHelper databaseHelper;



    public OrderInladenDialog(int orderNummer, int aantalProducten) // Aan constructor meegeven zodat de vorige klasse variablen mee kan geven
    {
        // Order nummer en aantal producten meegeven om later te gebruiken
        this.orderNummer = orderNummer;
        this.aantalProducten = aantalProducten;

        // Layout opties
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 300);
        setTitle("Order inladen");

        // Buttons en labels...
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
            // Database connectie
            databaseHelper = new DatabaseHelper();
            databaseHelper.openConnection();
            try{
                String SQL = "SELECT * FROM temporders";
                ResultSet rs = databaseHelper.selectQuery(SQL);
                rs.last();
                int aantalRows = rs.getRow();
                rs.beforeFirst();
                if(aantalRows > 3 || aantalRows == 3)
                {
                    JOptionPane.showMessageDialog(this, "Je kan niet meer dan 3 orders inladen.");
                    return;
                }
            }catch (Exception x){
                x.printStackTrace();
            }
            String selectedKleur = String.valueOf(jComboBox.getSelectedItem()); // Pak de kleur van de combobox
            System.out.println("Geselecteerde kleur: "+selectedKleur);
            String SQL = String.format("INSERT INTO temporders (orderkleur, aantalblokjes, orderid) VALUES ('%s', %S, %S)", selectedKleur, aantalProducten, orderNummer);
            System.out.println(" Aantal Blokjes: ["+aantalProducten+"]");

            int querySuccess = databaseHelper.executeUpdateQuery(SQL); // Execute de insert query

            if(querySuccess == 1) // Als succesvol in de database gezet is
            {
                JOptionPane.showMessageDialog(this, "Order " + orderNummer + " ingeladen met kleur " + selectedKleur);
                this.dispose();
            }

        }
    }

}
