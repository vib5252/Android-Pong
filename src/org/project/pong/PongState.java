package org.project.pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;

/***
 * 
 * @author vib
 * @ball the ballVelocity is added to the ball position
 * @wasllCollision the walls flip the sign of the velocity which updates the ball position
 * @paddleCollision if the ballX falls in between the bat x-position and (bat x-position + bat-length) AND ballY is less than topBatY AND ballY is greater than bottomBatY 
 *
 */

public class PongState {
	
//DEBUG
	public static int LOGLEVEL = -1;
	public static boolean WARN = LOGLEVEL > 1;
	public static boolean DEBUG = LOGLEVEL > 0;
	public static final String TAG = "Pong";
	public Score score;
	public Rect mTouch;
	
	
	//screen width and height (OG Droid)
	//final int screenWidth = 300;
	//final int screenHeight = 420;
	
	//ics values (720 x 1184)
	final int screenWidth = Game.width;
	final int screenHeight = Game.height;
	
	
	//The ball
	final int ballSize = 15;
	int ballX = 100;
	int ballY = 100;
	int ballVelocityX;
	int ballVelocityY;
	
	//The bats
	final int batLength = 70;
	final int batHeight = 10;
	
	int topBatXPos = (screenWidth/2) - (batLength/2);
	int bottomBatXPos = (screenWidth/2) - (batLength/2);
	
	final int batSpeed = 33;
	
	//ics values
	final int topBatY = 100;
	final int bottomBatY = screenHeight - 200;
	
	
	//variables and paint object for score board	
	private final Paint mPaint = new Paint();
	public PongView mPongView;

	//constructor
	public PongState() {
		Log.d(TAG, "Pong State Called.");
		if (!Game.savedScore) {
			score = new Score(10,0,0);
			
			ballVelocityX = Game.ballVelocityX;
			ballVelocityY = Game.ballVelocityY;
		}
		
		if (Game.savedScore) {
			score = new Score(10,Game.score1container,Game.score2container);
			ballVelocityX = Game.ballVelocityX;
			ballVelocityY = Game.ballVelocityY;
		}		
	}
/**
 * The main loop. Call this to update the game state.	
 */
	//The update method
	public void update() {
		
		//if 1 player mode is selected then do this
		if(PongThread.currentState == PongThread.STATE_RUNNING_1P)
		{
			if(DEBUG) {
				Log.d(TAG, "topBatX = "+ topBatXPos);
				Log.d(TAG, "bottomBatX = "+ bottomBatXPos);
				Log.d(TAG, "ballX = "+ballX);
				Log.d(TAG, "ballY = "+ballY);
				Log.d(TAG, "ballVelocityY = "+ ballVelocityY);
					
			}
			
			ballX += ballVelocityX;
			ballY += ballVelocityY;
			//Log.d(TAG, "ballX: "+ballX);
			//Log.d(TAG, "ballY: "+ballY);
//			Log.d(TAG, "ballVelocityX: "+ballVelocityX);
//			Log.d(TAG, "ballVelocityY: "+ballVelocityY);

			
			//GAME OVER (collision with the sides)
			if(ballY > screenHeight) {
				ballX = screenWidth / 2; 
				ballY = screenHeight / 2;
				score.Player1Scored();
				resetPaddles();
				pause();
				
				
			}else if (ballY < 0) {
				ballX = screenWidth / 2; 
				ballY = screenHeight / 2;
				score.Player2Scored();
				resetPaddles();
				pause();			
			}
			
			//collision with the bats
			if(ballX > screenWidth || ballX < 0)
			{
				ballVelocityX *= -1;
				//Log.d(TAG, "ballVelocityX: "+ballVelocityX);
			}
			
			
			if(ballX > topBatXPos && ballX < topBatXPos + batLength && ballY < topBatY)
			{
				ballVelocityY *= -1;
				//Log.d(TAG, "ballVelocityX: "+ballVelocityX);
			}
		
			
			if(ballX > bottomBatXPos && ballX < bottomBatXPos + batLength && ballY > bottomBatY)
			{
				ballVelocityY *= -1;
				//Log.d(TAG, "ballVelocityX: "+ballVelocityX);
			}
			
			doAI();
		}
		
		//if 2 player mode is selected then do this
		if(PongThread.currentState == PongThread.STATE_RUNNING_2P)
		{
			if(DEBUG) {
				Log.d(TAG, "topBatX = "+ topBatXPos);
				Log.d(TAG, "bottomBatX = "+ bottomBatXPos);
				Log.d(TAG, "ballX = "+ballX);
				Log.d(TAG, "ballY = "+ballY);
				Log.d(TAG, "ballVelocityY = "+ ballVelocityY);
					
			}
			
			ballX += ballVelocityX;
			ballY += ballVelocityY;
			
			//GAME OVER (collision with the sides)
			if(ballY > screenHeight) {
				ballX = screenWidth / 2; 
				ballY = screenHeight / 2;
				score.Player1Scored();
				resetPaddles();
				pause();
				
				
			}else if (ballY < 0) {
				ballX = screenWidth / 2; 
				ballY = screenHeight / 2;
				score.Player2Scored();
				resetPaddles();
				pause();			
			}
			
			//collision with the bats
			if(ballX > screenWidth || ballX < 0)
				ballVelocityX *= -1;
			
			if(ballX > topBatXPos && ballX < topBatXPos + batLength && ballY < topBatY)
				ballVelocityY *= -1;
		
			
			if(ballX > bottomBatXPos && ballX < bottomBatXPos + batLength && ballY > bottomBatY)
				ballVelocityY *= -1;
		}
	}//end update
	
