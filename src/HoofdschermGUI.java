import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;

// rechts 0, links 1

public class HoofdschermGUI extends JFrame implements ActionListener {

    private static int schermBreedte = 1100, schermHoogte = 800;

    private JButton orderInladenJB;
    private JButton alleProductenJB;
    private JButton startRobotJB;

    private HMIStatusGUI hmiStatusGUI;
    private VoorraadGUI voorraadGUI;
    private OrderInladenGUI orderInladenGUI;
    private DatabaseHelper databaseHelper;
    private ArduinoConnectie arduinoConnectie;
    private ArduinoConnectie arduinoConnectie2;

    private ArrayList<String> orderNummers;
    private int aantalRows;
    private int geteld;
    private boolean tellen;
    private StringBuilder stringBuilder;
    private String msg;
    private String msg2;
    private boolean startrobot2;

    public HoofdschermGUI() {

        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(schermBreedte / 2, schermHoogte / 2);
        setTitle("HMI Hoofdscherm");
        orderNummers = new ArrayList<>();
        databaseHelper = new DatabaseHelper();
        databaseHelper.openConnection();
        //arduinoConnectie = new ArduinoConnectie(9600, 1);

        orderInladenJB = new JButton("Order inladen");
        alleProductenJB = new JButton("Pas voorraad aan");
        startRobotJB = new JButton("Start robots");

        orderInladenJB.setPreferredSize(new Dimension(160, 30));
        alleProductenJB.setPreferredSize(new Dimension(160, 30));
        startRobotJB.setPreferredSize(new Dimension(160, 30));

        arduinoConnectie = new ArduinoConnectie(9600, 1);
        arduinoConnectie2 = new ArduinoConnectie(9600, 0);



        orderInladenJB.addActionListener(this);
        alleProductenJB.addActionListener(this);
        startRobotJB.addActionListener(this);

        add(Box.createRigidArea(new Dimension(schermBreedte / 2, 35)));  // lege Box voor de indeling
        add(orderInladenJB);
        add(Box.createRigidArea(new Dimension(schermBreedte / 2, 20)));
        add(alleProductenJB);
        add(Box.createRigidArea(new Dimension(schermBreedte / 2, 20)));
        add(startRobotJB);
        String SQL = "SELECT * FROM temporders";
        ResultSet rs = databaseHelper.selectQuery(SQL);
        try {
            rs.last();
            aantalRows = rs.getRow();
            rs.beforeFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ResultSet rs3 = databaseHelper.selectQuery("SELECT orderid, orderkleur FROM temporders"); // Query om alle order ids te pakken van temporders tabel
        for (int i = 0; i < aantalRows; i++) // Loop die ervoor zorgt dat alle order ids in een arraylist komen
        {
            try {
                if (rs3.next()) {
                    orderNummers.add(rs3.getString("orderid"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        setVisible(true);
    }

    public static int getSchermBreedte() {
        return schermBreedte;
    }

    public static int getSchermHoogte() {
        return schermHoogte;
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


        // Laat pagina met alle producten zien
        if (e.getSource() == alleProductenJB) {
            this.setVisible(false);
            voorraadGUI = new VoorraadGUI();
            voorraadGUI.setVisible(true);
        }

        // Start de robots
        if (e.getSource() == startRobotJB) {
            databaseHelper.openConnection();

            if (aantalRows < 1) {
                JOptionPane.showMessageDialog(this, "Je moet eerst 1 of meer orders inladen.");
                return;
            } else {
                arduinoConnectie.writeString("start");
                try{
                    Thread.sleep(1000);
                } catch (Exception x ){
                    x.printStackTrace();
                }

                arduinoConnectie2.writeString("start");

                JOptionPane.showMessageDialog(this, "Robots zijn gestart met " + aantalRows + " orders.");
                // Start de robots
                // Stuur aantal blokjes en kleur per order naar robots
                sendOrderToArduino(0);
                geteld = 1;
                arduinoConnectie.comPort.addDataListener(new SerialPortDataListener() {
                    @Override
                    public int getListeningEvents() {
                        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
                    }

                    public int getPacketSize() {
                        return 100;
                    }
                    //jannes
                    @Override
                    public void serialEvent(SerialPortEvent serialPortEvent) {
                        byte[] newData = serialPortEvent.getReceivedData();

                        stringBuilder = new StringBuilder();
                        for (int i = 0; i < newData.length; ++i) {
                            System.out.print((char) newData[i]);
                            stringBuilder.append((char) newData[i]);

                        }
                        msg = stringBuilder.toString();
                        if (msg.startsWith("n")) {
                            tellen = true;
                        }
                        if (tellen) {
                            for (int x = 0; x < aantalRows; x++) {
                                sendOrderToArduino(geteld);
                                geteld++;
                                tellen = false;
                                break;
                            }
                        }
                        arduinoConnectie2.comPort.addDataListener(new SerialPortDataListener() {
                            @Override
                            public int getListeningEvents() {
                                return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
                            }

                            public int getPacketSize() {
                                return 100;
                            }
                            //jannes
                            @Override
                            public void serialEvent(SerialPortEvent serialPortEvent) {
                                byte[] newData = serialPortEvent.getReceivedData();

                                stringBuilder = new StringBuilder();
                                for (int i = 0; i < newData.length; ++i) {
                                    System.out.print((char) newData[i]);
                                    stringBuilder.append((char) newData[i]);

                                }
                                msg2 = stringBuilder.toString();
                                if (msg2.startsWith("z")) {
                                    arduinoConnectie.writeString("zrood");
                                } else if(msg2.startsWith("y")){
                                    arduinoConnectie.writeString("ygroen");
                                } else if(msg2.startsWith("x")){
                                    arduinoConnectie.writeString("xgeel");
                                }

                            }
                        });

                    }
                });
                this.dispose();
                new HMIStatusGUI(arduinoConnectie, arduinoConnectie2).setVisible(true);

            }

        }


        // Stop de robots

    }

    public void sendOrderToArduino(int i) {
        try {
            String SQL2 = String.format("SELECT orderkleur, aantalblokjes FROM temporders WHERE orderid = %S", orderNummers.get(i));
            ResultSet rs2 = databaseHelper.selectQuery(SQL2);

            if (rs2.next()) {
                String orderKleur = rs2.getString("orderkleur");
                String orderAantal = rs2.getString("aantalblokjes");
                arduinoConnectie.writeString(orderKleur + ":" + orderAantal);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("geen orders meer te bekennen");
        }
    }
}
