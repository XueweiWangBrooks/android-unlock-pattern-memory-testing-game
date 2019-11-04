package xuw220.cse298.lehigh.edu.androidunlock;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Bitmap image;
    int order;
    private View.OnTouchListener nodeOnTouchListener;
    private long gameTimeMs;
    private long previousPatternTimeLeft;
    private long timeLeft;
    private ArrayList<View> nodeViewList;
    private CountDownTimer timer;
    private TextView timerView;
    private int score = 0;
    private TextView scoreView;
    private ArrayList<Pattern> challengePatternList;
    private int highScore;
    private int gameId;
    private ArrayList<Pattern> generatedPatternList;
    private boolean isNewGame;
    private TableLayout gamePane;
    private Vertex v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        scoreView = findViewById(R.id.scoreView);
        nodeOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ClipData.Item item = new ClipData.Item("");

                ClipData dragData = new ClipData(
                        "",
                        new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                        item);
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);

                view.startDrag(dragData, myShadow, null, 0);

                return true;
            }
        };
        Intent intent = getIntent();
        isNewGame = intent.getBooleanExtra("new game", true);
        if (isNewGame) {
            order = intent.getIntExtra("board size", 0);
            generatedPatternList = new ArrayList<>();
        } else {
            highScore = intent.getIntExtra("highScore", 0);
            challengePatternList = new ArrayList<>();
            gameId = intent.getIntExtra("gameId", 0);
            RESTfulManager manager = RESTfulManager.getManager(this);
            manager.getPatternList(gameId, challengePatternList);
        }

