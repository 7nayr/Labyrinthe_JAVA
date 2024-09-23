package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EcouteurMur implements ActionListener {
    private EcouteurGrille ecouteurGrille;

    public EcouteurMur(EcouteurGrille ecouteurGrille) {
        this.ecouteurGrille = ecouteurGrille;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ecouteurGrille.setMode("MUR");
    }
}
