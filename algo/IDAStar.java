package algo;

import Model.Labyrinthe;
import Model.Case;

import java.util.*;

public class IDAStar extends Algo {

    public IDAStar(Labyrinthe labyrinthe) {
        super(labyrinthe);
    }

    private boolean found = false;

    @Override
    public boolean findPath() {
        Case start = labyrinthe.getCase(labyrinthe.getDepart().x, labyrinthe.getDepart().y);
        int threshold = heuristic(start);

        while (true) {
            Map<Case, Case> cameFrom = new HashMap<>();
            int temp = search(start, 0, threshold, cameFrom, new HashSet<>());
            if (found) {
                reconstructPath(cameFrom, labyrinthe.getCase(labyrinthe.getArrivee().x, labyrinthe.getArrivee().y));
                return true;
            }
            if (temp == Integer.MAX_VALUE) {
                return false;
            }
            threshold = temp;
        }
    }

    private int search(Case current, int g, int threshold, Map<Case, Case> cameFrom, Set<Case> visited) {
        int f = g + heuristic(current);

        if (f > threshold) {
            return f;
        }
        if (current.equals(labyrinthe.getCase(labyrinthe.getArrivee().x, labyrinthe.getArrivee().y))) {
            found = true;
            return -1;
        }

        if (current.getStatut() != Case.Statut.DEPART && current.getStatut() != Case.Statut.ARRIVEE) {
            current.setStatut(Case.Statut.VISITE);
            labyrinthe.notifierObservateurs();
        }

        int min = Integer.MAX_VALUE;
        visited.add(current);

        for (Case neighbor : getNeighbors(current)) {
            if (!visited.contains(neighbor) && neighbor.getStatut() != Case.Statut.MUR) {
                cameFrom.put(neighbor, current);
                int temp = search(neighbor, g + 1, threshold, cameFrom, visited);
                if (found) return -1;
                if (temp < min) min = temp;
                cameFrom.remove(neighbor);
            }
        }
        visited.remove(current);
        return min;
    }

    private int heuristic(Case c) {
        return Math.abs(c.getX() - labyrinthe.getArrivee().x) + Math.abs(c.getY() - labyrinthe.getArrivee().y);
    }

    private List<Case> getNeighbors(Case c) {
        List<Case> neighbors = new ArrayList<>();
        int x = c.getX();
        int y = c.getY();

        // Haut
        if (y > 0) {
            Case haut = labyrinthe.getCase(x, y - 1);
            if (haut.getStatut() != Case.Statut.MUR) neighbors.add(haut);
        }
        // Bas
        if (y < labyrinthe.getColonnes() - 1) {
            Case bas = labyrinthe.getCase(x, y + 1);
            if (bas.getStatut() != Case.Statut.MUR) neighbors.add(bas);
        }
        // Gauche
        if (x > 0) {
            Case gauche = labyrinthe.getCase(x - 1, y);
            if (gauche.getStatut() != Case.Statut.MUR) neighbors.add(gauche);
        }
        // Droite
        if (x < labyrinthe.getLignes() - 1) {
            Case droite = labyrinthe.getCase(x + 1, y);
            if (droite.getStatut() != Case.Statut.MUR) neighbors.add(droite);
        }

        return neighbors;
    }

    private void reconstructPath(Map<Case, Case> cameFrom, Case current) {
        while (current != null && cameFrom.containsKey(current)) {
            if (current.getStatut() != Case.Statut.DEPART && current.getStatut() != Case.Statut.ARRIVEE) {
                current.setStatut(Case.Statut.CHEMIN);
                labyrinthe.notifierObservateurs();
            }
            current = cameFrom.get(current);
        }
    }
}
