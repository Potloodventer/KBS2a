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
        // Teken de dozen
        bppResultaatGUI.drawBins(g);
        // Teken de producten in de dozen
        bppResultaatGUI.drawProducts(g);
    }
}