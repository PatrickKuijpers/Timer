package nl.tcilegnar.timer.activities;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import nl.tcilegnar.timer.fragments.YearOverviewFragment;

public class YearOverviewActivity extends BaseActivity {

    @NonNull
    protected Fragment getInitialFragment() {
        return new YearOverviewFragment();
    }
}