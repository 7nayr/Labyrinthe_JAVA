package controller;

import Model.Labyrinthe;
import algo.*;
import vue.VueAffichage;
import vue.VueBoutons;
import vue.VueGrille;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EcouteurDemarrer implements ActionListener {
    private Labyrinthe labyrinthe;
    private VueAffichage vueAffichage;
    private VueBoutons vueBoutons;
    private VueGrille vueGrille;
    private String algorithmeChoisi;
    private volatile boolean algorithmeEnCours = false;
    private EcouteurGrille ecouteurGrille;
    private SwingWorker<Boolean, Void> worker; // Ajout du SwingWorker en tant que variable de classe

    public EcouteurDemarrer(Labyrinthe labyrinthe, VueAffichage vueAffichage, VueBoutons vueBoutons, VueGrille vueGrille, EcouteurGrille ecouteurGrille) {
        this.labyrinthe = labyrinthe;
        this.vueAffichage = vueAffichage;
        this.vueBoutons = vueBoutons;
        this.vueGrille = vueGrille;
        this.ecouteurGrille = ecouteurGrille;
        this.algorithmeChoisi = "A*";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (algorithmeEnCours) {
            // Interrompre l'algorithme en cours avant de démarrer un nouveau
            if (worker != null && !worker.isDone()) {
                worker.cancel(true); // Demande l'annulation du SwingWorker
            }
            // Réinitialiser le labyrinthe pour le nouvel algorithme
            labyrinthe.clearPath();
        }

        algorithmeEnCours = true;
        ecouteurGrille.setAlgorithmeEnCours(true);

        vueAffichage.setMessage("Démarrage de l'algorithme " + algorithmeChoisi);
        vueBoutons.setEnabled(false);
        vueGrille.setEnabled(false);

        Algo algo;
        switch (algorithmeChoisi) {
            case "A*":
                algo = new AStar(labyrinthe);
                break;
            case "Dijkstra":
                algo = new Dijkstra(labyrinthe);
                break;
            case "Largeur d'abord":
                algo = new BFS(labyrinthe);
                break;
            case "Profondeur d'abord":
                algo = new DFS(labyrinthe);
                break;
            case "IDA*":
                algo = new IDAStar(labyrinthe);
                break;
            default:
                vueAffichage.setMessage("Algorithme non reconnu");
                algorithmeEnCours = false;
                ecouteurGrille.setAlgorithmeEnCours(false);
                vueBoutons.setEnabled(true);
                vueGrille.setEnabled(true);
                return;
        }

        // Exécuter l'algorithme dans un SwingWorker
        worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                try {
                    return algo.findPath();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void done() {
                try {
                    if (isCancelled()) {
                        vueAffichage.setMessage("Algorithme annulé");
                    } else {
                        boolean result = get();
                        if (result) {
                            vueAffichage.setMessage("Chemin trouvé avec " + algorithmeChoisi);
                        } else {
                            vueAffichage.setMessage("Pas de chemin trouvé avec " + algorithmeChoisi);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    vueAffichage.setMessage("Erreur lors de l'exécution de l'algorithme : " + ex.getMessage());
                } finally {
                    algorithmeEnCours = false;
                    ecouteurGrille.setAlgorithmeEnCours(false);
                    vueBoutons.setEnabled(true);
                    vueGrille.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    public void setAlgorithme(String algorithme) {
        this.algorithmeChoisi = algorithme;
    }

    public boolean isAlgorithmeEnCours() {
        return algorithmeEnCours;
    }
}
