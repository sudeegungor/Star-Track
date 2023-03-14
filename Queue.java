
public class Queue {
	private Object[] storage;
	private int front;
	private int back;
	
	public Queue(int size) {
		storage = new Object[size];
		front = 0;
		back = size - 1;
	}
	
	public int size() {
		int _size = back - front + 1;
		if (storage[front] == null) _size = 0;
		else if (_size <= 0) _size += storage.length;
		return _size;
	}
	
	public boolean isEmpty() {
		return size() == 0;
	}
	
	public boolean isFull() {
		return size() == storage.length;
	}
	
	public void enqueue(Object object) {
		if (isFull()) {
			System.out.println("queue overflow!");
		}
		else {
			back++;
			if (back == storage.length) back = 0;
			storage[back] = object;
		}
	}
	
	public Object dequeue() {
		if (isEmpty()) {
			System.out.println("queue is empty!");
			return null;
		}
		else {
			Object object = storage[front];
			storage[front] = null;
			front++;
			if (front == storage.length) front = 0;
			return object;
		}
	}
	
	public Object peek() {
		if (isEmpty()) {
			System.out.println("queue is empty!");
			return null;
		}
		else {
			return storage[front];
		}
	}
}
