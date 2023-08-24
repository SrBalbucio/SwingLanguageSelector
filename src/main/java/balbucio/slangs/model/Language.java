package balbucio.slangs.model;

import balbucio.slangs.LanguageSelector;
import lombok.Data;
import org.json.JSONObject;

import java.util.Scanner;


@Data
public class Language {

    private boolean loaded = false;
    private String path;
    private String name;
    private String id;
    private JSONObject json;

    public Language(String path){
        this.path = path;
        preLoad(new JSONObject(getBuilder().toString()));
    }

    public Language(String path, JSONObject json){
        preLoad(json);
        this.path = path;
        this.json = json;
        this.loaded = true;
    }

    private StringBuilder getBuilder(){
        StringBuilder builder = new StringBuilder();
        Scanner scanner = new Scanner(this.getClass().getResourceAsStream(path));
        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }
        return builder;
    }

    private void preLoad(JSONObject json){
        this.name = json.optString("lang_name", "Invalid language name");
        this.id = json.optString("lang_id", "Invalid language id");
    }

    public void load(){
        this.json = new JSONObject(getBuilder().toString());
    }

    public String optString(String key, String def){
        return json.optString(key, def);
    }

}
