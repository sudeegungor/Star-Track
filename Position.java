
public class Position {
	public int x;
	public int y;
	
	public Position(int _x, int _y) {
		x = _x;
		y = _y;
	}
	
	public Position (Position _position) {
		x = _position.x;
		y = _position.y;
	}
	
	public Position UpdatePosition(int _direction) {// 0->left // 1->right // 2->up // 3->down // 
		switch (_direction) {
		case 0: {
			return new Position(x - 1, y);
		}
		case 1: {
			return new Position(x + 1, y);
		}
		case 2: {
			return new Position(x, y - 1);
		}
		case 3: {
			return new Position(x, y + 1);
		}
		default:
			return new Position(x, y);
		}
	}
	
	public boolean isEqual(Position _position) {
		return (x == _position.x && y == _position.y);
	}
}
