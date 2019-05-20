import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;

public class PakbonGUI extends JFrame implements ActionListener {

    private DatabaseHelper databaseHelper;
    // ArrayList voor ordernummers en orderkleuren
    private ArrayList<String> orderNummers;
    private ArrayList<String> orderKleuren;

    private int aantalOrders; // aantalRows uit ingeladen orders.
    // Elementen voor het frame.
    private JTabbedPane tabbedPane;
    private JTable jTable;
    private JPanel jPanel;
    private JScrollPane jScrollPane;

    private JLabel klantNaam, klantTelefoon, klantPostcode, klantAdres, klantId;
    private JLabel orderKleur, aantalDozen, doosVolume;
    private JLabel orderInfo, klantInfo, productInfo, totaalPrijs;
    private int totaalunitprice;


    public PakbonGUI() {

        // Layout opties.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(HoofdschermGUI.getSchermBreedte(), HoofdschermGUI.getSchermHoogte());
        setTitle("Pakbon");
        // Initialiseer arraylists.
        orderNummers = new ArrayList<>();
        orderKleuren = new ArrayList<>();
        // Initialiseer tabbedpane voor verschillende order ids.
        tabbedPane = new JTabbedPane();
        // Open connectie met de database.
        databaseHelper = new DatabaseHelper();
        databaseHelper.openConnection();

        ResultSet rs = databaseHelper.selectQuery("SELECT * FROM temporders"); // Query om rijen te tellen
        try { // Tel de rijen en sla op in aantalOrders
            rs.last();
            aantalOrders = rs.getRow();
            rs.beforeFirst();
        }catch (Exception e ){
            e.printStackTrace();
        }

        ResultSet rs3 = databaseHelper.selectQuery("SELECT orderid, orderkleur FROM temporders"); // Query om alle order ids te pakken van temporders tabel
        for(int i = 0; i < aantalOrders; i++) // Loop die ervoor zorgt dat alle order ids in een arraylist komen
        {
            try{
                if(rs3.next()) {
                    orderNummers.add(rs3.getString("orderid"));
                    orderKleuren.add(rs3.getString("orderkleur"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        for(int i = 0; i < aantalOrders; i++) // Loop die ervoor zorgt dat er tabs worden toegevoegd aan het panel gelijk aan het aantal orders
                                                // En alle elementen van de pakbon dynamisch toevoegt per order.
        {

            try{
                // Initialiseer alle elementen per order
                jPanel = new JPanel();
                jTable = new JTable();

                totaalunitprice = 0;

                orderInfo = new JLabel("Order info:");
                productInfo = new JLabel("Product info:");
                orderKleur = new JLabel("");
                aantalDozen = new JLabel("Aantal dozen: 0");
                doosVolume = new JLabel("Volume doos: 10");
                totaalPrijs = new JLabel("");

                klantNaam = new JLabel("");
                klantAdres = new JLabel("");
                klantId = new JLabel("");
                klantPostcode = new JLabel("");
                klantTelefoon = new JLabel("");
                klantInfo = new JLabel("Klant info:");

                jPanel.add(orderInfo);
                jPanel.add(Box.createRigidArea(new Dimension(HoofdschermGUI.getSchermBreedte() / 2, 0)));
                jPanel.add(orderKleur);
                jPanel.add(Box.createRigidArea(new Dimension(HoofdschermGUI.getSchermBreedte() / 2, 0)));
                jPanel.add(aantalDozen);
                jPanel.add(Box.createRigidArea(new Dimension(HoofdschermGUI.getSchermBreedte() / 2, 0)));
                jPanel.add(doosVolume);

                jPanel.add(Box.createRigidArea(new Dimension(HoofdschermGUI.getSchermBreedte() / 2, 8)));
                jPanel.add(klantInfo);
                jPanel.add(Box.createRigidArea(new Dimension(HoofdschermGUI.getSchermBreedte() / 2, 0)));
                jPanel.add(klantId);
                jPanel.add(Box.createRigidArea(new Dimension(HoofdschermGUI.getSchermBreedte() / 2, 0)));
                jPanel.add(klantNaam);
                jPanel.add(Box.createRigidArea(new Dimension(HoofdschermGUI.getSchermBreedte() / 2, 0)));

                jPanel.add(klantAdres);
                jPanel.add(Box.createRigidArea(new Dimension(HoofdschermGUI.getSchermBreedte() / 2, 0)));
                jPanel.add(klantPostcode);
                jPanel.add(Box.createRigidArea(new Dimension(HoofdschermGUI.getSchermBreedte() / 2, 0)));

                jPanel.add(klantTelefoon);
                jPanel.add(Box.createRigidArea(new Dimension(HoofdschermGUI.getSchermBreedte() / 2, 8)));

                jPanel.add(productInfo);
                jPanel.add(Box.createRigidArea(new Dimension(HoofdschermGUI.getSchermBreedte() / 2, 0)));
                jScrollPane = new JScrollPane(jTable);
                jScrollPane.setPreferredSize(new Dimension(450, 200));
                tabbedPane.add("Order " + orderNummers.get(i), jPanel);
                //COMMIT3
                String SQL = String.format("SELECT X.StockItemID, X.Description, X.UnitPrice, X.Quantity FROM orders AS Z JOIN orderlines AS X ON Z.OrderID = X.OrderID WHERE X.OrderID = %S", orderNummers.get(i));
                ResultSet rs2 = databaseHelper.selectQuery(SQL);
                OrderInladenDialogVerwijderen.resultSetToTableModel(rs2, jTable);


                int rowcount = jTable.getRowCount();
                for(int x=0;x<rowcount; x++) { // for loop om het totale prijs per order op te tellen.
                    BigDecimal unitPrice = (BigDecimal) jTable.getValueAt(x, jTable.getColumn("UnitPrice").getModelIndex());
                    int unitPriceInt = unitPrice.intValueExact();
                    totaalunitprice += unitPriceInt;

                }
                totaalPrijs.setText("Totale orderprijs: " + totaalunitprice + "€");
                jPanel.add(jScrollPane);
                jPanel.add(totaalPrijs);

                String selecteerKlant = String.format("SELECT X.CustomerID, C.CustomerName, C.PostalPostalCode, C.PostalAddressLine2, C.PhoneNumber\n" +
                        "FROM orders as X join customers as C on x.CustomerID = C.CustomerID\n" +
                        "WHERE X.OrderID = %S", orderNummers.get(i));
                ResultSet rs4 = databaseHelper.selectQuery(selecteerKlant);

                if(rs4.next()) {

                    klantId.setText("ID: " + rs4.getString("CustomerID"));
                    klantNaam.setText("Naam: " + rs4.getString("CustomerName"));
                    klantAdres.setText("Adres: " + rs4.getString("PostalAddressLine2"));
                    klantPostcode.setText("Postcode: " + rs4.getString("PostalPostalCode"));
                    klantTelefoon.setText("Telefoon: " + rs4.getString("PhoneNumber"));

                }
                orderKleur.setText("Kleur: " + orderKleuren.get(i));


            }catch (Exception e){
                e.printStackTrace();
            }
        }

        tabbedPane.setPreferredSize(new Dimension(500, 600));
        add(tabbedPane);
        setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
