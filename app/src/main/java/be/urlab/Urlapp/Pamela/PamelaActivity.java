package be.urlab.Urlapp.Pamela;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import utils.thread.ActionScheduler;
import utils.web.HttpsTrustManager;

/**
 * Created by julianschembri on 10/02/16.
 */
public class PamelaActivity extends Activity {

    private final int updateRate = 15;
    private final String url ="https://urlab.be/api/space/pamela/";

    private static ActionScheduler mainThreadScheduler = null;

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
                                final ListView memberList = (ListView) activity.findViewById(R.id.memberList);
                                final ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.updateProgressBar);

                                try {
                                    final Date date = DateUtil.fromString(response.getString("last_updated"), "yyyy-MM-dd'T'HH:mm:ss.SSS");

                                    progressBar.setProgress(0);
                                    ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 100);
                                    animation.setDuration(updateRate*1000);
                                    animation.setInterpolator(new LinearInterpolator());
                                    animation.start();

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
