package vue;

import Model.Labyrinthe;
import controller.*;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class VueFenetre extends JFrame implements Observer {
    private Labyrinthe labyrinthe;
    private VueGrille vueGrille;
    private VueBoutons vueBoutons;
    private VueAffichage vueAffichage;

    public VueFenetre(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
        this.labyrinthe.addObserver(this);

        setTitle("Labyrinthe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        vueGrille = new VueGrille(labyrinthe);
        vueBoutons = new VueBoutons();
        vueAffichage = new VueAffichage();

        // Création du panneau principal avec BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(vueBoutons);
        mainPanel.add(vueGrille);
        mainPanel.add(vueAffichage);

        // Suppression des marges du panneau principal
        mainPanel.setBorder(BorderFactory.createEmptyBorder());

        add(mainPanel);

        // Initialisation des écouteurs
        EcouteurGrille ecouteurGrille = new EcouteurGrille(labyrinthe, vueGrille);
        vueGrille.addMouseListener(ecouteurGrille);

        vueBoutons.getBtnDepart().addActionListener(new EcouteurDepart(ecouteurGrille));
        vueBoutons.getBtnArrivee().addActionListener(new EcouteurArrivee(ecouteurGrille));
        vueBoutons.getBtnMur().addActionListener(new EcouteurMur(ecouteurGrille));
        vueBoutons.getBtnVide().addActionListener(new EcouteurVide(ecouteurGrille));

        EcouteurDemarrer ecouteurDemarrer = new EcouteurDemarrer(labyrinthe, vueAffichage, vueBoutons, vueGrille, ecouteurGrille);
        vueBoutons.getBtnDemarrer().addActionListener(ecouteurDemarrer);

        vueBoutons.getBtnQuitter().addActionListener(new EcouteurQuitter());
        vueBoutons.getAlgoBox().addActionListener(new EcouteurAlgo(ecouteurDemarrer));

        pack(); // Ajuste la taille de la fenêtre pour correspondre à ses composants
        setLocationRelativeTo(null); // Centre la fenêtre à l'écran
        setResizable(false); // Empêche le redimensionnement de la fenêtre
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }
}
