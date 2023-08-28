# SwingLanguageSelector - Translate your Swing App into multiple languages
With this library you can translate your frames and swing components to multiple languages, just using json files to save the translations.

> Not all components and elements can be translated yet. [Click here to see all translatable elements](https://github.com/SrBalbucio/SwingLanguageSelector/wiki/Translatable-Components)

### Motivation
The main idea here is to enable developers to make their apps more accessible to different audiences by adding their native languages.

### Get started
First of all, make sure your application is running on Java 17 or later.

To get started you must first create a json file for each language you want to support, using this as a base:

```json
{
  "lang_id": "PT-BR",
  "lang_name": "Português Brasileiro",
  "lang_title": "Selecione um idioma:",
  "lang_selectbutton": "Selecionar"
}
```
> I believe it is obvious, but you must change the keys that start with "lang_" to the information of the language in question.

And to load the json of the language you must use the LanguageSelector:
```java
LanguageSelector langSelector = new LanguageSelector(String: defaultLanguageID);
langSelector.addLanguage(String: pathJson); // /lang.json
```
When you've loaded all the necessary languages, you must add the translatable components:
```java
langSelector.addComponent(Component: comp);
langSelector.addFrame(JFrame: frame);
```
For the Language Selector to identify a translated text, it must look like this ```Lang[<key>]```