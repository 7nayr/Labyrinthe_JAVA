package algo;

import Model.Labyrinthe;
import Model.Case;

import java.util.*;

public class GreedyBestFirst extends Algo {

    public GreedyBestFirst(Labyrinthe labyrinthe) {
        super(labyrinthe);
    }

    @Override
    public boolean findPath() {
        PriorityQueue<Case> openList = new PriorityQueue<>(Comparator.comparingInt(this::heuristic));
        Map<Case, Case> cameFrom = new HashMap<>();
        Set<Case> visited = new HashSet<>();

        Case start = labyrinthe.getCase(labyrinthe.getDepart().x, labyrinthe.getDepart().y);
        Case goal = labyrinthe.getCase(labyrinthe.getArrivee().x, labyrinthe.getArrivee().y);

        openList.add(start);

        while (!openList.isEmpty()) {
            Case current = openList.poll();

            if (current.equals(goal)) {
                reconstructPath(cameFrom, current);
                return true;
            }

            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            if (current.getStatut() != Case.Statut.DEPART && current.getStatut() != Case.Statut.VISITE) {
                current.setStatut(Case.Statut.VISITE);
                labyrinthe.notifierObservateurs();
            }

            for (Case neighbor : getNeighbors(current)) {
                if (!visited.contains(neighbor) && neighbor.getStatut() != Case.Statut.MUR) {
                    cameFrom.put(neighbor, current);
                    openList.add(neighbor);
                }
            }
        }

        return false;
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
