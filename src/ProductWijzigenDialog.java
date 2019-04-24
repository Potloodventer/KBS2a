import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class ProductWijzigenDialog extends JDialog implements ActionListener { // Dialoog om een product te wijzigen.

    // variablen voor de buttons en labels.
    private JButton jbLaadProduct;
    private JButton jbWijzigProduct;
    private JLabel naamLabel, prijsLabel, voorraadLabel, productIdLabel;
    private JTextField naamTextfield, prijsTextfield, voorraadTextfield, productIdTextfield;
    private DatabaseHelper databaseHelper;
    private VoorraadGUI voorraadGUI; // Geef voorraadGUI mee zodat hij refreshed kan worden na wijzigen product.

    public ProductWijzigenDialog(VoorraadGUI voorraadGUI)
    {
        this.voorraadGUI = voorraadGUI;

        // Layout opties.
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 300);
        setTitle("Product wijzigen");


        // Buttons en labels

        productIdLabel = new JLabel("Voer productID in");
        add(productIdLabel);
        productIdTextfield = new JTextField();
        productIdTextfield.setColumns(7);
        add(productIdTextfield);

        jbLaadProduct = new JButton("LAAD");
        jbLaadProduct.addActionListener(this);
        add(jbLaadProduct);
        add(Box.createRigidArea(new Dimension(HoofdschermGUI.getSchermBreedte() / 2, 20)));


        naamLabel = new JLabel("Naam product: ");
        add(naamLabel);
        naamTextfield = new JTextField();
        naamTextfield.setColumns(30);
        add(naamTextfield);
        add(Box.createRigidArea(new Dimension(HoofdschermGUI.getSchermBreedte() / 2, 20)));


        prijsLabel = new JLabel("Prijs product: ");
        add(prijsLabel);
        prijsTextfield = new JTextField();
        prijsTextfield.setColumns(15);
        add(prijsTextfield);

        add(Box.createRigidArea(new Dimension(HoofdschermGUI.getSchermBreedte() / 2, 20)));

        voorraadLabel = new JLabel("Voorraad product: ");
        add(voorraadLabel);
        voorraadTextfield = new JTextField();
        voorraadTextfield.setColumns(15);
        add(voorraadTextfield);


        add(Box.createRigidArea(new Dimension(HoofdschermGUI.getSchermBreedte() / 2, 20)));

        jbWijzigProduct = new JButton("WIJZIG");
        jbWijzigProduct.addActionListener(this);
        add(jbWijzigProduct);

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == jbLaadProduct) // Als er een productId wordt geladen dan wordt deze body uitgevoerd.
        {
            String selectedId = productIdTextfield.getText();
            if(selectedId.equals("")) // Als productId textfield leeg is dan wordt er een waarschuwing gegeven.
            {
                JOptionPane.showMessageDialog(this, "Voer alsjeblieft een productID in.");
                return;
            }

            databaseHelper = new DatabaseHelper(); // Initialiseer databasehelper klasse.
            databaseHelper.openConnection(); // Open de connectie.
            String SQL = String.format("SELECT StockItemName, UnitPrice, QuantityOnHand FROM stockitems JOIN stockitemholdings ON stockitems.StockItemID = stockitemholdings.StockItemID WHERE stockitemholdings.StockItemID = %S", selectedId);
            ResultSet rs = databaseHelper.selectQuery(SQL); // Voer query uit met ingevoerde productID.
            try{
                while(rs.next()){ // Zorg ervoor dat input worden ingevoerd met tekst van de database.
                    String name = rs.getString("StockItemName");
                    String voorraad = rs.getString("QuantityOnHand");
                    String prijs = rs.getString("UnitPrice");

                    naamTextfield.setText(name);
                    voorraadTextfield.setText(voorraad);
                    prijsTextfield.setText(prijs);

                }
                databaseHelper.closeConnection(); // Sluit connectie met de database.
            }catch (Exception x){
                JOptionPane.showMessageDialog(this, "Er is iets misgegaan.."); // Wordt uitgevoerd als er een error is gevonden.
                x.printStackTrace();
            }

        }
        if(e.getSource() == jbWijzigProduct) // Wordt uitgevoerd als er wat gewijzigd is aan het product.
        {

            // Pak ingevoerde tekst en sla op in een variabele.
            String name = naamTextfield.getText();
            String voorraad = voorraadTextfield.getText();
            String prijs = prijsTextfield.getText();
            String selectedId = productIdTextfield.getText();

            if(name.equals("") || voorraad.equals("") || prijs.equals("") || selectedId.equals("")){
                JOptionPane.showMessageDialog(this, "Laad alsjeblieft een productID in."); // Wordt uitgevoerd als er geen product is ingeladen met de laadknop.
                return;
            }

            try{
                databaseHelper = new DatabaseHelper(); // Initialiseer DatabaseHelper klasse.
                databaseHelper.openConnection(); // Open de connectie.
                // Queries om de velden in database up te daten met ingevulde waarden.
                String SQL = String.format("UPDATE stockitems SET StockItemName = '%s', UnitPrice = %S WHERE StockItemID = %S", name, prijs, selectedId);
                String SQL2 = String.format("UPDATE stockitemholdings SET QuantityOnHand = %S WHERE StockItemID = %S", voorraad, selectedId);

                // Execute deze queries.
                int query1 = databaseHelper.executeUpdateQuery(SQL);
                int query2 = databaseHelper.executeUpdateQuery(SQL2);
                if (query1 == 1 || query2 == 1) // Als query succesvol is, laat melding zien en refresh VoorraadGUI
                {
                    JOptionPane.showMessageDialog(this, "Product succesvol gewijzigd.");
                    this.dispose();
                    voorraadGUI.dispose();
                    new VoorraadGUI().setVisible(true);

                }
            }catch(Exception x){
                JOptionPane.showMessageDialog(this, "Er is iets misgegaan.."); // Als er iets fout is gegaan.
                x.printStackTrace();
            }

            databaseHelper.closeConnection(); // Sluit connectie met de database.

        }


    }
}
