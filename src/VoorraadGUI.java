import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class VoorraadGUI extends JFrame implements ActionListener {

    private JButton jbHome;
    private JTable jTable;
    private JScrollPane jScrollPane;
    private DatabaseHelper databaseHelper;
    private JButton jbWijzig;

    public VoorraadGUI() {
        // Layout opties.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
            OrderInladenDialogVerwijderen.resultSetToTableModel(rs, jTable);
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


}
