package project212.phase2.AVL;

class Node<T> {
    public T data;
    public Node<T> next;
    public Node(T val) { data = val; next = null; }
}

public class LinkedList<T> {
    private Node<T> head;
    private Node<T> current;

    public LinkedList() { head = current = null; }

    public boolean empty() { return head == null; }

    public boolean last() { return current != null && current.next == null; }

    public boolean full() { return false; }

    public void findfirst() { current = head; }

    public void findenext() { if (current != null) current = current.next; }

    public T retrieve() { return current != null ? current.data : null; }

    public void update(T val) { if (current != null) current.data = val; }

    public void insert(T val) {
        Node<T> tmp;
        if (empty()) {
            current = head = new Node<T>(val);
        } else {
            tmp = current.next;
            current.next = new Node<T>(val);
            current = current.next;
            current.next = tmp;
        }
    }

    public void remove() {
        if (current == null || head == null) return;
        if (current == head) {
            head = head.next;
        } else {
            Node<T> tmp = head;
            while (tmp.next != null && tmp.next != current) tmp = tmp.next;
            if (tmp.next == current) tmp.next = current.next;
        }
        if (current.next == null) current = head;
        else current = current.next;
    }

    public void display() {
        if (head == null) {
            System.out.println("empty list");
            return;
        }
        Node<T> p = head;
        while (p != null) {
            System.out.println(p.data + "  ");
            p = p.next;
        }
    }

    public boolean exists(T e) {
        Node<T> exes = head;
        while (exes != null) {
            if (exes.data.equals(e)) return true;
            exes = exes.next;
        }
        return false;
    }

    public void addLast(T a) {
        Node<T> temp = new Node<>(a);
        if (head == null) {
            head = temp;
            current = temp;
        } else {
            Node<T> p = head;
            while (p.next != null) p = p.next;
            p.next = temp;
            current = temp;
        }
    }

    public int size() {
        int n = 0;
        Node<T> p = head;
        while (p != null) {
            n++;
            p = p.next;
        }
        return n;
    }
}
