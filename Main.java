import enigma.console.TextAttributes;
import enigma.core.Enigma;

import java.util.Random;
import java.util.Scanner;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class Main {
	private static enigma.console.Console console = Enigma.getConsole("StarTrekWarpWars", 78, 31, 20, 2);
	
	public static Object[][] gameArea;		//0, 0 position is top left
	public static int gameTime;
	public static Queue queue;
	
	public static Player player;
	public static List numbers;
	public static List computers;
	public static List devices;
	
	private static KeyListener keyListener;
	private static boolean isKeyPressed;
	private static boolean isKeyPressing;
	private static int key;
	private static Random random;
	
	private static boolean restart;
	
	public static void main(String[] args) throws FileNotFoundException, InterruptedException{
		restart = true;
		random = new Random();
		
		while (restart) {
			gameArea = new Object[23][55];
			gameTime = 0;
			queue = new Queue(15);
			
			numbers = new List();
			computers = new List();
			devices = new List();
			
			File file = new File("maze.txt");
			try (Scanner scanner = new Scanner(file)) {
				int i = 0;
				while(scanner.hasNextLine()) {
					char[] temp = scanner.nextLine().toCharArray();
					for (int j = 0; j < gameArea[i].length; j++) {
						gameArea[i][j] = temp[j];
					}
					i++;
				}
			}
			Position _position = new Position(0, 0);
			while ((gameArea[_position.y][_position.x] instanceof Character && (char)gameArea[_position.y][_position.x] != ' ')) {
				_position = new Position(random.nextInt(0, 55), random.nextInt(0, 23));
			}
			player = new Player(_position);
			queue.enqueue('C');
			
			for (int i = 0; i < 34; i++) {
				if (i >= 14) QueueGet();
				QueueAdd();
			}

			isKeyPressed = false;
			isKeyPressing = false;
			keyListener = new KeyListener() {
				public void keyTyped(KeyEvent event) {}
				public void keyPressed(KeyEvent event) {
					if (!isKeyPressing) isKeyPressed = true;
					isKeyPressing = true;
					key = event.getKeyCode();
				}
				public void keyReleased(KeyEvent event) {
					isKeyPressing = false;
				}
			};
			console.getTextWindow().addKeyListener(keyListener);
			
			while (true) {
				console.getTextWindow().setCursorPosition(0, 0);
				gameTime++;
				if (gameTime % 5 == 0) {
					if (player.getEnergy() > 0 || gameTime % 10 == 0) {
						playerMove();
						if (gameTime % 20 == 0) player.decreaseEnergy();
					}
					if (gameTime % 10 == 0) {
						if (devices != null) {
							for (Object deviceObject : devices.GetArray()) {
								((Device)deviceObject).effect(true);
								if (gameTime % 20 == 0) ((Device)deviceObject).decreaseLifeTime();
							}
						}
						if (computers != null) {
							for (Object computerObject : computers.GetArray()) {
								((Computer)computerObject).move();
								((Computer)computerObject).trapCount = 0;
							}
						}
						for (Object numberObject : numbers.GetArray()) {
							if (((Number)numberObject).getType() != '1' && ((Number)numberObject).getType() != '2' && ((Number)numberObject).getType() != '3') ((Number)numberObject).move(random.nextInt(0, 4));
						}
						if (gameTime % 60 == 0) {
							QueueGet();
							QueueAdd();
						}
					}
					
					printScreen();
				}
				if (player.getHealth() <= 0) break;
				//player.setHealth(0);
				Thread.sleep(50);
			}
			while (true) {
				Scanner scanner = new Scanner(System.in);
				console.getTextWindow().setCursorPosition(0, 25);
				System.out.println("Game Over, Your Score is: " + (player.getScore() - Computer.getScore()) + "\nDo you want to play again? (y/n)");
				String answer = scanner.next();
				if (answer.equalsIgnoreCase("y")) {
					restart = true;
					console.getTextWindow().setCursorPosition(0, 25);
					System.out.println("                                                   \n                                                   \n                                                   \n                                                   ");
					break;
				}
				else if (answer.equalsIgnoreCase("n")) {
					restart = false;
					break;
				}
				else {
					System.out.println("Your input is wrong! please enter again");
					console.getTextWindow().setCursorPosition(0, 25);
					System.out.println("                                                   \n                                                   \n                                                   ");
				}
			}
			if (!restart){
				int xd = 0;
				while (xd <= 3) {
					console.getTextWindow().setCursorPosition(0, 25);
					System.out.print("                                                                                                                  ");
					console.getTextWindow().setCursorPosition(12, 25);
					for (int i = 0; i < 3; i++) {
						if (xd <= i) System.out.print(" o");
						else System.out.print("  ");
					}
					System.out.print(" Thanks for playing ");
					for (int i = 0; i < 3 - xd; i++) {
						System.out.print("o ");
					}
					System.out.println("                                                    \n                                                    \n    ");
					Thread.sleep(1000);
					xd++;
				}
				System.exit(0);
			}
		}
	}
	
	public static Position moveObject(Position _position, Object __type, int _direction) {// 0=left // 1=right // 2=up // 3=down //
		Position _positionToGo = new Position(_position.x, _position.y);
		switch (_direction) {
		case 0: {
			_positionToGo.x--;
			break;
		}
		case 1: {
			_positionToGo.x++;
			break;
		}
		case 2: {
			_positionToGo.y--;
			break;
		}
		case 3: {
			_positionToGo.y++;
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + _direction);
		}
		char _type = ' ';
		if (__type instanceof Player) _type = 'P';
		else if (__type instanceof Computer) _type = 'C';/*BURALAR!!!*/
		else if (__type instanceof Number) {
			_type = ((Number)__type).getType();
		}
		else if (__type instanceof Character) {
			_type = (char)__type;
		}
		if (_positionToGo.x > 0 && _positionToGo.x < gameArea[0].length - 1 && _positionToGo.y > 0 && _positionToGo.y < gameArea.length - 1 && ((!(gameArea[_positionToGo.y][_positionToGo.x] instanceof Character) && !(gameArea[_positionToGo.y][_positionToGo.x] instanceof Device)) || (gameArea[_positionToGo.y][_positionToGo.x] instanceof Character && ((char)gameArea[_positionToGo.y][_positionToGo.x]) != '#')) && (_type == 'P' || (_type == 'C' && !(gameArea[_positionToGo.y][_positionToGo.x] instanceof Computer)) || (gameArea[_positionToGo.y][_positionToGo.x] instanceof Player) || ((gameArea[_positionToGo.y][_positionToGo.x] instanceof Character) && ((char)gameArea[_positionToGo.y][_positionToGo.x]) == ' '))) {
			/*if (_type == 'C' && !(gameArea[_positionToGo.y][_positionToGo.x] instanceof Player) && (!(gameArea[_positionToGo.y][_positionToGo.x] instanceof Character) || ((char)gameArea[_positionToGo.y][_positionToGo.x]) != ' ')) {
				for (Object object : numbers.GetArray()) {
					if (_positionToGo.isEqual(((Number)object).getPosition())) {
						switch (_type) {
						case '1':{
							Computer.addScore(2);
							break;
						}
						case '2':{
							Computer.addScore(10);
							break;
						}
						case '3':{
							Computer.addScore(30);
							break;
						}
						case '4':{
							Computer.addScore(100);
							break;
						}
						case '5':{
							Computer.addScore(300);
							break;
						}
						}
						numbers.Remove(object);
					}
				}
			}*/
			
			if (gameArea[_positionToGo.y][_positionToGo.x] instanceof Number) {
				char _numberValue = ((Number)gameArea[_positionToGo.y][_positionToGo.x]).getType();
				if (_type == 'P') {
					if (_numberValue != '1' && Character.isDigit(_numberValue) && !player.pack.isFull()) {
						if (!player.pack.isEmpty()) {
							char _item = (char)player.pack.peek();
							if (Character.isDigit(_item)) {
								player.pack.pop();
								if(_numberValue == _item) {
									if(_numberValue == '2') {
										player.setEnergy(player.getEnergy() + 30);
									}
									else if(_numberValue == '3') {
										player.pack.push('=');
									}
									else if(_numberValue == '4') {
										player.setEnergy(player.getEnergy() + 240);	
									}
									else if(_numberValue == '5') {
										player.pack.push('*');
									}
								}
							}
							else {
								player.pack.push(_numberValue);
							}
						}
						else {
							player.pack.push(_numberValue);
						}
					}
					switch (_numberValue) {
					case '1':{
						player.addScore(1);
						break;
					}
					case '2':{
						player.addScore(5);
						break;
					}
					case '3':{
						player.addScore(15);
						break;
					}
					case '4':{
						player.addScore(50);
						break;
					}
					case '5':{
						player.addScore(150);
						break;
					}
					}
				}
				if (_type == 'C') {
					switch (_numberValue) {
					case '1':{
						Computer.addScore(2);
						break;
					}
					case '2':{
						Computer.addScore(10);
						break;
					}
					case '3':{
						Computer.addScore(30);
						break;
					}
					case '4':{
						Computer.addScore(100);
						break;
					}
					case '5':{
						Computer.addScore(300);
						break;
					}
					}
				}
				numbers.Remove((Number)gameArea[_positionToGo.y][_positionToGo.x]);
			}
			if (_type == 'P' || !player.getPosition().isEqual(_position)) {
				gameArea[_position.y][_position.x] = ' ';
				
				if (_type == 'P') {
					for (Object _object : computers.GetArray()) {
						if (((Computer)_object).getPosition().isEqual(player.getPosition())) {
							gameArea[_position.y][_position.x] = _object;
							break;
						}
					}
				}
			}
			_position = _positionToGo;
			if (_type == 'P' || !player.getPosition().isEqual(_position)) gameArea[_position.y][_position.x] = __type;
		}
		return _position;
	}/*

	public static Position moveObjectt(Position _position, Object __type, Position _positionToGo) {// 0=left // 1=right // 2=up // 3=down //
		char _type = ' ';
		if (__type instanceof Player) _type = 'P';
		else if (__type instanceof Computer) _type = 'C';
		else if (__type instanceof Number) {
			_type = ((Number)__type).getType();
		}
		else if (__type instanceof Character) {
			_type = (char)__type;
		}
		Position positionToGo = new Position(_positionToGo);
		if (_positionToGo.x > 0 && _positionToGo.x < gameArea[0].length - 1 && _positionToGo.y > 0 && _positionToGo.y < gameArea.length - 1 && ((!(gameArea[_positionToGo.y][_positionToGo.x] instanceof Character) && !(gameArea[_positionToGo.y][_positionToGo.x] instanceof Device)) || (gameArea[_positionToGo.y][_positionToGo.x] instanceof Character && ((char)gameArea[_positionToGo.y][_positionToGo.x]) != '#')) && (_type == 'P' || (_type == 'C' && !(gameArea[_positionToGo.y][_positionToGo.x] instanceof Computer)) || (gameArea[_positionToGo.y][_positionToGo.x] instanceof Player) || ((gameArea[_positionToGo.y][_positionToGo.x] instanceof Character) && ((char)gameArea[_positionToGo.y][_positionToGo.x]) == ' '))) {
			if (_type == 'P' || !player.getPosition().isEqual(_position)) gameArea[_position.y][_position.x] = ' ';
			_position = positionToGo;
			if (_type == 'P' || !player.getPosition().isEqual(_position)) gameArea[_position.y][_position.x] = __type;
		}
		return _position;
	}*/
	
	public static void put(Position _position, Object __type, int _direction, boolean _isTrap) {// 0=left // 1=right // 2=up // 3=down //
		Position _positionToPut = new Position(_position.x, _position.y);
		switch (_direction) {
		case 0: {
			_positionToPut.x--;
			break;
		}
		case 1: {
			_positionToPut.x++;
			break;
		}
		case 2: {
			_positionToPut.y--;
			break;
		}
		case 3: {
			_positionToPut.y++;
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + _direction);
		}
		Device newDevice = new Device(_positionToPut, _isTrap);
		devices.Add(newDevice);
	}
	
	public static void QueueGet() {
		Position _position = new Position(0, 0);
		while ((!(gameArea[_position.y][_position.x] instanceof Character) || ((char)gameArea[_position.y][_position.x]) != ' ')) {
			_position = new Position(random.nextInt(0, 55), random.nextInt(0, 23));
		}
		char _type = (char)queue.dequeue();
		if (_type == '=') {
			Device newDevice = new Device(_position, true);
			devices.Add(newDevice);
		}
		else if (_type == '*') {
			Device newDevice = new Device(_position, false);
			devices.Add(newDevice);
		}
		else if (_type == 'C') {
			Computer newComputer = new Computer(_position);
			computers.Add(newComputer);
		}
		else {
			Number newNumber = new Number(_position, _type);
			numbers.Add(newNumber);
		}
	}
	
	public static void playerMove() {
		if(isKeyPressing || isKeyPressed) {
			switch (key) {
			case KeyEvent.VK_A: {
				if (!player.pack.isEmpty() && gameArea[player.getPosition().UpdatePosition(0).y][player.getPosition().UpdatePosition(0).x] instanceof Character && ((char)gameArea[player.getPosition().UpdatePosition(0).y][player.getPosition().UpdatePosition(0).x]) == ' ') {
					if ((char)player.pack.peek() == '=') player.put(0, true);
					else if ((char)player.pack.peek() == '*') player.put(0, false);
					player.pack.pop();
				}
				break;
			}
			case KeyEvent.VK_D: {
				if (!player.pack.isEmpty() && gameArea[player.getPosition().UpdatePosition(1).y][player.getPosition().UpdatePosition(1).x] instanceof Character && ((char)gameArea[player.getPosition().UpdatePosition(1).y][player.getPosition().UpdatePosition(1).x]) == ' ') {
					if ((char)player.pack.peek() == '=') player.put(1, true);
					else if ((char)player.pack.peek() == '*') player.put(1, false);
					player.pack.pop();
				}
				break;
			}
			case KeyEvent.VK_W: {
				if (!player.pack.isEmpty() && gameArea[player.getPosition().UpdatePosition(2).y][player.getPosition().UpdatePosition(2).x] instanceof Character && ((char)gameArea[player.getPosition().UpdatePosition(2).y][player.getPosition().UpdatePosition(2).x]) == ' ') {
					if ((char)player.pack.peek() == '=') player.put(2, true);
					else if ((char)player.pack.peek() == '*') player.put(2, false);
					player.pack.pop();
				}
				break;
			}
			case KeyEvent.VK_S: {
				if (!player.pack.isEmpty() && gameArea[player.getPosition().UpdatePosition(3).y][player.getPosition().UpdatePosition(3).x] instanceof Character && ((char)gameArea[player.getPosition().UpdatePosition(3).y][player.getPosition().UpdatePosition(3).x]) == ' ') {
					if ((char)player.pack.peek() == '=') player.put(3, true);
					else if ((char)player.pack.peek() == '*') player.put(3, false);
					player.pack.pop();
				}
				break;
			}
			case KeyEvent.VK_LEFT: {
				player.move(0);
				break;
			}
			case KeyEvent.VK_RIGHT: {
				player.move(1);
				break;
			}
			case KeyEvent.VK_UP: {
				player.move(2);
				break;
			}
			case KeyEvent.VK_DOWN: {
				player.move(3);
				break;
			}
			default:
				break;
			}

			isKeyPressed = false;
        }
	}
	
	public static void QueueAdd() {
		int _random = random.nextInt(1, 41);
		
		if (_random <= 12) {
			queue.enqueue('1');
		}
		else if (_random <= 20) {
			queue.enqueue('2');
		}
		else if (_random <= 26) {
			queue.enqueue('3');
		}
		else if (_random <= 31) {
			queue.enqueue('4');
		}
		else if (_random <= 35) {
			queue.enqueue('5');
		}
		else if (_random <= 37) {
			queue.enqueue('=');
		}
		else if (_random <= 38) {
			queue.enqueue('*');
		}
		else {
			queue.enqueue('C');
		}
	}
	
	public static void printScreen() {
		System.out.println();
		for (int i = 0; i < gameArea.length; i++) {
			System.out.print("  ");
			for (int j = 0; j < gameArea[i].length; j++) {
				
				if (gameArea[i][j] instanceof Player){
					boolean isPlayerGetDamage = false;
					for (Object _object : computers.GetArray()) {
						if (((Computer)_object).getPosition().isEqual(player.getPosition())) isPlayerGetDamage = true;
					}
					if (!isPlayerGetDamage) console.setTextAttributes(new TextAttributes(Color.CYAN, Color.BLACK));
					else console.setTextAttributes(new TextAttributes(Color.RED, Color.BLACK));
					System.out.print("Ψ");//ĦΨ
				}
				else if (gameArea[i][j] instanceof Computer){
					if (((Computer)gameArea[i][j]).isChasing) console.setTextAttributes(new TextAttributes(Color.RED, Color.BLACK));
					else console.setTextAttributes(new TextAttributes(Color.CYAN, Color.BLACK));
					System.out.print("¶");
				}
				else if (gameArea[i][j] instanceof Number){
					if (((Number)gameArea[i][j]).getType() == '1'){
						console.setTextAttributes(new TextAttributes(Color.GREEN, Color.BLACK));
						System.out.print("1");
					}
					else if (((Number)gameArea[i][j]).getType() == '2'){
						console.setTextAttributes(new TextAttributes(Color.BLUE, Color.BLACK));
						System.out.print("2");
					}
					else if (((Number)gameArea[i][j]).getType() == '3'){
						console.setTextAttributes(new TextAttributes(Color.LIGHT_GRAY, Color.BLACK));
						System.out.print("3");
					}
					else if (((Number)gameArea[i][j]).getType() == '4'){
						console.setTextAttributes(new TextAttributes(Color.ORANGE, Color.BLACK));
						System.out.print("4");
					}
					else if (((Number)gameArea[i][j]).getType() == '5'){
						console.setTextAttributes(new TextAttributes(Color.MAGENTA, Color.BLACK));
						System.out.print("5");
					}
				}
				else if (gameArea[i][j] instanceof Device){
					console.setTextAttributes(new TextAttributes(Color.YELLOW, Color.BLACK));
					System.out.print(((Device)gameArea[i][j]).getType());
				}
				else {
					console.setTextAttributes(new TextAttributes(Color.LIGHT_GRAY, Color.BLACK));
					if (gameArea[i][j] instanceof Character) {
						if (((char)gameArea[i][j]) == ' ') System.out.print(" ");
						else if (((char)gameArea[i][j]) == '#') System.out.print("░");
					}
				}
			}
			console.setTextAttributes(new TextAttributes(Color.WHITE, Color.BLACK));
			System.out.println(" ");
		}
		console.getTextWindow().setCursorPosition(65, 1);
		System.out.print("Input: ");
		console.getTextWindow().setCursorPosition(59, console.getTextWindow().getCursorY() + 1);
		System.out.print("╔═══════════════╗ ");
		console.getTextWindow().setCursorPosition(59, console.getTextWindow().getCursorY() + 1);
		System.out.print("<");
		for (int i = 0; i < 15; i++) {
			System.out.print(queue.peek());
			queue.enqueue(queue.dequeue());
		}
		System.out.print("<");
		console.getTextWindow().setCursorPosition(59, console.getTextWindow().getCursorY() + 1);
		System.out.print("╚═══════════════╝ ");
		
		Stack tempPack = new Stack(8);
		while (!player.pack.isEmpty()) {
			tempPack.push(player.pack.pop());
		}
		console.getTextWindow().setCursorPosition(59, console.getTextWindow().getCursorY() + 9);
		for (int i = 0; i < 8; i++) {
			char _item = ' ';
			if (!tempPack.isEmpty()) {
				_item = (char)tempPack.peek();
				player.pack.push(tempPack.pop());
			}

			System.out.print("      ║ " + _item + " ║");
			console.getTextWindow().setCursorPosition(59, console.getTextWindow().getCursorY() - 1);
		}
		console.getTextWindow().setCursorPosition(59, console.getTextWindow().getCursorY() + 9);
		System.out.print("      ╚═══╝");
		console.getTextWindow().setCursorPosition(59, console.getTextWindow().getCursorY() + 1);
		System.out.println("    P.Backpack");
		console.getTextWindow().setCursorPosition(59, console.getTextWindow().getCursorY() + 2);
		System.out.print("P.Energy: ");
		if (player.getEnergy() < 10) System.out.println(" " + player.getEnergy());
		else System.out.println(player.getEnergy());
		console.getTextWindow().setCursorPosition(59, console.getTextWindow().getCursorY() + 1);
		System.out.println("P.Score: " + player.getScore());
		console.getTextWindow().setCursorPosition(59, console.getTextWindow().getCursorY() + 1);
		System.out.println("P.Life: " + player.getHealth());
		console.getTextWindow().setCursorPosition(59, console.getTextWindow().getCursorY() + 2);
		System.out.println("C.Score: " + Computer.getScore());
		console.getTextWindow().setCursorPosition(59, console.getTextWindow().getCursorY() + 2);
		System.out.println("Time: " + gameTime / 20);
	}
}
