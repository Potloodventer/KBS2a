import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Random;

public class BPPResultaatGUI extends JFrame implements ActionListener {

    private HMIStatusGUI hmiStatusGUI;
    private TekenPanelBPPResultaat tekenPanelBPPResultaat;
    private DatabaseHelper databaseHelper;

    private JLabel jlRood, jlGroen, jlGeel;
    private JLabel jlDoos1, jlDoos2, jlDoos3, jlDoos4, jlDoos5, jlDoos6, jlDoos7;
    private JButton jbPakbon;

    private int d1, d2, d3, d4, d5, d6, d7;
    private int yPlus = 0;
    private int roodY1 = 180, groenY1 = 330, geelY1 = 480; // Onderin rode doos
    private int roodX1 = 102, groenX1 = 102, geelX1 = 102; // helemaal links in 1e doos

    private ArrayList<Integer> productenRood = new ArrayList<>();
    private ArrayList<Integer> productenGroen = new ArrayList<>();
    private ArrayList<Integer> productenGeel =  new ArrayList<>(); // Array voor de random groottes per order (kleur)

    private ArrayList<Integer> gesorteerdRood = new ArrayList<>();
    private ArrayList<Integer> gesorteerdGroen = new ArrayList<>();
    private ArrayList<Integer> gesorteerdGeel = new ArrayList<>();

    public BPPResultaatGUI(HMIStatusGUI hmiStatusGUI) {
        this.hmiStatusGUI = hmiStatusGUI;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(HoofdschermGUI.getSchermBreedte(), HoofdschermGUI.getSchermHoogte());
        setTitle("BPP Resultaat");

        jlRood  = new JLabel("Rood: "  + hmiStatusGUI.getAantal("rood"));
        jlGroen = new JLabel("Groen: " + hmiStatusGUI.getAantal("groen"));
        jlGeel  = new JLabel("Geel: "  + hmiStatusGUI.getAantal("geel"));

        jlDoos1 = new JLabel("Doos 1: " + d1);
        jlDoos2 = new JLabel("Doos 2: " + d2);
        jlDoos3 = new JLabel("Doos 3: " + d3);
        jlDoos4 = new JLabel("Doos 4: " + d4);
        jlDoos5 = new JLabel("Doos 5: " + d5);
        jlDoos6 = new JLabel("Doos 6: " + d6);
        jlDoos7 = new JLabel("Doos 7: " + d7);

        tekenPanelBPPResultaat = new TekenPanelBPPResultaat(this);

//        tekenPanelBPPResultaat.add(jlDoos1);
//        tekenPanelBPPResultaat.add(jlDoos2);
//        tekenPanelBPPResultaat.add(jlDoos3);
//        tekenPanelBPPResultaat.add(jlDoos4);
//        tekenPanelBPPResultaat.add(jlDoos5);
//        tekenPanelBPPResultaat.add(jlDoos6);
//        tekenPanelBPPResultaat.add(jlDoos7);
        //tekenPanelBPPResultaat.add(Box.createRigidArea(new Dimension(100, 100)));

        tekenPanelBPPResultaat.add(jlRood);
        tekenPanelBPPResultaat.add(Box.createRigidArea(new Dimension(800, 200)));
        tekenPanelBPPResultaat.add(jlGroen);
        tekenPanelBPPResultaat.add(Box.createRigidArea(new Dimension(800, 90)));
        tekenPanelBPPResultaat.add(jlGeel);
        tekenPanelBPPResultaat.add(Box.createRigidArea(new Dimension(800, 200)));

        jbPakbon = new JButton("Pakbon");
        jbPakbon.addActionListener(this);

        // voeg dit allemaal toe op het scherm
        add(jbPakbon);
        add(tekenPanelBPPResultaat);


        setVisible(false);
    }

    // BPP probleem oplossing:
    // Eerst alle grootste producten (4) in dozen doen
    // Dan kijken of de som van 2 of 3 producten hier bij in past.
    // Als het past, kijken we welke de doos zo vol mogelijk maakt (dichtst bij 8)
    // Daarna kijken we of de som van 2 - 3 (4) producten een doos kan vullen, zo niet welke het dichtst bij de 8 komt
    // Zo moeten alle producten uiteindelijk in de dozen zijn geplaatst

    // Random nummer generator maken met getallen tussen x & x  (2, 3, 4)  /////////// af
    // Aantal producten vanuit Temporder tabel sorteren op kleur     ////////////////  af
    // Dit product aan een nummer koppelen                ////////////////// af
    // sorteer lijst van groot naar klein                 ////////////////// af


    // maak een random nummer 2,3 of 4
    public int generateNumber() {
        Random rand = new Random();
        int randomGetal = rand.nextInt(3) + 2; // 3 zorgt dat er 3 getallen 0, 1, 2 gemaakt worden,
        // + 2 maakt dit 2, 3, 4
        return randomGetal;
    }

