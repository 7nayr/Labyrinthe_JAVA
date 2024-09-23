package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EcouteurDepart implements ActionListener {
    private EcouteurGrille ecouteurGrille;

    public EcouteurDepart(EcouteurGrille ecouteurGrille) {
        this.ecouteurGrille = ecouteurGrille;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ecouteurGrille.setMode("DEPART");
    }
}
