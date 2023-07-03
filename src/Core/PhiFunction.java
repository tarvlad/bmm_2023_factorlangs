package Core;


import java.util.HashMap;
import java.util.Map;

/**
 * Represents morphism from set of letters into set of words <p>
 * <i>phi: Set_of_all_letters -> Set_of_all_words</i> <p>
 * <i>phi([0], ..., [n]) = phi([0]), ..., phi([n])</i>
 */
public class PhiFunction {
    private final Map<Letter, Word> map;

    /**
     * Construct map object
     * <p>
     * <b>{@code keys} length need to be same as {@code values}</b>
     * @param keys preimage
     * @param values image
     */
    PhiFunction(Letter[] keys, Word[] values) {
        if (keys.length != values.length) {
            throw new IllegalArgumentException("Cannot create PhiMap: keys and length size are not equal");
        }

        map = new HashMap<>();
        for (var i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }
    }

    /**
     * Applies morphism to every letter of word
     * @return phi(word)
     */
    Word apply(Word image) {
        var ret = new Word[image.size()];

        for (var i = 0; i < ret.length; i++) {
            ret[i] = map.get(image.elements[i]);
        }

        return new Word(ret);
    }

    /**
     * Applies morphism to every letter of word specified times
     * @return phi^{times}(image)
     */
    Word apply(Word image, int times) {
        if (times == 0) {
            return new Word(image);
        }

        var ret = image;
        for (var i = 0; i < times; i++) {
            ret = apply(ret);
        }

        return ret;
    }
}
