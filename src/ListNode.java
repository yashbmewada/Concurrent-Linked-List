import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ListNode<T extends Comparable<T>> {

	// item stored in list
	T listItem;

	// hash code as key
	Integer key;

	// next item in the list
	ListNode<T> next;

	// marked determines if deleted or not
	boolean marked;

	// tag for marking a node for replacement
	boolean tagToReplace;

	// lock to perform operation
	Lock lock;

	// constructor if only item is provided (default tagToReplace false);
	ListNode(T item) { // usual constructor
		this.listItem = item;
		this.key = item.hashCode();
		this.next = null;
		this.marked = false;
		this.tagToReplace = false;
		this.lock = new ReentrantLock();
	}

	// constructor for sentinel nodes
	ListNode(int key) {
		this.listItem = null;
		this.key = key;
		this.next = null;
		this.marked = false;
		this.tagToReplace = false;
		this.lock = new ReentrantLock();
	}

	// constructor for actual list item
	ListNode(T item, boolean tagToReplace) {
		this.listItem = item;
		this.key = item.hashCode();
		this.next = null;
		this.marked = false;
		this.tagToReplace = tagToReplace;
		this.lock = new ReentrantLock();
	}

	// Lock Node
	void lock() {
		lock.lock();
	}

	// Unlock Node
	void unlock() {
		lock.unlock();
	}

}
