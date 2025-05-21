package org.free.extend.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Used inside JavaScript to show we can invoke this Java class there
 */
public class HelpDialog extends JDialog {

    public HelpDialog(Frame owner, String message) {
        super(owner, "Help", true); // modal dialog
        setLayout(new BorderLayout());

        JLabel label = new JLabel(message);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(label, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> setVisible(false));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(300, 150);
        setLocationRelativeTo(null);
    }
}