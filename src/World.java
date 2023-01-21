package myPackage;

public class World
{
	private String[][] board = null;
	private int rows = 7;
	private int columns = 5;
	private int plColour = 0;
	private int noPrize = 9;
	
	public World()
	{
		this.board = new String[rows][columns];
		
		/* represent the board
		
		BP|BR|BK|BR|BP
		BP|BP|BP|BP|BP
		--|--|--|--|--
		P |P |P |P |P 
		--|--|--|--|--
		WP|WP|WP|WP|WP
		WP|WR|WK|WR|WP
		*/
		
		for(int i=0; i<rows; i++)
			for(int j=0; j<columns; j++)
				board[i][j] = " ";
		
		// setting the black player's chess parts
		
		// black pawns
		for(int j=0; j<columns; j++)
			board[1][j] = "BP";
		
		board[0][0] = "BP";
		board[0][columns-1] = "BP";
		
		// black rooks
		board[0][1] = "BR";
		board[0][columns-2] = "BR";
		
		// black king
		board[0][columns/2] = "BK";
		
		// setting the white player's chess parts
		
		// white pawns
		for(int j=0; j<columns; j++)
			board[rows-2][j] = "WP";
		
		board[rows-1][0] = "WP";
		board[rows-1][columns-1] = "WP";
		
		// white rooks
		board[rows-1][1] = "WR";
		board[rows-1][columns-2] = "WR";
		
		// white king
		board[rows-1][columns/2] = "WK";
		
		// setting the prizes
		for(int j=0; j<columns; j++)
			board[rows/2][j] = "P";
		
	}

	public void makeMove(int x1, int y1, int x2, int y2, int prizeX, int prizeY){
		String chessPiece = board[x1][y1];
		boolean rowFlag = false;
		
		// update player pieces according to the nature of the move
		
		if ((x2==0 && chessPiece.equals("WP")) || (x2==rows-1 && chessPiece.equals("BP"))){
			board[x2][y2] = " ";
			board[x1][y1] = " ";
			rowFlag = true;
		}
		
		if (rowFlag==false) {
			board[x2][y2] = chessPiece;
			board[x1][y1] = " ";
		}
		
		// check if a prize has been added in the game
		if(prizeX != noPrize)
			board[prizeX][prizeY] = "P";
			
	}
	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public int getPlColour() {
		return plColour;
	}

	public void setPlColour(int plColour) {
		this.plColour = plColour;
	}


	public int getNoPrize() {
		return noPrize;
	}

	public void setNoPrize(int noPrize) {
		this.noPrize = noPrize;
	}


	public void setBoard(String[][] board) {
		this.board = board;
	}


	public String[][] getBoard() {
		return board;
	}

}
