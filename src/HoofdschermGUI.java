import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class HoofdschermGUI extends JFrame implements ActionListener {

    private static int schermBreedte = 1100, schermHoogte = 800;

    private JButton orderInladenJB;
    private JButton robotStatusJB;
    private JButton alleProductenJB;
    private JButton startRobotJB;
    private JButton stopRobotJB;

    private HMIStatusGUI hmiStatusGUI;
    private VoorraadGUI voorraadGUI;
    private OrderInladenGUI orderInladenGUI;
    private DatabaseHelper databaseHelper;
    private ArduinoConnectie arduinoConnectie;
    private boolean sendOrder = true;

    public HoofdschermGUI() {

        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(schermBreedte /2, schermHoogte /2);
        setTitle("HMI Hoofdscherm");
        databaseHelper = new DatabaseHelper();
        arduinoConnectie = new ArduinoConnectie(9600, 0);

        orderInladenJB = new JButton("Order inladen");
        robotStatusJB = new JButton("Status robots");
        alleProductenJB = new JButton("Pas voorraad aan");
        startRobotJB = new JButton("Start robots");
        stopRobotJB = new JButton("Stop robots");

        orderInladenJB.setPreferredSize(new Dimension(160, 30));
        robotStatusJB.setPreferredSize(new Dimension(160, 30));
        alleProductenJB.setPreferredSize(new Dimension(160, 30));
        startRobotJB.setPreferredSize(new Dimension(160, 30));
        stopRobotJB.setPreferredSize(new Dimension(160, 30));

        orderInladenJB.addActionListener(this);
        robotStatusJB.addActionListener(this);
        alleProductenJB.addActionListener(this);
        startRobotJB.addActionListener(this);
        stopRobotJB.addActionListener(this);

        add(Box.createRigidArea(new Dimension(schermBreedte / 2, 35)));  // lege Box voor de indeling
        add(orderInladenJB);
        add(Box.createRigidArea(new Dimension(schermBreedte / 2, 20)));
        add(robotStatusJB);
        add(Box.createRigidArea(new Dimension(schermBreedte / 2, 20)));
        add(alleProductenJB);
        add(Box.createRigidArea(new Dimension(schermBreedte / 2, 20)));
        add(startRobotJB);
        add(Box.createRigidArea(new Dimension(schermBreedte / 2, 20)));
        add(stopRobotJB);

        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == orderInladenJB) {
            // laad de order?
            this.setVisible(false);
            orderInladenGUI = new OrderInladenGUI();
            orderInladenGUI.setVisible(true);

        }

            // laat de visuele weergave van de robot zien
            // sluit het homescherm
        if (e.getSource() == robotStatusJB) {
            this.setVisible(false);
            hmiStatusGUI = new HMIStatusGUI();
            hmiStatusGUI.setVisible(true);
        }

            // Laat pagina met alle producten zien
        if (e.getSource() == alleProductenJB) {
            this.setVisible(false);
            voorraadGUI = new VoorraadGUI();
            voorraadGUI.setVisible(true);
        }

            // Start de robots
        if (e.getSource() == startRobotJB) {
            databaseHelper.openConnection();
            String SQL = "SELECT * FROM temporders";
            ResultSet rs = databaseHelper.selectQuery(SQL);
            try{
                rs.last();
                int aantalRows = rs.getRow();
                rs.beforeFirst();
                if(aantalRows < 1)
                {
                    JOptionPane.showMessageDialog(this, "Je moet eerst 1 of meer orders inladen.");
                    return;
                }
                else{
                    JOptionPane.showMessageDialog(this, "Robots zijn gestart met " + aantalRows + " orders.");
                    // Start de robots
                    // Stuur aantal blokjes en kleur per order naar robots
                        if(sendOrder){
                            if(rs.next()){
                                sendOrder = false;
                                String orderKleur = rs.getString("orderkleur");
                                String orderAantal = rs.getString("aantalblokjes");
                                arduinoConnectie.writeString(orderKleur + ":" + orderAantal);
                                arduinoConnectie.comPort.addDataListener(new SerialPortDataListener() {
                                @Override
                                public int getListeningEvents() {
                                    return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;

                                }

                                @Override
                                public void serialEvent(SerialPortEvent serialPortEvent) {
                                    if(arduinoConnectie.readString().equals("nextorder")){
                                        sendOrder = true;
                                    }
                                }
                            });

                        }

                    }

                }

            }catch (Exception x)
            {
                x.printStackTrace();
            }


        }

            // Stop de robots
        if (e.getSource() == stopRobotJB) {
            // stop beide robots

            JOptionPane.showMessageDialog(this, "De robots worden gestopt.");
        }
    }


    public static int getSchermBreedte() {
        return schermBreedte;
    }
    public static int getSchermHoogte() {
        return schermHoogte;
    }
}
