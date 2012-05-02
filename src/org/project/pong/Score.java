package org.project.pong;

public class Score {
	
	//member variables
	public int player1Score;
	public int player2Score;
	public int scoreToWin;

	
	//constructor
	public Score(int scoreToWin, int player1Score, int player2Score) {
		this.scoreToWin = scoreToWin;
		this.player1Score = player1Score;
		this.player2Score = player2Score;
		
	}

	//processing functions	
	public void Reset() {
		player1Score = 0;
		player2Score = 0;
	}

	public void Player1Scored(){
		player1Score++;
	}
	
	public void Player2Scored(){
		player2Score++;
	}
	
	public String getPlayer1ScoreString() {
		return Integer.toString(player1Score);
		
	}
	
	public String getPlayer2ScoreString() {
		return Integer.toString(player2Score);
		
	}
	
	public int getPlayer1ScoreInt() {
		return player1Score;
		
	}
	
	public int getPlayer2ScoreInt() {
		return player2Score;
		
	}
	
	public void setPlayer1Score(int value) {
		//int i = Integer.parseInt(value);
		this.player1Score = value;
	}
	
	public void setPlayer2Score(int value) {
		//int i = Integer.parseInt(value);
		this.player2Score = value;
	}
	
	public String CreateScoreBoard(){
		if (player1Score > player2Score)
			return "Player 1 has won the game";
		else
			return "Player 2 has won the game";
	}
	
	public boolean IsGamFinished(){
		if (player1Score >= scoreToWin || player2Score >= scoreToWin)
			return true;
		else
			return false;
	}
}//end class
