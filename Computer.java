import java.util.Random;

public class Computer {
	Random random;
	
	private static int score = 0;
	
	private Position position;
	private Position targetPosition;
	int trapCount;
	
	boolean isChasing;
	
	public Computer(Position _position) {
		random = new Random();
		
		position = _position;
		
		Main.gameArea[position.y][position.x] = this;
		
		trapCount = 0;
	}
	
	public static int getScore() {
		return score;
	}
	
	public static void setScore(int _score) {
		score = _score;
	}
	
	public static void addScore(int _score) {
		score += _score;
	}
	
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position _position) {
		position = _position;
	}
	
	public void setTarget() {
		targetPosition = new Position(position);
		if (Math.abs(Main.player.getPosition().x - position.x) < 10 && Math.abs(Main.player.getPosition().y - position.y) < 10) {
			isChasing = true;
			targetPosition = Main.player.getPosition();
		}
		else {
			isChasing = false;
			int searchAreaSize = 3;
			while (targetPosition.isEqual(position)) {
				for (int x = position.x - searchAreaSize / 2; targetPosition.isEqual(position) && x < position.x + searchAreaSize / 2 + 1; x++) {
					if (x >= 0 && x < Main.gameArea[0].length) {
						if (position.y + searchAreaSize / 2 >= 0 && position.y + searchAreaSize / 2 < Main.gameArea.length && Main.gameArea[position.y + searchAreaSize / 2][x] instanceof Number) targetPosition = new Position(x, position.y + searchAreaSize / 2);
						else if (position.y - searchAreaSize / 2 >= 0 && position.y - searchAreaSize / 2 < Main.gameArea.length && Main.gameArea[position.y - searchAreaSize / 2][x] instanceof Number) targetPosition = new Position(x, position.y - searchAreaSize / 2);
					}
				}
				for (int y = position.y - searchAreaSize / 2 - 1; targetPosition.isEqual(position) && y < position.y + searchAreaSize / 2; y++) {
					if (y >= 0 && y < Main.gameArea.length) {
						if (position.x + searchAreaSize / 2 >= 0 && position.x + searchAreaSize / 2 < Main.gameArea[0].length && Main.gameArea[y][position.x + searchAreaSize / 2] instanceof Number) targetPosition = new Position(position.x + searchAreaSize / 2, y);
						else if (position.x - searchAreaSize / 2 >= 0 && position.x - searchAreaSize / 2 < Main.gameArea[0].length && Main.gameArea[y][position.x - searchAreaSize / 2] instanceof Number) targetPosition = new Position(position.x - searchAreaSize / 2, y);
					}
				}
				searchAreaSize += 2;
				if (searchAreaSize > 55) break;
			}
		}
	}
	
	public void move() {
		if (trapCount <= 0) {
			setTarget();
			
			if (!targetPosition.isEqual(position)) {
				int randomWay = random.nextInt(0, 2);
				
				Position oldPosition = new Position(position);
				
				if (randomWay == 0 && targetPosition.x - position.x < 0)
					position = Main.moveObject(position, this, 0);
				if (randomWay == 0 && position.isEqual(oldPosition) && targetPosition.x - position.x > 0)
					position = Main.moveObject(position, this, 1);
				if (position.isEqual(oldPosition) && targetPosition.y - position.y < 0)
					position = Main.moveObject(position, this, 2);
				if (position.isEqual(oldPosition) && targetPosition.y - position.y > 0)
					position = Main.moveObject(position, this, 3);
				if (position.isEqual(oldPosition) && randomWay != 0 && targetPosition.x - position.x < 0)
					position = Main.moveObject(position, this, 0);
				if (position.isEqual(oldPosition) && randomWay != 0 && targetPosition.x - position.x > 0)
					position = Main.moveObject(position, this, 1);
			}
			
			if (position.isEqual(Main.player.getPosition())) Main.player.damage(1);
		}
	}
}
