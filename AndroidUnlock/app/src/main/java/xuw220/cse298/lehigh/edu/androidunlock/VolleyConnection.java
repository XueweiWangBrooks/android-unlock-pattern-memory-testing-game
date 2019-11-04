package xuw220.cse298.lehigh.edu.androidunlock;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyConnection {
    private static VolleyConnection mInstance;
    private RequestQueue mRequestQueue;

    private VolleyConnection(Context context) {
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized VolleyConnection getVolleyConnection(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyConnection(context);
        }
        return mInstance;
    }

    public void addToRequestQueue(Request request) {
        mRequestQueue.add(request);
    }
}
