package nl.tcilegnar.timer.activities;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import nl.tcilegnar.timer.fragments.DayEditorFragment;
import nl.tcilegnar.timer.fragments.DayEditorFragment.SaveListener;

public class DayEditorActivity extends BaseActivity {

    @NonNull
    protected Fragment getInitialFragment() {
        DayEditorFragment dayEditorFragment = new DayEditorFragment();
        dayEditorFragment.setSaveListener(new SaveListener() {
            @Override
            public void onSaveSuccessful() {
                startWeekOverviewActivity();
            }
        });
        return dayEditorFragment;
    }
}
