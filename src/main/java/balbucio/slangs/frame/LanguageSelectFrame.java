package balbucio.slangs.frame;

import balbucio.slangs.LanguageSelector;
import balbucio.slangs.model.Language;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class LanguageSelectFrame extends JFrame {

    private LanguageSelector languageSelector;
    private JPanel panel;
    private JLabel title;
    private JComboBox<String> box;
    private JButton button;

    public LanguageSelectFrame(LanguageSelector languageSelector) throws HeadlessException {
        super("Select Language");
        this.languageSelector = languageSelector;
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(640, 480);
        this.setLayout(new GridBagLayout());
        this.panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);
        JPanel tp = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.title = new JLabel("Lang[lang_title]");
        tp.add(title);
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.box = new JComboBox<>(new Vector<>(languageSelector.getLanguageNames()));
        bp.add(box);
        JPanel bbp = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.button = new JButton("Lang[lang_selectbutton]");
        bbp.add(button);
        button.addActionListener(e -> updateLanguage());
        panel.add(tp);
        panel.add(bp);
        panel.add(bbp);
        this.add(panel, new GridBagConstraints());
        this.setVisible(true);
        SwingUtilities.invokeLater(() -> {
            languageSelector.addFrame(this);
        });
    }

    private void updateLanguage(){
        Language lang = languageSelector.getLanguageByName((String) box.getSelectedItem());
        if(lang != null){
            languageSelector.setLanguage(lang.getId());
            this.setVisible(false);
            this.dispose();
        }
    }
}
