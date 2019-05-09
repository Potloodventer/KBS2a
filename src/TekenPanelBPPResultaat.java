import javax.swing.*;
import java.awt.*;

public class TekenPanelBPPResultaat extends JPanel {

    private int tekenPanelBreedte = HoofdschermGUI.getSchermBreedte() - 200;
    private int tekenPanelHoogte = HoofdschermGUI.getSchermHoogte() - 200;

    private BPPResultaatGUI bppResultaatGUI;

    public TekenPanelBPPResultaat(BPPResultaatGUI bppResultaatGUI) {
        this.bppResultaatGUI = bppResultaatGUI;

        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(tekenPanelBreedte, tekenPanelHoogte));
        setBackground(Color.white);
        setVisible(true);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        bppResultaatGUI.drawBins(g);
        bppResultaatGUI.drawProducts(g, 15, 20, 10);
    }
}