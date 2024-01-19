package org.sk_dev;

import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Optional;

public class RingList<T extends Serializable> {
    private Optional<VPtr<T>> head;
    private VPtr<T> index = null;

    public RingList() {
        this.head = Optional.empty();
    }

    public void add(T v) {
        if (this.head.isEmpty()) {
            this.head = Optional.of(new VPtr<>(v));
            this.index = this.head.get();
        } else {
            this.index.next = new VPtr<>(v, head.get());
            this.index = this.index.next;
        }
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        head.ifPresentOrElse(
            headElem -> {
                VPtr<T> iter = headElem;
                for (; ; ) {
                    sb.append(iter.get().toString());
                    if (iter.next == headElem) {
                        break;
                    }
                    sb.append(",");
                    iter = iter.nextElem();
                }
            },
            () -> sb.append("*")
        );
        return sb.toString();
    }
}
