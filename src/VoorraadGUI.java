import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class VoorraadGUI extends JFrame implements ActionListener {

    private JButton jbHome;
    private JTable jTable;
    private JScrollPane jScrollPane;
    private DatabaseHelper databaseHelper;
    private JButton jbWijzig;

    public VoorraadGUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(HoofdschermGUI.getSchermBreedte(), HoofdschermGUI.getSchermHoogte());
        setTitle("Voorraad");

        jbHome = new JButton("HOME");
        jbHome.addActionListener(this);
        jbHome.setPreferredSize(new Dimension(100, 30));
        jbWijzig = new JButton("WIJZIG");
        jbWijzig.addActionListener(this);
        jbWijzig.setPreferredSize(new Dimension(100, 30));


        add(jbHome);
        add(jbWijzig);
        add(Box.createRigidArea(new Dimension(HoofdschermGUI.getSchermBreedte() / 2, 20)));

        // Database connectie

        databaseHelper = new DatabaseHelper();
        databaseHelper.openConnection();
        // Query de database

        ResultSet rs = databaseHelper.selectQuery("SELECT stockitems.StockItemID, StockItemName, UnitPrice, QuantityOnHand FROM stockitems JOIN stockitemholdings ON stockitems.StockItemID = stockitemholdings.StockItemID");
        jTable = new JTable();
        jScrollPane = new JScrollPane(jTable);
        jScrollPane.setPreferredSize(new Dimension(650, 700));

        // Gebruik de functie om een resultset in een jtable te plaatsen

        try {
            resultSetToTableModel(rs, jTable);
        }catch (Exception e){
            e.printStackTrace();
        }
        setVisible(false);

        add(jScrollPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Als op de homeknop gedrukt wordt sluit het scherm en opent het homescherm
        if (e.getSource() == jbHome) {
            this.dispose();
            new HoofdschermGUI().setVisible(true);
        }

        // Als op de wijzigknop gedrukt wordt opent het product wijzigen scherm
        if (e.getSource() == jbWijzig) {
            new ProductWijzigenDialog(this).setVisible(true);
        }


    }

    public void resultSetToTableModel(ResultSet rs, JTable table) throws SQLException { // Functie om resultset uit de database makkelijk in een JTable te verwerken
        // Maak nieuw tabel model aan
        DefaultTableModel tableModel = new DefaultTableModel();

        // Ontvang metadata van de resultset
        ResultSetMetaData metaData = rs.getMetaData();

        // Aantal kolommen in resultset
        int columnCount = metaData.getColumnCount();

        // Pak alle tabelnamen uit metadata en voeg die aan tabel toe
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++){
            tableModel.addColumn(metaData.getColumnLabel(columnIndex));
        }

        // Maak array de grootte van aantal kolommen
        Object[] row = new Object[columnCount];

        // Loop door de resultset
        while (rs.next()){
            for (int i = 0; i < columnCount; i++){
                row[i] = rs.getObject(i+1);
            }
            // Voeg elke row toe aan de tabel
            tableModel.addRow(row);
        }

        // Set de tabel voor de Jtable
        table.setModel(tableModel);

    }

}
