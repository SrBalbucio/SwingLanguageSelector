package balbucio.slangs;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import balbucio.slangs.model.Language;
import org.json.JSONObject;

public class LanguageSelector {

    private CopyOnWriteArrayList<Component> components = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Language> loadedLanguages = new CopyOnWriteArrayList<>();
    private String languageSelected;

    public LanguageSelector(String languageSelected) {
        this.languageSelected = languageSelected;
    }

    public void setLanguage(String langId){
        this.languageSelected = langId;
    }

    public void addLanguage(String path){
        loadedLanguages.add(new Language(path));
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
        addComponent(component);
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
            return getLanguage(languageSelected).optString(getKey(key),
                    "unterminated field");
        }
        return "unterminated language";
    }

    private void checkAndUpdate() {
        if(hasLanguage(languageSelected)){
            Language lang = getLanguage(languageSelected);
            if(!lang.isLoaded()){
                lang.load();
            }
        }

        components.forEach(c -> {
                updateComponent(c);
        });
    }

    private void updateComponent(Component c) {

        if(c == null || !c.isValid()){
            return;
        }

        if (c instanceof JLabel label) {
            label.setText(processKey(label.getText()));
        } else if (c instanceof JButton button) {
            button.setText(processKey(button.getText()));
        } else if (c instanceof JCheckBox check) {
            check.setText(processKey(check.getText()));
        } else if(c instanceof JFrame frame){
            Arrays.asList(frame.getComponents()).forEach(this::updateComponent);
        }

        if((c instanceof JComponent)) {
            ((JComponent) c).setToolTipText(processKey(((JComponent) c).getToolTipText()));
            Arrays.asList(((JComponent) c).getComponents()).forEach(this::updateComponent);
        }
    }

    public static boolean hasKey(String key){
        int startIndex = key.indexOf("[") + 1;
        int endIndex = key.indexOf("]");
        return endIndex != -1 && endIndex > startIndex;
    }

    public static String getKey(String key){
        int startIndex = key.indexOf("[") + 1;
        int endIndex = key.indexOf("]");

        if (endIndex != -1 && endIndex > startIndex) {
            return key.substring(startIndex, endIndex);
        } else {
            throw new IllegalArgumentException("There is no Lang[<key>] in the given parameter!");
        }
    }
}
