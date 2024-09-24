package algo;

import Model.Labyrinthe;
import Model.Case;

import java.util.*;

public class Dijkstra extends Algo {

    public Dijkstra(Labyrinthe labyrinthe) {
        super(labyrinthe);
    }

    @Override
    public boolean findPath() {
        Map<Case, Integer> distances = new HashMap<>();
        Map<Case, Case> previous = new HashMap<>();
        PriorityQueue<Case> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        Case start = labyrinthe.getCase(labyrinthe.getDepart().x, labyrinthe.getDepart().y);
        Case goal = labyrinthe.getCase(labyrinthe.getArrivee().x, labyrinthe.getArrivee().y);

        for (int i = 0; i < labyrinthe.getLignes(); i++) {
            for (int j = 0; j < labyrinthe.getColonnes(); j++) {
                Case c = labyrinthe.getCase(i, j);
                distances.put(c, Integer.MAX_VALUE);
            }
        }
        distances.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {
            Case current = queue.poll();

            if (current.equals(goal)) {
                reconstructPath(previous, current);
                return true;
            }

            if (current.getStatut() != Case.Statut.DEPART && current.getStatut() != Case.Statut.ARRIVEE) {
                current.setStatut(Case.Statut.VISITE);
                labyrinthe.notifierObservateurs();
            }

            for (Case neighbor : getNeighbors(current)) {
                int alt = distances.get(current) + 1; // Coût pour se déplacer vers un voisin
                if (alt < distances.get(neighbor)) {
                    distances.put(neighbor, alt);
                    previous.put(neighbor, current);
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

    private void reconstructPath(Map<Case, Case> previous, Case current) {
        List<Case> path = new ArrayList<>();
        while (previous.containsKey(current)) {
            path.add(current);
            current = previous.get(current);
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
