package Core;

/**
 * Basic class which represents letter in alphabet.<p>
 * Set of letters is a totally ordered set.
 */
public class Letter implements Comparable<Letter> {
    private final int value;


    /**
     * Constructs Letter from it int value.
     */
    public Letter(int value) {
        this.value = value;
    }

    /**
     * Constructs Letter from another letter,
     * take it {@code int} value.
     */
    public Letter(Letter l) {
        this.value = l.value;
    }


    /**
     * Set of all letters totally ordered as it's {@code int} values.
     */
    @Override
    public int compareTo(Letter o) {
        if (value < o.value) {
            return -10;
        } else if (value > o.value) {
            return 10;
        }

        return 0;
    }
}