    //Pakt de aantal producten in de order van een bepaalde kleur
    public int getaantallen(String kleur) {
        databaseHelper = new DatabaseHelper();
        databaseHelper.openConnection();
        int aantalblokjes = 0;
        try {
            //Pakt het aantal blokjes voor de gevraagde kleur.
            String SQL = String.format("Select aantalblokjes From temporders where orderkleur ='%s'", kleur);
            ResultSet rs = databaseHelper.selectQuery(SQL);
            if (rs.next()) {
                //Pakt het aantal blokjes die met de sql query zijn gevraagd
                aantalblokjes = Integer.parseInt(rs.getString("aantalblokjes"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return aantalblokjes;
    }

    // Genereer random nummers voor het aantal producten per kleur en stop deze in de productarray
    public void haalAantalProducten() {
        // Haal het aantal blokjes per kleur op uit de database

        int aantalRood = 14;  // Hoger dan 15 - 16 kan error geven omdat er maar 7 bakken zijn (array error)
        System.out.println("Rood: " + aantalRood);
        int aantalGroen = 2;
        System.out.println("Groen: " + aantalGroen);
        int aantalGeel = 3;
        System.out.println("Geel: " + aantalGeel);
        //int aantalRood = getaantallen("rood");
        //int aantalGroen = getaantallen("groen");
        //int aantalGeel = getaantallen("geel");

        for (int i = 0; i < aantalRood; i++) {
            productenRood.add(generateNumber());
        }
        for (int i = 0; i < aantalGroen; i++) {
            productenGroen.add(generateNumber());
        }
        for (int i = 0; i < aantalGeel; i++) {
            productenGeel.add(generateNumber());
        }
    }

    public void sorteerBlokjes() {
        haalAantalProducten(); // Genereert random nummers voor aantal producten per kleur en vult arrays

        //3 vershillende waardes. Voor elke kleur array index.
        int highestNumber = 0;

        //Loopen door de array en het hoogste nummer er uit pakken.//-- Array voor de Rode Blokjes --\\
        for (Integer a : productenRood) {
            if (a > highestNumber) {//Bepaal de hoogste waarde in de array
                highestNumber = a;
            }
        }
        //Gesorteerde array
        for (Integer a : productenRood) {
            if (a == highestNumber) {//Pak de nummers die het hoogst zijn en zet die voorin de array
                gesorteerdRood.add(a);
            }
        }
        for (Integer a : productenRood) {
            if (a == highestNumber - 1) {//Pak alle getallen die 1 lager zijn dan het hoogste getal.
                gesorteerdRood.add(a);
            }
        }
        for (Integer a : productenRood) {
            if (a == highestNumber - 2) {//Pak alle getallen die 2 lager zijn dan het hoogste getal.
                gesorteerdRood.add(a);
            }
        }
        //-- Array voor de GROENE Blokjes --\\
        for (Integer b : productenGroen) {
            if (b > highestNumber) {//Bepaal de hoogste waarde in de array
                highestNumber = b;
            }
        }
        //Gesorteerde array
        for (Integer b : productenGroen) {
            if (b == highestNumber) {//Pak de nummers die het hoogst zijn en zet die voor in de array
                gesorteerdGroen.add(b);
            }
        }
        for (Integer b : productenGroen) {
            if (b == highestNumber - 1) {//Pak alle getallen die 1 lager zijn dan het hoogste getal.
                gesorteerdGroen.add(b);
            }
        }
        for (Integer b : productenGroen) {
            if (b == highestNumber - 2) {//Pak alle getallen die 2 lager zijn dan het hoogste getal.
                gesorteerdGroen.add(b);
            }
        }
        //-- Array voor de GELE blokjes --\\
        for (Integer c : gesorteerdGeel) {
            if (c > highestNumber) { //Bepaal de hoogste waarde in de array
                highestNumber = c;
            }
        }
        //Gesorteerde array
        for (Integer c : gesorteerdGeel) {
            if (c == highestNumber) {//Pak de nummers die het hoogst zijn en zet die voor in de array
                gesorteerdGeel.add(c);
            }
        }
        for (Integer c : gesorteerdGeel) {
            if (c == highestNumber - 1) {//Pak alle getallen die 1 lager zijn dan het hoogste getal.
                gesorteerdGeel.add(c);
            }
        }
        for (Integer c : gesorteerdGeel) {
            if (c == highestNumber - 2) {//Pak alle getallen die 2 lager zijn dan het hoogste getal.
                gesorteerdGeel.add(c);
            }
        }
    }

    //Testen van arraywaardes
    public void printArrays() {
        sorteerBlokjes(); // Zet de groottes van groot naar klein

        System.out.println("ROOD: ");
        for (Integer a : productenRood) {
            if (a != 0) {
                System.out.println(a);
            }
        }
        System.out.println("GROEN: ");
        for (int a : productenGroen) {
            if (a != 0) {
                System.out.println(a);
            }
        }
        System.out.println("GEEL: ");
        for (int a : productenGeel) {
            if (a != 0) {
                System.out.println(a);
            }
        }
    }

    // Teken de dozen voor BPP resultaat
    public void drawBins(Graphics g) {

        for (int j = 2; j < 7; j += 2) {
            for (int i = 1; i < 8; i++) {

                int x1 = 100 * i;
                int x2 = 100 * i;

                int y1 = 50 * j + yPlus;
                int y2 = 50 * j + yPlus + 80;

                g.drawLine(x1, y1, x2, y2);                  // linker lijn
                g.drawLine(x1, y2, x2 + 80, y2);         // onderste lijn
                g.drawLine(x2 + 80, y1, x1 + 80, y2); // rechter lijn
            }
            yPlus += 50;
        }
    }

    int width = 77;
    int rDoos = 0;
    int xYHeightIndex = 0; // Voor rood
    int [][] xYHeight = { {0, 0, 0, 0, 0, 0, 0}, // X waardes = Array 0
                          {0, 0, 0, 0, 0, 0, 0}, // Y waardes = Array 1
                          {0, 0, 0, 0, 0, 0, 0} }; // Height waardes = Array 2

    // BPP algoritme
    public void decideBestFit(String kleur) {
        sorteerBlokjes();
        // ZET DE arrays om te printen in de juiste volgorde T.O.V. BPP oplossing
        if (kleur.equals("rood")) {

            for (Integer a : gesorteerdRood) {

                if (rDoos + a > 8) { // Als doos te vol zou worden
                    // Verplaats dan de waardes naar volgende doos
                    roodX1 += 100; // ga naar volgende doos (naar rechts)
                    roodY1 = 180;  // en begin onderin (onderkant rode doos)
                    rDoos = a;
                    roodY1 -= rDoos * 10; // zorg dat de Y waarde op zelfde hoogte begint als hoogte
                    xYHeightIndex++; // Ga naar volgende doos

                    xYHeight[0][xYHeightIndex] = roodX1;
                    xYHeight[1][xYHeightIndex] = roodY1;
                    xYHeight[2][xYHeightIndex] = rDoos * 10;
                } else {
                    if (a != 0) {
                        rDoos += a;
                        roodY1 = 180;
                        roodY1 -= rDoos * 10; // Zorgt dat die vanaf de juiste hoogte getekend wordt.

                        xYHeight[0][xYHeightIndex] = roodX1;
                        xYHeight[1][xYHeightIndex] = roodY1;
                        xYHeight[2][xYHeightIndex] = rDoos * 10;
                    }
                }
            }



            // loop door de rode array en kijk of er een oneven aantal van 4 is

            // Daarna, aparte if / else kijken of het op 1 na hoogste getal de doos op kan vullen
            // Als dat niet mogelijk is het op 2 na hoogste getal. Wel mogelijk && dichtst bij 8 --> stop het in de dozen
            // Ook op 2 na hoogste getal *2 + hoogste getal

            // plaats alle vieren in de dozen en kijk of de som van 1 of 2 andere blokjes het opvult tot 8,
            // zo niet gebruik degene die het dichtst bij 8 komt
        }
    }




    // HaalproductenOp() --- Pak hiervan het hoogste nummer
    // Als je het hoogste nummer heb print die dan elke keer in de doos tot het juiste aantal is bereikt
    // Als dat nummer een oneven aantal heeft 3 x 4 bv. dan moet er bij die laatste 4 een ander getal in de doos
    // Hierna moeten alle op 1 na hoogste getallen geprint worden.
    // Nu voor elke doos kijken of het laagste getal er nog bij in past.

    // Teken de producten in de dozen ///// blokGrootte moet getal tussen 1 en 4 zijn
    public void drawProducts(Graphics g, String kleur) {

        decideBestFit("rood");
        // groen
        // geel
        if (kleur.equals("rood")) { // kan ws weg, alles in 1x printen 3 verschillende arrays
            // Paint de arrays (3, per kleur)

            g.setColor(Color.RED);
            // For-loop hierom heen om te printen
            for (int i = 0; i < xYHeight[0].length; i++) {
                g.fillRect(xYHeight[0][i], xYHeight[1][i], width, xYHeight[2][i]);

                //System.out.println("PAINTEN X waarde: " + xYHeight[0][i]);
                //System.out.println("PAINTEN Y waarde: " + xYHeight[1][i]);
                //System.out.println("PAINTEN HEIGHT waarde: " + xYHeight[2][i]);
            }
            //g.fillRect(200, 200, 200, 200);
        } // EIND if kleur.equals("ROOD") /////////////////
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbPakbon) {
            this.dispose();
            new PakbonGUI().setVisible(true);
        }
    }
}