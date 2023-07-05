package Core;

import java.util.Stack;

/**
 * Represents word from alphabet.
 * Set of all words totally ordered in lexicographically order.
 */
public class Word implements Comparable<Word> {
    protected final Letter[] elements;

    /**
     * Constructs word by taking its letters.<p>
     * <b>Warning!</b> do not copy {@code elements}.
     */
    public Word(Letter[] elements) {
        this.elements = elements;
    }

    /**
     * Constructs word which contains only given letter.
     */
    public Word(Letter letter) {
        elements = new Letter[]{ letter };
    }

    /**
     * Copy constructor.
     */
    public Word(Word word) {
        elements = new Letter[word.elements.length];
        //Force copy because array elements is mutable, and we expect new word.
        System.arraycopy(word.elements, 0, elements, 0, elements.length);
    }

    /**
     * Copy constructor.
     * Construct word which contains all words from {@code words}
     * as sub-words.
     * <p>
     * {@code [words[0], ..., words[words.size() - 1]]}
     */
    public Word(Word[] words) {
        var sz = 0;
        for (var word : words) {
            sz += word.size();
        }

        elements = new Letter[sz];
        var idx = 0;
        for (var word : words) {
            for (var letter : word.elements) {
                elements[idx++] = letter;
            }
        }
    }

    /**
     * @return word size in letters.
     */
    public int size() {
        return elements.length;
    }

    /**
     * Words totally ordered in lexicographically order.
     * Comparison by hashcode now is not supported,
     * works for {@code O(n)}
     * @param o the object to be compared.
     */
    @Override
    public int compareTo(Word o) {
        var cmpLength = Math.min(elements.length, o.elements.length);

        for (var i = 0; i < cmpLength; i++) {
            var cmp = elements[i].compareTo(o.elements[i]);
            if (cmp != 0) {
                return cmp;
            }
        }

        if (elements.length < o.elements.length) {
            return -10;
        }

        return 0;
    }

    private boolean match(Word pattern, int beginIdx) {
        for (var i = 0; i < pattern.size(); i++) {
            if (elements[beginIdx + i].compareTo(pattern.elements[i]) != 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * @return beginning positions of all occurrences pattern in word
     */
    public int[] occurrences(Word pattern) {
        var occurrences = new Stack<Integer>();

        for (var i = 0; i < elements.length - pattern.elements.length + 1; i++) {
            if (match(pattern, i)) {
                occurrences.push(i);
            }
        }

        var ret = new int[occurrences.size()];
        for (var i = 0; i < ret.length; i++) {
            ret[i] = occurrences.pop();
        }

        return ret;
    }


    /**
     * Checks whether the word is bispecial in mainWord with given alphabet
     */
    public boolean isBispecial(Word mainWord, Letter[] alphabet) {
        for (var letterBegin: alphabet) {
            for (var letterEnd: alphabet) {
                var checkWordLetters = new Letter[elements.length + 2];
                checkWordLetters[0] = letterBegin;
                System.arraycopy(elements, 0, checkWordLetters, 1, elements.length);
                checkWordLetters[checkWordLetters.length - 1] = letterEnd;

                var checkWord = new Word(checkWordLetters);
                if (mainWord.occurrences(checkWord).length == 0) {
                    return false;
                }
            }
        }

        return true;
    }

    public int[] intArrayRepresentation() {
        var ret = new int[elements.length];
        for (var i = 0; i < elements.length; i++) {
            ret[i] = elements[i].value;
        }

        return ret;
    }
}
