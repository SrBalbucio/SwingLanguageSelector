import balbucio.slangs.LanguageSelector;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Vector;

public class TestFrame {

    public static void main(String[] args) {
        LanguageSelector languageSelector = new LanguageSelector("PT-BR");
        languageSelector.addLanguage("/pt_br.json");
        JFrame frame = new JFrame("Frame Teste");
        frame.setSize(640, 480);
        frame.setLayout(new GridBagLayout());
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);
        panel.add(new JLabel("Lang[first_line]"));
        panel.add(new JLabel("Lang[second_line]"));
        panel.add(new JButton("Lang[first_button]"));
        JButton b = new JButton("Lang[select]");
        b.addActionListener(e -> languageSelector.openSelectorFrame());
        panel.add(b);
        panel.add(new JCheckBox("Lang[checkbox]"));
        panel.add(new JComboBox<String>(new Vector<>(Arrays.asList("Teste 1","Teste 2", "Teste 3"))));
        frame.add(panel, new GridBagConstraints());
        frame.setVisible(true);
        SwingUtilities.invokeLater(() -> {
            languageSelector.addFrame(frame);
        });

    }
}
