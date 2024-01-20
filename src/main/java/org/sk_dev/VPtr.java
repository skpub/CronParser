package org.sk_dev;

class VPtr<T> {
    T v;
    VPtr<T> next;

    VPtr(T v) {
        this.v = v;
        this.next = this;
    }
    VPtr(T v, VPtr<T> next) {
        this.v = v;
        this.next = next;
    }

    T get() {
        return v;
    }

    VPtr<T> nextElem() {
        return this.next;
    }

    void add(T v) {
        VPtr<T> temp = new VPtr<T>(v);
        temp.next = this.next;
        this.next = temp;
    }
}
