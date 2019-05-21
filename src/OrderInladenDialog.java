import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class OrderInladenDialog extends JDialog implements ActionListener { // Dialoog voor wanneer je op de order inladen button klikt bij OrderInladenGui.

    private JLabel jLabel;
    private JButton jbLaadIn;


    private String[] kleuren = {"rood", "groen", "geel"}; // Kleuren voor de combobox.
    private JComboBox jComboBox;

    private int orderNummer; // Ordernummer die meegegeven is uit het vorige scherm.
    private int aantalProducten;

    private int orderIDDatabaseInt;

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

        if(e.getSource() == jbLaadIn) // Als op inladen gedrukt is.
        {
            // Database connectie
            databaseHelper = new DatabaseHelper();
            databaseHelper.openConnection();
            // Tel aantal rows uit ingeladen order database.
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
            try{
                String SQL2 = "SELECT orderkleur, orderid FROM temporders"; // Selecteer orderkleur uit de database met id
                ResultSet rs1 = databaseHelper.selectQuery(SQL2);

                while(rs1.next()){ // Check of order al niet ingeladen is en vang dit af met een joptionpane.
                    String kleurDatabase = rs1.getString("orderkleur");
                    String orderIDDatabase = rs1.getString("orderid");
                    orderIDDatabaseInt = Integer.parseInt(orderIDDatabase);
                    if(selectedKleur.equals(kleurDatabase)){
                        JOptionPane.showMessageDialog(this, "Je kan niet twee dezelfde kleuren toevoegen!");
                        return;
                    }
                    if(orderNummer == orderIDDatabaseInt){
                        JOptionPane.showMessageDialog(this, "Je kan niet twee dezelfde orderid's toevoegen!");
                        return;
                    }
                }
            }catch (Exception x){
                x.printStackTrace();
            }

            // Insert de order in de database om hem in te laden.
            String SQL = String.format("INSERT INTO temporders (orderkleur, aantalblokjes, orderid) VALUES ('%s', %S, %S)", selectedKleur, aantalProducten, orderNummer);
            System.out.println(" Aantal Blokjes: ["+aantalProducten+"]");

            int querySuccess = databaseHelper.executeUpdateQuery(SQL); // Execute de insert query

            if(querySuccess == 1) // Als succesvol in de database gezet is
            {
                JOptionPane.showMessageDialog(this, "Order " + orderNummer + " ingeladen met kleur " + selectedKleur);
                this.dispose();
                databaseHelper.closeConnection();
            }

        }
    }

}
