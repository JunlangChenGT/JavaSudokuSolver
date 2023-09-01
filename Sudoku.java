package h11;

import java.util.ArrayList;

/* 
 * find a solution to a Sudoku problem
 * @author	Biagioni, Edoardo
 * @date	October 23, 2013
 * @missing	fillSudoku, to be implemented by the students in ICS 211
 */
public class Sudoku {

	/* check that the sudoku rules hold in this sudoku puzzle.
	 * cells that contain 0 are not checked.
	 * @param the sudoku to be checked
	 * @param whether to print the error found, if any
	 * @return true if this sudoku obeys all of the sudoku rules, otherwise false
	 */
	public static boolean checkSudoku (int [] [] sudoku, boolean printErrors)
	{
		if (sudoku.length != 9) {
			if (printErrors) {
				System.out.println ("sudoku has " + sudoku.length +
						" rows, should have 9");
			}
			return false;
		}
		for (int i = 0; i < sudoku.length; i++) {
			if (sudoku [i].length != 9) {
				if (printErrors) {
					System.out.println ("sudoku row " + i + " has " +
							sudoku [i].length + " cells, should have 9");
				}
				return false;
			}
		}
		/* check each cell for conflicts */
		for (int i = 0; i < sudoku.length; i++) {
			for (int j = 0; j < sudoku.length; j++) {
				int cell = sudoku [i] [j];
				if (cell == 0) {
					continue;   /* blanks are always OK */
				}
				if ((cell < 1) || (cell > 9)) {
					if (printErrors) {
						System.out.println ("sudoku row " + i + " column " + j +
								" has illegal value " + cell);
					}
					return false;
				}
				/* does it match any other value in the same row? */
				for (int m = 0; m < sudoku.length; m++) {
					if ((j != m) && (cell == sudoku [i] [m])) {
						if (printErrors) {
							System.out.println ("sudoku row " + i + " has " + cell +
									" at both positions " + j + " and " + m);
						}
						return false;
					}
				}
				/* does it match any other value it in the same column? */
				for (int k = 0; k < sudoku.length; k++) {
					if ((i != k) && (cell == sudoku [k] [j])) {
						if (printErrors) {
							System.out.println ("sudoku column " + j + " has " + cell +
									" at both positions " + i + " and " + k);
						}
						return false;
					}
				}
				/* does it match any other value in the 3x3? */
				for (int k = 0; k < 3; k++) {
					for (int m = 0; m < 3; m++) {
						int testRow = (i / 3 * 3) + k;   /* test this row */
						int testCol = (j / 3 * 3) + m;   /* test this col */
						if ((i != testRow) && (j != testCol) &&
								(cell == sudoku [testRow] [testCol])) {
							if (printErrors) {
								System.out.println ("sudoku character " + cell + " at row " +
										i + ", column " + j + 
										" matches character at row " + testRow +
										", column " + testCol);
							}
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	/* convert the sudoku to a printable string
	 * @param the sudoku to be converted
	 * @param whether to check for errors
	 * @return the printable version of the sudoku
	 */
	public static String toString (int [] [] sudoku, boolean debug) {
		if ((! debug) || (checkSudoku (sudoku, true))) {
			String result = "";
			for (int i = 0; i < sudoku.length; i++) {
				if (i % 3 == 0) {
					result = result + "+-------+-------+-------+\n";
				}
				for (int j = 0; j < sudoku.length; j++) {
					if (j % 3 == 0) {
						result = result + "| ";
					}
					if (sudoku [i] [j] == 0) {
						result = result + "  ";
					} else {
						result = result + sudoku [i] [j] + " ";
					}
				}
				result = result + "|\n";
			}
			result = result + "+-------+-------+-------+\n";
			return result;
		}
		return "illegal sudoku";
	}

//	public int[] getPossibleNumbers() {
//		ArrayList<Integer> possibleNumbers;
//		for(int i = 0; i < 9; i++)
//		{
//			for(int x = 0; x < 9; x++)
//			{
//				if(sudoku[row][x] == i) possibleNumbers.add(i);
//			}
//		}
//	}



	/* find an assignment of values to sudoku cells that makes the sudoku valid
	 * @param the sudoku to be filled
	 * @return whether a solution was found 
	 *    if a solution was found, the sudoku is filled in with the solution
	 *    if no solution was found, restores the sudoku to its original value
	 */
	public static boolean fillSudoku (int [] [] sudoku)
	{
		// step 1 base case check if the sudoku is filled and valid
		//if the sudoku is filled, return the result of checkSudoku(sudoku, false)
		if(SudokuTest.isFilled(sudoku))
		{
			if(checkSudoku(sudoku, false))
			{
				return true;
			}
		}

		//step 2 find next empty cell: search for a cell that is 0
		int row = 0;
		int col = 0;
		boolean hitEnd = false;

		while( (sudoku[row][col] != 0) && (hitEnd == false) )
		{
			if(row == 8 && col == 8) hitEnd = true;
			else if(col != 8) col++;
			else
			{
				col = 0;
				row++;
			}


		}
		//System.out.println(row + " " + col);
		//System.out.println(toString(sudoku, false));

		// step 3 find possible values for empty cell:
		java.util.ArrayList<Integer> values = getValues(sudoku, row, col);

		//step 4 test values: try all possible values from step 3 and recursively call fillSudoku() with each possible value
		//when we recursively call fillSudoku(), it will return true if we found a valid solution, and false otherwise
		//if it fillSudoku() returns true return true for this method call as well
		// otherwise reset cell value to 0 and try next possible value
		for(int i : values)
		{
			sudoku[row][col] = i;
			if(fillSudoku(sudoku)) return true;
			
			sudoku[row][col] = 0;
			
		}
		


		// step 5 return false: if we can't return true in the above then we can't solve the sudoku
		return false;
	}

	public static java.util.ArrayList<Integer> getValues(int[][] sudoku, int row, int col)
	{
		boolean[] values = new boolean[10];

		//check for invalid values in the row
		for(int i = 0; i < sudoku.length; i++)
		{
			int cell = sudoku[row][i];
			if(cell != 0) values[cell] = true;
		}

		//check for invalid values in the column
		for(int i = 0; i < sudoku.length; i++)
		{
			int cell = sudoku[i][col];
			if(cell != 0) values[cell] = true;
		}

		//check for invalid values in the 3x3 square
		int startRow = row - (row % 3);
		int startCol = col - (col % 3);
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				int cell = sudoku[startRow + i][startCol + j];
				if(cell != 0) values[cell] = true;
			}
		}

		java.util.ArrayList<Integer> list = new java.util.ArrayList<>();
		for(int i = 1; i < values.length; i++)
		{
			if(!values[i]) list.add(i);
		}
		return list;
	}
}
