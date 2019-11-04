package xuw220.cse298.lehigh.edu.androidunlock;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class FormattedResponse {
    private boolean result;
    private String message;
    private Object data;

    private FormattedResponse(boolean result, String message, Object data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    public static FormattedResponse getFormattedResponse(JSONObject response) {
        try {
            String result = response.getString("result");
            if (result.equals("error")) {
                String message = response.getString("message");
                return new FormattedResponse(false, message, null);
            } else if (result.equals("ok")) {
                Object data;
                try {
                    data = response.getJSONObject("data");
                } catch (JSONException ex) {
                    data = response.getJSONArray("data");
                }
                return new FormattedResponse(true, null, data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static FormattedResponse getFormattedResponse(String responce) {
        try {
            return getFormattedResponse(new JSONObject(responce));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean getResult() {
        return result;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
