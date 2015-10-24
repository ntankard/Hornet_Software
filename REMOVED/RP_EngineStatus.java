package hornet.gui.rootPanels;

import javax.swing.*;

/**
 * Created by Nicholas on 18/09/2015.
 */
public class RP_EngineStatus {
    private JPanel rootPanel;
        private JProgressBar Engine1_Bar;
        private JProgressBar Engine2_Bar;
        private JProgressBar Engine3_Bar;
        private JProgressBar Engine4_Bar;

    private JProgressBar[] Bars = new JProgressBar[4];

    public RP_EngineStatus()
    {
        Bars[0] = Engine1_Bar;
        Bars[1] = Engine2_Bar;
        Bars[2] = Engine3_Bar;
        Bars[3] = Engine4_Bar;
    }

    public void setStatus(short[] bars)
    {
         for(int i=0;i<4;i++)
         {
             Bars[i].setValue(bars[i]);
         }
    }

}
