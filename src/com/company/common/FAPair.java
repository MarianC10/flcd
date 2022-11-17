package com.company.common;

public class FAPair {
    private final String state;
    private final Character letter;

    public FAPair(String state, Character letter) {
        this.state = state;
        this.letter = letter;
    }

    public String getState() {
        return state;
    }

    public Character getLetter() {
        return letter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FAPair faPair = (FAPair) o;

        if (!state.equals(faPair.state)) return false;
        return letter.equals(faPair.letter);
    }

    @Override
    public int hashCode() {
        int result = state.hashCode();
        result = 31 * result + letter.hashCode();
        return result;
    }
}
