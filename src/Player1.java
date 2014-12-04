import java.util.LinkedList;

// This player is as rudimentary as it gets.  It simply puts the ships in a static 
// location, and makes random moves until one sticks.  Your player can use this 
// as a base to expand upon. It is a good idea to play against this player until yours
// gets good enough to beat it regularly.

public class Player1 extends Player {

	// You must call the super to establish the necessary game variables
	// and register the game.
	public Player1(int playerNum) {
		super(playerNum);
	}
	
	int difficulty = 1;
	
	Boolean sank_SHIP_CARRIER = false; 		//5
	Boolean sank_SHIP_BATTLESHIP = false;	//4
	Boolean sank_SHIP_CRUISER = false;		//3
	Boolean sank_SHIP_SUBMARINE = false;	//3
	Boolean sank_SHIP_DESTROYER = false; 	//2
	
	int DIRECTION_NORTH = 0;
	int DIRECTION_EAST = 1;
	int DIRECTION_SOUTH = 2;
	int DIRECTION_WEST = 3;
	
	int persuitDirection = -1;
	int reverseCount = 0;
	Move lastMove;
	
	LinkedList<Move> moveQueue = new LinkedList<Move>();
	private void calculateMoveQueue(){
		if(this.investigating_hit){	
			moveQueue.clear();
			if(this.persuitHitCount == 1){
				moveQueue.add(getNextClockwiseMove(this.lastHitMove, lastMove));
				return;
			}
			else if(this.persuitHitCount == 2){
				if(this.lastMoveWasAHit()){
					this.persuitDirection = getPersuitDirection(originalHit, this.lastHitMove);
					moveQueue.add(this.getNextMoveInDirection(lastHitMove, persuitDirection));
					return;
				}
				else{
					reverser_persuitDirection();
					moveQueue.add(this.getNextMoveInDirection(lastHitMove, persuitDirection));
					return;
				}
			}else{
				if(lastMoveWasAHit()){
					moveQueue.add(this.getNextMoveInDirection(lastHitMove, persuitDirection));
					return;
				}else{
					reverser_persuitDirection();
					moveQueue.add(this.getNextMoveInDirection(lastHitMove, persuitDirection));
					return;
				}
			}
		}
		int step_size = 5;
		if(sank_SHIP_CARRIER){
			step_size = 4;
		}else if(sank_SHIP_BATTLESHIP){
			step_size = 3;
		}else if(sank_SHIP_CRUISER){
			step_size = 2;
		}else{
			step_size = 1;
		}
		//add moves to queue.)
	}

	private Move getNextMoveInDirection(Move lastHitMove2, int persuitDirection2){
		Move nextMove = new Move(lastHitMove.row, lastHitMove.col);
		if (persuitDirection2 == DIRECTION_NORTH){
			
		}
		return nextMove;
		
	}

	private void reverser_persuitDirection() {
		reverseCount++;
		// TODO Auto-generated method stub
		
	}

	private int getPersuitDirection(Move originalHit2, Move lastHitMove2) {
		int direction = DIRECTION_NORTH;
		if(originalHit2.row-lastHitMove2.row == 0){
			//same row: direction must be east/west
			direction = DIRECTION_EAST;
			if(lastHitMove2.col - originalHit2.col < 0){
				direction = DIRECTION_WEST;
			}
		}else if(lastHitMove2.row - originalHit2.row > 0){
			direction = DIRECTION_SOUTH;
		}
		return direction;
	}

	private Move getNextClockwiseMove(Move lastMove2, Move lastMove3) {
//
//		if(nextMove.col > 0)
//			lastMove.col--;
//		else if(nextMove.col<10)
//		else if(nextMove.col<10)
//			
		return null;
	}

	Move lastHitMove;
	private Boolean lastMoveWasAHit(){
		if(game.getMoveBoardValue(this.myMoves, lastMove.row, lastMove.col) == BSGame.PEG_HIT){
			lastHitMove = lastMove;
			return true;
		}
		return false;
	}
	private Boolean lastMoveWasASink(){
		if(game.getMoveBoardValue(this.myMoves, lastMove.row, lastMove.col) != BSGame.PEG_SHIP)
			return false;
		if (!sank_SHIP_CARRIER)
			sank_SHIP_CARRIER = game.shipSunk(hisShips, myMoves, Ships.SHIP_CARRIER);
		if (!sank_SHIP_BATTLESHIP)
			sank_SHIP_BATTLESHIP = game.shipSunk(hisShips, myMoves, Ships.SHIP_BATTLESHIP);
		if (!sank_SHIP_CRUISER)
			sank_SHIP_CRUISER = game.shipSunk(hisShips, myMoves, Ships.SHIP_CRUISER);
		if (!sank_SHIP_SUBMARINE)
			sank_SHIP_SUBMARINE = game.shipSunk(hisShips, myMoves, Ships.SHIP_SUBMARINE);
		if (!sank_SHIP_DESTROYER)
			sank_SHIP_DESTROYER = game.shipSunk(hisShips, myMoves, Ships.SHIP_DESTROYER);
		return true;
	}
	
	@Override
	public void makeMove(){
		if (difficulty == 0){
			while(!game.makeMove(hisShips, myMoves, randomRow(), randomCol()));
		}
		Boolean invalidMove = true;
		while(invalidMove){
			Move move = makeEducatedMove();
			invalidMove =  !game.makeMove(hisShips, myMoves, move.row, move.col);
		}
		numMoves++;
		System.out.println("Player " + myPlayerNum + " num Moves = " + numMoves);
	}
	
	int persuitHitCount = 0;
	Move originalHit;
	boolean investigating_hit = false;
	public Move makeEducatedMove(){
		if(!this.investigating_hit && this.lastMoveWasAHit()){
			this.investigating_hit = true;
			originalHit = this.lastHitMove;
			persuitHitCount = 1;
			this.calculateMoveQueue();
		}
		if(this.lastMoveWasASink()){
			this.investigating_hit = false;
			persuitHitCount = 0;
			this.calculateMoveQueue();
		}
		lastMove = this.moveQueue.pop();
		return lastMove;
	}
	
	public void gameOver(){
		System.out.println("GG");
	}

	public boolean addShips() {
		
		game.putShip(myShips, Ships.SHIP_CARRIER, 2, 2, Ships.SHIP_SOUTH);
		game.putShip(myShips, Ships.SHIP_BATTLESHIP, 5, 5, Ships.SHIP_EAST);
		game.putShip(myShips, Ships.SHIP_CRUISER, 6, 7, Ships.SHIP_EAST);
		game.putShip(myShips, Ships.SHIP_DESTROYER, 8, 3, Ships.SHIP_EAST);
		game.putShip(myShips, Ships.SHIP_SUBMARINE, 9, 9, Ships.SHIP_NORTH);

		return true;
	}

	private class Move{
		int row;
		int col;
		Move(int row, int col){
			this.row = row;
			this.col = col;
		}
	}


}
