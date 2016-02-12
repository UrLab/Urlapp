package be.urlab.Pamela;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import utils.thread.ActionScheduler;
import utils.web.HttpsTrustManager;

/**
 * Created by julianschembri on 10/02/16.
 */
public class PamelaActivity extends Activity {

    private final int updateRate = 15;
    private  final String url ="https://urlab.be/api/space/pamela/";

    private static ActionScheduler mainThreadScheduler = null;
    private static ActionScheduler secondaryThreadScheduler = null;

    @Override
    protected void onResume() {
        super.onResume();

        final Activity activity = this;

        HttpsTrustManager.allowAllSSL();

        if (PamelaActivity.mainThreadScheduler!=null) {
            mainThreadScheduler.cancel();
            mainThreadScheduler = null;
        }

        mainThreadScheduler = new ActionScheduler() {
            @Override
            protected void handler() {
                RequestQueue queue = Volley.newRequestQueue(activity);

                // Request a string response from the provided URL.
                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                activity.setContentView(R.layout.pamela_main);

                                final TextView counterTextView = (TextView) activity.findViewById(R.id.counter);
                                final TextView lastUpdateTextView = (TextView) activity.findViewById(R.id.lastUpdate);
                                final ListView memberList = (ListView) activity.findViewById(R.id.memberList);

                                try {
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                    df.setTimeZone(TimeZone.getTimeZone("UTC"));
                                    final Date date = df.parse(response.getString("last_updated").toString());

                                    // Cancel old thread if running
                                    if (secondaryThreadScheduler!=null) {
                                        secondaryThreadScheduler.cancel();
                                        secondaryThreadScheduler = null;
                                    }

                                    // Launch the update thread of Last Update field
                                    secondaryThreadScheduler = new ActionScheduler() {
                                        @Override
                                        protected void handler() {
                                            Date nowDate = new Date();
                                            int seconds = (int) (nowDate.getTime()-date.getTime())/1000;
                                            lastUpdateTextView.setText(Integer.toString(seconds)+" seconds");
                                        }
                                    };
                                    secondaryThreadScheduler.scheduleUI(activity, 1000);

                                    // Update Present Users field
                                    Integer nbUsers = response.getJSONArray("users").length();
                                    counterTextView.setText(Integer.toString(nbUsers));

                                    // Update the Member List view
                                    if (nbUsers>0) {
                                        memberList.setVisibility(View.VISIBLE);
                                        List<Member > memberArray = new ArrayList<Member>();
                                        for (int i = 0; i < response.getJSONArray("users").length(); ++i) {
                                            JSONObject memberJSON = (JSONObject) response.getJSONArray("users").get(i);
                                            Member aMember = new Member(memberJSON.getBoolean("has_key"),memberJSON.getString("username"),memberJSON.getString("gravatar"));
                                            memberArray.add(i, aMember);
                                        }
                                        final MemberListAdapter adapter = new MemberListAdapter(activity, memberArray);
                                        memberList.setAdapter(adapter);
                                    } else {
                                        memberList.setVisibility(View.INVISIBLE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        activity.setContentView(R.layout.pamela_error);
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        };
        mainThreadScheduler.schedule(updateRate * 1000);


    }

}
