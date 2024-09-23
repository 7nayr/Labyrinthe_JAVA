import Model.Labyrinthe;
import vue.VueFenetre;

public class Main {
    public static void main(String[] args) {
        Labyrinthe labyrinthe = new Labyrinthe(10, 10); // Taille de la grille

        javax.swing.SwingUtilities.invokeLater(() -> {
            VueFenetre fenetre = new VueFenetre(labyrinthe);
            fenetre.setVisible(true);
        });
    }
}
