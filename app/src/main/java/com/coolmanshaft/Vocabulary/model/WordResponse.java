package com.coolmanshaft.Vocabulary.model;

import java.util.List;

public class WordResponse {
    private String word;
    private List<Meaning> meanings;

    public String getWord() {
        return word;
    }

    public List<Meaning> getMeanings() {
        return meanings;
    }

    public static class Meaning {
        private String partOfSpeech;
        private List<Definition> definitions;

        public String getPartOfSpeech() {
            return partOfSpeech;
        }

        public List<Definition> getDefinitions() {
            return definitions;
        }
    }

    public static class Definition {
        private String definition;
        private String example;  // Optional example sentence

        public String getDefinition() {
            return definition;
        }

        public String getExample() {
            return example;
        }
    }
}
