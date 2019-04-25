import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.AlphaComposite;

public class TekenPanel extends JPanel {

    private HMIStatusGUI hmiStatusGUI;
    private int tekenPanelBreedte = HoofdschermGUI.getSchermBreedte() - 200;
    private int tekenPanelHoogte = HoofdschermGUI.getSchermHoogte() - 200;
    //Image Toevoegen
    private BufferedImage blauwdruk;



    public TekenPanel(HMIStatusGUI hmiStatusGUI) {
        this.hmiStatusGUI = hmiStatusGUI;
        //Kijken of plaatje bestaat anders wordt er een fout weergegeven
        try{
            blauwdruk = ImageIO.read(new File("src/Images/blauwdruk.png"));

        }catch(IOException IOex){
            System.out.println("Plaatje Niet gevonden");
        }

        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(tekenPanelBreedte, tekenPanelHoogte));
        setBackground(Color.DARK_GRAY);
        setVisible(true);
    }

    //Resize the bufferedImage
    //Image scaledBlauwdruk = blauwdruk.getScaledInstance(1000,300,Image.SCALE_SMOOTH);

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        hmiStatusGUI.beweegPijlen(g, null); // null wordt: hmiStatusGUI.getKleur();
        //Teken het plaatje
        //ImageIcon imageIcon = new ImageIcon(scaledBlauwdruk);
        g.drawImage(blauwdruk,100,100,null);

    }


}
