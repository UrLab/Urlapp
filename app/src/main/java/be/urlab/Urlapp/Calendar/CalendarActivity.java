package be.urlab.Urlapp.Calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.urlab.Urlapp.R;
import utils.date.DateUtil;
import utils.format.MarkDownToHtml;

/**
 * Created by julianschembri on 11/02/16.
 */
public class CalendarActivity extends Activity {

    private final String url = "https://urlab.be/api/events/?ordering=-start&status=r";

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.calendar_main);

        final ListView eventsListView = (ListView) this.findViewById(R.id.eventsListView);
        final Activity activity = this;

        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<Event> eventArray = new ArrayList<Event>();
                            for (int i = 0; i < response.getJSONArray("results").length(); ++i) {
                                JSONObject eventJSON = (JSONObject) response.getJSONArray("results").get(i);
                                try {
                                    Date start = DateUtil.fromString(eventJSON.getString("start"),"yyyy-MM-dd'T'HH:mm:ss");
                                    Date stop = DateUtil.fromString(eventJSON.getString("stop"), "yyyy-MM-dd'T'HH:mm:ss");
                                    String imageUrl = eventJSON.getString("picture");
                                    String description = eventJSON.getString("description");
                                    Event anEvent = new Event(eventJSON.getString("title"),eventJSON.getString("place"),start,stop,imageUrl,description);
                                    eventArray.add(i, anEvent);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            EventListAdapter adapter = new EventListAdapter(activity,eventArray);
                            eventsListView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("JSONObject response", "Error");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event anEvent = (Event) parent.getItemAtPosition(position);

                PopupWindow p = new PopupWindow(activity);

                RelativeLayout r = (RelativeLayout) findViewById(R.id.calendar_main);

                final PopupWindow popup = new PopupWindow(r,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        true);
                popup.setBackgroundDrawable(getDrawable(R.drawable.event_selected_window));
                popup.setOutsideTouchable(true);

                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.event_selected, parent, false);
                TextView title = (TextView) v.findViewById(R.id.title);
                title.setText(anEvent.getTitle());
                WebView description = (WebView) v.findViewById(R.id.description);
                description.loadData(MarkDownToHtml.format(anEvent.getDescription()), "text/html; charset=UTF-8", null);
                description.setBackgroundColor(Color.TRANSPARENT);
                description.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

                TextView place = (TextView) v.findViewById(R.id.place);
                place.setText(anEvent.getPlace());
                TextView date = (TextView) v.findViewById(R.id.date);
                date.setText(anEvent.getTime());

                popup.setContentView(v);
                popup.showAtLocation(parent, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);

                View container = (View) popup.getContentView().getParent();
                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                WindowManager.LayoutParams pe = (WindowManager.LayoutParams) container.getLayoutParams();
                pe.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                pe.dimAmount = 0.5f;
                wm.updateViewLayout(container, pe);

            }
        });

    }

}
