import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JRadioButton;

/*
 * Created on Tevet 5770 
 */

/**
 * @author לויאן
 */


public class MyActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        JRadioButton butt = (JRadioButton) e.getSource();
        System.out.println(butt.getName());
        //		butt.setEnabled(false);
        //		butt.setSelected(false);
    }

}
