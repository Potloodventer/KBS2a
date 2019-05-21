import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;


public class HoofdschermGUI extends JFrame implements ActionListener { // Klasse voor het hoofdscherm.


    private static int schermBreedte = 1100, schermHoogte = 800; // Bepaal breedte en hoogte van scherm.

    // Buttons.
    private JButton orderInladenJB;
    private JButton alleProductenJB;
    private JButton startRobotJB;
    private JButton statusRobotJB;

    // Schermen die nodig zijn.
    private HMIStatusGUI hmiStatusGUI;
    private VoorraadGUI voorraadGUI;
    private OrderInladenGUI orderInladenGUI;
    private BPPResultaatGUI bppResultaatGUI;

    // Database connectie en Arduino connecties.
    private DatabaseHelper databaseHelper;
    private ArduinoConnectie arduinoConnectie;
    private ArduinoConnectie arduinoConnectie2;


    private ArrayList<String> orderNummers; // ArrayList met ordernummers van temporders uit de database.
    private int aantalRows; // Aantal rows van temporders uit de database.
    private int geteld;
    private boolean tellen;

    // Arduinoconnectie variablen
    private StringBuilder stringBuilder;
    private String msg;
    private String msg2;

    // Aantal blokes die de telsensor optelt.
    private int aantalRood;
    private int aantalGeel;
    private int aantalGroen;


