package xuw220.cse298.lehigh.edu.androidunlock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {

    private RESTfulManager manager;
    private GameListAdapter.ClickChallengeBtnListener onClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onClick = new GameListAdapter.ClickChallengeBtnListener() {
            @Override
            public void onClick(GameData gameData) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("new game", false);
                i.putExtra("gameId", gameData.getGameId());
                i.putExtra("highScore", gameData.getHighScore());
                startActivityForResult(i, 101);
            }
        };
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        manager = RESTfulManager.getManager(HomeActivity.this);
        final RecyclerView recyclerView = findViewById(R.id.gameDataView);
        manager.getGameListing(recyclerView,onClick);
    }
    public void goToEasyGame(View v){
        Intent intent = new Intent(v.getContext(), MainActivity.class);
        intent.putExtra("new game", true);
        intent.putExtra("board size", 4);
        startActivityForResult(intent, 100);

    }
    public void goToMediumGame(View v){
        Intent intent = new Intent(v.getContext(), MainActivity.class);
        intent.putExtra("new game", true);
        intent.putExtra("board size", 5);
        startActivityForResult(intent, 100);

    }
    public void goToHardGame(View v){
        Intent intent = new Intent(v.getContext(), MainActivity.class);
        intent.putExtra("new game", true);
        intent.putExtra("board size", 7);
        startActivityForResult(intent, 100);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        manager = RESTfulManager.getManager(HomeActivity.this);
        final RecyclerView recyclerView = findViewById(R.id.gameDataView);
        manager.getGameListing(recyclerView, onClick);
    }
}
