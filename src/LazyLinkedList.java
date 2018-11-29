
public class LazyLinkedList<T extends Comparable<T>> {

	private ListNode<T> head;
	private static long baseTime;
	public LazyLinkedList() {
		this.baseTime = System.nanoTime();
		this.head = new ListNode<>(Integer.MIN_VALUE);
		this.head.next = new ListNode<>(Integer.MAX_VALUE);
	}

	// validate that the pred.next is pointing to same current when it was first
	// called.
	private boolean validateNodes(ListNode<T> pred, ListNode<T> curr) {
		return !pred.marked && !curr.marked && pred.next == curr;
	}

	// validate whether an item can be replaced or tagged by some other thread
	// already to replace
	private boolean validateReplace(ListNode<T> pred, ListNode<T> curr) {
		return !pred.marked && !curr.marked && pred.next == curr && !pred.tagToReplace && !curr.tagToReplace;
	}

	// add method if only item is given , No Tag - default TagtoReplace is false.
	public boolean add(T item) {
		print("add", "started", item.toString(), "","");
		boolean res = add(item,false);
		print("add", "finished", item.toString(), "",String.valueOf(res));
		return res;
	}

	private boolean add(T item, boolean tagToReplace) {
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
					int yes=0;
					if (tagToReplace==false && validateNodes(pred, curr)) {
						yes=1;
						if (curr.key == key) {
							return false;
						}
					}
					else if (tagToReplace==true && validateReplace(pred, curr)) {
						yes=1;
						if (curr.key==key) {
							return false;
						} 
					}
					if(yes==1) {
						ListNode<T> nodeToInsert = new ListNode<>(item, tagToReplace);
						nodeToInsert.next = curr;
						pred.next = nodeToInsert;
						return true;
					}
					
				} finally { // always unlock
					curr.unlock();
				}
			} finally { // always unlock
				pred.unlock();
			}
		}
	}

	

	public boolean removeItem(T item) {
		print("remove", "started", item.toString(), "","");
		boolean res = remove(item);
		print("remove", "finished", item.toString(), "",String.valueOf(res));
		return res;
	}
	
	// delete item from the linkedlist.
	public boolean remove(T item) {
		Integer key = item.hashCode();
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
						if (curr.key != key) { 
							return false;
						} else {
							curr.marked = true; 
							pred.next = curr.next;
							return true;
						}
					}
				} finally {
					curr.unlock();
				}
			} finally { 
				pred.unlock();
			}

		}
	}

	// replace the item from linkedList
	public boolean replaceItem(T oldItem, T newItem) {
		print("replace", "started", oldItem.toString(), newItem.toString(),"");
		boolean res = replace(oldItem,newItem);
		print("replace","finished",oldItem.toString(), newItem.toString(),String.valueOf(res));
		return res;
	}

	public boolean replace(T oldItem, T newItem) {
		if(oldItem.compareTo(newItem)==0) {
			return false;
		}
		boolean itemModified = add(newItem, true);
		itemModified = itemModified | remove(oldItem);
		Integer key = newItem.hashCode();
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

	// contains implementation
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
	
	public void print(String operation, String status,String item1, String item2,String res) {
		System.out.println(
				String.format("%d, %s, %s, %s, %s, %s, %s ", 
				System.nanoTime() - baseTime, 
				operation,
				status,
				item1.toString(),
				item2.toString(),
				res,
				this.toString()));
	}

}
