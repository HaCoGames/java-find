package dev.hafnerp.async;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListWrapper<Type> {
    private List<Type> list;

    private static boolean found;

    public ListWrapper() {
        this.list = new LinkedList<>();
    }

    public synchronized void add(Type element) {
        list.add(element);
        System.out.println(element);
    }

    public synchronized void setFound(boolean found) {
        ListWrapper.found = found;
    }

    public synchronized boolean isFound() {
        return found;
    }

    public synchronized List<Type> getList() {
        return new ArrayList<>(list);
    }
}
