package myPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Agent {
	private int totalPrizes = 9;
	private int plColour;
	private int opColour;

	private int initDepth = 8;
	private int currDepth = initDepth;
	private int depthCounter = 1;
	private boolean depthFlag;
	
	public ArrayList<Move> blackMoves(String[][] currentBoard)
	{	
		ArrayList<Move> availableMoves = new ArrayList<Move>();
		Move moveObj;
		String target = "";
		String piece = "";
		
		for (int i=6; i>=0; i--) {
			for (int j=0; j<5; j++) {
				piece = Character.toString(currentBoard[i][j].charAt(0));
				
				if (!piece.equals("B")) {
					continue;
				
				} else {
					piece = Character.toString(currentBoard[i][j].charAt(1));
					if(piece.equals("P")) {
						// check if it can move one vertical position ahead
						target = Character.toString(currentBoard[i+1][j].charAt(0));
						
						if(target.equals(" ") || target.equals("P"))
						{
							moveObj = new Move(currentBoard, i,j,i+1,j);					
							moveObj.simulateMove(totalPrizes, totalPrizes, totalPrizes);
							moveObj.setMadeBy(1);
							// check if black pawn reached last row
							if (i+1 == 6)
								moveObj.setValue(1);
							
							// check if black pawn obtained a prize
							if (target.equals("P"))
								moveObj.increaseValue(0.95);
							
							availableMoves.add(moveObj);
						}
						
						// check if it can move cross-wise to the left
						if(j!=0)
						{
							target = Character.toString(currentBoard[i+1][j-1].charAt(0));
							
							if ((target.equals("W"))){
								
								moveObj = new Move(currentBoard,i,j,i+1,j-1);
								moveObj.simulateMove(totalPrizes, totalPrizes, totalPrizes);
								moveObj.setMadeBy(1);
								if (currentBoard[i+1][j-1].equals("WK")) {
									moveObj.decreasewEval(7);
									moveObj.setValue(7);
									moveObj.setwKing(0);
								}
								else if (currentBoard[i+1][j-1].equals("WR")) {
									moveObj.decreasewEval(3);
									moveObj.setValue(3);
									moveObj.decreasePieces();
								}
								else {
									moveObj.decreasewEval(1);
									moveObj.setValue(1);
									moveObj.decreasePieces();
								}
								availableMoves.add(moveObj);
							}
						}
						
						// check if it can move crosswise to the right
						if(j!=4)
						{
							target = Character.toString(currentBoard[i+1][j+1].charAt(0));
							
							if(target.equals("W")) {
								moveObj = new Move(currentBoard,i,j,i+1,j+1);
								moveObj.simulateMove(totalPrizes, totalPrizes, totalPrizes);
								moveObj.setMadeBy(1);
								if (currentBoard[i+1][j+1].equals("WK")) {
									moveObj.decreasewEval(7);
									moveObj.setValue(7);
									moveObj.setwKing(0);
								}
								else if (currentBoard[i+1][j+1].equals("WR")) {
									moveObj.decreasewEval(3);
									moveObj.setValue(3);
									moveObj.decreasePieces();
								}
								else {
									moveObj.decreasewEval(1);
									moveObj.setValue(1);
									moveObj.decreasePieces();
								}	
								availableMoves.add(moveObj);
							}
						}
					}
					else if(piece.equals("R")) {
						for(int k=0; k<3; k++) {
							
							// check if it can move downwards
							if((i-(k+1)) < 0)
								break;
							
							target = Character.toString(currentBoard[i-(k+1)][j].charAt(0));
							
							if(target.equals("B"))
								break;
							
							moveObj = new Move(currentBoard,i, j, i-(k+1), j);
							moveObj.simulateMove(totalPrizes, totalPrizes, totalPrizes);
							moveObj.setMadeBy(1);
							if (currentBoard[i-(k+1)][j].equals("WK")){
								moveObj.decreasewEval(7);
								moveObj.setFlag(1);
								moveObj.setValue(7);
								moveObj.setwKing(0);
							}
							else if (currentBoard[i-(k+1)][j].equals("WR")){
								moveObj.decreasewEval(3);
								moveObj.setFlag(1);
								moveObj.setValue(3);
								moveObj.decreasePieces();
							}
							else if (currentBoard[i-(k+1)][j].equals("WP")){
								moveObj.decreasewEval(3);
								moveObj.setFlag(1);
								moveObj.setValue(1);
								moveObj.decreasePieces();
							}
							else if (currentBoard[i-(k+1)][j].equals("P"))
								moveObj.setValue(0.95);
							
							availableMoves.add(moveObj);
							
							// prevent detouring a chesspart to attack the other
							if(target.equals("W") || target.equals("P"))
								break;
						}
						
						// check if it can move downwards
						for(int k=0; k<3; k++) {
							if((i+(k+1)) == 7)
								break;
							
							target = Character.toString(currentBoard[i+(k+1)][j].charAt(0));
							
							if(target.equals("B"))
								break;
							
							moveObj = new Move(currentBoard,i,j,i+(k+1),j);
							moveObj.simulateMove(totalPrizes, totalPrizes, totalPrizes);
							moveObj.setMadeBy(1);
							
							if (currentBoard[i+(k+1)][j].equals("WK")){
								moveObj.decreasewEval(7);
								moveObj.setFlag(1);
								moveObj.setValue(7);
								moveObj.setwKing(0);
							}
							else if (currentBoard[i+(k+1)][j].equals("WR")){
								moveObj.decreasewEval(3);
								moveObj.setFlag(1);
								moveObj.setValue(3);
								moveObj.decreasePieces();
							}
							else if (currentBoard[i+(k+1)][j].equals("WP")){
								moveObj.decreasewEval(1);
								moveObj.setFlag(1);
								moveObj.setValue(1);
								moveObj.decreasePieces();
							}
							else if (currentBoard[i+(k+1)][j].equals("P"))
								moveObj.setValue(0.45);
							
							availableMoves.add(moveObj);
							
							// prevent detouring a chesspart to attack the other
							if(target.equals("W") || target.equals("P"))
								break;
						}
						
						// check if it can move on the left
						for(int k=0; k<3; k++)
						{
							if((j-(k+1)) < 0)
								break;
							
							target = Character.toString(currentBoard[i][j-(k+1)].charAt(0));
							
							if(target.equals("B"))
								break;
							
							moveObj = new Move(currentBoard,i,j,i,j-(k+1));
							moveObj.simulateMove(totalPrizes, totalPrizes, totalPrizes);
							moveObj.setMadeBy(1);
							
							if (currentBoard[i][j-(k+1)].equals("WK")){
								moveObj.decreasewEval(7);
								moveObj.setFlag(1);
								moveObj.setValue(7);
								moveObj.setwKing(0);
							}
							else if (currentBoard[i][j-(k+1)].equals("WR")){
								moveObj.decreasewEval(3);
								moveObj.setFlag(1);
								moveObj.setValue(3);
								moveObj.decreasePieces();
							}
							else if (currentBoard[i][j-(k+1)].equals("WP")){
								moveObj.decreasewEval(1);
								moveObj.setFlag(1);
								moveObj.setValue(1);
								moveObj.decreasePieces();
							}
							else if (currentBoard[i][j-(k+1)].equals("P"))
								moveObj.setValue(0.45);
							
							availableMoves.add(moveObj);
							
							// prevent detouring a chesspart to attack the other
							if(target.equals("W") || target.equals("P"))
								break;
						}
						
						// check of it can move on the right
						for(int k=0; k<3; k++)
						{
							if((j+(k+1)) == 5)
								break;
							
							target = Character.toString(currentBoard[i][j+(k+1)].charAt(0));
							
							if(target.equals("B"))
								break;
							
							moveObj = new Move(currentBoard,i,j,i,j+(k+1));
							moveObj.simulateMove(totalPrizes, totalPrizes, totalPrizes);
							moveObj.setMadeBy(1);
							
							if (currentBoard[i][j+(k+1)].equals("WK")){
								moveObj.decreasewEval(7);
								moveObj.setFlag(1);
								moveObj.setValue(7);
								moveObj.setwKing(0);
							}
							else if (currentBoard[i][j+(k+1)].equals("WR")){
								moveObj.decreasewEval(3);
								moveObj.setFlag(1);
								moveObj.setValue(3);
								moveObj.decreasePieces();
							}
							else if (currentBoard[i][j+(k+1)].equals("WP")){
								moveObj.decreasewEval(1);
								moveObj.setFlag(1);
								moveObj.setValue(1);
								moveObj.decreasePieces();
							}
							else if (currentBoard[i][j+(k+1)].equals("P"))
								moveObj.setValue(0.95);
							
							availableMoves.add(moveObj);
							
							// prevent detouring a chesspart to attack the other
							if(target.equals("W") || target.equals("P"))
								break;
						}
					}
					else // it is the king
					{
						// check if it can move upwards
						if((i-1) >= 0)
						{
							target = Character.toString(currentBoard[i-1][j].charAt(0));
							
							if(!target.equals("B")) {
								moveObj = new Move(currentBoard,i,j,i-1,j);
								moveObj.simulateMove(totalPrizes, totalPrizes, totalPrizes);
								moveObj.setMadeBy(1);
								
								if (currentBoard[i-1][j].equals("WK")){
									moveObj.decreasewEval(7);
									moveObj.setFlag(2);
									moveObj.setValue(7);
									moveObj.setwKing(0);
								}
								else if (currentBoard[i-1][j].equals("WR")){
									moveObj.decreasewEval(3);
									moveObj.setFlag(2);
									moveObj.setValue(3);
									moveObj.decreasePieces();
								}
								else if (currentBoard[i-1][j].equals("WP")){
									moveObj.decreasewEval(1);
									moveObj.setFlag(2);
									moveObj.setValue(1);
									moveObj.decreasePieces();
								}
								else if (currentBoard[i-1][j].equals("P"))
									moveObj.setValue(0.95);

								availableMoves.add(moveObj);
							}
						}
						
						// check if it can move downwards
						if((i+1) < 7) {
							target = Character.toString(currentBoard[i+1][j].charAt(0));
							
							if(!target.equals("B")) {
								moveObj = new Move(currentBoard,i,j,i+1,j);								
								moveObj.simulateMove(totalPrizes, totalPrizes, totalPrizes);
								moveObj.setMadeBy(1);
								
								if (currentBoard[i+1][j].equals("WK")){
									moveObj.decreasewEval(7);
									moveObj.setFlag(2);
									moveObj.setValue(7);
									moveObj.setwKing(0);
								}
								else if (currentBoard[i+1][j].equals("WR")){
									moveObj.decreasewEval(3);
									moveObj.setFlag(2);
									moveObj.setValue(3);
									moveObj.decreasePieces();
								}
								else if (currentBoard[i+1][j].equals("WP")){
									moveObj.decreasewEval(1);
									moveObj.setFlag(2);
									moveObj.setValue(1);
									moveObj.decreasePieces();
								}
								else if (currentBoard[i+1][j].equals("P"))
									moveObj.setValue(0.95);
								
								availableMoves.add(moveObj);
							}
						}
						
						// check if it can move on the left
						if((j-1) >= 0)
						{
							target = Character.toString(currentBoard[i][j-1].charAt(0));
							
							if(!target.equals("B"))
							{
								moveObj = new Move(currentBoard,i,j,i,j-1);							
								moveObj.simulateMove(totalPrizes, totalPrizes, totalPrizes);
								moveObj.setMadeBy(1);
								
								if (currentBoard[i][j-1].equals("WK")){
									moveObj.decreasewEval(7);
									moveObj.setFlag(2);
									moveObj.setValue(7);
									moveObj.setwKing(0);
								}
								else if (currentBoard[i][j-1].equals("WR")){
									moveObj.decreasewEval(3);
									moveObj.setFlag(2);
									moveObj.setValue(3);
									moveObj.decreasePieces();
								}
								else if (currentBoard[i][j-1].equals("WP")){
									moveObj.decreasewEval(1);
									moveObj.setFlag(2);
									moveObj.setValue(1);
									moveObj.decreasePieces();
								}
								else if (currentBoard[i][j-1].equals("P"))
									moveObj.setValue(0.95);

								availableMoves.add(moveObj);	
							}
						}
						
						// check if it can move on the right
						if((j+1) < 5)
						{
							target = Character.toString(currentBoard[i][j+1].charAt(0));
							
							if(!target.equals("B"))
							{
								moveObj = new Move(currentBoard,i,j,i,j+1);
								moveObj.simulateMove(totalPrizes, totalPrizes, totalPrizes);
								moveObj.setMadeBy(1);
								
								if (currentBoard[i][j+1].equals("WK")){
									moveObj.decreasewEval(7);
									moveObj.setFlag(2);
									moveObj.setValue(7);
									moveObj.setwKing(0);
								}
								else if (currentBoard[i][j+1].equals("WR")){
									moveObj.decreasewEval(3);
									moveObj.setFlag(2);
									moveObj.setValue(3);
									moveObj.decreasePieces();
								}
								else if (currentBoard[i][j+1].equals("WP")){
									moveObj.decreasewEval(1);
									moveObj.setFlag(2);
									moveObj.setValue(1);
									moveObj.decreasePieces();
								}
								else if (currentBoard[i][j+1].equals("P"))
									moveObj.setValue(0.95);

								availableMoves.add(moveObj);	
							}
						}
					}			
				}
			}
		}
		Collections.sort(availableMoves,new MoveComparator());
		return availableMoves;	
	}
	
	public ArrayList<Move> whiteMoves(String[][] currentBoard)
	{	
		ArrayList<Move> availableMoves = new ArrayList<Move>();
		Move moveObj;
		String target = "";
		String piece = "";
		
		for (int i=0; i<7; i++) {
			for (int j=0; j<5; j++) {
				piece = Character.toString(currentBoard[i][j].charAt(0));
				
				if (!piece.equals("W")) {
					continue; 
			} else {
			piece = Character.toString(currentBoard[i][j].charAt(1));	
		    if(piece.equals("P"))	// it is a pawn
			{
				// check if it can move one vertical position ahead
				target = Character.toString(currentBoard[i-1][j].charAt(0));
				
				if(target.equals(" ") || target.equals("P"))
				{
					moveObj = new Move(currentBoard,i,j,i-1,j);
					moveObj.simulateMove(totalPrizes, totalPrizes, totalPrizes);
					moveObj.setMadeBy(0);
					
					if (i-1 == 0)
						moveObj.setValue(1);
					
					if (target.equals("P"))
						moveObj.increaseValue(0.95);
					
					availableMoves.add(moveObj);
				}
				
				// check if it can move crosswise to the left
				if(j!=0)
				{
					target = Character.toString(currentBoard[i-1][j-1].charAt(0));
					
					if ((target.equals("B"))){
						moveObj = new Move(currentBoard,i,j,i-1,j-1);				
						moveObj.simulateMove(totalPrizes, totalPrizes, totalPrizes);
						moveObj.setMadeBy(0);
					
						if (currentBoard[i-1][j-1].equals("BK")) {
							moveObj.decreasebEval(7);
							moveObj.setValue(7);
							moveObj.setbKing(0);
							}
						else if (currentBoard[i-1][j-1].equals("BR")) {
							moveObj.decreasebEval(3);
							moveObj.setValue(3);
							moveObj.decreasePieces();
						}
						else {
							moveObj.decreasebEval(1);
							moveObj.setValue(1);
							moveObj.decreasePieces();
						}
						availableMoves.add(moveObj);
					}
				}
				
				// check if it can move crosswise to the right
				if(j!=4)
				{
					target = Character.toString(currentBoard[i-1][j+1].charAt(0));
					
					if(target.equals("B")) {
						moveObj = new Move(currentBoard,i,j,i-1,j+1);
						moveObj.simulateMove(totalPrizes, totalPrizes, totalPrizes);
						moveObj.setMadeBy(0);
						
						if (currentBoard[i-1][j+1].equals("BK")) {
							moveObj.decreasebEval(7);
							moveObj.setValue(7);
							moveObj.setbKing(0);
						}
						else if (currentBoard[i-1][j+1].equals("BR")) {
							moveObj.decreasebEval(3);
							moveObj.setValue(3);
							moveObj.decreasePieces();
						}
						else {
							moveObj.decreasebEval(1);
							moveObj.setValue(1);
							moveObj.decreasePieces();
						}	
						availableMoves.add(moveObj);
					}
						continue;
					
				}
			}
			else if(piece.equals("R"))	// it is a rook
			{
				// check if it can move upwards
				for(int k=0; k<3; k++)
				{
					if((i-(k+1)) < 0)
						break;
					
					target = Character.toString(currentBoard[i-(k+1)][j].charAt(0));
					
					if(target.equals("W"))
						break;
					
					moveObj = new Move(currentBoard,i,j,i-(k+1),j);
					moveObj.simulateMove(totalPrizes, totalPrizes, totalPrizes);
					moveObj.setMadeBy(0);
					
					if (currentBoard[i-(k+1)][j].equals("BK")){
						moveObj.decreasebEval(7);
						moveObj.setFlag(1);
						moveObj.setValue(7);
						moveObj.setbKing(0);
					}
					else if (currentBoard[i-(k+1)][j].equals("BR")){
						moveObj.decreasebEval(3);
						moveObj.setFlag(1);
						moveObj.setValue(3);
						moveObj.decreasePieces();
					}
					else if (currentBoard[i-(k+1)][j].equals("BP")){
						moveObj.decreasebEval(1);
						moveObj.setFlag(1);
						moveObj.setValue(1);
						moveObj.decreasePieces();
					}
					else if (currentBoard[i-(k+1)][j].equals("P"))
						moveObj.setValue(0.95);
					
					availableMoves.add(moveObj);
					
					// prevent detouring a chesspart to attack the other
					if(target.equals("B") || target.equals("P"))
						break;
				}
				
				// check if it can move downwards
				for(int k=0; k<3; k++)
				{
					if((i+(k+1)) == 7)
						break;
					
					target = Character.toString(currentBoard[i+(k+1)][j].charAt(0));
					
					if(target.equals("W"))
						break;
					
					moveObj = new Move(currentBoard,i,j,i+(k+1),j);
					moveObj.simulateMove(totalPrizes, totalPrizes, totalPrizes);
					moveObj.setMadeBy(0);
					
					if (currentBoard[i+(k+1)][j].equals("BK")){
						moveObj.decreasebEval(7);
						moveObj.setFlag(1);
						moveObj.setValue(7);
						moveObj.setbKing(0);
					}
					else if (currentBoard[i+(k+1)][j].equals("BR")){
						moveObj.decreasebEval(3);
						moveObj.setFlag(1);
						moveObj.setValue(3);
						moveObj.decreasePieces();
					}
					else if (currentBoard[i+(k+1)][j].equals("BP")){
						moveObj.decreasebEval(1);
						moveObj.setFlag(1);
						moveObj.setValue(1);
						moveObj.decreasePieces();
					}
					else if (currentBoard[i+(k+1)][j].equals("P"))
						moveObj.setValue(0.95);
					
					availableMoves.add(moveObj);
					
					// prevent detouring a chesspart to attack the other
					if(target.equals("B") || target.equals("P"))
						break;
				}
				
				// check if it can move on the left
				for(int k=0; k<3; k++)
				{
					if((j-(k+1)) < 0)
						break;
					
					target = Character.toString(currentBoard[i][j-(k+1)].charAt(0));
					
					if(target.equals("W"))
						break;
					
					moveObj = new Move(currentBoard,i,j,i,j-(k+1));
					moveObj.simulateMove(totalPrizes, totalPrizes, totalPrizes);
					moveObj.setMadeBy(0);
					
					if (currentBoard[i][j-(k+1)].equals("BK")){
						moveObj.decreasebEval(7);
						moveObj.setFlag(1);
						moveObj.setValue(7);
						moveObj.setbKing(0);
					}
					else if (currentBoard[i][j-(k+1)].equals("BR")){
						moveObj.decreasebEval(3);
						moveObj.setFlag(1);
						moveObj.setValue(3);
						moveObj.decreasePieces();
					}
					else if (currentBoard[i][j-(k+1)].equals("BP")){
						moveObj.decreasebEval(1);
						moveObj.setFlag(1);
						moveObj.setValue(1);
						moveObj.decreasePieces();
					}
					else if (currentBoard[i][j-(k+1)].equals("P"))
						moveObj.setValue(0.95);
					
					availableMoves.add(moveObj);
					
					// prevent detouring a chesspart to attack the other
					if(target.equals("B") || target.equals("P"))
						break;
				}
				
				for(int k=0; k<3; k++) {
					// check of it can move to the right
					if((j+(k+1)) == 5)
						break;
					
					target = Character.toString(currentBoard[i][j+(k+1)].charAt(0));
					
					if(target.equals("W"))
						break;
					
					moveObj = new Move(currentBoard,i,j,i,j+(k+1));
					moveObj.simulateMove(totalPrizes, totalPrizes, totalPrizes);
					moveObj.setMadeBy(0);
					
					if (currentBoard[i][j+(k+1)].equals("BK")){
						moveObj.decreasebEval(7);
						moveObj.setFlag(1);
						moveObj.setValue(7);
						moveObj.setbKing(0);
					}
					else if (currentBoard[i][j+(k+1)].equals("BR")){
						moveObj.decreasebEval(3);
						moveObj.setFlag(1);
						moveObj.setValue(3);
						moveObj.decreasePieces();
					}
					else if (currentBoard[i][j+(k+1)].equals("BP")){
						moveObj.decreasebEval(1);
						moveObj.setFlag(1);
						moveObj.setValue(1);
						moveObj.decreasePieces();
					}
					else if (currentBoard[i][j+(k+1)].equals("P"))
						moveObj.setValue(0.95);
					
					availableMoves.add(moveObj);
					
					// prevent detouring a chesspart to attack the other
					if(target.equals("B") || target.equals("P"))
						break;
				}
			} else { // examine king options 
				
				// check if it can move upwards
				if((i-1) >= 0) {
					target = Character.toString(currentBoard[i-1][j].charAt(0));
					
					if(!target.equals("W")) {
						moveObj = new Move(currentBoard,i,j,i-1,j);
						moveObj.simulateMove(totalPrizes, totalPrizes, totalPrizes);
						moveObj.setMadeBy(0);
						
						if (currentBoard[i-1][j].equals("BK")){
							moveObj.decreasebEval(7);
							moveObj.setFlag(2);
							moveObj.setValue(7);
							moveObj.setbKing(0);
						}
						else if (currentBoard[i-1][j].equals("BR")){
							moveObj.decreasebEval(3);
							moveObj.setFlag(2);
							moveObj.setValue(3);
							moveObj.decreasePieces();
						}
						else if (currentBoard[i-1][j].equals("BP")){
							moveObj.decreasebEval(1);
							moveObj.setFlag(2);
							moveObj.setValue(1);
							moveObj.decreasePieces();
						}
						else if (currentBoard[i-1][j].equals("P"))
							moveObj.setValue(0.95);

						availableMoves.add(moveObj);
					}
				}
				
				// check if it can move downwards
				if((i+1) < 7)
				{
					target = Character.toString(currentBoard[i+1][j].charAt(0));
					
					if(!target.equals("W"))
					{
						moveObj = new Move(currentBoard,i,j,i+1,j);
						moveObj.simulateMove(totalPrizes, totalPrizes, totalPrizes);
						moveObj.setMadeBy(0);
						
						if (currentBoard[i+1][j].equals("BK")){
							moveObj.decreasebEval(7);
							moveObj.setFlag(2);
							moveObj.setValue(7);
							moveObj.setbKing(0);
						}
						else if (currentBoard[i+1][j].equals("BR")){
							moveObj.decreasebEval(3);
							moveObj.setFlag(2);
							moveObj.setValue(3);
							moveObj.decreasePieces();
						}
						else if (currentBoard[i+1][j].equals("BP")){
							moveObj.decreasebEval(1);
							moveObj.setFlag(2);
							moveObj.setValue(1);
							moveObj.decreasePieces();
						}
						else if (currentBoard[i+1][j].equals("P"))
							moveObj.setValue(0.95);
						
						availableMoves.add(moveObj);
					}
				}
				
				// check if it can move on the left
				if((j-1) >= 0)
				{
					target = Character.toString(currentBoard[i][j-1].charAt(0));
					
					if(!target.equals("W"))
					{
						moveObj = new Move(currentBoard,i,j,i,j-1);
						moveObj.simulateMove(totalPrizes, totalPrizes, totalPrizes);
						moveObj.setMadeBy(0);
						
						if (currentBoard[i][j-1].equals("BK")){
							moveObj.decreasebEval(7);
							moveObj.setFlag(2);
							moveObj.setValue(7);
							moveObj.setbKing(0);
						}
						else if (currentBoard[i][j-1].equals("BR")){
							moveObj.decreasebEval(3);
							moveObj.setFlag(2);
							moveObj.setValue(3);
							moveObj.decreasePieces();
						}
						else if (currentBoard[i][j-1].equals("BP")){
							moveObj.decreasebEval(1);
							moveObj.setFlag(2);
							moveObj.setValue(1);
							moveObj.decreasePieces();
						}
						else if (currentBoard[i][j-1].equals("P"))
							moveObj.setValue(0.95);

						availableMoves.add(moveObj);	
					}
				}
				// check if it can move to the right
				if((j+1) < 5) {
					target = Character.toString(currentBoard[i][j+1].charAt(0));
					
					if(!target.equals("W")) {	
						moveObj = new Move(currentBoard,i,j,i,j+1);
						moveObj.simulateMove(totalPrizes, totalPrizes, totalPrizes);
						moveObj.setMadeBy(0);
						
						if (currentBoard[i][j+1].equals("BK") ){
							moveObj.decreasebEval(7);
							moveObj.setFlag(2);
							moveObj.setValue(7);
							moveObj.setbKing(0);
						} else if (currentBoard[i][j+1].equals("BR")) {
							moveObj.decreasebEval(3);
							moveObj.setFlag(2);
							moveObj.setValue(3);
							moveObj.decreasePieces();
						} else if (currentBoard[i][j+1].equals("BP")){
							moveObj.decreasebEval(1);
							moveObj.setFlag(2);
							moveObj.setValue(1);
							moveObj.decreasePieces();
						} else if (currentBoard[i][j+1].equals("P")) {
							moveObj.setValue(0.95);
						}
						availableMoves.add(moveObj);	
					}
				}
			}
		    Collections.sort(availableMoves,new MoveComparator());
		    }}}
		return availableMoves;	
}

	/**** Original MiniMax Algorithm ***/
	public String minimax(ArrayList<Move> availableMoves, int myColor){
		
		double u = -Integer.MAX_VALUE;
		double tempU;
		String bestMove = null;
		this.plColour = myColor;
		
		for(Move successorMove: availableMoves ){
	        tempU=minimizer(successorMove);
			depthCounter--;
			if (tempU > u){
				u = tempU;
				bestMove = (successorMove.getMove());
			}
		}
		return bestMove;
	}
	
	private double maximizer(Move move){
		depthCounter++;
		if (move.checkLeafState()==true || depthCounter>=currDepth){
			return move.getValue();
		}
		double u = -Integer.MAX_VALUE;
		ArrayList<Move> successors = null;
		if (plColour == 0) {
			successors = this.whiteMoves(move.getMoveBoard());
		} else {
			successors = this.blackMoves(move.getMoveBoard());
		}	
		for(Move successorMove: successors ){
			successorMove.setValue(move.getValue()+successorMove.getValue());
			u = Math.max(u, minimizer(successorMove));
			depthCounter--;
        }
		return u;		
	}

	private double minimizer(Move move){
		depthCounter++;
		if (move.checkLeafState()==true || depthCounter>=currDepth){
			return move.getValue();
		}
		double u = Integer.MAX_VALUE;
		ArrayList<Move> successors = null;
		if (plColour == 0) {
			successors = this.blackMoves(move.getMoveBoard());
		} else {
			successors = this.whiteMoves(move.getMoveBoard());
		}
		for(Move successorMove: successors ){
			successorMove.setValue(move.getValue()-successorMove.getValue());
			u = Math.min(u, maximizer(successorMove));
			depthCounter--;
        }
		return u;		
	}

	private Move selectRandomAction(ArrayList<Move> availableMoves) {		
		Random ran = new Random();
		int x = ran.nextInt(availableMoves.size());
		
		return availableMoves.get(x);
	}
	
	public String monteCarloTreeSearch (ArrayList<Move> availableMoves, int scoreWhite, int scoreBlack) {
		Boolean simResult = false;
		Move expandedNode;
		Move root,current,successor;
		Move endstate = null;
		// while time left
		int efficiency=0;
		
		while(efficiency<5) {
			root = selectRandomAction(availableMoves);
			current = root;
			
			// go down the search tree, in order to reach an unexpanded node
			while(!current.getChildren().isEmpty()) {
				successor = current.getChildren().get(0);
				successor.setParent(current);
				if (successor.getMadeBy()==0) {
					successor.setwScore(current.getwScore()+successor.getValue());
					successor.setbScore(current.getbScore());
				} else {
					successor.setbScore(current.getbScore()+successor.getValue());
					successor.setwScore(current.getwScore());
				}
				current = successor;
			}
			
			// expand node
			if (current.getMadeBy()==0) {
				current.setChildren(blackMoves(current.getMoveBoard()));
			} else {
				current.setChildren(whiteMoves(current.getMoveBoard()));
			}
			expandedNode= current.getChildren().get(0);
			expandedNode.setParent(current);
				if (expandedNode.getMadeBy()==0) {
					expandedNode.setwScore(current.getwScore()+expandedNode.getValue());
					expandedNode.setbScore(current.getbScore());
				} else {
					expandedNode.setbScore(current.getbScore()+expandedNode.getValue());
					expandedNode.setwScore(current.getwScore());
				}
				
				// simulation
				while(!expandedNode.checkLeafState()) {
					if (expandedNode.getMadeBy()==0) {
						endstate=blackMoves(expandedNode.getMoveBoard()).get(0);
						endstate.setParent(expandedNode);
						endstate.setwScore(expandedNode.getwScore()+endstate.getValue());
						endstate.setbScore(expandedNode.getbScore());
					}
					else {
						endstate=whiteMoves(expandedNode.getMoveBoard()).get(0);
						endstate.setParent(expandedNode);
						endstate.setbScore(expandedNode.getbScore()+endstate.getValue());
						endstate.setwScore(expandedNode.getwScore());
					}
					expandedNode=endstate;
				}
			if (endstate==null) {
				break;
			}
			// evaluate terminal state
			if(plColour==1) { // player controls black pieces
				if (endstate.getbScore()>endstate.getwScore()) {
					endstate.increaseWins();
					endstate.increaseVisited();
					simResult=true;
					}
			} else {
				if (endstate.getwScore()>endstate.getbScore()) {
					endstate.increaseWins();
					endstate.increaseVisited();
					simResult=true;
				}
			}
			// backpropagation 
			while(endstate.getParent()!=null) {
				endstate=endstate.getParent();
				endstate.increaseVisited();
				if (simResult==true){
					endstate.increaseWins();
				}
			}
			efficiency++;
			}
		
		Move decision=selectRandomAction(availableMoves);
		for (Move option: availableMoves) {
			if (option.getWins()>=decision.getWins()) {
				decision=option;
			}
		}
		return decision.getMove();
	}
	
	/**** MiniMax Algorithm with Alpha-Beta Pruning ****/	
	public String minimaxABP(ArrayList<Move> availableMoves, int scoreWhite, int scoreBlack, int depth){
		double currentMoveValue = -Integer.MAX_VALUE;
		double a = -Integer.MAX_VALUE;
		double b = Integer.MAX_VALUE;
		double successorValue;
		
		depthFlag=false;
		String bestMove = null;
		
		this.currDepth = depth;
		this.initDepth = depth;
		
		for(Move successorMove: availableMoves ){
			
			if (plColour==0){
				successorMove.setbScore(scoreBlack);
				successorMove.setwScore(scoreWhite + successorMove.getValue());
			} else{
				successorMove.setwScore(scoreWhite);	
				successorMove.setbScore(scoreBlack + successorMove.getValue());
			}
				
	        successorValue=minimizerABP(successorMove,a,b);
			
	        depthCounter--;
	        
			if (successorValue > currentMoveValue){
				currentMoveValue = successorValue;
				bestMove = successorMove.getMove();
			}
			
			a = Math.max(a, currentMoveValue);
		}
		
		return bestMove;
			
	}
	
	private double maximizerABP(Move move, double a, double b){
		boolean depthIncreasedFlag=false;
		depthCounter++;
		
		if (move.checkLeafState()==true) {
			return move.evaluate(plColour);
		} else if(depthCounter>=currDepth){
			if(!cutOffFunction(move))
				return move.evaluate(plColour);
            depthIncreasedFlag=true;
        }
		ArrayList<Move> successors = null;
		double opMoveValue = -Integer.MAX_VALUE;
		
		if (plColour == 0) {
			successors = this.whiteMoves(move.getMoveBoard());
		} else {
			successors = this.blackMoves(move.getMoveBoard());
		}
		for(Move successorMove: successors ){
			// nextPlayer=PL -> next move's score is updated by increasing pl's score
			if (plColour == 0) {
				successorMove.setwScore(move.getwScore()+successorMove.getValue());
				successorMove.setbScore(move.getbScore());
			} else {
				successorMove.setbScore(move.getbScore()+successorMove.getValue());
				successorMove.setwScore(move.getwScore());
			}
			// maximise gain recursively
			opMoveValue = Math.max(opMoveValue, minimizerABP(successorMove,a,b));
			depthCounter--;
			if (opMoveValue >= b){
				if (depthIncreasedFlag==true){
					depthIncreasedFlag=false;
					depthFlag=false;
					currDepth=initDepth;
				}
				return opMoveValue;
			}
			// set new a - bound
			a = Math.max(a, opMoveValue);
        }
		if (depthIncreasedFlag==true){
			depthIncreasedFlag=false;
			depthFlag=false;
			currDepth=initDepth;
		}
		return opMoveValue;		
		
	}
	
	// Min-Value Function for Search Function above
	private double minimizerABP(Move move, double a, double b){
		depthCounter++;
		boolean depthIncreaseFlag=false;
		if (move.checkLeafState()==true ){
			return move.evaluate(plColour);
		} else if(depthCounter>=currDepth){
            if(cutOffFunction(move))
                return move.evaluate(plColour);
            depthIncreaseFlag=true;
        }
		
		double playerMoveValue = Integer.MAX_VALUE;
		ArrayList<Move> successors = null;
		
		
		if (plColour == 0) {
			successors = this.blackMoves(move.getMoveBoard());
		} else {
			successors = this.whiteMoves(move.getMoveBoard());
		}
		//nextPlayer=OP -> next move's score is updated by increasing op's score
		for(Move successorMove: successors ){
			if (plColour == 0) {
				successorMove.setbScore(move.getbScore()+successorMove.getValue());
				successorMove.setwScore(move.getwScore());
			} else {
				successorMove.setwScore(move.getwScore()+successorMove.getValue());
				successorMove.setbScore(move.getbScore());
			}
			// minimise maximizer's gain
			playerMoveValue = Math.min(playerMoveValue, maximizerABP(successorMove,a,b));
			depthCounter--;
			// stop the search when currentMoveValue is less than a bound (Alpha pruning)
			if (playerMoveValue <= a){
				// check if depth has increased from this point deeper
				if (depthIncreaseFlag==true){
					// if so, reset variables
					depthIncreaseFlag=false;
					depthFlag=false;
					currDepth=initDepth;
				}
				return playerMoveValue;
			}
			// set new b - bound
			b = Math.min(b, playerMoveValue);
        }
		// check if depth has increased from this point deeper
		if (depthIncreaseFlag==true) {
			// if so, reset variables for further checks
			depthIncreaseFlag=false;
			depthFlag=false;
			currDepth=initDepth;
		}
		
		return playerMoveValue;
	}
	
	private boolean cutOffFunction(Move move){
		// king captured piece and depth has not increased from this point
		if (move.getFlag()==2 && depthFlag==false){
			currDepth += 4;
			depthFlag=true;
			return false;
		// rook captured piece and depth has not increased from this point
		} else if(move.getFlag()==1 && depthFlag==false){
			currDepth += 2;	
			depthFlag=true;
			return false;
		}	
		// Otherwise the given move is a dull move
		return true;
	}
	
	public int getOpColour() {
		return opColour;
	}

	public void setOpColour(int opColour) {
		this.opColour = opColour;
	}

	public int getPlColour() {
		return plColour;
	}

	public void setPlColour(int plColour) {
		this.plColour = plColour;
	}
	
}
