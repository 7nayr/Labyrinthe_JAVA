package algo;

import Model.Labyrinthe;
import Model.Case;

import java.util.*;

public class BFS extends Algo {

    public BFS(Labyrinthe labyrinthe) {
        super(labyrinthe);
    }

    @Override
    public boolean findPath() {
        Queue<Case> queue = new LinkedList<>();
        Map<Case, Case> cameFrom = new HashMap<>();

        Case start = labyrinthe.getCase(labyrinthe.getDepart().x, labyrinthe.getDepart().y);
        Case goal = labyrinthe.getCase(labyrinthe.getArrivee().x, labyrinthe.getArrivee().y);

        queue.add(start);

        while (!queue.isEmpty()) {
            Case current = queue.poll();

            if (current.equals(goal)) {
                reconstructPath(cameFrom, current);
                return true; // Chemin trouvé
            }

            if (current.getStatut() != Case.Statut.DEPART && current.getStatut() != Case.Statut.ARRIVEE) {
                current.setStatut(Case.Statut.VISITE);
            }
            labyrinthe.notifierObservateurs();

            for (Case neighbor : getNeighbors(current)) {
                if (!cameFrom.containsKey(neighbor)) {
                    cameFrom.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        return false; // Pas de chemin trouvé
    }

    private List<Case> getNeighbors(Case c) {
        List<Case> neighbors = new ArrayList<>();
        int x = c.getX();
        int y = c.getY();

        // Haut
        if (x > 0) {
            Case haut = labyrinthe.getCase(x - 1, y);
            if (haut.getStatut() != Case.Statut.MUR && haut.getStatut() != Case.Statut.VISITE) neighbors.add(haut);
        }
        // Bas
        if (x < labyrinthe.getLignes() - 1) {
            Case bas = labyrinthe.getCase(x + 1, y);
            if (bas.getStatut() != Case.Statut.MUR && bas.getStatut() != Case.Statut.VISITE) neighbors.add(bas);
        }
        // Gauche
        if (y > 0) {
            Case gauche = labyrinthe.getCase(x, y - 1);
            if (gauche.getStatut() != Case.Statut.MUR && gauche.getStatut() != Case.Statut.VISITE) neighbors.add(gauche);
        }
        // Droite
        if (y < labyrinthe.getColonnes() - 1) {
            Case droite = labyrinthe.getCase(x, y + 1);
            if (droite.getStatut() != Case.Statut.MUR && droite.getStatut() != Case.Statut.VISITE) neighbors.add(droite);
        }

        return neighbors;
    }

    private void reconstructPath(Map<Case, Case> cameFrom, Case current) {
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            if (current.getStatut() != Case.Statut.DEPART) {
                current.setStatut(Case.Statut.CHEMIN);
                labyrinthe.notifierObservateurs();
            }
        }
    }
}
