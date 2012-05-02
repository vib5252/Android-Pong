package org.project.pong;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


public class Game extends Activity{
	
	public static PongView mPongView;
	//private SurfaceHolder holder;

	private static final String TAG = "Pong";
	public static final String PLAYER1SCORE_KEY = "player1score_key";
	public static final String PLAYER2SCORE_KEY = "player2score_key";
	public static String PLAYER1SCORE_VALUE = "default_value";
	public static String PLAYER2SCORE_VALUE = "default_value";
	
	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_MEDIUM = 1;
	public static final int DIFFICULTY_HARD = 2;
	public static final int DIFFICULTY_CONTINUE_VALUE = -1;
	public static final String DIFFICULTY_KEY = 
			"org.project.pong.difficulty";
		
	public static int score1container, score2container;
	public static boolean savedScore;	//if scoreFlag is true then restore the saved score
	private boolean dataSaved;
	static Context context;
	
	public static int width;
	public static int height;
	
	public static int ballVelocityX;
	public static int ballVelocityY;
	
	public static int difficulty;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "CREATE");	
		mPongView = new PongView(this, null);
		setContentView(mPongView);
		mPongView.requestFocus();
		dataSaved = false;
		
		//Get the screen dimension of the phone
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		
		difficulty = getIntent().getIntExtra(DIFFICULTY_KEY, DIFFICULTY_EASY);
		
		Log.d(TAG,"DIFFICULTY SET "+difficulty);
		getGame(difficulty);

	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "PAUSE");
		
		//Save the current score
		getPreferences(MODE_PRIVATE).edit().putString(PLAYER1SCORE_KEY,
				((PongView) mPongView).thread.mState.score.getPlayer1ScoreString()).commit();
		getPreferences(MODE_PRIVATE).edit().putString(PLAYER2SCORE_KEY,
				((PongView) mPongView).thread.mState.score.getPlayer2ScoreString()).commit();		
		dataSaved = true;
		
	}	
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "RESUME");
		setContentView(mPongView);
		
		Log.d(TAG,"DIFFICULTY SET "+difficulty);
		//if dataSaved then use the savedScore constructor in PongState
		if (dataSaved) {
			getIntent().putExtra(DIFFICULTY_KEY, DIFFICULTY_CONTINUE_VALUE); // If the activity is restarted, do a continue next time
			savedScore = true;
		}
		difficulty = getIntent().getIntExtra(DIFFICULTY_KEY, DIFFICULTY_CONTINUE_VALUE);
		getGame(difficulty);			
	}
		
	@Override
	protected void onStop() {
		Log.d(TAG, "STOP");
		super.onStop();
		
		boolean retry = true;
		((PongView) mPongView).thread.setRunning(false);
		while(retry) {
			try {
				((PongView) mPongView).thread.join();
				retry = false;
			} catch (InterruptedException e) {
				//we will try it again and again...
			}
		}
		//((PongView) mPongView).thread.stop();
		((PongView) mPongView).thread.interrupt();
	}
	
	
	private void getGame(int diff) {
		String score1, score2;
		switch (diff) {
		case DIFFICULTY_CONTINUE_VALUE:
			score1 = getPreferences(MODE_PRIVATE).getString(PLAYER1SCORE_KEY, PLAYER1SCORE_VALUE);
			score2 = getPreferences(MODE_PRIVATE).getString(PLAYER2SCORE_KEY, PLAYER2SCORE_VALUE);
			
			score1container = Integer.parseInt(score1);
			score2container = Integer.parseInt(score2);
			//Log.d(TAG, "score converted to int...");
			//Log.d(TAG, "score stored...");
			//Log.d(TAG, "converted to int p1: " + score1container);
			//Log.d(TAG, "converted to int p2: " + score2container);

			break;
			// ...
		case DIFFICULTY_HARD:
			ballVelocityX = 10;
			ballVelocityY= 10;
			break;

		case DIFFICULTY_MEDIUM:
			ballVelocityX = 7;
			ballVelocityY= 7;
			break;			
			
		default:
			ballVelocityX = 5;
			ballVelocityY = 5;

			break;
		}	 
	}//end getGame


	/**
	 * Handle for toast message
	 * (Handler allows you to send and process Message and Runnable objects associated with a 
	 * thread.)
	 */
/*	
	public Handler returnHandler() {
		return mHandler;
	}
	private Handler mHandler = new Handler() {
		public void handlerMessage (Message m) {
			//toast code
			
		}
	};
*/
	
	
	
	
}//end class
