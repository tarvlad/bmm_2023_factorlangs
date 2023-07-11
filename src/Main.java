import Core.Letter;
import Core.PhiFunction;
import Core.Word;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static Word readWord(Scanner reader, int length) {
        var wordLetters = new Letter[length];
        for (var i = 0; i < wordLetters.length; i++) {
            wordLetters[i] = new Letter(reader.nextInt());
        }

        return new Word(wordLetters);
    }

    private static Word readMainWord(Scanner reader) {
        System.out.print("Length of main word in letters: ");
        var length = reader.nextInt();
        System.out.print(": ");
        return readWord(reader, length);
    }


    private static void printWordsArray(Word[] words) {
        for (var word : words) {
            System.out.println(Arrays.toString(word.intArrayRepresentation()));
        }
    }


    private static Word[] uniqueWords(Word[] words) {
        var retList = new ArrayList<Word>();

        for (var word : words) {
            if (retList.isEmpty()) {
                retList.add(word);
                continue;
            }

            var flagUnique = true;
            for (var checkWord : retList) {
                if (word.compareTo(checkWord) == 0) {
                    flagUnique = false;
                    break;
                }
            }

            if (flagUnique) {
                retList.add(word);
            }
        }

        var ret = new Word[retList.size()];

        for (var i = 0; i < ret.length; i++) {
            ret[i] = retList.get(i);
        }

        return ret;
    }

    private static PhiFunction readMorphism(Scanner reader, int alphabetLength) {
        var phiImage = new Word[alphabetLength];

        for (var i = 0; i < phiImage.length; i++) {
            System.out.print("    Input length of " + i + " image: ");
            var wordLen = reader.nextInt();

            System.out.print("    " + i + " -> ");
            var word = readWord(reader, wordLen);
            phiImage[i] = word;
        }

        return new PhiFunction(phiImage);
    }

    public static void main(String[] args) {
        var reader = new Scanner(System.in);
        System.out.print("Num letters in alphabet: ");

        var numLettersInAlphabet = reader.nextInt();
        assert numLettersInAlphabet > 0;

        System.out.println("Initializing alphabet...");
        var alphabet = new Letter[numLettersInAlphabet];
        for (var i = 0; i < alphabet.length; i++) {
            alphabet[i] = new Letter(i);
        }
        System.out.println("Alphabet init success. Letters [0.." + (numLettersInAlphabet - 1) + "]");

        var mainWord = readMainWord(reader);
        System.out.println("Main word read successfully");

        System.out.println("Specify a morphism: ");
        var morphism = readMorphism(reader, numLettersInAlphabet);

        System.out.print("Specify morphism degree: ");
        var morphismDegree = reader.nextInt();

        mainWord = morphism.apply(mainWord, morphismDegree);
        System.out.print("View main word after morphism apply?\n0 - no, 1 - yes: ");
        if (reader.nextInt() == 1) {
            System.out.println("Main word after morphism phi^" + morphismDegree + " apply: ");
            System.out.println(Arrays.toString(mainWord.intArrayRepresentation()));
        }

        System.out.print("Max len of sub-words: ");
        var maxSubWordLen = reader.nextInt();

        System.out.println("Calculating all sub-words with len <= " + maxSubWordLen + "...");
        var subWords = uniqueWords(mainWord.subWords(maxSubWordLen));

        System.out.print("Input 0 if you want all sub-words of given word, 1 if all bispecial: ");
        var command = reader.nextInt();

        switch (command) {
            case 0 -> printWordsArray(subWords);

            case 1 -> {
                System.out.println("Calculating all bispecial sub-words with len <= " +
                        maxSubWordLen + "..."
                );

                var bispecialWords = mainWord.allBispecialWords(subWords, alphabet);
                printWordsArray(bispecialWords);
            }

            default -> System.out.println("Unexpected command");
        }
    }
}