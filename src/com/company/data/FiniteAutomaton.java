package com.company.data;

import com.company.common.FAPair;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class FiniteAutomaton {
    private final HashSet<String> states;
    private final String initialState;
    private final HashSet<String> finalStates;
    private final HashSet<Character> alphabet;
    private final HashMap<FAPair, String> transitions;

    public static FiniteAutomaton provideFiniteAutomaton(String fileName) throws IOException {
        FileInputStream fis = new FileInputStream(fileName);
        Scanner sc = new Scanner(fis);
        var states = new HashSet<String>();
        String initialState = null;
        var finalStates = new HashSet<String>();
        var alphabet = new HashSet<Character>();
        var transitions = new HashMap<FAPair, String>();

        while (sc.hasNextLine()) {
            var stringTokenizer = new StringTokenizer(sc.nextLine(), ",");
            var tokensCount = stringTokenizer.countTokens();
            if (tokensCount != 2 && tokensCount != 3) {
                throw new RuntimeException("Invalid file format!");
            }

            if (tokensCount == 2) {
                var state = stringTokenizer.nextToken();
                var type = stringTokenizer.nextToken();

                if (!type.equals("initial") && !type.equals("final")) {
                    throw new RuntimeException("Invalid file format!");
                }

                if (type.equals("initial")) {
                    if (initialState != null) {
                        throw new RuntimeException("Invalid file format!");
                    }

                    initialState = state;
                }
                else {
                    finalStates.add(state);
                }
            }
            else {
                var from = stringTokenizer.nextToken();
                var stringLetter = stringTokenizer.nextToken();
                var to = stringTokenizer.nextToken();
                if (stringLetter.length() != 1) {
                    throw new RuntimeException("Invalid file format!");
                }
                var letter = stringLetter.charAt(0);

                alphabet.add(letter);
                states.add(from);
                states.add(to);
                transitions.put(new FAPair(from, letter), to);
            }
        }

        return new FiniteAutomaton(states, initialState, finalStates, alphabet, transitions);
    }

    public boolean matches(String toBeMatched) {
        var currentState = initialState;

        for (var c: toBeMatched.toCharArray()) {
            currentState = transitions.get(new FAPair(currentState, c));
            if (currentState == null) {
                return false;
            }
        }

        return finalStates.contains(currentState);
    }

    public FiniteAutomaton(HashSet<String> states,
                           String initialState,
                           HashSet<String> finalStates,
                           HashSet<Character> alphabet,
                           HashMap<FAPair, String> transitions) {
        this.states = states;
        this.initialState = initialState;
        this.finalStates = finalStates;
        this.alphabet = alphabet;
        this.transitions = transitions;
    }
}
