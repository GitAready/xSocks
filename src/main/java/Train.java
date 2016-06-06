public class Train {
    //
    private final long[] goodsArray; // array to transfer freight goods

    private int index;
    private int capacity;

    public Train(int capacity) {
        this.capacity = capacity;
        goodsArray = new long[capacity];
    }

    public int goodsCount() { // returns the count of goods
        return index;
    }
    public void addGoods(long i) { // adds item to the train
        goodsArray[index++] = i;
    }
    public long getGoods(int i) { //removes the item from the train
        index--;
        return goodsArray[i];
    }

    public int getCapacity() {
        return capacity;
    }
}