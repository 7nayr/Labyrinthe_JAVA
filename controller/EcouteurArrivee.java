package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EcouteurArrivee implements ActionListener {
    private EcouteurGrille ecouteurGrille;

    public EcouteurArrivee(EcouteurGrille ecouteurGrille) {
        this.ecouteurGrille = ecouteurGrille;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ecouteurGrille.setMode("ARRIVEE");
    }
}
