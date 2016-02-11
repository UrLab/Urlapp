package be.urlab.Pamela;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by julianschembri on 10/02/16.
 */
public class PamelaActivity extends Activity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.pamela_main);
        }

        @Override
        protected void onResume() {
            super.onResume();
            PamelaFieldsUpdater pfu = new PamelaFieldsUpdater();
            pfu.update(this);
        }

}
