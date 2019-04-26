import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;

public class PakbonGUI extends JFrame implements ActionListener {

    private DatabaseHelper databaseHelper;

    private ArrayList<String> orderNummers;

    private int aantalOrders;
    private JTabbedPane tabbedPane;
    private JTable jTable;
    private JPanel jPanel;
    private JScrollPane jScrollPane;

    public PakbonGUI() {

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(HoofdschermGUI.getSchermBreedte(), HoofdschermGUI.getSchermHoogte());
        setTitle("Pakbon");
        orderNummers = new ArrayList<>();
        tabbedPane = new JTabbedPane();
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

        ResultSet rs3 = databaseHelper.selectQuery("SELECT orderid FROM temporders"); // Query om alle order ids te pakken van temporders tabel
        for(int i = 0; i < aantalOrders; i++) // Loop die ervoor zorgt dat alle order ids in een arraylist komen
        {
            try{
                if(rs3.next()) {
                    orderNummers.add(rs3.getString("orderid"));
                    System.out.println(orderNummers.get(i));
                }
                }catch (Exception e){
                e.printStackTrace();
            }
        }

        for(int i = 0; i < aantalOrders; i++) // Loop die ervoor zorgt dat er tabs worden toegevoegd aan het panel gelijk aan het aantal orders
        {
            try{
                jPanel = new JPanel();
                jTable = new JTable();
                jScrollPane = new JScrollPane(jTable);
                tabbedPane.add("Order " + orderNummers.get(i), jPanel);

                String SQL = String.format("SELECT X.StockItemID, X.Description, X.UnitPrice, X.Quantity FROM orders AS Z JOIN orderlines AS X ON Z.OrderID = X.OrderID WHERE X.OrderID = %S", orderNummers.get(i));
                ResultSet rs2 = databaseHelper.selectQuery(SQL);
                try {
                    OrderInladenDialogVerwijderen.resultSetToTableModel(rs2, jTable);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                jPanel.add(jScrollPane);

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        tabbedPane.setPreferredSize(new Dimension(1000, 700));
        add(tabbedPane);
        setVisible(false);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
