import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class OrderInladenGUI extends JFrame implements ActionListener {

    private JButton jbHome;
    private DatabaseHelper databaseHelper;
    private JTable jTable;
    private JScrollPane jScrollPane;
    private JTable jTable2;
    private JScrollPane jScrollPane2;
    private JButton jbInladen;
    private JButton jbVerwijderen;
    private int iSelectedIndex = 0;

    public OrderInladenGUI() {
        // Frame opties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        jbVerwijderen = new JButton("INGELADEN ORDERS");
        jbVerwijderen.addActionListener(this);
        jbVerwijderen.setPreferredSize(new Dimension(180, 30));

        add(jbHome);
        add(jbInladen);
        add(jbVerwijderen);
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
            OrderInladenDialogVerwijderen.resultSetToTableModel(rs, jTable);
        } catch (Exception e){
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

            int selectedOrder = jTable.getSelectedRow() + 1; // Pak geselecteerde order in jTable en tel er 1 bij op
            int aantalProducten = jTable2.getRowCount(); // Aantal producten in geselecteerde order

            if(selectedOrder == -1 || selectedOrder == 0) // Als er geen order is geselecteerd
            {
                JOptionPane.showMessageDialog(this, "Selecteer eerst een order alsjeblieft.");
                return;
            }
            new OrderInladenDialog(selectedOrder, aantalProducten).setVisible(true);
        }

        if(e.getSource() == jbVerwijderen)
        {
            new OrderInladenDialogVerwijderen().setVisible(true);
        }
    }


    protected void handleSelectionEvent(ListSelectionEvent e) { // Functie om de producten van de geselecteerde order
        // te laten zien in de 2de jtable
        if (e.getValueIsAdjusting())
            return;
        int selectedRow = jTable.getSelectedRow() + 1; //  Pak geselecteerde orderid
        // Gebruik order id om producten op te halen uit de database
        String SQL = String.format("SELECT X.StockItemID, X.Description, X.UnitPrice, X.Quantity FROM orders AS Z JOIN orderlines AS X ON Z.OrderID = X.OrderID WHERE X.OrderID = %S", selectedRow);
        ResultSet rs = databaseHelper.selectQuery(SQL);

        // Zet de data van de database om in een jtable
        try {
            OrderInladenDialogVerwijderen.resultSetToTableModel(rs, jTable2);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}