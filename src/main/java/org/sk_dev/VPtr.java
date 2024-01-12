package org.sk_dev;

public class VPtr<T> {
    private T v;
    private VPtr<T> next;

    public VPtr(T v) {
        this.v = v;
        this.next = this;
    }

    public T get() {
        return v;
    }

    public VPtr<T> nextElem() {
        return this.next;
    }

    public void add(T v) {
        VPtr<T> temp = new VPtr<T>(v);
        temp.next = this.next;
        this.next = temp;
    }
}
