package algo;

import Model.Labyrinthe;
import Model.Case;

import java.util.*;

public class IDAStar extends Algo {

    public IDAStar(Labyrinthe labyrinthe) {
        super(labyrinthe);
    }

    @Override
    public boolean findPath() {
        Case start = labyrinthe.getCase(labyrinthe.getDepart().x, labyrinthe.getDepart().y);
        int threshold = heuristic(start);

        while (true) {
            Map<Case, Case> cameFrom = new HashMap<>();
            int temp = search(start, 0, threshold, cameFrom, new HashSet<>());
            if (temp == -1) {
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
                if (temp == -1) {
                    return -1;
                }
                if (temp < min) {
                    min = temp;
                }
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
        if (x > 0) {
            neighbors.add(labyrinthe.getCase(x - 1, y));
        }
        // Bas
        if (x < labyrinthe.getLignes() - 1) {
            neighbors.add(labyrinthe.getCase(x + 1, y));
        }
        // Gauche
        if (y > 0) {
            neighbors.add(labyrinthe.getCase(x, y - 1));
        }
        // Droite
        if (y < labyrinthe.getColonnes() - 1) {
            neighbors.add(labyrinthe.getCase(x, y + 1));
        }

        // Filtrer les cases non traversables
        neighbors.removeIf(n -> n.getStatut() == Case.Statut.MUR);

        return neighbors;
    }

    private void reconstructPath(Map<Case, Case> cameFrom, Case current) {
        List<Case> path = new ArrayList<>();
        while (current != null && cameFrom.containsKey(current)) {
            path.add(current);
            current = cameFrom.get(current);
        }
        // Marquer le chemin
        for (Case c : path) {
            if (c.getStatut() != Case.Statut.DEPART && c.getStatut() != Case.Statut.ARRIVEE) {
                c.setStatut(Case.Statut.CHEMIN);
                labyrinthe.notifierObservateurs();
            }
        }
    }
}
