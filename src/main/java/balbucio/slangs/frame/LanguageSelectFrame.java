package balbucio.slangs.frame;

import balbucio.slangs.LanguageSelector;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class LanguageSelectFrame extends JFrame {

    private LanguageSelector languageSelector;
    private JPanel panel;
    private JLabel title;
    private JComboBox<String> box;

    public LanguageSelectFrame(LanguageSelector languageSelector) throws HeadlessException {
        super("Select Language");
        this.languageSelector = languageSelector;
        this.setSize(640, 480);
        this.setLayout(new GridBagLayout());
        this.panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);
        this.title = new JLabel("Lang[lang_title]");
        this.box = new JComboBox<>(new Vector<>(languageSelector.getLanguageNames()));
        this.add(panel, new GridBagConstraints());
        this.setVisible(true);
    }
}