    public HoofdschermGUI() {

        // Layout opties
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(schermBreedte / 2, schermHoogte / 2);
        setTitle("HMI Hoofdscherm");

        orderNummers = new ArrayList<>(); // ArrayList voor de ordernummers.
        // Open de database connectie in constructor.
        databaseHelper = new DatabaseHelper();
        databaseHelper.openConnection();

        //arduinoConnectie = new ArduinoConnectie(9600, 1);

        // Initialiseer buttons.
        orderInladenJB = new JButton("Order inladen");
        alleProductenJB = new JButton("Pas voorraad aan");
        startRobotJB = new JButton("Start robots");
        statusRobotJB = new JButton("Status robots");

        // Button opties.
        statusRobotJB.setPreferredSize(new Dimension(160, 30));
        orderInladenJB.setPreferredSize(new Dimension(160, 30));
        alleProductenJB.setPreferredSize(new Dimension(160, 30));
        startRobotJB.setPreferredSize(new Dimension(160, 30));

        // Open arduinoconnecties in de constructor
        arduinoConnectie = new ArduinoConnectie(9600, 1);
        arduinoConnectie2 = new ArduinoConnectie(9600, 0);



        // Actionlisteners voor de buttons.
        orderInladenJB.addActionListener(this);
        alleProductenJB.addActionListener(this);
        startRobotJB.addActionListener(this);
        statusRobotJB.addActionListener(this);


        add(Box.createRigidArea(new Dimension(schermBreedte / 2, 35)));  // lege Box voor de indeling
        add(orderInladenJB);
        add(Box.createRigidArea(new Dimension(schermBreedte / 2, 20)));
        add(alleProductenJB);
        add(Box.createRigidArea(new Dimension(schermBreedte / 2, 20)));
        add(startRobotJB);
        add(Box.createRigidArea(new Dimension(schermBreedte / 2, 20)));
        add(statusRobotJB);


        String SQL = "SELECT * FROM temporders"; // Query om de rows te tellen van temporders.
        ResultSet rs = databaseHelper.selectQuery(SQL);
        try {
            rs.last();
            aantalRows = rs.getRow(); // Aantal wordt opgeslagen in aantalRows variable.
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

    public static int getSchermBreedte() { // Getter voor de schermbreedte.
        return schermBreedte;
    }

    public static int getSchermHoogte() { // Getter voor de schermhoogte.
        return schermHoogte;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == orderInladenJB) {
            // laad de order?
            this.setVisible(false);
            orderInladenGUI = new OrderInladenGUI();
            orderInladenGUI.setVisible(true);
            databaseHelper.closeConnection();
        }

        // laat de visuele weergave van de robot zien
        // sluit het homescherm


        // Laat pagina met alle producten zien
        if (e.getSource() == alleProductenJB) {
            this.setVisible(false);
            voorraadGUI = new VoorraadGUI();
            voorraadGUI.setVisible(true);
        }
        if(e.getSource() == statusRobotJB){
            hmiStatusGUI = new HMIStatusGUI(arduinoConnectie, arduinoConnectie2);
            hmiStatusGUI.setVisible(true);
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
                hmiStatusGUI = new HMIStatusGUI(arduinoConnectie, arduinoConnectie2);
                hmiStatusGUI.setVisible(true);
                arduinoConnectie2.writeString("start");

                JOptionPane.showMessageDialog(this, "Robots zijn gestart met " + aantalRows + " orders.");
                // Start de robots
                // Stuur aantal blokjes en kleur per order naar robots
                sendOrderToArduino(0);
                geteld = 1;

                arduinoConnectie.comPort.addDataListener(new SerialPortDataListener() { // Listener voor sorteerrobot.
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

                        stringBuilder = new StringBuilder(); // Maak een stringbuilder
                        for (int i = 0; i < newData.length; ++i) {
                            System.out.print((char) newData[i]);
                            stringBuilder.append((char) newData[i]); // Voeg char voor char toe aan stringbuilder.

                        }
                        msg = stringBuilder.toString(); // Maak er een string van zodat het leesbaar is.

                        // ifs om te lezen uit de arduino.
                        if (msg.startsWith("k")) { // Als k wordt gestuurd, stuurt de applicatie de volgende order.
                            tellen = true; // Tellen gaat op true.
                            hmiStatusGUI.setTelSensorKleur(null); // Telsensor kleur wordt zwart

                        } else if (msg.startsWith("q")) { // Als q gestuurd wordt, dan is er een blokje langs telsensor gekomen en wordt de sensor eventjes paars gemaakt.
                            hmiStatusGUI.setPaars(true);
                            if (hmiStatusGUI.getPaars()) {
                                hmiStatusGUI.setTelSensorKleur("paars");
                            } else {
                                hmiStatusGUI.setTelSensorKleur(null);
                            }
                        } else if (msg.startsWith("j")) { // rood
                            hmiStatusGUI.setNummer1(2);
                            hmiStatusGUI.setNummer2(3);
                        } else if (msg.startsWith("b")) { // geel
                            hmiStatusGUI.setNummer1(2);
                            hmiStatusGUI.setNummer2(2);
                        } else if (msg.startsWith("f")) { // groen
                            hmiStatusGUI.setNummer1(2);
                            hmiStatusGUI.setNummer2(1);
                        } else if (msg.startsWith("a")) { // fout
                            hmiStatusGUI.setNummer1(1);
                        }

                        if (msg.startsWith("m")) { // Als m gestuurd wordt dan is er een rood blokje geteld en wordt een label geupdate in de live status.
                            aantalRood = hmiStatusGUI.getAantalRood();
                            hmiStatusGUI.setAantalRood(aantalRood + 1);
                            hmiStatusGUI.setTelSensorKleur(null);
                            System.out.println(hmiStatusGUI.getAantalRood());
                        } else if (msg.startsWith("p")) { // Als p gestuurd wordt dan is er een geel blokje geteld en wordt een label geupdate in de live status.
                            aantalGeel = hmiStatusGUI.getAantalGeel();
                            hmiStatusGUI.setAantalGeel(aantalGeel + 1);
                            hmiStatusGUI.setTelSensorKleur(null);

                        } else if (msg.startsWith("v")) { // Als v gestuurd wordt dan is er een groen blokje geteld en wordt er een label geupdate.
                            aantalGroen = hmiStatusGUI.getAantalGroen();
                            hmiStatusGUI.setAantalGroen(aantalGroen + 1);
                            hmiStatusGUI.setTelSensorKleur(null);
                        }

                        if (tellen) { // Als dit true is wordt er een nieuwe order gestuurd naar de arduino.
                            for (int x = 0; x < aantalRows; x++) {
                                sendOrderToArduino(geteld);
                                geteld++;
                                tellen = false;
                                break;
                            }
                        }

                        arduinoConnectie2.comPort.addDataListener(new SerialPortDataListener() { // Luistert naar de serial van de kleurrobot
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
                                // Hetzelfde als de eerste listener.
                                stringBuilder = new StringBuilder();
                                for (int i = 0; i < newData.length; ++i) {
                                    System.out.print((char) newData[i]);
                                    stringBuilder.append((char) newData[i]);

                                }

                                msg2 = stringBuilder.toString();

                                if (msg2.startsWith("z")) { // Als kleursensor z stuurt dan is er een rood blokje gezien en wordt er zrood geprint naar de sorteerrobot
                                    arduinoConnectie.writeString("zrood");
                                    hmiStatusGUI.setKleur("rood");

                                } else if(msg2.startsWith("y")){ // Hetzelfde als de bovenstaande if
                                    arduinoConnectie.writeString("ygroen");
                                    hmiStatusGUI.setKleur("groen");

                                } else if(msg2.startsWith("x")){ // Hetzelfde als de bovenstaande if
                                    arduinoConnectie.writeString("xgeel");
                                    hmiStatusGUI.setKleur("geel");

                                }



                            }
                        });

                    }
                });
                this.dispose();
            }
        }
    }


    public void sendOrderToArduino(int i) { // Functie om een order naar de arduino te sturen van de ingeladen orders uit de database.

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