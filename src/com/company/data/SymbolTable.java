package com.company.data;

public class SymbolTable {
    private static class HashNode {
        public final String key;
        public final int value;

        public HashNode() {
            this.key = "";
            this.value = -1;
        }

        public HashNode(String key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private int capacity;
    private HashNode[] data;
    private String[] list;
    private int size;
    private final double c1;
    private final double c2;

    private static int newPosition = 0;

    public SymbolTable() {
        this.capacity = 16;
        this.size = 0;
        this.data = new HashNode[this.capacity];
        this.list = new String[this.capacity];
        this.c1 = 0.5;
        this.c2 = 0.5;
        for (int i = 0; i < this.capacity; ++i)
            this.data[i] = new HashNode();
    }

    private static int generatePosition() {
        return SymbolTable.newPosition++;
    }

    public int add(String key) {
        int value = generatePosition();
        this.put(key, value);
        this.list[value] = key;
        this.size++;
        return value;
    }

    private void put(String key, int value) {
        int hashCode = key.hashCode();
        int ind = 0;
        int position = this.getHashFunction(hashCode, ind);

        while (ind < this.capacity && !this.data[position].key.equals(key) && this.data[position].value != -1) {
            ++ind;
            position = this.getHashFunction(hashCode, ind);
        }

        if (ind == this.capacity) {
            this.resize();
            this.put(key, value);
        }
        else {
            if (this.data[position].key.equals(key))
                throw new IllegalArgumentException("The key already exists!");

            this.data[position] = new HashNode(key, value);
        }
    }

    public int get(String key) {
        int hashCode = key.hashCode();
        int ind = 0;
        int position = this.getHashFunction(hashCode, ind);

        while (ind < this.capacity && !this.data[position].key.equals(key) && this.data[position].value != -1) {
            ++ind;
            position = this.getHashFunction(hashCode, ind);
        }

        if (ind == this.capacity || this.data[position].value == -1)
            throw new IllegalArgumentException("The key could not be found!");

        return this.data[position].value;
    }

    public boolean contains(String key) {
        int hashCode = key.hashCode();
        int ind = 0;
        int position = this.getHashFunction(hashCode, ind);

        while (ind < this.capacity && !this.data[position].key.equals(key) && this.data[position].value != -1) {
            ++ind;
            position = this.getHashFunction(hashCode, ind);
        }

        return ind != this.capacity && this.data[position].value != -1;
    }

    private void resize() {
        int oldCapacity = this.capacity;
        HashNode[] oldData = this.data;
        String[] oldList = this.list;

        this.capacity *= 2;
        this.data = new HashNode[this.capacity];
        this.list = new String[this.capacity];
        for (int i = 0; i < this.capacity; ++i)
            this.data[i] = new HashNode();
        for (int i = 0; i < oldCapacity; ++i) {
            this.put(oldData[i].key, oldData[i].value);
            this.list[i] = oldList[i];
        }
    }

    private int getHashFunction(int key, int ind) {
        return (getSimpleHashFunction(key) + (int)(this.c1 * ind + this.c2 * ind * ind)) % this.capacity;
    }

    private int getSimpleHashFunction(int key) {
        while (key < 0)
            key += this.capacity;
        return key % this.capacity;
    }

    @Override
    public String toString() {
        var stringBuilder = new StringBuilder("Symbol Table\nPosition | Symbol");
        for (int i = 0; i < this.size; ++i) {
            stringBuilder.append("\n").append(i).append(" ").append(this.list[i]);
        }
        return stringBuilder.toString();
    }
}