//
//        ImageView background = findViewById(R.id.backgroundView);
//        Bitmap bitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888);
//        final Canvas canvas = new Canvas(bitmap);
//        background.setImageBitmap(bitmap);
//
//        Paint paint = new Paint();
//
//        paint.setColor(Color.BLUE);
//        canvas.drawCircle(100, 100, 50, paint);


        //background.setOnDragListener(onDragListener);


        timerView = findViewById(R.id.timerView);
        timerView.setVisibility(View.INVISIBLE);
        gamePane = findViewById(R.id.gamePane);
    }

    public void startBtn(View view) {
        if ((!isNewGame) && challengePatternList.isEmpty()) {
            Toast.makeText(this, "Wait Shortly to Get the Game Downloaded.", Toast.LENGTH_LONG).show();
            return;
        }
        if (!isNewGame)
            order = challengePatternList.get(0).getOrder();
        gameTimeMs = order * 5000;
        view.setVisibility(View.INVISIBLE);
        timerView.setVisibility(View.VISIBLE);
        previousPatternTimeLeft = timeLeft = gameTimeMs;
        timer = new CountDownTimer(timeLeft, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                timerView.setText(String.format("%ds", timeLeft / 1000));
            }

            @Override
            public void onFinish() {
                MainActivity.this.finish();
            }
        };
        timer.start();
        drawPattern();
    }


    public void drawPattern() {
        if (timer != null) {
            timer.cancel();
        }
        Pattern pattern;

        if (isNewGame) {
            pattern = Pattern.randomPatternGenerator(order, order * 2);
            generatedPatternList.add(pattern);
        } else {
            if (challengePatternList.isEmpty()) {
                Log.i("pattern", "challenge finished");
                Intent i = new Intent(getApplicationContext(), ResultActivity.class);
                i.putExtra("isNewGame", false);
                i.putExtra("rawScore", score);
                i.putExtra("score", (int) (score + timeLeft / 100));
                i.putExtra("isNewRecord", score > highScore);
                i.putExtra("earlyFinishBonus", (int) (timeLeft / 100));
                i.putExtra("gameId", gameId);
                startActivity(i);
                finish();
                return;
            }
            pattern = challengePatternList.remove(0);
        }

        nodeViewList = new ArrayList<>();
        gamePane.removeAllViews();

        for (int i = 0; i < order; i++) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(0,
                    TableRow.LayoutParams.WRAP_CONTENT);

            for (int j = 0; j < order; j++) {
                ImageView dot = new ImageView(this);
                dot.setImageResource(R.drawable.dot);
                dot.setLayoutParams(params);
                dot.animate().alpha(0f).setDuration(1000);
//                        dot.setOnDragListener(onDragListener);
                dot.setTag(new Coordinates(j, i));

                //numAdjacent = new TextView(this);
                //numAdjacent.setLayoutParams(params);

                row.addView(dot);
                nodeViewList.add(dot);
                //row.addView(numAdjacent);
                row.setLayoutParams(params);

                //board.flagCell(j,i);
            }
            gamePane.setStretchAllColumns(true);
            gamePane.addView(row, params);
//                    Coordinates cellCoordinates = (Coordinates)view.getTag();
//
//                    int col = cellCoordinates.getX();
//                    int rowCoordinate = cellCoordinates.getY();
//
//                    //MineSweeperCell status = board.getCell(col,row);
//                    Log.i("status", " col is: " + col + " row is: " + rowCoordinate);

        }
        animatePattern(pattern);
    }

    public void animatePattern(final Pattern pattern) {
        final Handler handler = new Handler();
        long msDelayed = 1000;
        for (int i = 0; i < pattern.getVertexCount(); i++) {
            Vertex v = pattern.getVertex(i);
            final View vertexView = this.nodeViewList.get(v.getI() * order + v.getJ());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    vertexView.animate().alpha(0f).setDuration(500/order).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            vertexView.animate().alpha(1f).setDuration(1000/order);
                        }
                    });
                }
            }, msDelayed);
            msDelayed += 1000;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setDrawPattern(pattern);
            }
        }, msDelayed);
    }

    public void setDrawPattern(final Pattern pattern) {

        final TableLayout tblLayout = findViewById(R.id.gamePane);
        nodeViewList = new ArrayList<>();
        int totalRows = order;
        int totalCols = order;
        tblLayout.removeAllViews();

        for (int i = 0; i < totalRows; i++) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(0,
                    TableRow.LayoutParams.WRAP_CONTENT);

            for (int j = 0; j < totalCols; j++) {
                ImageView dot = new ImageView(this);
                dot.setImageResource(R.drawable.dot);
                dot.setLayoutParams(params);

                dot.setOnTouchListener(nodeOnTouchListener);
                row.addView(dot);
                nodeViewList.add(dot);
                row.setLayoutParams(params);
            }
            tblLayout.setStretchAllColumns(true);
            tblLayout.addView(row, params);
        }

        View.OnDragListener onDragListener = new View.OnDragListener() {

            Pattern patternByPlayer = null;

            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {

                final View v = (View) dragEvent.getLocalState();
                switch (dragEvent.getAction()) {

                    case DragEvent.ACTION_DRAG_STARTED:
                        Log.i("drag start", "drag start here");
                        patternByPlayer = new Pattern(order);

                        return true;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        int tl_height = tblLayout.getHeight();
                        int tl_width = tblLayout.getWidth();
                        final int cell_height = tl_height / order;
                        final int cell_width = tl_width / order;
                        Log.i("table height", Integer.toString(tblLayout.getHeight()));
                        int column = (int) (dragEvent.getX() / cell_width);
                        int row = (int) (dragEvent.getY() / cell_height);
                        Log.i("Drag", "" + row + "," + column);
                        patternByPlayer.addVertex(new Vertex(row, column));
                        Log.i("player's pattern", patternByPlayer.toString());
                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:
                        Log.i("drag end", "drag end here");
                        Log.i("player's pattern", patternByPlayer.toString());
                        checkInput(patternByPlayer, pattern);
                        return true;
                }

                return true;
            }
        };
        tblLayout.setOnDragListener(onDragListener);

        timer = new CountDownTimer(timeLeft, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                timerView.setText(String.format("%ds", timeLeft / 1000));
            }

            @Override
            public void onFinish() {
                if (isNewGame) {
                    Intent i = new Intent(getApplicationContext(), ResultActivity.class);
                    i.putExtra("isNewGame", true);
                    String patternAsJson = Pattern.encodeAsJson(generatedPatternList);
                    i.putExtra("patternList", patternAsJson);
                    i.putExtra("isNewRecord", true);
                    i.putExtra("rawScore", score);
                    i.putExtra("score", score);
                    startActivity(i);
                    finish();
                } else {
                    Log.i("pattern", "challenge finished");
                    Intent i = new Intent(getApplicationContext(), ResultActivity.class);
                    i.putExtra("isNewGame", false);
                    i.putExtra("rawScore", score);
                    i.putExtra("score", score);
                    i.putExtra("isNewRecord", score > highScore);
                    i.putExtra("earlyFinishBonus", timeLeft / 100);
                    i.putExtra("gameId", gameId);
                    startActivity(i);
                    finish();
                }
            }
        };

        timer.start();
    }


    public void checkInput(Pattern playerPattern, Pattern patternProvided) {
        if (playerPattern.equals(patternProvided)) {
            score += 1000000 / (previousPatternTimeLeft - timeLeft);
            previousPatternTimeLeft = timeLeft;
            Log.i("score", "" + score);
            scoreView.setText(String.format("%d", score));
            Toast.makeText(this, "You get it right.", Toast.LENGTH_SHORT).show();
            drawPattern();
            return;
        }
        Toast.makeText(this, "Try again.", Toast.LENGTH_SHORT).show();
    }


    public void goHome(View v) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        startActivity(intent);
    }
}