package dev.hafnerp.async;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListWrapper<Type> {
    private List<Type> list;

    public ListWrapper() {
        this.list = new LinkedList<>();
    }

    public void add(Type element) {
        list.add(element);
        System.out.println(element);
    }

    public List<Type> getList() {
        return new ArrayList<>(list);
    }
}
