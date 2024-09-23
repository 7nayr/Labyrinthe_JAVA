package algo;

import Model.Labyrinthe;
import Model.Case;

import java.util.*;

public class AStar extends Algo {

    public AStar(Labyrinthe labyrinthe) {
        super(labyrinthe);
    }

    @Override
    public boolean findPath() {
        PriorityQueue<Case> openList = new PriorityQueue<>(Comparator.comparingInt(this::fScore));
        Map<Case, Integer> gScore = new HashMap<>();
        Map<Case, Case> cameFrom = new HashMap<>();

        Case start = labyrinthe.getCase(labyrinthe.getDepart().x, labyrinthe.getDepart().y);
        Case goal = labyrinthe.getCase(labyrinthe.getArrivee().x, labyrinthe.getArrivee().y);

        gScore.put(start, 0);
        openList.add(start);

        while (!openList.isEmpty()) {
            Case current = openList.poll();

            if (current.equals(goal)) {
                reconstructPath(cameFrom, current);
                return true;
            }

            if (current.getStatut() != Case.Statut.DEPART && current.getStatut() != Case.Statut.ARRIVEE) {
                current.setStatut(Case.Statut.VISITE);
            }
            labyrinthe.notifierObservateurs();

            for (Case neighbor : getNeighbors(current)) {
                int tentativeGScore = gScore.get(current) + 1;

                if (tentativeGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }
        }
        return false;
    }

    private int fScore(Case c) {
        int g = Math.abs(c.getX() - labyrinthe.getDepart().x) + Math.abs(c.getY() - labyrinthe.getDepart().y);
        int h = heuristic(c);
        return g + h;
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
