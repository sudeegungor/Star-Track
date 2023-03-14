
public class Device {
	private Position position;
	private boolean isTrap;
	private char type;
	private int lifeTime;

	public Device(Position _position, boolean _isTrap) {
		position = _position;
		isTrap = _isTrap;
		if (isTrap) type = '=';
		else type = '*';
		lifeTime = 25;
		
		Main.gameArea[position.y][position.x] = this;
	}
	
	public Position getPosition() {
		return position;
	}
	
	public void setPosition(Position _position) {
		position = _position;
	}
	
	public char getType() {
		return type;
	}
	
	public void setType(char _type) {
		type = _type;
	}
	
	public void decreaseLifeTime() {
		lifeTime--;
		if (lifeTime <= 0) {
			effect(false);
			Main.gameArea[position.y][position.x] = ' ';
			Main.devices.Remove(this);
		}
	}
	
	public void effect(boolean _effect) {
		Object effectedObject;
		effectedObject = Main.gameArea[position.y + 1][position.x];
		effectObject(effectedObject, _effect);
		effectedObject = Main.gameArea[position.y + 1][position.x + 1];
		effectObject(effectedObject, _effect);
		effectedObject = Main.gameArea[position.y][position.x + 1];
		effectObject(effectedObject, _effect);
		effectedObject = Main.gameArea[position.y - 1][position.x + 1];
		effectObject(effectedObject, _effect);
		effectedObject = Main.gameArea[position.y - 1][position.x];
		effectObject(effectedObject, _effect);
		effectedObject = Main.gameArea[position.y - 1][position.x - 1];
		effectObject(effectedObject, _effect);
		effectedObject = Main.gameArea[position.y][position.x - 1];
		effectObject(effectedObject, _effect);
		effectedObject = Main.gameArea[position.y + 1][position.x - 1];
		effectObject(effectedObject, _effect);
	}
	
	private void effectObject(Object effectedObject, boolean freeze) {
		if (isTrap) {
			if (effectedObject instanceof Number) {
				if (freeze) ((Number)effectedObject).trapCount++;
				else ((Number)effectedObject).trapCount--;
			}
			else if (effectedObject instanceof Computer) {
				if (freeze) ((Computer)effectedObject).trapCount++;
				else ((Computer)effectedObject).trapCount--;
			}
		}
		else {
			if (effectedObject instanceof Number) {
				Main.gameArea[((Number)effectedObject).getPosition().y][((Number)effectedObject).getPosition().x] = ' ';
				Main.numbers.Remove(effectedObject);
			}
			else if (effectedObject instanceof Computer) {
				Main.gameArea[((Computer)effectedObject).getPosition().y][((Computer)effectedObject).getPosition().x] = ' ';
				Main.computers.Remove(effectedObject);
			}
		}
	}
}