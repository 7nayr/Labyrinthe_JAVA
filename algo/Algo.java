package algo;

import Model.Labyrinthe;

public abstract class Algo {
    protected Labyrinthe labyrinthe;

    public Algo(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
    }

    public abstract boolean findPath();
}
