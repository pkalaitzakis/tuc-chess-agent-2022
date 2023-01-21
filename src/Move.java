package myPackage;

import java.util.ArrayList;

public class Move {
		// board after move was made
		private String[][] moveBoard = new String[7][5];
	    
		// move coordinates
		private int x1, y1, x2, y2;
		
		// move value
	    private double value;
	    
	    private int madeBy, wins;

	    private Move parent;
	    private long timesVisited;
	    
	    // wScore, bScore during this move (computed outside move)
	    private double wScore, bScore;
	    
	    // Weighted Evaluation of each player's state:
	    //  for each active pawn, the respective player is awarded with 1 point
	    //  for each active rook, the respective player is awarded with 3 points
	    //  for each active king, the respective player is awarded with 7 points
	    private double wEval, bEval;
		
		// Sum of total active pieces on the current moveBoard without 
		// taking into consideration the two kings
		private int pieces;
		
		
		// flag that signals the capturing of a piece by a rook or king during this Move instance
		private int flag;
		
		// flag for each player's king state: 1 -> active | 0 -> captured
		private int wKing, bKing;
		
		// special terminal state flag (only two kings remaining)
		private Boolean twoKings;
	    
		// move constructor
	    public Move(String[][] board, int x1, int y1, int x2, int y2) {
	    	String piece;
	    	pieces = 0;
	    	wins = 0; // count wins for monte carlo
	    	timesVisited = 0; // also only for monte carlo
	    	
	    	// copy board
	        for (int i = 0; i < 7; i++) {
	        	for (int j = 0; j < 5; j++) { 
	        		// copy board piece
	        		this.moveBoard[i][j] = board[i][j];
	        		
	        		// examine first letter of piece
	        		piece = Character.toString(board[i][j].charAt(0));
	        		
	        		if (piece.equals("W")) { // it's a white piece
	        			// get second letter
	        			piece = Character.toString(board[i][j].charAt(1));
	        			if (piece.equals("P")) { // pawn
	        				pieces++;
	        				wEval++;
	        			}
	        			else if (piece.equals("R")) { // rook
	        				pieces++;
	        				wEval=wEval+3;
	        			} else { // king
	        				wEval=wEval+7;
	        				wKing++;
	        			}
	        		} else if (piece.equals("B")) { // it's a black piece
	        			// get second letter
		        		piece = Character.toString(board[i][j].charAt(1));
		        		if (piece.equals("P")) { // pawn
		        			pieces++;
		        			bEval++;
		        		}
		        		else if (piece.equals("R")) { //
		        			pieces++;
		        			bEval=bEval+3;
		        		} else { // king
		        			bEval=bEval+7;
		        			bKing++;
		        		}
	        		}
	        	}
	        }
	        
	        this.x1=x1;
	        this.x2=x2;
	        this.y1=y1;
	        this.y2=y2;
	        this.twoKings = false;
	        this.flag=0;
	        this.value=0;
	    }
	    
	    // checks if current move is a leaf state node (game-ending state)
		public boolean checkLeafState() {
			if (wKing+bKing==1) {
				return true;
			} else if(wKing+bKing==2 && pieces==0){
				twoKings = true;
				return true;
			} else
				return false;
		}
		
		public void simulateMove(int prizeX, int prizeY, int totalPrizes) {		
			String chessPiece = moveBoard[x1][y1];
			boolean rowFlag = false;
			
			if((x2==6 && chessPiece.equals("BP")) || (x2==0 && chessPiece.equals("WP"))) {
				moveBoard[x2][y2] = " ";
				moveBoard[x1][y1] = " ";
				rowFlag = true;
			}
			// update player pieces according to the nature of the move
			if (rowFlag==false) {
				moveBoard[x2][y2] = chessPiece;
				moveBoard[x1][y1] = " ";
			}
				
			// check if a prize has been added in the game
			if(prizeX != totalPrizes)
				moveBoard[prizeX][prizeY] = "P";
		}
  
