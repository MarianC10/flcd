package com.company.common;

public class PIFPair {
    private final String token;
    private final int position;

    public PIFPair(String token) {
        this.token = token;
        this.position = -1;
    }

    public PIFPair(String token, int position) {
        this.token = token;
        this.position = position;
    }

    public String getToken() {
        return token;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "PifPair{" +
                "token='" + token + '\'' +
                ", position=" + position +
                '}';
    }
}
