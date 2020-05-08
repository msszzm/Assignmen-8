package eg.edu.alexu.csd.datastructure.mailServer;


public class PriorityQueue implements IPriorityQueue {
	
	
		private class Node {
		private Object data;
		private Node next;
		private int key;
		
		public Node(Object element,int Key) {
			data = element; 
			key = Key;
			next = null;
		}
		
		public Object getElement() { 
			return data;
		}
		
		public Object getKey() { 
			return key;
		}
		
		public Node getNext() { 
			return next; 
		}
		
		public void setNext(Node n) {
			next = n;
		}
	}
	
	private Node Min;
	private int Size;
	public PriorityQueue() {
		Min = null;
		Size = 0;
	}
	
	
	
	@Override
	public void insert(Object item, int key) {
		if(key<1)
			throw new RuntimeException("Invalid key");
		Node n = new Node(item,key);
		Size++;
		if(Size == 1)
			Min = n;
		else {
			if((int)Min.getKey()>key) {
				n.setNext(Min);
				Min = n;
			}
			else {
				Node temp = Min;
				while(temp.getNext()!=null) {								
					if((int)(temp.getNext()).getKey()<=key) {
						temp = temp.getNext();
					}
					else
						break;
				}
				n.setNext(temp.getNext());
				temp.setNext(n);
			}
		}
	}

	@Override
	public Object removeMin() {
		if(Size==0)
			throw new RuntimeException("Queue is empty");
		
		Node n = Min;
		Min = Min.getNext();
		Size--;
		return n.getElement();
	}

	@Override
	public Object min() {
		if(Size==0)
			throw new RuntimeException("Queue is empty");
		
		return Min.getElement();
	}

	@Override
	public boolean isEmpty() {
		return Size==0;
	}

	@Override
	public int size() {
		return Size;
	}

}
