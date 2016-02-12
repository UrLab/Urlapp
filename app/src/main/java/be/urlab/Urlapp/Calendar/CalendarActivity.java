package be.urlab.Urlapp.Calendar;

import android.app.Activity;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import be.urlab.Urlapp.R;

/**
 * Created by julianschembri on 11/02/16.
 */
public class CalendarActivity extends Activity {

    private final String url = "https://urlab.be/api/events/";

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.calendar_main);

        ListView eventsListView = (ListView) this.findViewById(R.id.eventsListView);

        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("JSONObject response", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("JSONObject response", "Error");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}
