package controller;

import Model.Labyrinthe;
import Model.Case;
import vue.VueGrille;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EcouteurGrille extends MouseAdapter {
    private Labyrinthe labyrinthe;
    private VueGrille vueGrille;
    private String modeActuel;
    private volatile boolean algorithmeEnCours = false;

    public EcouteurGrille(Labyrinthe labyrinthe, VueGrille vueGrille) {
        this.labyrinthe = labyrinthe;
        this.vueGrille = vueGrille;
        this.modeActuel = "VIDE";
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (algorithmeEnCours) {
            return;
        }

        int x = e.getX() / vueGrille.getTailleCellule();
        int y = e.getY() / vueGrille.getTailleCellule();

        if (x < 0 || x >= labyrinthe.getColonnes() || y < 0 || y >= labyrinthe.getLignes()) {
            return;
        }

        synchronized (labyrinthe) {
            switch (modeActuel) {
                case "MUR":
                    labyrinthe.setCaseStatut(x, y, Case.Statut.MUR);
                    break;
                case "DEPART":
                    labyrinthe.setCaseStatut(x, y, Case.Statut.DEPART);
                    break;
                case "ARRIVEE":
                    labyrinthe.setCaseStatut(x, y, Case.Statut.ARRIVEE);
                    break;
                case "VIDE":
                    labyrinthe.setCaseStatut(x, y, Case.Statut.VIDE);
                    break;
            }
        }
    }

    public void setMode(String mode) {
        this.modeActuel = mode;
    }

    public void setAlgorithmeEnCours(boolean enCours) {
        this.algorithmeEnCours = enCours;
    }
}
