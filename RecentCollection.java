
import java.util.*;

class ReverseCollection<E> extends AbstractCollection<E> implements Iterable<E> {
    private final List<E> elements;

    public ReverseCollection(Collection<E> original) {
        if (original instanceof RandomAccess) {
            elements = (List<E>) original;
        } else {
            elements = new ArrayList<E>(original);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private int index = size();

            @Override
            public boolean hasNext() {
                return index > 0;
            }

            @Override
            public E next() throws NoSuchElementException {
                if (!hasNext()) throw new NoSuchElementException();
                --index;
                return elements.get(index);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public int size() {
        return elements.size();
    }
}

public class RecentCollection<E> implements Iterable<E> {
    private static final int MAXIMUM = 10;
    private final int maximumElements;

    private final Collection<E> recent =
        Collections.newSetFromMap(
                new LinkedHashMap<E, Boolean>(32, 0.7f, true) {
                    protected boolean removeEldestEntry(
                            Map.Entry<E, Boolean> eldest) {
                        return size() > maximumElements;
                            }
                }
                );

    private RecentCollection() {
        this(MAXIMUM);
    }

    private RecentCollection(int maximumElements) {
        this.maximumElements = maximumElements;
    }

    public void add(E element) {
        recent.add(element);
    }

    public void clear() {
        recent.clear();
    }

    @Override
    public Iterator<E> iterator() {
        return new ReverseCollection<E>(recent).iterator();
    }

    public static void main(String[] args) {
        RecentCollection<String> rc = new RecentCollection<String>();
        rc.add("F");
        rc.add("A");
        rc.add("E");
        rc.add("A");
        rc.add("D");
        rc.add("C");
        rc.add("B");
        rc.add("A");

        Iterator<String> rcIterator = rc.iterator();
        while (rcIterator.hasNext()) {
            try { System.out.println(rcIterator.next()); }
            catch (NoSuchElementException e) {System.out.println("No Such Element");}
        }
    }
}
