package Model;

import java.awt.*;
import java.util.Observable;

public class Labyrinthe extends Observable {
    private int lignes;
    private int colonnes;
    private Case[][] cases;
    private Point depart;
    private Point arrivee;

    public Labyrinthe(int lignes, int colonnes) {
        this.lignes = lignes;
        this.colonnes = colonnes;
        cases = new Case[lignes][colonnes];
        initialiserCases();
    }

    private void initialiserCases() {
        for (int y = 0; y < lignes; y++) {
            for (int x = 0; x < colonnes; x++) {
                cases[y][x] = new Case(x, y, Case.Statut.VIDE);
            }
        }
    }

    public int getLignes() {
        return lignes;
    }

    public int getColonnes() {
        return colonnes;
    }

    public synchronized Case getCase(int x, int y) {
        if (x >= 0 && x < colonnes && y >= 0 && y < lignes) {
            return cases[y][x]; // [ligne][colonne] = [y][x]
        }
        return null;
    }

    public synchronized void setCaseStatut(int x, int y, Case.Statut statut) {
        Case c = getCase(x, y);
        if (c != null) {
            c.setStatut(statut);
            if (statut == Case.Statut.DEPART) {
                depart = new Point(x, y);
            } else if (statut == Case.Statut.ARRIVEE) {
                arrivee = new Point(x, y);
            } else {
                if (depart != null && depart.equals(new Point(x, y))) {
                    depart = null;
                }
                if (arrivee != null && arrivee.equals(new Point(x, y))) {
                    arrivee = null;
                }
            }
            setChanged();
            notifyObservers();
        }
    }

    public synchronized Point getDepart() {
        return depart;
    }

    public synchronized Point getArrivee() {
        return arrivee;
    }

    public void notifierObservateurs() {
        setChanged();
        notifyObservers();
    }

    public synchronized void clearPath() {
        for (int y = 0; y < lignes; y++) {
            for (int x = 0; x < colonnes; x++) {
                Case c = cases[y][x];
                if (c.getStatut() == Case.Statut.VISITE || c.getStatut() == Case.Statut.CHEMIN) {
                    c.setStatut(Case.Statut.VIDE);
                }
            }
        }
        setChanged();
        notifyObservers();
    }
}
