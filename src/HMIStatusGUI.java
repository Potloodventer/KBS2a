import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HMIStatusGUI extends JFrame implements ActionListener {

    private int roodAantal = 0, groenAantal = 0, blauwAantal = 0;
    private int schermBreedte = 1000, schermHoogte = 1000;
    private JButton jbResultaat;
    private JLabel jlRood, jlGroen, jlBlauw;
    private TekenPanel tekenPanel;

    public HMIStatusGUI() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(schermBreedte, schermHoogte);
        //setBackground(Color.DARK_GRAY);
        setTitle("Visuele Weergave Robots");

        //Implementeerd het tekenpaneel
        add(Box.createRigidArea(new Dimension(800, 50)));
        tekenPanel = new TekenPanel(this);
        add(tekenPanel);

        setVisible(true);
    }

    public void TekenBlueprint(Graphics g){
        g.setColor(Color.RED);
        //boven band
        //Links
        g.drawLine(500,10,500,90);
        //Rechts
        g.drawLine(700,10,700,90);
        //boven
        g.drawLine(500,10,700,10);
        //Onder
        g.drawLine(500,90,700,90);

        //Middel band
        //Links
        g.drawLine(100,110,100,190);
        //Rechts
        g.drawLine(700,110,700,190);
        //boven
        g.drawLine(100,110,700,110);
        //Onder
        g.drawLine(100,190,700,190);

        //boven band
        //Links
        g.drawLine(500,210,500,290);
        //Rechts
        g.drawLine(700,210,700,290);
        //boven
        g.drawLine(500,210,700,210);
        //Onder
        g.drawLine(500,290,700,290);
    }


    public int getSchermBreedte() {
        return schermBreedte;
    }
    public void setSchermBreedte(int getal) {
        schermBreedte = getal;
    }

    public int getSchermHoogte() {
        return schermHoogte;
    }
    public void setSchermHoogte(int getal) {
        schermHoogte = getal;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
