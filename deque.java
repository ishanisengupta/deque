import tester.Tester;
import java.util.function.Predicate;

// represents either a sentinel or a node
abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  // returns the size of a deque
  abstract int size();

  // adds an element to the head of a deque
  public void addAtHead(T data) {
    new Node<T>(data, this, this.next);
  }

  // adds an element to the tail of a deque
  public void addAtTail(T data) {
    new Node<T>(data, this.prev, this);
  }

  // removes an element from the head of a deque
  abstract T removeFromHead();

  // removes an element from the tail of a deque
  abstract T removeFromTail();

  // removes an element from a deque
  public T remove(T t) {
    this.next.prev = this.prev;
    this.prev.next = this.next;
    return t;
  }

  // returns the node which returns true for the predicate
  abstract ANode<T> find(Predicate<T> pred);

  // removes the specified node from a deque
  abstract void removeNode(ANode<T> node);

}

// represents a node with data
class Node<T> extends ANode<T> {
  T data;

  // null constructor
  Node(T data) {
    this.data = data;
    this.next = null;
    this.prev = null;
  }

  // convenience constructor
  Node(T data, ANode<T> prev, ANode<T> next) {
    if (prev == null || next == null) {
      throw new IllegalArgumentException("Null nodes not accepted.");
    }
    this.data = data;
    this.next = next;
    this.prev = prev;
    prev.next = this;
    next.prev = this;
  }

  // returns the size of a node
  public int size() {
    return 1 + this.next.size();
  }

  // removes first node from list
  public T removeFromHead() {
    return this.remove(this.data);
  }

  //   removes the last element
  public T removeFromTail() {
    return this.remove(this.data);
  }

  // returns the node that returns true for the given predicate
  ANode<T> find(Predicate<T> pred) {
    if (pred.test(this.data)) {
      return this;
    }
    else {
      return this.next.find(pred);
    }
  }

  // removes the specified node
  public void removeNode(ANode<T> node) {
    if (this.next == node.next && this.prev == node.prev) {
      this.remove(this.data);
    }
    else {
      this.next.removeNode(node);
    }
  }
}

// represents an sentinel
class Sentinel<T> extends ANode<T> {
  //  ANode<T> next;
  //  ANode<T> prev;

  Sentinel() {
    this.next = this;
    this.prev = this;
  }

  // returns size of a sentinel
  public int size() {
    return 0;
  }

  // removes an element from the head of a sentinel
  public T removeFromHead() {
    throw new RuntimeException("Cannot run on empty list.");
  }

  // removes an element from the tail of a sentinel
  public T removeFromTail() {
    throw new RuntimeException("Cannot run on empty list.");
  }

  // returns the node that returns true for the predicate
  ANode<T> find(Predicate<T> pred) {
    return this;
  }

  public void removeNode(ANode<T> node) {
    return;
  }

}

// represents a deque
class Deque<T> {
  Sentinel<T> header;

  // zero arg constructor
  Deque() {
    this.header = new Sentinel<T>();
  }

  // convenience constructor
  Deque(Sentinel<T> header) {
    this.header = header;
  }

  // counts number of nodes in a deque
  int size() {
    return this.header.next.size();
  }

  // adds a node to the head
  void addAtHead(T data) {
    this.header.addAtHead(data);

  }

  // adds a node to the tail
  void addAtTail(T data) {
    this.header.addAtTail(data);
  }

  // removes the first node
  T removeFromHead() {
    return this.header.prev.removeFromHead();
  }

  //removes the first node
  T removeFromTail() {
    return this.header.next.removeFromTail();
  }

  // returns the node which returns true for the specified predicate
  ANode<T> find(Predicate<T> pred) {
    if (this.header.next == null) {
      return this.header;
    }
    else {
      return this.header.next.find(pred);
    }
  }

  // removes the specified node from the deque
  void removeNode(ANode<T> node) {
    this.header.next.removeNode(node);
  }

}

// predicate which determines if a node is equal to "abc"
class EqualsAbc implements Predicate<String> {

  public boolean test(String t) {
    return t.equals("abc");
  }
}

//predicate which determines if a node is equal to "cde"
class EqualsCde implements Predicate<String> {

  public boolean test(String t) {
    return t.equals("cde");
  }
}

// predicate which determines if a node has length 1
class LengthOne implements Predicate<String> {

  public boolean test(String t) {
    return t.length() == 1;
  }
}

// represents examples of a deque
class ExamplesDeque {

  Deque<String> emptyDeque;

  Sentinel<String> sentinel1;
  Deque<String> Deque1;
  Node<String> abc;
  Node<String> bcd;
  Node<String> cde;
  Node<String> def;

  Sentinel<String> sentinel2;
  Deque<String> Deque2;
  Node<String> g;
  Node<String> a;
  Node<String> l;

  Sentinel<String> sentinel3;
  Deque<String> Deque3;
  Node<String> abc1;
  Node<String> bcd2;

