package xuw220.cse298.lehigh.edu.androidunlock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultActivity extends AppCompatActivity {
    private int score;
    private String username;
    private String patternListAsStr;
    private boolean isNewRecord;
    private boolean isNewGame;
    private int gameId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        TextView scoreView = findViewById(R.id.scoreView);
        TextView finishEarlyView = findViewById(R.id.finishEarlyView);
        TextView rawScoreView = findViewById(R.id.rawScoreView);
        TextView newRecordView = findViewById(R.id.newRecordView);
        Intent i = getIntent();
        score = i.getIntExtra("score", 0);
        isNewGame =i.getBooleanExtra("isNewGame", false);
        if (isNewGame)
            patternListAsStr = i.getStringExtra("patternList");
        else
            gameId = i.getIntExtra("gameId", 0);
        isNewRecord = i.getBooleanExtra("isNewRecord", false);
        if (!isNewRecord)
            newRecordView.setVisibility(View.INVISIBLE);
        scoreView.setText("" + score);
        if (i.getIntExtra("earlyFinishBonus", 0) > 0)
            finishEarlyView.setText(String.format(getString(R.string.early_finish_bonus_format), i.getIntExtra("earlyFinishBonus", 0)));
        else
            finishEarlyView.setVisibility(View.INVISIBLE);
        rawScoreView.setText(String.format(getString(R.string.raw_score_format), i.getIntExtra("rawScore", 0)));
    }

    public void OnClickConfirmBtn(View view) {
        EditText nameView = findViewById(R.id.nameEditView);
        if (isNewRecord) {
            username = nameView.getText().toString();
            if (username.length() < 1) {
                Toast.makeText(this, "Please Enter a Name.", Toast.LENGTH_SHORT).show();
                return;
            }
            sendNewRecord();
            finish();
        } else {
            Intent i = new Intent();
            i.putExtra("newRecord", false);
            setResult(RESULT_OK, i);
            finish();
        }
    }

    public void OnClickCancelBtn(View view){
        setResult(RESULT_CANCELED);
        finish();
    }

    private void sendNewRecord() {
        if(isNewGame) {
            StringBuilder resultJsonAsStr = new StringBuilder();
            resultJsonAsStr.append("{");
            resultJsonAsStr.append("'user_name':" + "'" + username + "'" + ",");
            resultJsonAsStr.append("'high_score':" + score + ",");
            resultJsonAsStr.append("'pattern_list':" + patternListAsStr);
            resultJsonAsStr.append("}");
            Log.i("json", resultJsonAsStr.toString());
            try {
                JSONObject resultJson = new JSONObject(resultJsonAsStr.toString());
                RESTfulManager.getManager(this).postGameResults(resultJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            RESTfulManager.getManager(this).putHighScore(gameId, username, score);
        }
    }

}
