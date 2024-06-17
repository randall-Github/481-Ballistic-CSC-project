package com.example.main;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class CorrectionStack {
//    private Stack<Item> crossedItemStack;
    private LinkedList<Item> crossedItemStack;
    private Set<Item> crossedItemSet;
    private Item queued;

    public CorrectionStack() {
        this.crossedItemStack = new LinkedList<>();
        this.crossedItemSet = new HashSet<>();
    }

    public Item pop() {
        Item poppedItem = crossedItemStack.pop();
        crossedItemSet.remove(poppedItem);
        this.queued = null;
        return poppedItem;
    }

    public Item peekStack() {
        return crossedItemStack.peek();
    }

    public void push(Item newItem) {
        if (!crossedItemSet.contains(queued) && queued != newItem) {
            crossedItemStack.push(queued);
            crossedItemSet.add(queued);
        }
        queued = newItem;
//        debug();
    }

    public void resetStack() {
        this.crossedItemStack = new LinkedList<>();
        this.crossedItemSet = new HashSet<>();
        this.queued = null;
    }

    public int size() {
        return this.crossedItemStack.size();
    }

    public void debug() {
        System.out.printf("Queued Item: %s, Top of stack: %s\n", queued, peekStack());
    }
}
