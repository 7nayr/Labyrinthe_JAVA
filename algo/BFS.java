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
        Set<Case> visited = new HashSet<>();

        Case start = labyrinthe.getCase(labyrinthe.getDepart().x, labyrinthe.getDepart().y);
        Case goal = labyrinthe.getCase(labyrinthe.getArrivee().x, labyrinthe.getArrivee().y);

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Case current = queue.poll();

            if (current.equals(goal)) {
                reconstructPath(cameFrom, current);
                return true;
            }

            if (current.getStatut() != Case.Statut.DEPART && current.getStatut() != Case.Statut.ARRIVEE) {
                current.setStatut(Case.Statut.VISITE);
            }
            labyrinthe.notifierObservateurs();

            for (Case neighbor : getNeighbors(current)) {
                if (!visited.contains(neighbor) && neighbor.getStatut() != Case.Statut.MUR) {
                    visited.add(neighbor);
                    cameFrom.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        return false;
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
        while (cameFrom.containsKey(current)) {
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
