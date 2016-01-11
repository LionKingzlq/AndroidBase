package com.chetuan.askforit.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YT on 2015/7/24.
 */
public class MinHeap {

    private List<MinHeapItem> heap;

    public MinHeap() {
        heap = new ArrayList<MinHeapItem>();
    }


    private MinHeapItem leftchild(MinHeapItem node) {
        if(node.getIndex() * 2 + 1>= heap.size())
        {
            return null;
        }
        return heap.get(node.getIndex() * 2 + 1);
    }


    private MinHeapItem rightchild(MinHeapItem node) {
        if(node.getIndex() * 2 + 2 >= heap.size())
        {
            return null;
        }
        return heap.get(node.getIndex() * 2 + 2);
    }


    private MinHeapItem parent(MinHeapItem node) {
    	if(node.getIndex() == 0)
    	{
    		return null;
    	}
        return heap.get((node.getIndex() - 1) / 2);
    }


    private boolean isleaf(MinHeapItem node) {
        int size = heap.size();
        return ((node.getIndex() >= size / 2) && (node.getIndex() < size));
    }


    private void swap(MinHeapItem node1, MinHeapItem node2) {
        int index1 = node1.getIndex();
        int index2 = node2.getIndex();
        node1.setIndex(index2);
        node2.setIndex(index1);
        heap.set(index1, node2);
        heap.set(index2, node1);
    }


    public void insert(MinHeapItem node) {
        node.setIndex(heap.size());
        heap.add(node);
        MinHeapItem current = node;
        MinHeapItem parent = parent(current);
        while (parent != null && current.lt(parent)) {
            swap(current, parent);
            parent = parent(current);
        }
    }


    public void print() {
        for (int i = 0; i < heap.size(); i++)
        {
            System.out.print(heap.get(i).toString() + " ");
        }
        System.out.println();
    }


    public MinHeapItem removeMin() {
        if(heap.size() > 1)
        {
            MinHeapItem first = heap.get(0);
            MinHeapItem last = heap.get(heap.size() - 1);
            swap(first, last);
            heap.remove(heap.size() - 1);
            pushdown(last);
            return first;
        }
        else if(heap.size() == 1)
        {
            MinHeapItem first = heap.get(0);
            heap.remove(0);
            return first;
        }
        return null;
    }


    private void pushdown(MinHeapItem node) {
        MinHeapItem smallestchild;
        while (!isleaf(node)) {
            smallestchild = leftchild(node);
            MinHeapItem rightChild = null;
            if (smallestchild.getIndex() + 1 < heap.size()
                    && !smallestchild.lt(rightChild = rightchild(node)))
            {
                smallestchild = rightChild;
            }
            if (node.lt(smallestchild))
            {
                return;
            }
            swap(node, smallestchild);
        }
    }

    public boolean notEmpty()
    {
    	return heap != null && heap.size() > 0;
    }


    public static void sort(List resource)
    {
        if(resource != null)
        {
            MinHeap sortMH = new MinHeap();
            for(Object item: resource)
            {
                if(!(item instanceof MinHeapItem))
                {
                    return;
                }
                sortMH.insert((MinHeapItem)item);
            }
            resource.clear();
            while (sortMH.notEmpty())
            {
                resource.add(sortMH.removeMin());
            }
        }
    }

}