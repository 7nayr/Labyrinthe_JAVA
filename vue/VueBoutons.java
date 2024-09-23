package vue;

import javax.swing.*;
import java.awt.*;

public class VueBoutons extends JPanel {
    private JButton btnDepart;
    private JButton btnArrivee;
    private JButton btnMur;
    private JButton btnVide;
    private JButton btnDemarrer;
    private JButton btnQuitter;
    private JComboBox<String> algoBox;

    public VueBoutons() {
        btnDepart = new JButton("Départ");
        btnArrivee = new JButton("Arrivée");
        btnMur = new JButton("Mur");
        btnVide = new JButton("Effacer");
        btnDemarrer = new JButton("Démarrer");
        btnQuitter = new JButton("Quitter");
        algoBox = new JComboBox<>(new String[]{"A*", "Dijkstra", "Largeur d'abord", "Profondeur d'abord", "IDA*"});

        // Suppression des marges et des espacements
        setBorder(BorderFactory.createEmptyBorder());
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        add(btnDepart);
        add(btnArrivee);
        add(btnMur);
        add(btnVide);
        add(btnDemarrer);
        add(btnQuitter);
        add(algoBox);
    }

    public JButton getBtnDepart() {
        return btnDepart;
    }

    public JButton getBtnArrivee() {
        return btnArrivee;
    }

    public JButton getBtnMur() {
        return btnMur;
    }

    public JButton getBtnVide() {
        return btnVide;
    }

    public JButton getBtnDemarrer() {
        return btnDemarrer;
    }

    public JButton getBtnQuitter() {
        return btnQuitter;
    }

    public JComboBox<String> getAlgoBox() {
        return algoBox;
    }
}
