import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.xml.transform.Result;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class OrderInladenGUI extends JFrame implements ActionListener {

    private JButton jbHome;
    private DatabaseHelper databaseHelper;
    private JTable jTable;
    private JScrollPane jScrollPane;
    private JTable jTable2;
    private JScrollPane jScrollPane2;
    private JButton jbInladen;
    private int iSelectedIndex = 0;

    public OrderInladenGUI() {
        // Frame opties
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(HoofdschermGUI.getSchermBreedte(), HoofdschermGUI.getSchermHoogte());
        setTitle("Voorraad");

        // Buttons
        jbHome = new JButton("HOME");
        jbHome.addActionListener(this);
        jbHome.setPreferredSize(new Dimension(100, 30));

        jbInladen = new JButton("INLADEN");
        jbInladen.addActionListener(this);
        jbInladen.setPreferredSize(new Dimension(100, 30));

        add(jbHome);
        add(jbInladen);

        add(Box.createRigidArea(new Dimension(HoofdschermGUI.getSchermBreedte() / 2 + 170, 20)));


        // Database connectie
        databaseHelper = new DatabaseHelper();
        databaseHelper.openConnection();
        ResultSet rs = databaseHelper.selectQuery("SELECT OrderID FROM orders ORDER BY OrderID");

        // Tables
        jTable = new JTable();
        jScrollPane = new JScrollPane(jTable);

        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel selectionModel = jTable.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                handleSelectionEvent(e);
            }
        });
        try {
            resultSetToTableModel(rs, jTable);
        }catch (Exception e){
            e.printStackTrace();
        }
        setVisible(false);
        jTable2 = new JTable();
        jScrollPane2 = new JScrollPane(jTable2);


        add(jScrollPane);
        add(jScrollPane2);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Als op de homeknop gedrukt wordt sluit het scherm en opent het homescherm
        if (e.getSource() == jbHome) {
            this.dispose();
            new HoofdschermGUI().setVisible(true);
        }
        if(e.getSource() == jbInladen)
        {

            int selectedOrder = jTable.getSelectedRow() + 1;
            int aantalProducten = jTable2.getRowCount();
            if(selectedOrder == -1 || selectedOrder == 0)
            {
                JOptionPane.showMessageDialog(this, "Selecteer eerst een order alsjeblieft.");
                return;
            }
            new OrderInladenDialog(selectedOrder, aantalProducten).setVisible(true);
        }
    }

    protected void resultSetToTableModel(ResultSet rs, JTable table) throws SQLException { // Functie om resultset uit de database makkelijk in een JTable te verwerken
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

    protected void handleSelectionEvent(ListSelectionEvent e) {
        if (e.getValueIsAdjusting())
            return;
        int selectedRow = jTable.getSelectedRow() + 1;
        System.out.println(selectedRow);

        String SQL = String.format("SELECT X.StockItemID, X.Description, X.UnitPrice, X.Quantity FROM orders AS Z JOIN orderlines AS X ON Z.OrderID = X.OrderID WHERE X.OrderID = %S", selectedRow);
        ResultSet rs = databaseHelper.selectQuery(SQL);

        try {
            resultSetToTableModel(rs, jTable2);
        }catch (Exception x){
            x.printStackTrace();
        }


    }

}
