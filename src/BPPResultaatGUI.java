import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Array;
import java.sql.ResultSet;
import java.util.Random;

public class BPPResultaatGUI extends JFrame implements ActionListener {

    private HMIStatusGUI hmiStatusGUI;
    private TekenPanelBPPResultaat tekenPanelBPPResultaat;
    private DatabaseHelper databaseHelper;

    private JLabel jlRood, jlGroen, jlGeel;
    private JLabel jlDoos1, jlDoos2, jlDoos3, jlDoos4, jlDoos5, jlDoos6, jlDoos7;

    private int d1, d2, d3, d4, d5, d6, d7;

    private int roodY1 = 160, groenY1 = 310, geelY1 = 460; // Onderin rode doos
    private int roodX1 = 105, groenX1 = 105, geelX1 = 105; // helemaal links in 1e doos

    private JButton jbPakbon;

    private int[] productenRood, productenGroen, productenGeel;//Array voor de blokjes
    private int[] gesorteerdRood, gesorteerdGroen, gesorteerdGeel;//Gesorteerde array van groot naar klein.

    private String aantalblokjes;

    private int yPlus = 0;

    public BPPResultaatGUI(HMIStatusGUI hmiStatusGUI) {
        this.hmiStatusGUI = hmiStatusGUI;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(HoofdschermGUI.getSchermBreedte(), HoofdschermGUI.getSchermHoogte());
        setTitle("BPP Resultaat");

        jlRood = new JLabel("Rood: " + hmiStatusGUI.getAantal("rood"));
        jlGroen = new JLabel("Groen: " + hmiStatusGUI.getAantal("groen"));
        jlGeel = new JLabel("Geel: " + hmiStatusGUI.getAantal("geel"));

        jlDoos1 = new JLabel("Doos 1: " + d1); // d1 krijgt de 2 - 4 groottes van producten mee
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
    // Aantal producten vanuit Temporder tabel sorteren op kleur
    // Dit product aan een nummer koppelen, Begin gewoon van ID = 0 die krijgt grootte generateNumber()
    // Zo doorgaan tot laatste product
    // sorteer lijst van groot naar klein


    // BPP algoritme
  /*  public void decideBestFit() {
        int vierCounter = 0;
        // loop door de rode array en kijk of er een oneven aantal van 4 is
        for (int i = 0; i < productenRood.length; i++) {
            if (productenRood[i] == 4) {
                vierCounter++;
            }
        }
        if (vierCounter % 2 == 0) { // als er een even aantal van vier is
            // plaats alle vieren in de dozen
        } else {
            // plaats alle vieren in de dozen en kijk of de som van 1 of 2 andere blokjes het opvult tot 8,
            // zo niet gebruik degene die het dichtst bij 8 komt
        }
    }   */


    // maak een random nummer 2,3 of 4
    public int generateNumber() {
        Random rand = new Random();
        int randomGetal = rand.nextInt(3) + 2; // 3 zorgt dat er 3 getallen 0, 1, 2 gemaakt worden,
        // + 2 maakt dit 2, 3, 4
        return randomGetal;
    }

    //Array maken voor elke kleur die we sorteren
    //pak alle items van een kleur die in order 1 zitte


    public void haalAantalProducten() { // Genereer random nummers voor het aantal producten per kleur.

        int aantalRood = Integer.parseInt(getaantallen("rood"));
        int aantalGroen = Integer.parseInt(getaantallen("groen"));
        int aantalGeel = Integer.parseInt(getaantallen("geel"));

        for (int i = 0; i < aantalRood; i++) {/*aantalblokjes = aantal opgehaalde aantal per kleur*/
            productenRood[i] = generateNumber();
        }
        for (int i = 0; i < aantalGroen; i++) {/*aantalblokjes = aantal opgehaalde aantal per kleur*/
            productenGroen[i] = generateNumber();
        }
        for (int i = 0; i < aantalGeel; i++) {/*aantalblokjes = aantal opgehaalde aantal per kleur*/
            productenGeel[i] = generateNumber();
        }
    }

    //Pakt de aantal prodcuten in de order van een bepaalde kleur
    public String getaantallen(String kleur) {
        databaseHelper = new DatabaseHelper();
        databaseHelper.openConnection();
        try {
            //Pakt de aantal blokjes voor de gevraagde kleur.
            String SQL = String.format("Select aantalblokjes From temporders where orderkleur ='%s'", kleur);
            ResultSet rs = databaseHelper.selectQuery(SQL);
            if (rs.next()) {
                //Pakt het aantal blokjes die met de sql query zijn gevraagd
                aantalblokjes = rs.getString("aantalblokjes");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return aantalblokjes;
    }

    public void sorteerBlokjes() {
        //van groot naar klein
        //3 vershillende waardes. Voor elke kleur array index.
        int highestNumber = 0;
        int indexRood = 0;
        int indexGroen = 0;
        int indexGeel = 0;


        //Loopen door de array en het hoogste nummer er uit pakken.//-- Array voor de Rode Blokjes --\\
        for (int i = 0; i < productenRood.length; i++) {
            if (productenRood[i] > highestNumber) {
                highestNumber = productenRood[i];
            }
        }
        //Gesorteerde array
        for (int i = 0; i < productenRood.length; i++) {
            if (productenRood[i] == highestNumber) {//Pak de nummers die het hoogst zijn en zet die voor in de array
                gesorteerdRood[indexRood] = productenRood[i];
                indexRood++;
            }
        }
        for (int i = 0; i < productenRood.length; i++) {//Pak alle getallen die 1 lager zijn dan het hoogste getal.
            if (productenRood[i] == highestNumber - 1) {
                gesorteerdRood[indexRood] = productenRood[i];
                indexRood++;
            }
        }
        for (int i = 0; i < productenRood.length; i++) {//Pak alle getallen die 2 lager zijn dan het hoogste getal.
            if (productenRood[i] == highestNumber - 2) {
                gesorteerdRood[indexRood] = productenRood[i];
                indexRood++;
            }
        }
        //-- Array voor de GROENE Blokjes --\\
        for (int i = 0; i < productenGroen.length; i++) {
            if (productenGroen[i] > highestNumber) {
                highestNumber = productenGroen[i];
            }
        }
        //Gesorteerde array
        for (int i = 0; i < productenGroen.length; i++) {
            if (productenGroen[i] == highestNumber) {//Pak de nummers die het hoogst zijn en zet die voor in de array
                gesorteerdGroen[indexGroen] = productenGroen[i];
                indexGroen++;
            }
        }
        for (int i = 0; i < productenGroen.length; i++) {//Pak alle getallen die 1 lager zijn dan het hoogste getal.
            if (productenGroen[i] == highestNumber - 1) {
                gesorteerdGroen[indexGroen] = productenGroen[i];
                indexGroen++;
            }
        }
        for (int i = 0; i < productenGroen.length; i++) {//Pak alle getallen die 2 lager zijn dan het hoogste getal.
            if (productenGroen[i] == highestNumber - 2) {
                gesorteerdGroen[indexGroen] = productenGroen[i];
                indexRood++;
            }
        }
        //-- Array voor de GELE blokjes --\\
        for (int i = 0; i < productenGeel.length; i++) {
            if (productenGeel[i] > highestNumber) {
                highestNumber = productenGeel[i];
            }
        }
        //Gesorteerde array
        for (int i = 0; i < productenGeel.length; i++) {
            if (productenGeel[i] == highestNumber) {//Pak de nummers die het hoogst zijn en zet die voor in de array
                gesorteerdGeel[indexGeel] = productenGeel[i];
                indexGeel++;
            }
        }
        for (int i = 0; i < productenGeel.length; i++) {//Pak alle getallen die 1 lager zijn dan het hoogste getal.
            if (productenGeel[i] == highestNumber - 1) {
                gesorteerdGeel[indexGeel] = productenGeel[i];
                indexGeel++;
            }
        }
        for (int i = 0; i < productenGeel.length; i++) {//Pak alle getallen die 2 lager zijn dan het hoogste getal.
            if (productenGeel[i] == highestNumber - 2) {
                gesorteerdGeel[indexGeel] = productenGeel[i];
                indexRood++;
            }
        }
    }



    //Testen van arraywaardes
    public void printArrays() {

        sorteerBlokjes();

        for (int a : productenRood) {
            System.out.println(a);
        }
        for (int a : productenGroen) {
            System.out.println(a);
        }
        for (int a : productenGeel) {
            System.out.println(a);
        }

    }


    public void drawBins(Graphics g) {

        for (int j = 2; j < 7; j += 2) {

            for (int i = 1; i < 8; i++) {
                int x1 = 100 * i;
                int x2 = 100 * i;

                int y1 = 50 * j + yPlus;
                int y2 = 50 * j + yPlus + 100;

                g.drawLine(x1, y1, x2, y2);                  // linker lijn
                g.drawLine(x1, y2, x2 + 80, y2);         // onderste lijn
                g.drawLine(x2 + 80, y1, x1 + 80, y2); // rechter lijn
            }
            yPlus += 50;
        }
    }

    // Teken de producten in de dozen ///// blokGrootte moet getal tussen 1 en 4 zijn
    public void drawProducts(Graphics g, String kleur, int aantal, int blokGrootte) {
        int width = 0;
        int height = 0;
        if (kleur.equals("rood")) {


            g.setColor(Color.RED);
            g.fillRect(roodX1, roodY1, 30, 35);

            //int blok = 0; //// dit uit de for loop halen
            //blok++;
                /* blokjes in de dozen
                if (blok == 1) {
                    x1 += 10 + 30;
                } else if (blok == 2) {
                    x1 -= 10 + 30;
                    y1 -= 5 + 35;
                } else if (blok == 3) {
                    x1 += 10 + 30;
                } else if (blok == 4) {
                    y1 += 5 + 35;
                    x1 += 60;
                    blok = 0;
                }  */

        }
/*
        if (aGroen > 0) {
            int y1 = 310; // Onderin doos
            int x1 = 105; // helemaal links in 1e doos
            for (int i = 0; i < aGroen; i++) {
                g.setColor(Color.GREEN);
                g.fillRect(x1, y1, 30, 35);
            }
        }

        if (aGeel > 0) {
            int y1 = 460; // Onderin doos
            int x1 = 105; // helemaal links in 1e doos
            for (int i = 0; i < aGeel; i++) {
                g.setColor(Color.YELLOW);
                g.fillRect(x1, y1, 30, 35);
            }
        } */
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbPakbon) {
            this.dispose();
            new PakbonGUI().setVisible(true);
        }
    }
}