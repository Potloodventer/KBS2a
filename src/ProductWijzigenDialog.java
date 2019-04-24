import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductWijzigenDialog extends JDialog implements ActionListener {

    public ProductWijzigenDialog()
    {
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setTitle("Product wijzigen");
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
