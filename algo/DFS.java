package algo;

import Model.Labyrinthe;
import Model.Case;

import java.util.*;

public class DFS extends Algo {

    public DFS(Labyrinthe labyrinthe) {
        super(labyrinthe);
    }

    @Override
    public boolean findPath() {
        Stack<Case> stack = new Stack<>();
        Map<Case, Case> cameFrom = new HashMap<>();
        Set<Case> visited = new HashSet<>();

        // Récupérer les points de départ et d'arrivée
        Case start = labyrinthe.getCase(labyrinthe.getDepart().x, labyrinthe.getDepart().y);
        Case goal = labyrinthe.getCase(labyrinthe.getArrivee().x, labyrinthe.getArrivee().y);

        // Vérifier que le départ et l'arrivée sont définis
        if (start == null || goal == null) {
            System.err.println("Point de départ ou d'arrivée non défini.");
            return false;
        }

        stack.push(start);
        visited.add(start);

        while (!stack.isEmpty()) {
            // Vérifier si le thread a été interrompu
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Algorithme interrompu");
                return false;
            }

            Case current = stack.pop();

            // Vérifier si la case actuelle est l'objectif
            if (current.equals(goal)) {
                reconstructPath(cameFrom, current);
                return true; // Chemin trouvé
            }

            // Marquer la case comme visitée (si ce n'est ni le départ ni l'arrivée)
            if (current.getStatut() != Case.Statut.DEPART && current.getStatut() != Case.Statut.ARRIVEE) {
                current.setStatut(Case.Statut.VISITE);
                labyrinthe.notifierObservateurs();

                // Pour visualiser le processus, vous pouvez ajouter un délai (facultatif)
                /* try {
                    Thread.sleep(50); // Pause de 50 ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                } */
            }

            // Parcourir les voisins de la case actuelle
            for (Case neighbor : getNeighbors(current)) {
                if (!visited.contains(neighbor) && neighbor.getStatut() != Case.Statut.MUR) {
                    visited.add(neighbor);
                    cameFrom.put(neighbor, current);
                    stack.push(neighbor);
                }
            }
        }

        return false; // Pas de chemin trouvé
    }

    /**
     * Méthode pour obtenir les voisins accessibles d'une case
     */
    private List<Case> getNeighbors(Case c) {
        List<Case> neighbors = new ArrayList<>();
        int x = c.getX();
        int y = c.getY();

        // Haut
        if (y > 0) {
            Case haut = labyrinthe.getCase(x, y - 1);
            if (haut != null && haut.getStatut() != Case.Statut.MUR) {
                neighbors.add(haut);
            }
        }
        // Bas
        if (y < labyrinthe.getLignes() - 1) {
            Case bas = labyrinthe.getCase(x, y + 1);
            if (bas != null && bas.getStatut() != Case.Statut.MUR) {
                neighbors.add(bas);
            }
        }
        // Gauche
        if (x > 0) {
            Case gauche = labyrinthe.getCase(x - 1, y);
            if (gauche != null && gauche.getStatut() != Case.Statut.MUR) {
                neighbors.add(gauche);
            }
        }
        // Droite
        if (x < labyrinthe.getColonnes() - 1) {
            Case droite = labyrinthe.getCase(x + 1, y);
            if (droite != null && droite.getStatut() != Case.Statut.MUR) {
                neighbors.add(droite);
            }
        }

        return neighbors;
    }

    /**
     * Méthode pour reconstruire le chemin trouvé
     */
    private void reconstructPath(Map<Case, Case> cameFrom, Case current) {
        while (current != null && cameFrom.containsKey(current)) {
            if (current.getStatut() != Case.Statut.DEPART && current.getStatut() != Case.Statut.ARRIVEE) {
                current.setStatut(Case.Statut.CHEMIN);
                labyrinthe.notifierObservateurs();

                // Pour visualiser le chemin, vous pouvez ajouter un délai (facultatif)
                /* try {
                    Thread.sleep(50); // Pause de 50 ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                } */
            }
            current = cameFrom.get(current);
        }
    }
}
