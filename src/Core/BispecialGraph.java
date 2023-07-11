package Core;

import java.util.Arrays;

public class BispecialGraph {
    private final int[][] elements;

    private static final int FORBIDDEN = 0;
    private static final int ENABLED = 1;

    public BispecialGraph(Word mainWord, Letter[] alphabet, Word bispecialWord) {
        elements = new int[alphabet.length][alphabet.length];

        for (var letterBeginIdx = 0; letterBeginIdx < alphabet.length; letterBeginIdx++) {
            for (var letterEndIdx = 0; letterEndIdx < alphabet.length; letterEndIdx++) {
                var checkWordElements = new Letter[bispecialWord.size() + 2];
                checkWordElements[0] = alphabet[letterBeginIdx];
                System.arraycopy(bispecialWord.elements, 0, checkWordElements, 1, bispecialWord.size());
                checkWordElements[checkWordElements.length - 1] = alphabet[letterEndIdx];

                var checkWord = new Word(checkWordElements);
                if (mainWord.occurrences(checkWord).length != 0) {
                    elements[letterBeginIdx][letterEndIdx] = ENABLED;
                    elements[letterEndIdx][letterBeginIdx] = ENABLED;
                }
            }
        }
    }

    public String listView() {
        var builder = new StringBuilder();

        builder.append("{\n");
        for (var letterFromIdx = 0; letterFromIdx < elements.length; letterFromIdx++) {
            for (var letterToIdx = 0; letterToIdx < elements[letterFromIdx].length; letterToIdx++) {
                if (elements[letterFromIdx][letterToIdx] == ENABLED) {
                    builder.append("    ");
                    builder.append(letterFromIdx);
                    builder.append(" ");
                    builder.append(letterToIdx);
                    builder.append("\n");
                }
            }
        }
        builder.append("}");

        return new String(builder);
    }
}