  Predicate<String> equalsAbc;
  Predicate<String> lengthOne;
  Predicate<String> equalsCde;

  void initData() {

    emptyDeque = new Deque<String>();

    sentinel1 = new Sentinel<String>();
    Deque1 = new Deque<String>(sentinel1);
    abc = new Node<String>("abc", sentinel1, sentinel1);
    bcd = new Node<String>("bcd", abc, sentinel1);
    cde = new Node<String>("cde", bcd, sentinel1);
    def = new Node<String>("def", cde, sentinel1);

    sentinel2 = new Sentinel<String>();
    Deque2 = new Deque<String>(sentinel2);
    g = new Node<String>("g", sentinel2, sentinel2);
    a = new Node<String>("a", g, sentinel2);
    l = new Node<String>("l", a, sentinel2);

    sentinel3 = new Sentinel<String>();
    Deque3 = new Deque<String>(sentinel3);
    abc1 = new Node<String>("abc", sentinel3, sentinel3);
    bcd2 = new Node<String>("bcd", abc1, sentinel3);

    equalsAbc = new EqualsAbc();
    lengthOne = new LengthOne();
    equalsCde = new EqualsCde();
  }

  boolean testSize(Tester t) {
    this.initData();
    return t.checkExpect(abc.size(), 4) && t.checkExpect(sentinel1.size(), 0)
        && t.checkExpect(Deque1.size(), 4) && t.checkExpect(l.size(), 1)
        && t.checkExpect(Deque2.size(), 3);
  }

  void testAddAtHead(Tester t) {
    this.initData();

    t.checkExpect(emptyDeque.size(), 0);
    emptyDeque.addAtHead("def");
    t.checkExpect(emptyDeque.size(), 1);
    emptyDeque.addAtHead("cde");
    t.checkExpect(emptyDeque.size(), 2);
    emptyDeque.addAtHead("bcd");
    t.checkExpect(emptyDeque.size(), 3);
    emptyDeque.addAtHead("abc");
    t.checkExpect(emptyDeque, Deque1);

    this.initData();
    t.checkExpect(emptyDeque.size(), 0);
    emptyDeque.addAtHead("l");
    t.checkExpect(emptyDeque.size(), 1);
    emptyDeque.addAtHead("a");
    t.checkExpect(emptyDeque.size(), 2);
    emptyDeque.addAtHead("g");
    t.checkExpect(emptyDeque.size(), 3);
    t.checkExpect(emptyDeque, Deque2);
  }

  void testAddAtTail(Tester t) {
    this.initData();

    t.checkExpect(emptyDeque.size(), 0);
    emptyDeque.addAtTail("abc");
    t.checkExpect(emptyDeque.size(), 1);
    emptyDeque.addAtTail("bcd");
    t.checkExpect(emptyDeque.size(), 2);
    emptyDeque.addAtTail("cde");
    t.checkExpect(emptyDeque.size(), 3);
    emptyDeque.addAtTail("def");
    t.checkExpect(emptyDeque, Deque1);
  }

  boolean testRemoveFromHead(Tester t) {
    this.initData();

    return t.checkExpect(Deque2.size(), 3) && t.checkExpect(Deque2.removeFromHead(), "l")
        && t.checkExpect(Deque2.size(), 2) && t.checkExpect(Deque2.removeFromHead(), "a")
        && t.checkExpect(Deque2.size(), 1) && t.checkExpect(Deque2.removeFromHead(), "g")
        && t.checkExpect(Deque2.size(), 0);
  }

  boolean testFind(Tester t) {
    this.initData();

    return t.checkExpect(Deque1.find(equalsAbc), this.abc)
        && t.checkExpect(Deque2.find(lengthOne), this.g)
        && t.checkExpect(Deque1.find(equalsCde), this.cde)
        && t.checkExpect(Deque1.find(lengthOne), Deque1.header)
        && t.checkExpect(Deque2.find(equalsAbc), Deque2.header);
  }

  boolean testNodeException(Tester t) {
    return t.checkConstructorException(new IllegalArgumentException("Null nodes not accepted."),
        "Node", "nullNode", null, null);
  }

  boolean testRemoveFromHeadException(Tester t) {
    return t.checkException(new RuntimeException("Cannot run on empty list."), "Sentinel",
        "removeFromHead", sentinel1);
  }

  boolean testRemoveFromTailException(Tester t) {
    return t.checkException(new RuntimeException("Cannot run on empty list."), "Sentinel",
        "removeFromTail", sentinel1);
  }

  void testRemoveNode(Tester t) {
    t.checkExpect(Deque1.size(), 4);
    Deque1.removeNode(def);
    t.checkExpect(Deque1.size(), 3);
    Deque1.removeNode(cde);
    t.checkExpect(Deque1.size(), 2);
    Deque1.removeNode(bcd);
    t.checkExpect(Deque1.size(), 1);
    Deque1.removeNode(abc);
    t.checkExpect(Deque1, new Deque<String>(sentinel1));
  }

}