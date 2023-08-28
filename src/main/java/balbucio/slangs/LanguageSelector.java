package balbucio.slangs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

import balbucio.slangs.frame.LanguageSelectFrame;
import balbucio.slangs.model.Language;
import balbucio.slangs.processor.Processor;
import org.json.JSONObject;

public class LanguageSelector {

    private CopyOnWriteArrayList<Component> components = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Language> loadedLanguages = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Processor> processors = new CopyOnWriteArrayList<>();
    private String languageSelected;

    public LanguageSelector(String languageSelected) {
        this.languageSelected = languageSelected;
    }

    public LanguageSelectFrame openSelectorFrame() {
        return new LanguageSelectFrame(this);
    }

    public void setLanguage(String langId) {
        this.languageSelected = langId;
        checkAndUpdate();
    }

    public void addLanguage(String path) {
        loadedLanguages.add(new Language(path));
        checkAndUpdate();
    }

    public void addLanguage(JSONObject json) {
        loadedLanguages.add(new Language("Unknown", json));
        checkAndUpdate();
    }

    public void addComponent(Component component) {
        components.add(component);
        checkAndUpdate();
    }

    public void addFrame(JFrame component) {
        component.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                checkAndUpdate();
            }
        });
        addComponent(component);
    }

    public void addProcessor(Processor processor){
        processors.add(processor);
        checkAndUpdate();
    }

    public java.util.List<String> getLanguageNames() {
        java.util.List<String> values = new ArrayList<>();
        loadedLanguages.forEach(l -> values.add(l.getName()));
        return values;
    }

    public Language getLanguageByName(String name) {
        return loadedLanguages.stream()
                .filter(l -> l.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public Language getLanguage(String id) {
        return loadedLanguages.stream()
                .filter(l -> l.getId().equalsIgnoreCase(id))
                .findFirst().orElse(null);
    }

    public boolean hasLanguage(String id) {
        return loadedLanguages.stream().anyMatch(l -> l.getId().equalsIgnoreCase(id));
    }

    public String processKey(String key) {
        if (hasLanguage(languageSelected) && (key != null && hasKey(key))) {
            String text = getLanguage(languageSelected).optString(getKey(key),
                    "unterminated field");
            for (Processor processor : processors) {
                text = text.replace(processor.getKey(), processor.replace(text));
            }
            return text;
        }
        return "unterminated language";
    }

    private void checkAndUpdate() {
        if (hasLanguage(languageSelected)) {
            Language lang = getLanguage(languageSelected);
            if (!lang.isLoaded()) {
                lang.load();
            }
        }

        components.forEach(c -> {
            updateComponent(c);
        });
    }

    private void updateComponent(Component c) {

        if (c == null || !c.isValid()) {
            return;
        }

        if (c instanceof JLabel label) {
            label.setText(processKey(label.getText()));
        } else if (c instanceof JButton button) {
            button.setText(processKey(button.getText()));
        } else if (c instanceof JCheckBox check) {
            check.setText(processKey(check.getText()));
        } else if (c instanceof JFileChooser file) {
            file.setApproveButtonText(processKey(file.getApproveButtonText()));
        }
        else if (c instanceof JFrame frame) {
            Arrays.asList(frame.getComponents()).forEach(this::updateComponent);
        }

        if (c instanceof JComponent comp) {
            if (comp.getToolTipText() != null && !comp.getToolTipText().isEmpty()) {
                comp.setToolTipText(processKey(comp.getToolTipText()));
            }
            Arrays.asList(comp.getComponents()).forEach(this::updateComponent);
        }
    }

    public static boolean hasKey(String key) {
        int startIndex = key.indexOf("[") + 1;
        int endIndex = key.indexOf("]");
        return startIndex != -1 && endIndex != -1 && endIndex > startIndex;
    }

    public static String getKey(String key) {
        int startIndex = key.indexOf("[") + 1;
        int endIndex = key.indexOf("]");

        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            return key.substring(startIndex, endIndex);
        } else {
            return "undefined";
        }
    }
}
