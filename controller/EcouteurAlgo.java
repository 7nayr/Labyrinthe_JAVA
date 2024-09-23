package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

public class EcouteurAlgo implements ActionListener {
    private EcouteurDemarrer ecouteurDemarrer;

    public EcouteurAlgo(EcouteurDemarrer ecouteurDemarrer) {
        this.ecouteurDemarrer = ecouteurDemarrer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
        String selectedAlgo = (String) comboBox.getSelectedItem();
        ecouteurDemarrer.setAlgorithme(selectedAlgo);
    }
}
