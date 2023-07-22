/*******************************************************************************
 * Copyright (c) 2023. VitaminL
 * All rights reserved.
 * <p>
 * Randomized queue, random swap the last inserted item.
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int size;
    private Item[] dataList;

    public RandomizedQueue() {
        dataList = (Item[]) new Object[1];
        size = 0;
    }

    private void resizeAdd() {
        Item[] tmp = (Item[]) new Object[size * 2];
        if (size >= 0) System.arraycopy(dataList, 0, tmp, 0, size);
        dataList = tmp;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (size == dataList.length) {
            resizeAdd();
        }
        dataList[size++] = item;
        itemSwap(StdRandom.uniformInt(size), size - 1);
    }

    private void resizeRemove() {
        Item[] tmp = (Item[]) new Object[dataList.length / 2];
        if (size >= 0) System.arraycopy(dataList, 0, tmp, 0, size);
        dataList = tmp;
    }

    private void itemSwap(int i, int j) {
        Item tmp = dataList[i];
        dataList[i] = dataList[j];
        dataList[j] = tmp;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = dataList[--size];
        dataList[size] = null;
        if (size < dataList.length / 4) {
            resizeRemove();
        }
        return item;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return dataList[StdRandom.uniformInt(size)];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Iterator<Item> iterator() {
        return new ItemIterator();
    }

    private class ItemIterator implements Iterator<Item> {
        private int current = 0;
        private final int[] index = StdRandom.permutation(size);

        public boolean hasNext() {
            return current < size;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return dataList[index[current++]];
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> a = new RandomizedQueue<>();

        a.enqueue(10);
        a.enqueue(1);
        a.enqueue(123);
        a.enqueue(41);
        a.enqueue(14);
        a.enqueue(12);
        a.enqueue(97);

        StdOut.print("(");
        for (Integer item : a) {
            StdOut.print(item.toString());
            StdOut.print(" -> ");
        }
        StdOut.print("null)\n");

        StdOut.print("(");
        for (Integer item : a) {
            StdOut.print(item.toString());
            StdOut.print(" -> ");
        }
        StdOut.print("null)\n");

        StdOut.print("(");
        for (Integer item : a) {
            StdOut.print(item.toString());
            StdOut.print(" -> ");
        }
        StdOut.print("null)\n");

        for (Integer integer : a) {
            for (Integer value : a) {
                StdOut.print(value + " ");
            }
            StdOut.println();
            StdOut.println(integer);
        }
        StdOut.println(a.sample() + a.size());
    }
}
