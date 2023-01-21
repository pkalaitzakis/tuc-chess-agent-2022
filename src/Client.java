package myPackage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client
{
	private static final int PORTServer = 9876;
	private DatagramSocket clientSocket = null;
	private byte[] sendData = null;
	private byte[] receiveData = null;
	private int size = 200;
	private DatagramPacket sendPacket = null;
	private DatagramPacket receivePacket = null;
	private InetAddress host = null;
	
	private String playerName = "MrMorale";
	private String receivedMsg = "";
	private int plColour;
	private World world = null;
	private int scoreWhite = 0;
	private int scoreBlack = 0;
	private int delay = 10;
	
	private Agent agent;
	
	private ArrayList<Move> availableMoves = null;

	
	public Client()
	{
		try
		{
			clientSocket = new DatagramSocket();
			
			sendData = new byte[size];
			receiveData = new byte[size];
			
			host = InetAddress.getLocalHost();
			
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			sendPacket = new DatagramPacket(sendData, sendData.length, host, PORTServer);
		}
		catch(SocketException | UnknownHostException e)
		{
			// print the occurred exception
			System.out.println(e.getClass().getName() + " : " + e.getMessage());
		}
		world = new World();
		agent = new Agent();
	}
	
	private void transmitName()
	{
		// add your name here, remove comment below
		// myName = "your_name"
		
		try
		{
			// turn my name into bytes
			sendData = playerName.getBytes("UTF-8");
			sendPacket.setData(sendData);
			sendPacket.setLength(sendData.length);
			clientSocket.send(sendPacket);
		}
		catch(IOException e)
		{
			// print the occurred exception
			System.out.println(e.getClass().getName() + " : " + e.getMessage());
		}
	}
	
	private void listen(String arg)
	{
		// keep on receiving messages and act according to their content
		while(true)
		{
			try
			{
				// waiting for a message from the server
				clientSocket.receive(receivePacket);
				
				// counterMsg++;
				
				// get the String of the message
				// no need to check for IPAddress and Port of sender, it must be the server of TUC-CHESS
				receivedMsg = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");
				
				System.out.println("Received message from server : " + receivedMsg);
				
				// get the first letter of the String
				String firstLetter = Character.toString(receivedMsg.charAt(0));
				
				if(firstLetter.equals("P"))		// received information is about my colour
				{
					// get the second letter of the String
					String secondLetter = Character.toString(receivedMsg.charAt(1));
					
					if(secondLetter.equals("W")) {
						plColour = 0;
						world.setPlColour(0);
						agent.setPlColour(0);
						agent.setOpColour(1);
					}
					else {
						plColour = 1;
						world.setPlColour(0);
						agent.setPlColour(1);
						agent.setOpColour(0);
					}
				}				
				else if(firstLetter.equals("G"))	// received information is about the game (begin/end)
				{
					// get the second letter of the String
					String secondLetter = Character.toString(receivedMsg.charAt(1));
					
					if(secondLetter.equals("B"))
					{
						// beginning of the game
						if(plColour == 0)
						{
							String action = selectAction(arg,scoreWhite,scoreBlack);
							
							try
							{
								synchronized(this)
								{
									this.wait(delay);
								}
							}
							catch(InterruptedException e)
							{
								System.out.println(e.getClass().getName() + " : " + e.getMessage());
							}
							
							sendData = action.getBytes("UTF-8");
							sendPacket.setData(sendData);
							sendPacket.setLength(sendData.length);
							clientSocket.send(sendPacket);
						}
						else
							continue;
					}
					else	// secondLetter.equals("E") - the game has ended
					{
						scoreWhite = Integer.parseInt(Character.toString(receivedMsg.charAt(2))
								                    + Character.toString(receivedMsg.charAt(3)));
						
						scoreBlack = Integer.parseInt(Character.toString(receivedMsg.charAt(4))
						                            + Character.toString(receivedMsg.charAt(5)));
						
						if(scoreWhite - scoreBlack > 0)
						{
							if(plColour == 0)
								System.out.println("I won! " + scoreWhite + "-" + scoreBlack);
							else
								System.out.println("I lost. " + scoreWhite + "-" + scoreBlack);
							
						}
						else if(scoreWhite - scoreBlack < 0)
						{
							if(plColour == 0)
								System.out.println("I lost. " + scoreWhite + "-" + scoreBlack);
							else
								System.out.println("I won! " + scoreWhite + "-" + scoreBlack);
							
							
						}
						else
						{
							System.out.println("It is a draw! " + scoreWhite + "-" + scoreBlack);
							
						}
							
						break;
					}
				}
				else	// firstLetter.equals("T") - a move has been made
				{
					// decode the rest of the message
					int nextPlayer = Integer.parseInt(Character.toString(receivedMsg.charAt(1)));
					
					int x1 = Integer.parseInt(Character.toString(receivedMsg.charAt(2)));
					int y1 = Integer.parseInt(Character.toString(receivedMsg.charAt(3)));
					int x2 = Integer.parseInt(Character.toString(receivedMsg.charAt(4)));
					int y2 = Integer.parseInt(Character.toString(receivedMsg.charAt(5)));
					
					int prizeX = Integer.parseInt(Character.toString(receivedMsg.charAt(6)));
					int prizeY = Integer.parseInt(Character.toString(receivedMsg.charAt(7)));
					
					scoreWhite = Integer.parseInt(Character.toString(receivedMsg.charAt(8)) 
							                      + Character.toString(receivedMsg.charAt(9)));
					
					scoreBlack = Integer.parseInt(Character.toString(receivedMsg.charAt(10)) 
												  + Character.toString(receivedMsg.charAt(11)));
					
					world.makeMove(x1,y1,x2,y2,prizeX,prizeY);
					
					if(nextPlayer==plColour)
					{
						
						String action = selectAction(arg,scoreWhite,scoreBlack);
												
						try
						{
							synchronized(this)
							{
								this.wait(delay);
							}
						}
						catch(InterruptedException e)
						{
							System.out.println(e.getClass().getName() + " : " + e.getMessage());
						}
						
						sendData = action.getBytes("UTF-8");
						sendPacket.setData(sendData);
						sendPacket.setLength(sendData.length);
						clientSocket.send(sendPacket);			
					}
					else
					{
						continue;
					}				
				}
				
			}
			catch(IOException e)
			{
				System.out.println(e.getClass().getName() + " : " + e.getMessage());
			}
		}
	}
	
	public String selectAction(String algo, int scoreWhite, int scoreBlack)
	{
		availableMoves = new ArrayList<Move>();
		
		if (plColour == 0) {
			availableMoves = agent.whiteMoves(world.getBoard());
		} else {
			availableMoves = agent.blackMoves(world.getBoard());
		}		
		
		String bestMove=" ";
		
		if (algo.equals("minimax")) {
			bestMove=agent.minimaxABP(availableMoves, scoreWhite, scoreBlack, 8);
		} else if(algo.equals("MCTS")) {
			bestMove = agent.monteCarloTreeSearch(availableMoves, scoreWhite, scoreBlack);
		}
	
		return bestMove;
			
	}
	
	public static void main(String[] args)
	{	
		if (args.length==1) {
			String choice = args[0];
			Client client = new Client();
			client.transmitName();
			client.listen(choice);
		} else {
			System.out.println("Please provide 'minimax' or 'MCTS' arguement!");
		}
		
	}

}
