import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class OrderInladenDialogVerwijderen extends JDialog implements ActionListener { // Dialoog om een ingeladen order te verwijderen vanuit OrderInladenGui

    private JTable jTable;
    private JScrollPane jScrollPane;
    private JButton jButton;
    private JLabel jLabel;
    private DatabaseHelper databaseHelper;


    public OrderInladenDialogVerwijderen()
    {
        // Layout opties
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 300);
        setTitle("Ingeladen orders");

        // Open de database connectie.
        databaseHelper = new DatabaseHelper();
        databaseHelper.openConnection();




        jLabel = new JLabel("Ingeladen orders:");
        add(jLabel);
        add(Box.createRigidArea(new Dimension(HoofdschermGUI.getSchermBreedte() / 2, 10)));


        jTable = new JTable();
        jScrollPane = new JScrollPane(jTable);
        jScrollPane.setPreferredSize(new Dimension(300, 150));

        add(jScrollPane);
        add(Box.createRigidArea(new Dimension(HoofdschermGUI.getSchermBreedte() / 2, 10)));

        jButton = new JButton("Verwijder");
        jButton.addActionListener(this);
        add(jButton);

        // Selecteer de ingeladen orders uit de database en laat dit zien in een table.
        String SQL = "SELECT orderid, orderkleur, aantalblokjes FROM temporders";
        ResultSet rs = databaseHelper.selectQuery(SQL);

        try{
            resultSetToTableModel(rs, jTable);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // if lijst is leeg : error, else verwijder de order

        if(e.getSource() == jButton)
        {
            try {
                int column = 0;
                int row = jTable.getSelectedRow();
                String selectedValue = jTable.getModel().getValueAt(row, 0).toString();
                System.out.println("Geselecteerde order " + selectedValue);
                String SQL = String.format("DELETE FROM temporders WHERE orderid = %S", selectedValue);
                int querySucces = databaseHelper.executeUpdateQuery(SQL);
                if (querySucces == 1) {
                    JOptionPane.showMessageDialog(this, "Ingeladen order " + selectedValue + " succesvol verwijderd.");
                    this.dispose();
                    new OrderInladenDialogVerwijderen().setVisible(true);
                }
            } catch (Exception x) {
                x.printStackTrace();
                JOptionPane.showMessageDialog(this, "Je kan niks verwijderen als er niks ingeladen is.");
            }
        }


    }
    protected static void resultSetToTableModel(ResultSet rs, JTable table) throws SQLException { // Statische functie om resultset uit de database makkelijk in een JTable te verwerken
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
