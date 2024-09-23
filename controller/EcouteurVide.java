package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EcouteurVide implements ActionListener {
    private EcouteurGrille ecouteurGrille;

    public EcouteurVide(EcouteurGrille ecouteurGrille) {
        this.ecouteurGrille = ecouteurGrille;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ecouteurGrille.setMode("VIDE");
    }
}
