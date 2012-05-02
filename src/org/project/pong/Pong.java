package org.project.pong;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;					//for Log.d
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;					//this resolves View
import android.view.View.OnClickListener;
import android.content.DialogInterface;
import android.content.Intent;				//this resolves Intent


//read about implement keyword: they are used to implement interfaces
public class Pong extends Activity {
    
//----DEBUG-----	
	private static final String TAG = "Pong";
//----DEBUG-----
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setListeners();    
    }//end onCreate
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.game_menu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case R.id.menu_preferences:
    		Intent i = new Intent(this, PongPreferences.class);
    		startActivity(i);
    		break;
    	}
    	return false;
    }
    
    //Listeners
    protected void setListeners () {
    	this.findViewById(R.id.one_player_button)
    	.setOnClickListener(new OnClickListener () 
    	{
    		@Override
    		public void onClick(View v) {
    			openNewGameDialog();
    			Game.savedScore = false;
    			PongThread.currentState = PongThread.STATE_RUNNING_1P;
    		}
    	}); 
    	
    	this.findViewById(R.id.two_player_button)
    	.setOnClickListener(new OnClickListener () {
    		@Override
    		public void onClick(View v) {
    			openNewGameDialog();
    			Game.savedScore = false;
    			PongThread.currentState = PongThread.STATE_RUNNING_2P;
    		}
    	});
  	
    	this.findViewById(R.id.continue_button)
    	.setOnClickListener(new OnClickListener () {
    		@Override
    		public void onClick(View v) {
    			startGame(Game.DIFFICULTY_CONTINUE_VALUE);
    			Game.savedScore = true;
    		}
    	});
    	
    	this.findViewById(R.id.exit_button)
    	.setOnClickListener(new OnClickListener () {
    		@Override
    		public void onClick(View v) {
    			finish();
    			Game.savedScore = false;
    		}
    	});
    }

    //Start the game
    private void startGame(int i) {
    	Intent intent = new Intent(this, Game.class);
    	startActivity(intent);
    	intent.putExtra(Game.DIFFICULTY_KEY, i);
    	Log.d(TAG, "startGame INTENT");
    	Log.d(TAG, "DIFFICULTY "+i);
    	
    }
    
    private void openNewGameDialog() {
    	new AlertDialog.Builder(this)
    	.setTitle(R.string.new_game_title)
    	.setItems(R.array.difficulty,new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialoginterface, int i) {
    			startGame(i);}
    	})
    	.show();
    }    

    
}//end class