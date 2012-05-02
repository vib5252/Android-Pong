package org.project.pong;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;




public class PongView extends SurfaceView implements SurfaceHolder.Callback{

	public PongThread thread;
	//public static int tx = 0;
	//public static int ty = 0;
	

	
//	private static float xPosition1 = Game.width / 2;
//	private static float yPosition1;
//	private static float xPosition2 = Game.width / 2;
//	private static float yPosition2;
	
	private Context context;
	private SurfaceHolder holder;
	
	private boolean threadFlag = true;
	//----DEBUG-----	
	private static final String TAG = "Pong";
	//----DEBUG-----	

//------------------------------------------------------------------	
	//constructor
	public PongView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//this.game = (Game) context;
		this.context = context;
		Log.d(TAG, "Game View Called.");					
		//listen for events...
		//SurfaceHolder holder = getHolder();
		this.holder = getHolder();
		holder.addCallback(this);
		setFocusable(true);
		//_thread = CreateNewPongThread();
		
	
		//instantiate the thread
		//_thread = new PongThread(holder, context, new Handler());
		thread = new PongThread(holder, this);
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

//----------------------------------------------------------------------------------------	
	//SurfaceHolder.Callback interface
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

		
	}
		
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		Log.d(TAG, "surfaceCreated");
		
		//if(_thread.isAlive()) {
		if(threadFlag) {
			thread.interrupt();
			thread = new PongThread(getHolder(), this);
			//_thread = new PongThread(holder, getContext(), new Handler());
		}
			
		thread.setRunning(true);
		thread.start();
		threadFlag = true;
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
 
		boolean retry = true;
		thread.setRunning(false);
		while(retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				//we will try it again and again...
			}
		}
		//thread.stop(); //this won't work in ics (deprecated)
		thread.interrupt();
	}
//----------------------------------------------------------------------------------------
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)){
			//System.exit(0);
			//this makes it go to the previous activity from the game
			return super.onKeyDown(keyCode, event);
		}
			
		return thread.getPongState().keyPressed(keyCode, event);
	}
	
	
/**
 * Multi-touch
 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		float xPosition1 = Game.width / 2;
		float xPosition2 = Game.width / 2;
		float yPosition1 = 0;
		float yPosition2 = 0;
				
		int fingersTouching = event.getPointerCount();
		int index = 0;

		//Log.d(TAG, "fingersTouching: "+fingersTouching);
		
		while(index < fingersTouching)
		{
			float yp = event.getY(index) / Game.height;
			
			//check y-position and move bottom paddle
			if(yp >= 0.8)
			{
				xPosition1 = event.getX(index);
				yPosition1 = event.getY(index);
			}
			
			//check y-position and move top paddle
			if(yp <= 0.2)
			{
				xPosition2= event.getX(index);
				yPosition2 = event.getY(index);
			}
			
			index++;

		}
			
		switch (event.getAction())
		{
			case MotionEvent.ACTION_MOVE:
				thread.setPaddlePosition(xPosition1, yPosition1, xPosition2, yPosition2, event);
				break;
		}
		
		return true;
	}

	
	
}//end class
