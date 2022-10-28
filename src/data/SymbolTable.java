package data;

public class SymbolTable {
    private class HashNode {
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
    private final double c1;
    private final double c2;

    public SymbolTable() {
        this.capacity = 16;
        this.data = new HashNode[this.capacity];
        this.c1 = 0.5;
        this.c2 = 0.5;
        for (int i = 0; i < this.capacity; ++i)
            this.data[i] = new HashNode();
    }

    public void add(String key, int value) {
        int hashCode = key.hashCode();
        int ind = 0;
        int position = this.getHashFunction(hashCode, ind);

        while (ind < this.capacity && !this.data[position].key.equals(key) && this.data[position].value != -1) {
            ++ind;
            position = this.getHashFunction(hashCode, ind);
        }

        if (ind == this.capacity) {
            this.resize();
            this.add(key, value);
        }
        else
            this.data[position] = new HashNode(key, value);
    }

    public int get(String key){
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

    private void resize() {
        int oldCapacity = this.capacity;
        HashNode[] oldData = this.data;

        this.capacity *= 2;
        this.data = new HashNode[this.capacity];
        for (int i = 0; i < this.capacity; ++i)
            this.data[i] = new HashNode();
        for (int i = 0; i < oldCapacity; ++i)
            this.add(oldData[i].key, oldData[i].value);
    }

    private int getHashFunction(int key, int ind) {
        return (getSimpleHashFunction(key) + (int)(this.c1 * ind + this.c2 * ind * ind)) % this.capacity;
    }

    private int getSimpleHashFunction(int key) {
        while (key < 0)
            key += this.capacity;
        return key % this.capacity;
    }
}
