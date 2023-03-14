
public class Number {
	private Position position;
	private char type;
	int trapCount;
	
	public Number(Position _position, char _type) {
		position = _position;
		type = _type;
		
		Main.gameArea[position.y][position.x] = this;
		trapCount = 0;
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
	
	public void move(int _direction) {
		if (trapCount <= 0) position = Main.moveObject(position, this, _direction);
	}
}
