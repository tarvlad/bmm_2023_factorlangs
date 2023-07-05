package Core;


/**
 * Represents morphism from set of letters into set of words <p>.
 * <i>phi: Set_of_all_letters -> Set_of_all_words</i> <p>
 * <i>phi([0], ..., [n]) = phi([0]), ..., phi([n])</i>
 */
public class PhiFunction {
    //Using array (not hashtable) because alphabet letters can be numerated.
    private final Word[] map;

    public PhiFunction(Word[] image) {
        map = image;
    }

    public Word apply(Word word) {
        var ret = new Word[word.size()];

        for (var i = 0; i < ret.length; i++) {
            ret[i] = map[word.elements[i].value];
        }

        return new Word(ret);
    }

    public Word apply(Word word, int times) {
        if (times == 0) {
            return new Word(word);
        }

        var ret = word;
        for (var i = 0; i < times; i++) {
            ret = apply(ret);
        }

        return new Word(ret);
    }
}
