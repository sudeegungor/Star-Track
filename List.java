
public class List {
	Object[] objects;
	public List() {
		objects = new Object[0];
	}
	
	public void Add(Object _object) {
		if (_object == null) return;
		Object[] temp = objects;
		objects = new Object[temp.length + 1];
		for (int i = 0; i < temp.length; i++) {
			objects[i] = temp[i];
		}
		objects[temp.length] = _object;
	}
	
	public void Remove(Object _object) {
		Object[] temp = objects;
		objects = new Object[temp.length - 1];
		boolean find = false;
		for (int i = 0; i < temp.length - 1; i++) {
			if (!find && temp[i] == _object) find = true;
			if (!find) {
				objects[i] = temp[i];
			}
			else {
				objects[i] = temp[i + 1];
			}
		}
	}
	
	public boolean ContainsPosition(Object _object) {
		boolean contains = false;
		for (Object object : objects) {
			if (((Position)object).isEqual((Position)_object)) {
				contains = true;
				break;
			}
		}
		return contains;
	}
	
	public void Clear() {
		objects = new Object[0];
	}
	
	public Object[] GetArray() {
		return objects;
	}
	
	public void SetArray(Object[] _objects) {
		objects = new Object[_objects.length];
		for (int i = 0; i < _objects.length; i++) {
			objects[i] = _objects[i];
		}
	}
}
