
public class Player {
	private Position position;
	
	private int health;
	private int energy;
	private int score;
	
	public Stack pack;

	public Player(Position _position){
		position = _position;
		
		health = 5;
		energy = 50;
		score = 0;
		
		pack = new Stack(8);

		Main.gameArea[position.y][position.x] = this;
	}
	

	
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position _position) {
		position = _position;
	}

	public int getHealth() {
		return health;
	}
	
	public void setHealth(int _health) {
		health = _health;
	}
	
	public void damage(int _value) {
		health -= _value;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	public void setEnergy(int _energy) {
		energy = _energy;
	}
	
	public void decreaseEnergy() {
		if (energy > 0) energy--;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int _score) {
		score = _score;
	}
	
	public void addScore(int _value) {
		score += _value;
	}
	
	public void move(int _direction) {
		position = Main.moveObject(position, this, _direction);
	}
	
	public void put(int _direction, boolean _isTrap) {
		Main.put(position, this, _direction, _isTrap);
	}
}
