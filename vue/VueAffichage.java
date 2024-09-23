package vue;

import javax.swing.*;
import java.awt.*;

public class VueAffichage extends JPanel {
    private JLabel messageLabel;

    public VueAffichage() {
        messageLabel = new JLabel("Prêt");

        // Suppression des marges et des espacements
        setBorder(BorderFactory.createEmptyBorder());
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        add(messageLabel);
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}
