package be.urlab.Calendar;

import android.app.Activity;
import android.os.Bundle;

import be.urlab.Pamela.R;

/**
 * Created by julianschembri on 11/02/16.
 */
public class CalendarActivity extends Activity {
    public class PamelaActivity extends Activity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.calendar_main);
        }

        @Override
        protected void onResume() {
            super.onResume();
        }

    }
}
