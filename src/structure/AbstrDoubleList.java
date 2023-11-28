package structure;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class AbstrDoubleList<T> implements IAbstrDoubleList<T> {

    private Node<T> first;
    private Node<T> actual;
    private Node<T> last;
    private int numOfNodes;

    private static class Node<T> {
        private final T data;
        private Node<T> next;
        private Node<T> previous;

        public Node(T data) {
            this.data = data;
            this.next = null;
            this.previous = null;
        }

        public Node(T data, Node<T> previous, Node<T> next) {
            this.data = data;
            this.next = next;
            this.previous = previous;
        }
    }

    public AbstrDoubleList() {
        this.numOfNodes = 0;
        this.first = null;
        this.last = null;
        this.actual = null;
    }


    @Override
    public void zrus() {
        first = null;
        last = null;
        actual = null;
        numOfNodes = 0;
    }

    @Override
    public boolean jePrazdny() {
        return numOfNodes == 0;
    }

    @Override
    public void vlozPrvni(T data) {
        if (isDataNotNull(data)) {
            Node<T> node = new Node<>(data);
            if (jePrazdny()) {
                setSingleNodedList(node);
            } else {
                node = new Node<>(data, null, first);
                first.previous = node;
                first = node;
                actual = node;
            }
        } else {
            System.err.println("vlozPrvni(): Data jsou null!");
            return;
        }
        numOfNodes++;
    }

    @Override
    public void vlozPosledni(T data) {
        if (isDataNotNull(data)) {
            Node<T> node = new Node<>(data);
            if (jePrazdny()) {
                setSingleNodedList(node);
            } else {
                node = new Node<>(data, last, null);
                last.next = node;
                last = node;
                actual = node;
            }
        } else {
            System.err.println("vlozPosledni(): Data jsou null!");
            return;
        }
        numOfNodes++;
    }

    @Override
    public void vlozNaslednika(T data) {
        if (isDataNotNull(data)) {
            if (actual == null) {   // pokud se jedná o první prvek v seznamu
                vlozPrvni(data);
            } else {
                Node<T> node = new Node<>(data, actual, actual.next);
                if (actual.next != null) {  // pokud má aktualní prvek následníka
                    actual.next.previous = node;
                }
                actual.next = node;
                if (actual == last) {   // pokud je aktuální zároveň poslední prvek
                    last = node;
                }
            }
        } else {
            System.err.println("vlozNaslednika(): Data jsou null!");
            return;
        }
        numOfNodes++;
    }

    @Override
    public void vlozPredchudce(T data) {
        if (isDataNotNull(data)) {
            if (actual == null) {    // pokud se jedná o první prvek v seznamu
                vlozPrvni(data);
            } else {
                Node<T> node = new Node<>(data, actual.previous, actual);
                if (actual.previous != null) {  // pokud má aktualní prvek předchudce
                    actual.previous.next = node;
                }
                actual.previous = node;
                if (actual == first) {  // pokud je aktuální zároveň první prvek
                    first = node;
                }
            }
        } else {
            System.err.println("vlozPredchudce(): Data jsou null!");
            return;
        }
        numOfNodes++;
    }

    @Override
    public T zpristupniAktualni() {
        if (actual != null) {
            return actual.data;
        }
        System.err.println("Aktualni je null!");
        return null;
    }

    @Override
    public T zpristupniPrvni() {
        if (first != null) {
            actual = first;
            return first.data;
        }
        System.err.println("Prvni je null!");
        return null;
    }

    @Override
    public T zpristupniPosledni() {
        if (last != null) {
            actual = last;
            return last.data;
        }
        System.err.println("Posledni je null!");
        return null;
    }

    @Override
    public T zpristupniNaslednika() {
        T data;
        if (actual.next != null) {
            data = actual.next.data;
            actual = actual.next;
            return data;
        }
        System.err.println("Naslednik je null!");
        return null;
    }

    @Override
    public T zpristupniPredchudce() {
        T data;
        if (actual.previous != null) {
            data = actual.previous.data;
            actual = actual.previous;
            return data;
        }
        System.err.println("Predchudce je null!");
        return null;
    }

    @Override
    public T odeberAktualni() {
        if (jePrazdny()) {
            System.err.println("odeberAktualni(): Není co odebírat protože seznam je prázdný!");
            return null;
        }
        if (actual != null) {
            T removedData = actual.data;
            if (actual == first) {
                odeberPrvni();
            } else if (actual == last) {
                odeberPosledni();
            }
            if (actual.previous != null && actual.next != null) {   // Pokud má aktuální prvek předchůdce i následnovníka
                actual.previous.next = actual.next;
                actual.next.previous = actual.previous;
            }
            if (actual.next != null && actual.previous == null) {   // Pokud má aktuální prvek jen následovníka
                actual.next.previous = null;
            }
            if (actual.previous != null && actual.next == null) {   // Pokud má aktuální prvek jen předchůdce
                actual.previous.next = null;
            }
            actual = first;
            numOfNodes--;
            return removedData;
        }
        return null;
    }

    @Override
    public T odeberPrvni() {
        if (jePrazdny()) {
            System.err.println("odeberPrvni(): Není co odebírat protože seznam je prázdný!");
            return null;
        }
        if (first != null) {
            T removedData = first.data;
            if (this.numOfNodes == 1) {   // Je tam sám => můžu zrušit seznam
                zrus();
                return removedData;
            }

            if (first.next != null) {   // Pokud má první prvek následovnika (neni jediny v seznamu)
                first = first.next;
            }
            if (first == actual && actual.next != null) {   // Pokud je aktuální prvek zároveň první a má následníka
                actual = actual.next;
            }
            numOfNodes--;
            return removedData;
        }
        return null;
    }

    @Override
    public T odeberPosledni() {
        if (jePrazdny()) {
            System.err.println("odeberPosledni(): Není co odebírat protože seznam je prázdný!");
            return null;
        }
        T removedData = last.data;
        if (this.numOfNodes == 1) {   // Je tam sám => můžu zrušit seznam
            zrus();
            return removedData;
        }

        if (last.previous != null) {   // Pokud má poslední prvek předchudce (neni jediny v seznamu)
            last.previous.next = null;
            last = last.previous;
        }
        if (last == actual && actual.previous != null) {   // Pokud je aktuální prvek zároveň poslední a má předchůdce
            actual = actual.previous;
        }
        numOfNodes--;
        return removedData;
    }

    @Override
    public T odeberNaslednika() {
        if (jePrazdny()) {
            System.err.println("odeberNaslednika(): Není co odebírat protože seznam je prázdný!");
            return null;
        }
        if (actual.next != null) {
            T removedData = actual.next.data;
            if (actual.next.next != null) {
                actual.next = actual.next.next;
                actual.next.previous = actual;
            } else if (actual.next == last) {   // Následovník je zároveň poslední
                actual.next = null;
                last = actual;
            } else {
                actual.next = null;
            }
            numOfNodes--;
            return removedData;
        }
        return null;
    }

    @Override
    public T odeberPredchudce() {
        if (jePrazdny()) {
            System.err.println("odeberPredchudce(): Není co odebírat protože seznam je prázdný!");
            return null;
        }
        if (actual.previous != null) {
            T removedData = actual.previous.data;
            if (actual.previous.previous != null) {
                actual.previous = actual.previous.previous;
                actual.previous.next = actual;
            } else if (actual.previous == first) {  // Předchůdce je zároveň první
                actual.previous = null;
                first = actual;
            } else {
                actual.previous = null;
            }
            numOfNodes--;
            return removedData;
        }
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private Node<T> current = first;

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T data = current.data;
                current = current.next;
                return data;
            }

            @Override
            public boolean hasNext() {
                return current != null;
            }
        };
    }

    private void setSingleNodedList(Node<T> node) {
        this.first = node;
        this.last = node;
        this.actual = node;
    }

    private boolean isDataNotNull(T data) {
        return data != null;
    }
}
