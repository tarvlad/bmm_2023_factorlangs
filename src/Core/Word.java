package Core;

import java.util.ArrayList;
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
        } else if (elements.length > o.elements.length) {
            return 10;
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
        //search in begin and end
        var numContinueLeft = 0;
        var numContinueRight = 0;

        for (var letterBegin : alphabet) {
            var checkWordLetters = new Letter[elements.length + 1];
            checkWordLetters[0] = letterBegin;
            System.arraycopy(elements, 0, checkWordLetters, 1, elements.length);

            var checkWord = new Word(checkWordLetters);
            if (mainWord.occurrences(checkWord).length != 0) {
                numContinueLeft++;
            }
        }

        for (var letterEnd : alphabet) {
            var checkWordLetters = new Letter[elements.length + 1];
            System.arraycopy(elements, 0, checkWordLetters, 0, elements.length);
            checkWordLetters[checkWordLetters.length - 1] = letterEnd;

            var checkWord = new Word(checkWordLetters);
            if (mainWord.occurrences(checkWord).length != 0) {
                numContinueRight++;
            }
        }

        return numContinueLeft >= 2 && numContinueRight >= 2;
    }

    /**
     * @return returns {@code int array} representation of {@code this} word
     */
    public int[] intArrayRepresentation() {
        var ret = new int[elements.length];
        for (var i = 0; i < elements.length; i++) {
            ret[i] = elements[i].value;
        }

        return ret;
    }


    /**
     * @return all sub-words of {@code this} which {@code len <= maxSubWordLen}
     */
    public Word[] subWords(int maxSubWordLen) {
        //number of all sub-words is \frac{n(n - 1)}{2}
        //number of all sub-words, which len <= k is \sum_{i = 0}^{k - 1}[n - i]
        var lenRet = maxSubWordLen * size() + maxSubWordLen / 2 - maxSubWordLen * maxSubWordLen / 2;
        var ret = new Word[lenRet];

        var retIdx = 0;
        for (var len = 1; len <= maxSubWordLen; len++) {
            for (var idxBegin = 0; idxBegin <= elements.length - len; idxBegin++) {
                var retILetters = new Letter[len];
                System.arraycopy(elements, idxBegin, retILetters, 0, retILetters.length);

                ret[retIdx++] = new Word(retILetters);
            }
        }

        return ret;
    }

    /**
     * Checks that {@code word} is irreducible in {@code words}
     */
    private static boolean isIrreducible(Word word, Word[] words, PhiFunction phi) {
        // length phi(word) >= length word
        var checkWords = new ArrayList<Word>();
        for (var checkWord : words) {
            if (checkWord.size() >= word.size()) {
                break;
            }
            checkWords.add(checkWord);
        }

        for (var checkWord : checkWords) {
            var phiPower = 1;
            while (true /*forall k : phi^k (checkWord) .size < word.size */) {
                var phiCheckWord = phi.apply(checkWord, phiPower);
                if (phiCheckWord.size() > word.size()) {
                    break;
                } else if (phiCheckWord.size() == word.size()) {
                    if (word.compareTo(phiCheckWord) == 0) {
                        return false;
                    }
                }

                phiPower++;
            }


        }

        return true;
    }

    public static Word[] filterReducibleBispecialWords(Word[] bispecialWords, PhiFunction phi) {
        var ret = new ArrayList<Word>();

        for (var word : bispecialWords) {
            if (isIrreducible(word, bispecialWords, phi)) {
                ret.add(word);
            }
        }

        var retArr = new Word[ret.size()];
        for (var i = 0; i < retArr.length; i++) {
            retArr[i] = ret.get(i);
        }

        return retArr;
    }

    /**
     * @return all sub-words of {@code this} which is bispecial in {@code this}
     */
    public Word[] allBispecialSubWords(Word[] subWords, Letter[] alphabet) {
        var bispecialWords = new ArrayList<Word>();

        for (var word : subWords) {
            if (word.isBispecial(this, alphabet)) {
                bispecialWords.add(word);
            }
        }

        //it's cheap because it's all only references
        var ret = new Word[bispecialWords.size()];
        for (var i = 0; i < bispecialWords.size(); i++) {
            ret[i] = bispecialWords.get(i);
        }

        return ret;
    }
}
