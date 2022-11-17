package com.company.data;

import com.company.common.PIFPair;

import java.util.ArrayList;

public class ProgramInternalForm {
    public ArrayList<PIFPair> data;

    public ProgramInternalForm() {
        this.data = new ArrayList<>();
    }

    public boolean add(PIFPair pifPair) {
        return this.data.add(pifPair);
    }

    @Override
    public String toString() {
        var stringBuilder = new StringBuilder("Program Internal Form \nToken | Position");
        this.data.forEach(pair -> stringBuilder.append("\n").append(pair.getToken())
                .append(" ").append(pair.getPosition()));

        return stringBuilder.toString();
    }
}
