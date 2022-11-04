package com.company.common;

public class PifPair {
    private final String token;
    private final int position;

    public PifPair(String token) {
        this.token = token;
        this.position = -1;
    }

    public PifPair(String token, int position) {
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
