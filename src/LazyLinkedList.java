

public class LazyLinkedList<T extends Comparable<T>> {

	private ListNode<T> head;
	
	
	public LazyLinkedList() {
		this.head = new ListNode<>(Integer.MIN_VALUE);
		this.head.next = new ListNode<>(Integer.MAX_VALUE);
	}
	
	//validate that the pred.next is pointing to same current when it was first called.
	private boolean validateNodes(ListNode<T> pred, ListNode<T> curr) {
		return !pred.marked && !curr.marked && pred.next == curr;
	}
	
	//validate whether an item can be replaced or tagged by some other thread already to replace
	private boolean validateReplace(ListNode<T> pred, ListNode<T> curr) {
		return !pred.marked && !curr.marked && pred.next == curr && !pred.tagToReplace && !curr.tagToReplace;
	}
	
	//add method if only item is given , No Tag - default TagtoReplace is false.
	public boolean add(T item) {
		int key = item.hashCode();
		while(true) {
			ListNode<T> pred = this.head;
			ListNode<T> curr = head.next;
			while(curr.key < key) {
				pred = curr;
				curr = curr.next;
			}
			pred.lock();
			try {
				curr.lock();
				try {
					if(validateNodes(pred,curr)) {
						if(curr.key == key) {
							return false;
						}else {
							ListNode<T> nodeToInsert = new ListNode<>(item);
							nodeToInsert.next = curr;
							pred.next = nodeToInsert;
							return true;
						}
					}
				}finally {
					curr.unlock();
				}
			}finally {
				pred.unlock();
			}

		}

	}
	
	
	
	//add an item while tagging it to replace another item
	private boolean add(T item,boolean tagToReplace) {
		int key = item.hashCode();
		while(true) {
			ListNode<T> pred = this.head;
			ListNode<T> curr = head.next;
			while(curr.key < key) {
				pred = curr;
				curr = curr.next;
			}
			pred.lock();
			try {
				curr.lock();
				try {
					if(validateReplace(pred,curr)) {
						if(curr.listItem.compareTo(item) < 0) {
							return false;
						}else {
							ListNode<T> nodeToInsert = new ListNode<>(item,tagToReplace);
							nodeToInsert.next = curr;
							pred.next = nodeToInsert;
							return true;

						}
					}
				}finally {
					curr.unlock();
				}
			}finally {
				pred.unlock();
			}

		}
	}
	
	
	//delelte item from the linkedlist.
	
	public boolean remove(T item) {
		int key = item.hashCode();
		while (true) {
			ListNode<T> pred = this.head;
			ListNode<T> curr = head.next;
			while (curr.key < key) {
				pred = curr;
				curr = curr.next;
			}
			pred.lock();
			try {
				curr.lock();
				try {
					if (validateNodes(pred, curr)) {
						if (curr.key != key) { // present
							return false;
						} else { // absent
							curr.marked = true; // logically remove
							pred.next = curr.next; // physically remove
							return true;
						}
					}
				} finally { // always unlock curr
					curr.unlock();
				}
			} finally { // always unlock pred
				pred.unlock();
			}

		}
	}
	
	//replace the item from linkedList
	
	public boolean replace(T oldItem, T newItem) {
		boolean itemModified = add(newItem, true);
		itemModified = itemModified | remove(oldItem);
		int key = newItem.hashCode();
		while(true) {
			ListNode<T> pred = this.head;
			ListNode<T> curr = head.next;
			while(curr.key != key) {
				pred = curr;
				curr = curr.next;
			}
			pred.lock();
			try {
				curr.lock();
				try {
					curr.tagToReplace = false;
					return itemModified;
				} finally {
					curr.unlock();
				}
			} finally {
				pred.unlock();
			}
		}
	}
	
	//contains implementation
	public boolean contains(T item) {
		int key = item.hashCode();
		ListNode<T> curr = this.head;
		while (curr.key < key)
			curr = curr.next;
		return curr.key == key && !curr.marked && !curr.tagToReplace;
	}
	
	
	@Override
	public synchronized String toString() {
		ListNode<T> ptr = head.next;
		StringBuilder sb = new StringBuilder("[ ");
		while (ptr.listItem != null) {
			sb.append(ptr.listItem.toString() + " ");
			ptr = ptr.next;
		}
		sb.append("]");
		return sb.toString();
	}
	



}