package Model;

import java.util.Objects;

public class Case {
    public enum Statut {
        MUR, DEPART, ARRIVEE, VIDE, VISITE, CHEMIN
    }

    private int x;
    private int y;
    private Statut statut;

    public Case(int x, int y, Statut statut) {
        this.x = x;
        this.y = y;
        this.statut = statut;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Case aCase = (Case) o;

        return x == aCase.x && y == aCase.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
