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
                int newDist = distances.get(current) + 1; // Coût pour se déplacer vers un voisin
                if (newDist < distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, current);

                    // Mettre à jour la priorité du voisin dans la file de priorité
                    if (queue.contains(neighbor)) {
                        queue.remove(neighbor);
                    }
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
            if (haut.getStatut() != Case.Statut.MUR) {
                neighbors.add(haut);
            }
        }
        // Bas
        if (x < labyrinthe.getLignes() - 1) {
            Case bas = labyrinthe.getCase(x + 1, y);
            if (bas.getStatut() != Case.Statut.MUR) {
                neighbors.add(bas);
            }
        }
        // Gauche
        if (y > 0) {
            Case gauche = labyrinthe.getCase(x, y - 1);
            if (gauche.getStatut() != Case.Statut.MUR) {
                neighbors.add(gauche);
            }
        }
        // Droite
        if (y < labyrinthe.getColonnes() - 1) {
            Case droite = labyrinthe.getCase(x, y + 1);
            if (droite.getStatut() != Case.Statut.MUR) {
                neighbors.add(droite);
            }
        }

        return neighbors;
    }

    private void reconstructPath(Map<Case, Case> previous, Case current) {
        while (previous.containsKey(current)) {
            if (current.getStatut() != Case.Statut.DEPART && current.getStatut() != Case.Statut.ARRIVEE) {
                current.setStatut(Case.Statut.CHEMIN);
                labyrinthe.notifierObservateurs();
            }
            current = previous.get(current);
        }
    }
}