	 // move evaluation function
		public double evaluate(int playerColour){
			double finalWEval, finalBEval;
			
			if ((bKing==0 || twoKings==true) && wScore>bScore) {
				finalWEval = wEval + wScore + 1000;
			} else if (bKing==0 || twoKings==true && wScore<bScore) {
				finalWEval = wEval + wScore - 1000;
			} else {
				finalWEval = wEval + wScore;
			}
			if ((wKing==0 || twoKings==true) && bScore>wScore) {
				finalBEval = bEval + bScore + 1000;
			} else if ((wKing==0 || twoKings==true) && bScore<wScore) {
				finalBEval = bEval + bScore - 1000;
			} else {
				finalBEval = bEval + bScore;
			}
			
			if (playerColour==0) {
				return finalWEval - finalBEval;
			} else{
				return finalBEval - finalWEval;
				}
		}
		
		public void increaseValue(double value) {
			this.value = this.value + value;
		}
		
		public double upperConfidenceBound() {
			return value+2*(1/Math.sqrt(2))*Math.sqrt(Math.log(parent.timesVisited/timesVisited));
		}
		
		
		public String getMove() {
			return Integer.toString(x1)+Integer.toString(y1)+Integer.toString(x2)+Integer.toString(y2);
		}

		public int getX1() {
			return x1;
		}

		public void setX1(int x1) {
			this.x1 = x1;
		}

		public int getY1() {
			return y1;
		}

		public void setY1(int y1) {
			this.y1 = y1;
		}

		public int getX2() {
			return x2;
		}

		public void setX2(int x2) {
			this.x2 = x2;
		}

		public int getY2() {
			return y2;
		}

		public void setY2(int y2) {
			this.y2 = y2;
		}

		public String[][] getMoveBoard() {
			return moveBoard;
		}

		public void setMoveBoard(String[][] moveBoard) {
			this.moveBoard = moveBoard;
		}

		public double getValue() {
			return value;
		}

		public void setValue(double value) {
			this.value = value;
		}

		public int getwKing() {
			return wKing;
		}

		public void setwKing(int wKing) {
			this.wKing = wKing;
		}

		public int getbKing() {
			return bKing;
		}

		public void setbKing(int bKing) {
			this.bKing = bKing;
		}

		public int getPieces() {
			return pieces;
		}

		public void decreasePieces() {
			this.pieces--;
		}
		
		public void decreasebEval(double value) {
			this.bEval=this.bEval - value;
		}
		
		public void decreasewEval(double value) {
			this.wEval=this.wEval - value;
		}

		public int getFlag() {
			return flag;
		}

		public void setFlag(int flag) {
			this.flag = flag;
		}

		public Boolean getLeaf() {
			return twoKings;
		}

		public double getwScore() {
			return wScore;
		}

		public void setwScore(double wScore) {
			this.wScore = wScore;
		}

		public double getbScore() {
			return bScore;
		}

		public void setbScore(double bScore) {
			this.bScore = bScore;
		}

		public void setLeaf(Boolean leaf) {
			this.twoKings = leaf;
		}
		
		 public void increaseWins() {
		    	this.wins++;
		    }
		    
		    public int getWins() {
				return wins;
			}

			public void setWins(int wins) {
				this.wins = wins;
			}

			public int getMadeBy() {
				return madeBy;
			}

			public void setMadeBy(int madeBy) {
				this.madeBy = madeBy;
			}

			private ArrayList<Move> children= new ArrayList<Move>();
		    
		    
			public ArrayList<Move> getChildren() {
				return children;
			}

			public void setChildren(ArrayList<Move> children) {
				this.children = children;
			}

			public Move getParent() {
				return this.parent;
			}

			public void setParent(Move parent) {
				this.parent = parent;
			}

			public long getTimesVisited() {
				return timesVisited;
			}

			public void increaseVisited() {
				this.timesVisited++;
			}
		
		
	}