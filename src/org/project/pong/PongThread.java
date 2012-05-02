package org.project.pong;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class PongThread extends Thread{
	
	/* Handle to the surface manager object we interact with */
	public PongState mState;
	public static int currentState;
	
	public static String TAG = "pong";
	private SurfaceHolder mSurfaceHolder;
	private Paint mPaint;
	private boolean run = false;
	
	//Game States
	public static final int STATE_PAUSED = 2;
	public static final int STATE_RUNNING_1P = 4;
	public static final int STATE_RUNNING_2P = 5;
	public static final int STATE_RUNNING_0P = 6;

	
	
	
//------------------------------------------------------------------------------------	
	//constructor
	//public PongThread(SurfaceHolder surfaceHolder, Context context, Handler handler) {
	public PongThread(SurfaceHolder surfaceHolder, PongView pongView) {		
		
		mSurfaceHolder = surfaceHolder;
		mPaint = new Paint();
		mState = new PongState();
		Log.d(TAG, "Pong Thread Called.");
		
	}

//------------------------------------------------------------------------------------
	@Override
	public void run() {
		Canvas canvas;
		//long startTime = SystemClock.uptimeMillis();
		Log.d(TAG, "INSIDE THREAD RUN LOOP");
		//Log.d(TAG, "game activity p1: "+ mState.score.getPlayer1ScoreString());
		//Log.d(TAG, "game activity p2: "+ mState.score.getPlayer2ScoreString());
		Log.d(TAG, "currentState: "+currentState);
		
		while(run) {
			canvas = null;
			try {
				
				canvas = mSurfaceHolder.lockCanvas(null);
				//check score
				if (mState.score.getPlayer1ScoreInt() >= 10 || mState.score.getPlayer2ScoreInt() >= 10)
					run = false;
				
				synchronized (mSurfaceHolder) {
					//update game state
					mState.update();
					mState.draw(canvas,mPaint);
				}

			}
			finally {
				//do this in a 'finally' so that if an exception is thrown
				//during the above, we don't leave the Surface in an inconsistent state
				if (canvas != null)
					mSurfaceHolder.unlockCanvasAndPost(canvas);
				}
		}//end while
	}//end run

//------------------------------------------------------------------------------------	
	//helper functions
	public PongState getPongState() {
		return mState;
	}
	
	public void setRunning(boolean flag) {
		run = flag;
	}
//------------------------------------------------------------------------------------

	public void setPaddlePosition(float xPosition1, float yPosition1, float xPosition2, float yPosition2, MotionEvent event)
	{
			
			if (currentState == STATE_RUNNING_1P)	
			{
				int paddleXPos1 = (int) xPosition1;
				//check and set limits
				if(paddleXPos1 <= 0)
					paddleXPos1 = 0;
				if(paddleXPos1 >= 650)
					paddleXPos1 = 650;
				
				mState.bottomBatXPos = paddleXPos1;
			}
			
			if (currentState == STATE_RUNNING_2P)
			{
				int paddleXPos1 = (int) xPosition1;
				//check and set limits for paddle 1
				if(paddleXPos1 <= 0)
					paddleXPos1 = 0;			
				if(paddleXPos1 >= 650)
					paddleXPos1 = 650;
				//update position
				mState.bottomBatXPos = paddleXPos1;
				
				int paddleXPos2 = (int) xPosition2;
				//check and set limits for paddle 2
				if(paddleXPos2 <= 0)
					paddleXPos2 = 0;
				if(paddleXPos2 >= 650)
					paddleXPos2 = 650;
				//update position
				mState.topBatXPos = paddleXPos2;
			}
	
	}/* setPaddlePosition */

}//end class
