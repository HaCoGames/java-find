package dev.hafnerp.async;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class HashSetWrapper<Type> {
    private HashSet<Type> set;

    public HashSetWrapper() {
        this.set = new HashSet<>();
    }

    public void add(Type element) {
        set.add(element);
        System.out.println(element);
    }

    public List<Type> getSet() {
        return new ArrayList<>(set);
    }
}
