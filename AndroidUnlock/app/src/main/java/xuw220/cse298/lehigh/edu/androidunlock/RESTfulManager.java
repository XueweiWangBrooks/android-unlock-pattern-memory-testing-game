package xuw220.cse298.lehigh.edu.androidunlock;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RESTfulManager {
    private Context context;
    private VolleyConnection connection;
    private static RESTfulManager ourInstance;
    private static final String BASE_URL = "http://10.0.2.2:8000/";

    public static RESTfulManager getManager(Context c) {
        if (ourInstance == null) {
            ourInstance = new RESTfulManager(c);
        } else {
            ourInstance.context = c;
        }
        return ourInstance;
    }

    private RESTfulManager(Context c) {
        context = c;
        connection = VolleyConnection.getVolleyConnection(c.getApplicationContext());
    }

    public void putHighScore(int gameId, String name, int newScore) {
        try {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, BASE_URL + "game/put/" + gameId,
                    new JSONObject(String.format("{'score': %d, 'username': '%s'}", newScore, name)),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            FormattedResponse formatted = FormattedResponse.getFormattedResponse(response);
                            if (!formatted.getResult())
                                Log.i("error", formatted.getMessage());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            connection.addToRequestQueue(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getPatternList(int gameId, final ArrayList<Pattern> patterns) {
        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL + "game/" + gameId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                FormattedResponse formatted = FormattedResponse.getFormattedResponse(response);
                if (!formatted.getResult()) {
                    Log.i("error", "Internal Error: " + formatted.getMessage());
                } else {
                    try {
                        JSONArray jsonArray = ((JSONObject) formatted.getData()).getJSONArray("patterns");
                        for (int i = 0; i < jsonArray.length(); i++)
                            patterns.add(new Pattern(jsonArray.getJSONObject(i).getString("pattern_str")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("err", "VolleyError: " + error.getMessage());
                error.printStackTrace();
            }
        });
        connection.addToRequestQueue(request);
    }

    public void postGameResults(JSONObject gameToSend) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL + "game/post", gameToSend,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        FormattedResponse formatted = FormattedResponse.getFormattedResponse(response);
                        Log.i("result", "" + formatted.getResult());
                        if (!formatted.getResult()) {
                            Log.i("error", "Score Did Not Recorded Due to Internal Error: " + formatted.getMessage());
                        } else
                            Log.i("score", "Recorded ");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("err", "VolleyError: " + error.getMessage());
                error.printStackTrace();
            }
        });
        connection.addToRequestQueue(request);
    }

    public void getGameListing(final RecyclerView rv, final GameListAdapter.ClickChallengeBtnListener onClick) {
        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL + "game", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                FormattedResponse formatted = FormattedResponse.getFormattedResponse(response);
                if (!formatted.getResult()) {
                    Toast.makeText(context, "Internal Error: " + formatted.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                Log.i("Json", "Get Result");
                try {
                    JSONArray jsonGameList = (JSONArray) formatted.getData();
                    ArrayList<GameData> gameList = new ArrayList<>();
                    for (int i = 0; i < jsonGameList.length(); i++) {
                        JSONObject jsonGame = jsonGameList.getJSONObject(i);
                        Date date = null;
                        try {
                            date = new SimpleDateFormat("MM/dd/yyyy", Locale.US).parse(jsonGame.getString("hscore_date"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        gameList.add(new GameData(jsonGame.getInt("id"), jsonGame.getInt("high_score"), jsonGame.getString("user_name"), date));
                    }
                    rv.setLayoutManager(new LinearLayoutManager(context));
                    GameListAdapter adapter = new GameListAdapter(context, gameList);
                    adapter.setClickChallengeBtnListener(onClick);
                    rv.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Internal Error: Json Format.", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        connection.addToRequestQueue(request);
    }
}
