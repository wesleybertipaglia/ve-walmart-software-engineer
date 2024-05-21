package tasks.PowerOfTwoMaxHeap;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class PowerOfTwoMaxHeap {
    private final int childrenCountExponent;
    private final int childrenCount;
    private ArrayList<Integer> heap;

    public PowerOfTwoMaxHeap(int childrenCountExponent) {
        this.childrenCountExponent = childrenCountExponent;
        this.childrenCount = (int) Math.pow(2, childrenCountExponent);
        this.heap = new ArrayList<>();
    }

    public void insert(int value) {
        heap.add(value);
        bubbleUp(heap.size() - 1);
    }

    public int popMax() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("Heap is empty.");
        }

        int max = heap.get(0);
        int lastValue = heap.remove(heap.size() - 1);

        if (!heap.isEmpty()) {
            heap.set(0, lastValue);
            bubbleDown(0);
        }

        return max;
    }

    private void bubbleUp(int index) {
        int currentIndex = index;
        while (currentIndex > 0) {
            int parentIndex = (currentIndex - 1) / childrenCount;
            if (heap.get(currentIndex) > heap.get(parentIndex)) {
                swap(currentIndex, parentIndex);
                currentIndex = parentIndex;
            } else {
                break;
            }
        }
    }

    private void bubbleDown(int index) {
        int currentIndex = index;
        int size = heap.size();

        while (true) {
            int largest = currentIndex;
            for (int i = 1; i <= childrenCount; i++) {
                int childIndex = childrenCount * currentIndex + i;
                if (childIndex < size && heap.get(childIndex) > heap.get(largest)) {
                    largest = childIndex;
                }
            }

            if (largest != currentIndex) {
                swap(currentIndex, largest);
                currentIndex = largest;
            } else {
                break;
            }
        }
    }

    private void swap(int index1, int index2) {
        int temp = heap.get(index1);
        heap.set(index1, heap.get(index2));
        heap.set(index2, temp);
    }

    public static void main(String[] args) {
        PowerOfTwoMaxHeap heap = new PowerOfTwoMaxHeap(2);
        heap.insert(10);
        heap.insert(4);
        heap.insert(15);
        heap.insert(20);
        heap.insert(8);

        System.out.println(heap.popMax());
        System.out.println(heap.popMax());
    }
}