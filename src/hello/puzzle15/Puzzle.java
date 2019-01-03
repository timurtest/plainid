package hello.puzzle15;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Puzzle {
	
	private static int PUZZLE_SIZE = 4;
	private Map<Integer, Map<Integer, Integer>> matrix;
	private int size;
	private Random rand = new Random();
	private int startNumber = 1;
	private HashMap<Integer, Integer[]> numToIndex = new HashMap<>();
	private Set<Integer> numbers; 
	
	public Puzzle(int size) {
		this.size = size;
		int endNumber = size * size - 1;
		matrix = new HashMap<>();
		this.numbers = new HashSet<>();
		for (int i = 1; i <= size; i++) {
			matrix.put(i, new HashMap<>());
			for (int j = 1; j <= size; j++) {
				int randomNum = getIntRandom(endNumber);
				while(numbers.contains(randomNum) && numbers.size() < endNumber) {
					randomNum = getIntRandom(endNumber);
				}
				numbers.add(randomNum);
				if(i == size && j == size) {
					randomNum = 0;
				}
				setNumberIndex(i, j, randomNum);
				matrix.get(i).put(j, randomNum);
			}
		}
	}

	public void move (Integer number, String move) {
		if(!numbers.contains(number)) {
			System.out.println("ERROR: Invalid number " + number);
			return;
		}
		
		Integer rowNumber = numToIndex.get(number)[0];
		Integer columnNumber = numToIndex.get(number)[1];

		switch (move) {
		case "r":
			moveRight(number, rowNumber, columnNumber);
			break;
		case "l":
			moveLeft(number, rowNumber, columnNumber);
			break;
		case "d":
			moveDown(number, rowNumber, columnNumber);
			break;
		case "u":
			moveUp(number, rowNumber, columnNumber);
			break;

		default:
			System.out.println("ERROR: Unsupported move " + move);
			break;
		}
	}

	private void moveUp(Integer number, Integer rowNumber, Integer columnNumber) {
		if(rowNumber <= 1) {
			System.out.println("ERROR: You can't move " + number + " up at the moment");
			return;
		}
		boolean canMove = false;
		for(int i = 1; i < rowNumber; i++) {
			Integer num = matrix.get(i).get(columnNumber);
			canMove = num.equals(0);
			if(canMove) {
				break;
			}
		}
		if(!canMove) {
			System.out.println("ERROR: You can't move " + number + " up at the moment");
			return;
		}
		
		int index = 1;
		Integer currentNumber = matrix.get(index).get(columnNumber);
		while(index < rowNumber) {
			if(currentNumber.equals(0)) {
				swapValuesVerticaly(columnNumber, index, index + 1);
			}
			index++;
			currentNumber = matrix.get(index).get(columnNumber);
		}
	}

	private void moveDown(Integer number, Integer rowNumber, Integer columnNumber) {
		if(columnNumber > size) {
			System.out.println("ERROR: You can't move " + number + " down at the moment");
			return;
		}
		boolean canMove = false;
		for(int i = size; i > rowNumber; i--) {
			Integer num = matrix.get(i).get(columnNumber);
			canMove = num.equals(0);
			if(canMove) {
				break;
			}
		}
		if(!canMove) {
			System.out.println("ERROR: You can't move " + number + " down at the moment");
			return;
		}
		
		int index = size;
		Integer currentNumber = matrix.get(index).get(columnNumber);
		while(index > rowNumber) {
			if(currentNumber.equals(0)) {
				swapValuesVerticaly(columnNumber, index, index - 1);
			}
			index--;
			currentNumber = matrix.get(index).get(columnNumber);
		}
	}

	private void moveLeft(Integer number, Integer rowNumber, Integer columnNumber) {
		if(columnNumber <= 1) {
			System.out.println("ERROR: You can't move " + number + " left at the moment");
			return;
		}
		boolean canMove = false;
		for(int i = 1; i < columnNumber; i++) {
			Integer num = matrix.get(rowNumber).get(i);
			canMove = num.equals(0);
			if(canMove) {
				break;
			}
		}
		if(!canMove) {
			System.out.println("ERROR: You can't move " + number + " left at the moment");
			return;
		}
		
		int index = 1;
		Integer currentNumber = matrix.get(rowNumber).get(index);
		while(index < columnNumber) {
			if(currentNumber.equals(0)) {
				swapValuesHorizontaly(rowNumber, index, index + 1);
			}
			index++;
			currentNumber = matrix.get(rowNumber).get(index);
		}
	}

	private void moveRight(Integer number, Integer rowNumber, Integer columnNumber) {
		if(columnNumber > size) {
			System.out.println("ERROR: You can't move " + number + " right at the moment");
			return;
		}
		boolean canMove = false;
		for(int i = size; i > columnNumber; i--) {
			Integer num = matrix.get(rowNumber).get(i);
			canMove = num.equals(0);
			if(canMove) {
				break;
			}
		}
		if(!canMove) {
			System.out.println("ERROR: You can't move " + number + " right at the moment");
			return;
		}
		
		int index = size;
		Integer currentNumber = matrix.get(rowNumber).get(index);
		while(index > columnNumber) {
			if(currentNumber.equals(0)) {
				swapValuesHorizontaly(rowNumber, index, index - 1);
			}
			index--;
			currentNumber = matrix.get(rowNumber).get(index);
		}
	}

	private void swapValuesVerticaly(Integer columnNumber, int rowNumber, int newIndex) {
		Integer movedNumber = matrix.get(newIndex).get(columnNumber);
		matrix.get(rowNumber).put(columnNumber, movedNumber);
		matrix.get(newIndex).put(columnNumber, 0);
		setNumberIndex(rowNumber, columnNumber, movedNumber);
		setNumberIndex(newIndex, columnNumber, 0);
	}

	private void swapValuesHorizontaly(Integer rowNumber, int columnNumber, int newIndex) {
		Integer movedNumber = matrix.get(rowNumber).get(newIndex);
		matrix.get(rowNumber).put(columnNumber, movedNumber);
		matrix.get(rowNumber).put(newIndex, 0);
		setNumberIndex(rowNumber, columnNumber, movedNumber);
		setNumberIndex(rowNumber, newIndex, 0);
	}

	private void setNumberIndex(int rowNum, int colNum, Integer currentNumber) {
		if(numToIndex.get(currentNumber) == null) {
			numToIndex.put(currentNumber, new Integer[2]);
		}
		numToIndex.get(currentNumber)[0] = rowNum;
		numToIndex.get(currentNumber)[1] = colNum;
	}
	
	private int getIntRandom(int endNumber) {
		return rand.nextInt((endNumber - startNumber) + 1) + startNumber;
	}
	
	public void print() {
		for(Map<Integer, Integer> row : matrix.values()) {
			String rowString = "";
			for(Integer number : row.values()) {
				rowString += (number.compareTo(10) < 0) ? (" " + number + " ") : (number + " ");
			}
			System.out.println(rowString);
		}
	}
	
	public boolean hasWon() {
		int expectedNext = 1;
		int expectedLast = size * size;
		for (int i = 1; i <= size; i++) {
			for (int j = 1; j <= size; j++) {
				if(expectedNext == expectedLast) {
					return true;
				}
				if(!matrix.get(i).get(j).equals(expectedNext++)) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	public static void main(String[] args) {
		
		
		Puzzle puzzle = new Puzzle(PUZZLE_SIZE);
		puzzle.print();
		
		while(true) {
			System.out.println();
			System.out.println("Enter move in format one leter for direction and then number to move, like u10 to move number 10 up");
			System.out.println("Possible moves letters u/d/l/r");
			System.out.println("Zero means free cell");
			System.out.println("Enter x for exit");
			String response = new Scanner(System.in).next();
			if(response.equals("x")) {
				break;
			}
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			String move = response.substring(0, 1);
			try {
				int number = Integer.parseInt(response.substring(1));
				puzzle.move(number, move);
				puzzle.print();
			} catch (NumberFormatException e) {
				System.out.println("Invalid input " + response);
			}
			if(puzzle.hasWon()) {
				System.out.println("CONGRATULATIONS!!! You won!!!");
				return;
			}
		}
	}

}