	private void doAI() {
		topBatXPos = ballX - 10;
	}

/**
 * Resets the position of the paddles.
 */
	private void resetPaddles() {
		int mid = (screenWidth / 2) - (batLength/2);
		topBatXPos = mid;
		bottomBatXPos = mid;
/*		
		Rect topTouch = new Rect(0,0,screenWidth,screenHeight / 8);
    	Rect bottomTouch = new Rect(0, 7 * screenHeight / 8, screenWidth, screenHeight);
    	
    	setTouchbox(topTouch);
    	setTouchbox(bottomTouch);
*/    	
		
	}
/**
 * Pause the game	
 */
	public void pause () {
		SystemClock.sleep(2000);
		
	}
/**
 * Input method. Handles left and right dpad inputs. (OG Droid)	
 * @param keyCode
 * @param msg
 * @return
 */
	public boolean keyPressed(int keyCode, KeyEvent msg) {
		
		if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			topBatXPos += batSpeed;
			bottomBatXPos -= batSpeed;
		}
		
		if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			topBatXPos -= batSpeed;
			bottomBatXPos += batSpeed;
		}
		return true;
	}//end keyPressed
	

/**
 * Draw method. Draws game object and score.
 * @param canvas
 * @param paint
 */
	//The draw method
	public void draw(Canvas canvas, Paint paint) {
		
		if (canvas != null) 
		{
			//clear screen
			canvas.drawRGB(20, 20, 20);
			
			//set color
			paint.setARGB(200, 165, 0, 255);
			
			//draw ball
			canvas.drawRect(new Rect(ballX, ballY, ballX+ballSize, ballY+ballSize), paint);
			
			//draw bats
			canvas.drawRect(new Rect(topBatXPos, topBatY, topBatXPos+batLength, topBatY+batHeight), paint); //top bat
			canvas.drawRect(new Rect(bottomBatXPos, bottomBatY, bottomBatXPos+batLength, bottomBatY+batHeight), paint); //bottom bat
			
			//draw score
			mPaint.setColor(Color.WHITE);
			canvas.drawText(score.getPlayer1ScoreString() + " : " + score.getPlayer2ScoreString(),screenWidth - 100,screenHeight - 75, mPaint);
			
			//check score
			if (score.getPlayer1ScoreInt() >= 10) {
				canvas.drawText("Player 1 Wins",(screenWidth/2)-batLength,screenHeight/2, mPaint);
				canvas.drawText("Hit Back to Start New Game",(screenWidth/2)-batLength,(screenHeight/2)+batLength, mPaint);	
			}
			if (score.getPlayer2ScoreInt() >= 10) {
				canvas.drawText("Player 2 Wins",(screenWidth/2)-batLength,screenHeight/2, mPaint);
				canvas.drawText("Hit Back to Start New Game",(screenWidth/2)-batLength,(screenHeight/2)+batLength, mPaint);	
			}
		}
			
	}//end draw
	

	public void setBallVelocity(int x, int y) 
	{
		ballVelocityX = x;
		ballVelocityY = y;
		
	}
}//end class

