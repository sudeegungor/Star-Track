
public class Stack {
	private Object[] storage;
	private int top;
	
	public Stack(int size) {
		storage = new Object[size];
		top = -1;
	}
	
	public int size() {
		return top + 1;
	}
	
	public boolean isEmpty() {
		return top == -1;
	}
	
	public boolean isFull() {
		return top == storage.length - 1;
	}
	
	public void push(Object object) {
		if (isFull()) {
			System.out.println("stack overflow!");
		}
		else {
			top++;
			storage[top] = object;
		}
	}
	
	public Object pop() {
		if (isEmpty()) {
			System.out.println("stack is empty!");
			return null;
		}
		else {
			Object object = storage[top]; 
			storage[top] = null;
			top--;
			return object;
		}
	}
	
	public Object peek() {
		if (isEmpty()) {
			System.out.println("stack is empty!");
			return null;
		}
		else {
			return storage[top];
		}
	}
}
