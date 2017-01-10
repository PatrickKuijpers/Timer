package nl.tcilegnar.timer.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.fragments.DayEditorFragment;

public class DayEditorActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.activity_content, new DayEditorFragment()).commit();
        }
    }
}
