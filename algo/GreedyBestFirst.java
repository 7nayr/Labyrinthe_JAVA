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
        Set<Case> closedList = new HashSet<>();

        Case start = labyrinthe.getCase(labyrinthe.getDepart().x, labyrinthe.getDepart().y);
        Case goal = labyrinthe.getCase(labyrinthe.getArrivee().x, labyrinthe.getArrivee().y);

        openList.add(start);

        while (!openList.isEmpty()) {
            Case current = openList.poll();

            if (current.equals(goal)) {
                reconstructPath(cameFrom, current);
                return true;
            }

            closedList.add(current);

            if (current.getStatut() != Case.Statut.DEPART && current.getStatut() != Case.Statut.VISITE) {
                current.setStatut(Case.Statut.VISITE);
                labyrinthe.notifierObservateurs();
            }

            for (Case neighbor : getNeighbors(current)) {
                if (!closedList.contains(neighbor) && neighbor.getStatut() != Case.Statut.MUR) {
                    if (!openList.contains(neighbor)) {
                        cameFrom.put(neighbor, current);
                        openList.add(neighbor);
                    }
                }
            }
        }

        return false; // Pas de chemin trouvé
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
            Case haut = labyrinthe.getCase(x - 1, y);
            if (haut.getStatut() != Case.Statut.MUR) neighbors.add(haut);
        }
        // Bas
        if (x < labyrinthe.getLignes() - 1) {
            Case bas = labyrinthe.getCase(x + 1, y);
            if (bas.getStatut() != Case.Statut.MUR) neighbors.add(bas);
        }
        // Gauche
        if (y > 0) {
            Case gauche = labyrinthe.getCase(x, y - 1);
            if (gauche.getStatut() != Case.Statut.MUR) neighbors.add(gauche);
        }
        // Droite
        if (y < labyrinthe.getColonnes() - 1) {
            Case droite = labyrinthe.getCase(x, y + 1);
            if (droite.getStatut() != Case.Statut.MUR) neighbors.add(droite);
        }

        return neighbors;
    }

    private void reconstructPath(Map<Case, Case> cameFrom, Case current) {
        while (cameFrom.containsKey(current)) {
            if (current.getStatut() != Case.Statut.DEPART && current.getStatut() != Case.Statut.ARRIVEE) {
                current.setStatut(Case.Statut.CHEMIN);
                labyrinthe.notifierObservateurs();
            }
            current = cameFrom.get(current);
        }
    }
}
