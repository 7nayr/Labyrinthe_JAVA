package vue;

import Model.Labyrinthe;
import Model.Case;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class VueGrille extends JPanel implements Observer {
    private Labyrinthe labyrinthe;
    private final int tailleCellule = 30; // Vous pouvez ajuster la taille des cellules ici

    public VueGrille(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
        this.labyrinthe.addObserver(this);

        Dimension preferredSize = new Dimension(
                labyrinthe.getColonnes() * tailleCellule,
                labyrinthe.getLignes() * tailleCellule);
        setPreferredSize(preferredSize);
        setMinimumSize(preferredSize);
        setMaximumSize(preferredSize);

        // Suppression des marges et des bordures
        setBorder(BorderFactory.createEmptyBorder());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int y = 0; y < labyrinthe.getLignes(); y++) {
            for (int x = 0; x < labyrinthe.getColonnes(); x++) {
                Case c = labyrinthe.getCase(x, y);
                switch (c.getStatut()) {
                    case MUR:
                        g.setColor(Color.BLACK);
                        break;
                    case DEPART:
                        g.setColor(Color.GREEN);
                        break;
                    case ARRIVEE:
                        g.setColor(Color.RED);
                        break;
                    case VISITE:
                        g.setColor(Color.YELLOW);
                        break;
                    case CHEMIN:
                        g.setColor(Color.BLUE);
                        break;
                    case VIDE:
                    default:
                        g.setColor(Color.WHITE);
                        break;
                }
                g.fillRect(x * tailleCellule, y * tailleCellule, tailleCellule, tailleCellule);
                g.setColor(Color.GRAY);
                g.drawRect(x * tailleCellule, y * tailleCellule, tailleCellule, tailleCellule);
            }
        }
    }

    public int getTailleCellule() {
        return tailleCellule;
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }
}
