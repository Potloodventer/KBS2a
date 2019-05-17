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

        jlRood  = new JLabel("Rood: ");
        jlGroen = new JLabel("Groen: " );
        jlGeel  = new JLabel("Geel: ");

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
    // Dan kijken of de som van 2 andere producten hier bij in past. (en op 1 na grootste of die m vult)
    // Als het past, kijken we welke de doos zo vol mogelijk maakt (dichtst bij 8)
    // Daarna kijken we of de som van 2 - 3 producten een doos kan vullen, zo niet welke het dichtst bij de 8 komt
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
    public void haalAantalProducten(String kleur) {

        if (kleur.equals("rood")) {
            //int aantalRood = getaantallen("rood"); // Haal het aantal blokjes per kleur op uit de database
            int aantalRood = 10;  // Hoger dan 15 - 16 kan error geven omdat er maar 7 bakken zijn (array error)
            for (int i = 0; i < aantalRood; i++) {
                productenRood.add(generateNumber());
            }
        } else if (kleur.equals("groen")) {
            int aantalGroen = 10;
            //int aantalGroen = getaantallen("groen");
            for (int i = 0; i < aantalGroen; i++) {
                productenGroen.add(generateNumber());
            }
        } else if (kleur.equals("geel")) {
            int aantalGeel = 10;
            //int aantalGeel = getaantallen("geel");
            for (int i = 0; i < aantalGeel; i++) {
                productenGeel.add(generateNumber());
            }
        }


    }

    int highestNumberRood = 0;
    int highestNumberGroen = 0;
    int highestNumberGeel = 0;

    public void sorteerBlokjes(String kleur) {
        haalAantalProducten(kleur); // Genereert random nummers voor aantal producten per kleur en vult arrays

        //3 vershillende waardes. Voor elke kleur array index.


        if (kleur.equals("rood")) {
            //Loopen door de array en het hoogste nummer er uit pakken.//-- Array voor de Rode Blokjes --\\
            for (Integer a : productenRood) {
                if (a > highestNumberRood) {//Bepaal de hoogste waarde in de array
                    highestNumberRood = a;
                }
            }
            //Gesorteerde array
            for (Integer a : productenRood) {
                if (a == highestNumberRood) {//Pak de nummers die het hoogst zijn en zet die voorin de array
                    gesorteerdRood.add(a);
                }
            }
            for (Integer a : productenRood) {
                if (a == highestNumberRood - 1) {//Pak alle getallen die 1 lager zijn dan het hoogste getal.
                    gesorteerdRood.add(a);
                }
            }
            for (Integer a : productenRood) {
                if (a == highestNumberRood - 2) {//Pak alle getallen die 2 lager zijn dan het hoogste getal.
                    gesorteerdRood.add(a);
                }
            }
        } else if (kleur.equals("groen")) {
            //-- Array voor de GROENE Blokjes --\\
            for (Integer b : productenGroen) {
                if (b > highestNumberGroen) {//Bepaal de hoogste waarde in de array
                    highestNumberGroen = b;
                }
            }
            //Gesorteerde array
            for (Integer b : productenGroen) {
                if (b == highestNumberGroen) {//Pak de nummers die het hoogst zijn en zet die voor in de array
                    gesorteerdGroen.add(b);
                }
            }
            for (Integer b : productenGroen) {
                if (b == highestNumberGroen - 1) {//Pak alle getallen die 1 lager zijn dan het hoogste getal.
                    gesorteerdGroen.add(b);
                }
            }
            for (Integer b : productenGroen) {
                if (b == highestNumberGroen - 2) {//Pak alle getallen die 2 lager zijn dan het hoogste getal.
                    gesorteerdGroen.add(b);
                }
            }
        } else if (kleur.equals("geel")) {
            //-- Array voor de GELE blokjes --\\
            for (Integer c : productenGeel) {
                if (c > highestNumberGeel) { //Bepaal de hoogste waarde in de array
                    highestNumberGeel = c;
                }
            }
            //Gesorteerde array
            for (Integer c : productenGeel) {
                if (c == highestNumberGeel) {//Pak de nummers die het hoogst zijn en zet die voor in de array
                    gesorteerdGeel.add(c);
                }
            }
            for (Integer c : productenGeel) {
                if (c == highestNumberGeel - 1) {//Pak alle getallen die 1 lager zijn dan het hoogste getal.
                    gesorteerdGeel.add(c);
                }
            }
            for (Integer c : productenGeel) {
                if (c == highestNumberGeel - 2) {//Pak alle getallen die 2 lager zijn dan het hoogste getal.
                    gesorteerdGeel.add(c);
                }
            }

//            // testen waardes
//            for (int a : gesorteerdGeel) {
//                System.out.println(a);
//            }
        }
    }

    //Testen van arraywaardes
    public void printArrays() {

        System.out.println("ROOD: ");
        for (int a : gesorteerdRood) {
            if (a != 0) {
                System.out.println(a);
            }
        }
        System.out.println("GROEN: ");
        for (int a : gesorteerdGroen) {
            if (a != 0) {
                System.out.println(a);
            }
        }
        System.out.println("GEEL: ");
        for (int a : gesorteerdGeel) {
            if (a != 0) {
                System.out.println(a);
            }
        }

        for (int i = 0; i < 7; i++) {
            if (xYHeightRood[3][i] != 0) {
                System.out.println("Doos " + i + ": " + xYHeightRood[3][i]);
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

    // Alles voor decideBestFit
    int width = 77;
    int rDoos = 0; // rode dozen
    int grDoos = 0; // groene dozen
    int geDoos = 0; // gele dozen
    int xYHeightIndexRood = 0; // Voor rood
    int xYHeightIndexGroen = 0; // groen
    int xYHeightIndexGeel = 0; // geel
    int [][] xYHeightRood = { {0, 0, 0, 0, 0, 0, 0}, // X waardes = Array 0
                              {0, 0, 0, 0, 0, 0, 0}, // Y waardes = Array 1
                              {0, 0, 0, 0, 0, 0, 0}, // Height waardes = Array 2
                              {0, 0, 0, 0, 0, 0, 0} }; // gevulde waarde per doos

    int [][] xYHeightGroen ={ {0, 0, 0, 0, 0, 0, 0}, // X waardes = Array 0
                              {0, 0, 0, 0, 0, 0, 0}, // Y waardes = Array 1
                              {0, 0, 0, 0, 0, 0, 0}, // Height waardes = Array 2
                              {0, 0, 0, 0, 0, 0, 0} }; // gevulde waarde per doos

    int [][] xYHeightGeel = { {0, 0, 0, 0, 0, 0, 0}, // X waardes = Array 0
                              {0, 0, 0, 0, 0, 0, 0}, // Y waardes = Array 1
                              {0, 0, 0, 0, 0, 0, 0}, // Height waardes = Array 2
                              {0, 0, 0, 0, 0, 0, 0} }; // gevulde waarde per doos

    // BPP algoritme

    public void vulDozen(String kleur, int aantal) { // Geef bij aantal highestNumber, -1 of -2 mee

        if (kleur.equals("rood") ) {
            for (Integer a : gesorteerdRood) {
                if (a == aantal) {
                    if ((rDoos + a) > 8) { // Als doos te vol zou worden
                        // Verplaats dan de waardes naar volgende doos
                        roodX1 += 100; // ga naar volgende doos (naar rechts)
                        roodY1 = 180;  // en begin onderin (onderkant rode doos)
                        rDoos = a;
                        roodY1 -= rDoos * 10; // zorg dat de Y waarde op zelfde hoogte begint als hoogte
                        xYHeightIndexRood++; // Ga naar volgende doos

                        xYHeightRood[0][xYHeightIndexRood] = roodX1;
                        xYHeightRood[1][xYHeightIndexRood] = roodY1;
                        xYHeightRood[2][xYHeightIndexRood] = rDoos * 10;
                        xYHeightRood[3][xYHeightIndexRood] = rDoos;

                        //a = 0;
                    } else {
                        rDoos += a;
                        roodY1 = 180;
                        roodY1 -= rDoos * 10; // Zorgt dat die vanaf de juiste hoogte getekend wordt.

                        xYHeightRood[0][xYHeightIndexRood] = roodX1;
                        xYHeightRood[1][xYHeightIndexRood] = roodY1;
                        xYHeightRood[2][xYHeightIndexRood] = rDoos * 10;
                        xYHeightRood[3][xYHeightIndexRood] = rDoos;

                        //a = 0;
                    }
                }
            }
        } else if (kleur.equals("groen")) {
            for (Integer a : gesorteerdGroen) {
                if (a == aantal) {
                    if (grDoos + a > 8) { // Als doos te vol zou worden
                        // Verplaats dan de waardes naar volgende doos
                        groenX1 += 100; // ga naar volgende doos (naar rechts)
                        groenY1 = 330;  // en begin onderin (onderkant rode doos)
                        grDoos = a;
                        System.out.println("Grootte doos groen: " + grDoos);
                        groenY1 -= grDoos * 10; // zorg dat de Y waarde op zelfde hoogte begint als hoogte
                        xYHeightIndexGroen++; // Ga naar volgende doos

                        xYHeightGroen[0][xYHeightIndexGroen] = groenX1;
                        xYHeightGroen[1][xYHeightIndexGroen] = groenY1;
                        xYHeightGroen[2][xYHeightIndexGroen] = grDoos * 10;
                        xYHeightGroen[3][xYHeightIndexGroen] = grDoos;

                        a = 0;
                    } else {
                        grDoos += a;
                        groenY1 = 330;
                        groenY1 -= grDoos * 10; // Zorgt dat die vanaf de juiste hoogte getekend wordt.

                        xYHeightGroen[0][xYHeightIndexGroen] = groenX1;
                        xYHeightGroen[1][xYHeightIndexGroen] = groenY1;
                        xYHeightGroen[2][xYHeightIndexGroen] = grDoos * 10;
                        xYHeightGroen[3][xYHeightIndexGroen] = grDoos;

                        a = 0;
                    }
                }
            }
        } else if (kleur.equals("geel")) {
            for (Integer a : gesorteerdGeel) {
                if (a == aantal) {
                    if (geDoos + a > 8) { // Als doos te vol zou worden
                        // Verplaats dan de waardes naar volgende doos
                        geelX1 += 100; // ga naar volgende doos (naar rechts)
                        geelY1 = 480;  // en begin onderin (onderkant rode doos)
                        geDoos = a;
                        geelY1 -= geDoos * 10; // zorg dat de Y waarde op zelfde hoogte begint als hoogte
                        xYHeightIndexGeel++; // Ga naar volgende doos

                        xYHeightGeel[0][xYHeightIndexGeel] = geelX1;
                        xYHeightGeel[1][xYHeightIndexGeel] = geelY1;
                        xYHeightGeel[2][xYHeightIndexGeel] = geDoos * 10;
                        xYHeightGeel[3][xYHeightIndexGeel] = geDoos;

                        a = 0;
                    } else {
                        geDoos += a;
                        geelY1 = 480;
                        geelY1 -= geDoos * 10; // Zorgt dat die vanaf de juiste hoogte getekend wordt.

                        xYHeightGeel[0][xYHeightIndexGeel] = geelX1;
                        xYHeightGeel[1][xYHeightIndexGeel] = geelY1;
                        xYHeightGeel[2][xYHeightIndexGeel] = geDoos * 10;
                        xYHeightGeel[3][xYHeightIndexGeel] = geDoos;

                        a = 0;
                    }
                }
            }
        }
    }

    public void decideBestFit(String kleur) {

        sorteerBlokjes(kleur);
        // ZET DE arrays om te printen in de juiste volgorde T.O.V. BPP oplossing
        if (kleur.equals("rood") ) {
            vulDozen("rood", highestNumberRood);

            // stop alle highestNumbers -1 in dozen waar er plek voor is
            for (int i = 0; i < xYHeightRood[3].length; i++) { // per doos kijken naar alle andere dozen
                if (xYHeightRood[3][i] != 8) { // als doos niet vol is
                    for (int a : gesorteerdRood) { // Alle waardes van gesorteerde array afgaan
                        if (a == (highestNumberRood - 1) ) {
                            if (xYHeightRood[3][i] <= 8 - (highestNumberRood - 1)) { // (3)
                                xYHeightRood[3][i] += (highestNumberRood - 1); // verander gevulde waarde doos
                                xYHeightRood[1][i] -= (highestNumberRood - 1) * 10; // verander Y waarde doos
                                xYHeightRood[2][i] += (highestNumberRood - 1) * 10; // verander getekende hoogte doos

                                //a = 0; // zorgt ervoor dat ditzelfde getal niet weer gepakt wordt (--, als het ware)
                            }
                        }
                    }
                }
            }
            // Stop alle overgebleven hoogstenummers -1 in nieuwe dozen
            vulDozen("rood", highestNumberRood -1);

            // stop alle hoogstenummers -2 in dozen waar er plek voor is
            for (int i = 0; i < xYHeightRood[3].length; i++) { // per doos kijken naar alle andere dozen
                if (xYHeightRood[3][i] != 8) {
                    for (int a : gesorteerdRood) { // Alle waardes van gesorteerde array afgaan
                         if (a == (highestNumberRood - 2) ){
                        if (xYHeightRood[3][i] <= 8 - (highestNumberRood - 2))  {
                            xYHeightRood[3][i] += (highestNumberRood - 2); // verander gevulde waarde doos
                            xYHeightRood[1][i] -= (highestNumberRood - 2) * 10; // verander Y waarde doos
                            xYHeightRood[2][i] += (highestNumberRood - 2) * 10; // verander getekende hoogte doos

                            //a = 0; // zorgt ervoor dat ditzelfde getal niet weer gepakt wordt (--, als het ware)
                        }
                        }
                    }
                }
            }
            // Stop alle overgebleven hoogstenummers -2 in nieuwe dozen
            vulDozen("rood", highestNumberRood -2);
        } else if (kleur.equals("groen") ) {
            vulDozen("groen", highestNumberGroen);

            // stop alle hoogstenummers -1 in dozen waar er plek voor is
            for (int i = 0; i < xYHeightGroen[3].length; i++) { // per doos kijken naar alle andere dozen
                if (xYHeightGroen[3][i] != 8) {
                    for (int a : gesorteerdGroen) { // Alle waardes van gesorteerde array afgaan
                        if (xYHeightGroen[3][i] <= 8 - (highestNumberGroen - 1) && a == (highestNumberGroen - 1) ) { // (3)
                            xYHeightGroen[3][i] += (highestNumberGroen - 1); // verander gevulde waarde doos
                            xYHeightGroen[1][i] -= (highestNumberGroen - 1) * 10; // verander Y waarde doos
                            xYHeightGroen[2][i] += (highestNumberGroen - 1) * 10; // verander getekende hoogte doos

                            a = 0; // zorgt ervoor dat ditzelfde getal niet weer gepakt wordt (--, als het ware)
                        }
                    }
                }
            }
            // Stop alle overgebleven hoogstenummers -1 in nieuwe dozen
            vulDozen("groen", highestNumberGroen -1);

            // stop alle hoogstenummers -2 in dozen waar er plek voor is
            for (int i = 0; i < xYHeightGroen[3].length; i++) { // per doos kijken naar alle andere dozen
                if (xYHeightGroen[3][i] != 8) {
                    for (int a : gesorteerdGroen) { // Alle waardes van gesorteerde array afgaan
                        if (xYHeightGroen[3][i] <= 8 - (highestNumberGroen - 2) && a == (highestNumberGroen - 2) ) {
                            xYHeightGroen[3][i] += (highestNumberGroen - 2); // verander gevulde waarde doos
                            xYHeightGroen[1][i] -= (highestNumberGroen - 2) * 10; // verander Y waarde doos
                            xYHeightGroen[2][i] += (highestNumberGroen - 2) * 10; // verander getekende hoogte doos

                            a = 0; // zorgt ervoor dat ditzelfde getal niet weer gepakt wordt (--, als het ware)
                        }
                    }
                }
            }
            // Stop alle overgebleven hoogstenummers -2 in nieuwe dozen
            vulDozen("groen", highestNumberGroen -2);
        } else if (kleur.equals("geel") ) {
            vulDozen("geel", highestNumberGeel);

            // stop alle hoogstenummers -1 in dozen waar er plek voor is
            for (int i = 0; i < xYHeightGeel[3].length; i++) { // per doos kijken naar alle andere dozen
                if (xYHeightGeel[3][i] != 8) {
                    for (int a : gesorteerdGeel) { // Alle waardes van gesorteerde array afgaan
                        if (xYHeightGeel[3][i] <= 8 - (highestNumberGeel - 1) && a == (highestNumberGeel - 1) ) {
                            xYHeightGeel[3][i] += (highestNumberGeel - 1); // verander gevulde waarde doos
                            xYHeightGeel[1][i] -= (highestNumberGeel - 1) * 10; // verander Y waarde doos
                            xYHeightGeel[2][i] += (highestNumberGeel - 1) * 10; // verander getekende hoogte doos

                            a = 0; // zorgt ervoor dat ditzelfde getal niet weer gepakt wordt (--, als het ware)
                        }
                    }
                }
            }
            // Stop alle overgebleven hoogstenummers -1 in nieuwe dozen
            vulDozen("geel", highestNumberGeel -1);

            // stop alle hoogstenummers -2 in dozen waar er plek voor is
            for (int i = 0; i < xYHeightGeel[3].length; i++) { // per doos kijken naar alle andere dozen
                if (xYHeightGeel[3][i] != 8) {
                    for (int a : gesorteerdGeel) { // Alle waardes van gesorteerde array afgaan
                        if (xYHeightGeel[3][i] <= 8 - (highestNumberGeel - 2) && a == (highestNumberGeel - 2) ) { // (3)
                            xYHeightGeel[3][i] += (highestNumberGeel - 2); // verander gevulde waarde doos
                            xYHeightGeel[1][i] -= (highestNumberGeel - 2) * 10; // verander Y waarde doos
                            xYHeightGeel[2][i] += (highestNumberGeel - 2) * 10; // verander getekende hoogte doos

                            a = 0; // zorgt ervoor dat ditzelfde getal niet weer gepakt wordt (--, als het ware)
                        }
                    }
                }
            }
            // Stop alle overgebleven hoogstenummers -2 in nieuwe dozen
            vulDozen("geel", highestNumberGeel -2);
        }
    }

    // Teken de producten in de dozen ///// blokGrootte moet getal tussen 1 en 4 zijn
    public void drawProducts(Graphics g) {

        decideBestFit("rood");
        decideBestFit("groen");
        decideBestFit("geel");
        //printArrays();
        for (int a : gesorteerdRood) {
            if (a != 0) {
                System.out.println("Rood : " + a);
            }
        }

        g.setColor(Color.RED);
        // For-loop hierom heen om te printen
        for (int i = 0; i < 7; i++) {
            g.fillRect(xYHeightRood[0][i], xYHeightRood[1][i], width, xYHeightRood[2][i]);
            System.out.println(i + " 0: " + xYHeightRood[0][i]);

            System.out.println(i + " 1: " + xYHeightRood[1][i]);

            System.out.println(i + " 2: " + xYHeightRood[2][i]);

            System.out.println(i + " 3: " + xYHeightRood[3][i]);
        }
        g.setColor(Color.GREEN);
        // For-loop hierom heen om te printen
        for (int i = 0; i < xYHeightGroen[0].length; i++) {
            g.fillRect(xYHeightGroen[0][i], xYHeightGroen[1][i], width, xYHeightGroen[2][i]);
        }
        g.setColor(Color.YELLOW);
        // For-loop hierom heen om te printen
        for (int i = 0; i < xYHeightGeel[0].length; i++) {
            g.fillRect(xYHeightGeel[0][i], xYHeightGeel[1][i], width, xYHeightGeel[2][i]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbPakbon) {
            this.dispose();
            new PakbonGUI().setVisible(true);
        }
    }
}