/*******************************************************************************
 * Copyright (c) 2023. VitaminL
 * All rights reserved.
 * <p>
 * Deque, use circle array implementation.
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Item[] dataList;
    private int first;
    private int last;

    public Deque() {
        dataList = (Item[]) new Object[1];
        first = -1;
        last = 0;
    }

    public boolean isEmpty() {
        return first == -1;
    }

    public int size() {
        if (last < first) {
            return dataList.length - first + last;
        } else if (first == -1) {
            return 0;
        } else if (last > first) {
            return last - first;
        } else {
            return dataList.length;
        }
    }

    private void resizeAdd() {
        Item[] tmp = (Item[]) new Object[dataList.length * 2];
        if (last >= 0) System.arraycopy(dataList, 0, tmp, 0, last);
        for (int i = dataList.length - 1; i >= first; i--) {
            tmp[dataList.length + i] = dataList[i];
        }
        first = first + dataList.length;
        dataList = tmp;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null item!");
        }
        if (first == -1) {
            dataList[++first] = item;
            return;
        }
        if (first == last) {
            resizeAdd();
        }
        if (first == 0) {
            first = dataList.length - 1;
            dataList[first] = item;
        } else {
            dataList[--first] = item;
        }
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null item!");
        }
        if (first == -1) {
            dataList[++first] = item;
            return;
        }
        if (first == last) {
            resizeAdd();
        }
        if (last == dataList.length - 1) {
            dataList[last] = item;
            last = 0;
        } else {
            dataList[last++] = item;
        }

    }

    private void resizeRemove() {
        Item[] tmp = (Item[]) new Object[dataList.length / 2];
        if (first > last) {
            if (last >= 0) System.arraycopy(dataList, 0, tmp, 0, last);
            int j = tmp.length - 1;
            for (int i = dataList.length - 1; i >= first; i--) {
                tmp[j--] = dataList[i];
            }
            first = j + 1;
        } else {
            int j = 0;
            for (int i = first; i < last; i++) {
                tmp[j++] = dataList[i];
            }
            first = 0;
            last = j;
        }
        dataList = tmp;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item removed = dataList[first];
        dataList[first] = null;
        if (first == dataList.length - 1) {
            first = 0;
        } else {
            first++;
        }
        if (first == last) {
            first = -1;
            last = 0;
            dataList = (Item[]) new Object[1];
        } else if (size() < dataList.length / 4) {
            resizeRemove();
        }
        return removed;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item removed;
        if (last == 0) {
            removed = dataList[dataList.length - 1];
            dataList[dataList.length - 1] = null;
            last = dataList.length - 1;
        } else {
            removed = dataList[--last];
            dataList[last] = null;
        }
        if (first == last) {
            first = -1;
            last = 0;
            dataList = (Item[]) new Object[1];
        } else if (size() < dataList.length / 4) {
            resizeRemove();
        }
        return removed;
    }

    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {
        int n = size();
        int current = first;

        public boolean hasNext() {
            return n > 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            n--;
            if (current != dataList.length - 1) {
                return dataList[current++];
            } else {
                Item next = dataList[current];
                current = 0;
                return next;
            }
        }
    }

    public static void main(String[] args) {
        Deque<Integer> a = new Deque<>();
        StdOut.println("isEmpty:" + a.isEmpty() + " size: " + a.size());
        a.addFirst(1);
        StdOut.println("isEmpty:" + a.isEmpty() + " size: " + a.size());
        a.removeFirst();
        StdOut.println("isEmpty:" + a.isEmpty() + " size: " + a.size());
        a.addLast(2);
        StdOut.println("isEmpty:" + a.isEmpty() + " size: " + a.size());
        a.removeFirst();
        StdOut.println("isEmpty:" + a.isEmpty() + " size: " + a.size());
        a.addFirst(2);
        StdOut.println("isEmpty:" + a.isEmpty() + " size: " + a.size());
        a.removeLast();
        StdOut.println("isEmpty:" + a.isEmpty() + " size: " + a.size());
        a.addLast(10);
        a.addFirst(1);
        a.addLast(123);
        a.addLast(41);
        a.addLast(14);
        a.addFirst(12);
        StdOut.println("isEmpty:" + a.isEmpty() + " size: " + a.size());
        a.addFirst(97);
        a.addLast(10);
        a.addLast(4);

        StdOut.print("(");
        for (Integer item : a) {
            StdOut.print(item.toString());
            StdOut.print(" -> ");
        }
        StdOut.print("null)\n");
        StdOut.println("isEmpty:" + a.isEmpty() + " size: " + a.size());


        StdOut.println(a.removeFirst());
        StdOut.println(a.removeLast());
        StdOut.println(a.removeLast());
        StdOut.println(a.removeLast());
        a.removeLast();
        StdOut.println("isEmpty:" + a.isEmpty() + " size: " + a.size());
        a.removeLast();
        StdOut.println(a.removeLast());
        a.removeLast();

        StdOut.print("(");
        for (Integer item : a) {
            StdOut.print(item.toString());
            StdOut.print(" -> ");
        }
        StdOut.print("null)\n");
        StdOut.println("isEmpty:" + a.isEmpty() + " size: " + a.size());

        StdOut.println(a.removeLast());
        StdOut.println("isEmpty:" + a.isEmpty() + " size: " + a.size());
        a.addFirst(8);
        a.addFirst(9);
        a.addFirst(213);
        a.addFirst(2);
        StdOut.println(a.removeLast());
        StdOut.println(a.removeLast());
        StdOut.println(a.removeLast());
        StdOut.println(a.removeLast());
        a.addFirst(2);
        StdOut.println("isEmpty:" + a.isEmpty() + " size: " + a.size());

    }
}
