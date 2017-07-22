package nl.tcilegnar.timer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import nl.tcilegnar.timer.R;

public abstract class BaseActivity extends AppCompatActivity {
    protected final String LOGTAG = getClass().getSimpleName();

    private class MenuItemId {
        public static final int DAY_EDITOR = 0;
        public static final int WEEK_OVERVIEW = 1;
        public static final int YEAR_OVERVIEW = 2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);
        initToolbar();

        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.activity_content, getInitialFragment()).commit();
        }
    }

    @NonNull
    protected abstract Fragment getInitialFragment();

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override // TODO: maak er een 3x tab swipe view van
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean showMenu = super.onCreateOptionsMenu(menu);
        menu.add(0, MenuItemId.DAY_EDITOR, 0, R.string.menu_item_day_editor);
        menu.add(0, MenuItemId.WEEK_OVERVIEW, 1, R.string.menu_item_week_overview);
        menu.add(0, MenuItemId.YEAR_OVERVIEW, 1, R.string.menu_item_year_overview);
        return showMenu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = true;
        int itemId = item.getItemId();
        switch (itemId) {
            case MenuItemId.DAY_EDITOR:
                startDayEditorActivity();
                break;
            case MenuItemId.WEEK_OVERVIEW:
                startWeekOverviewActivity();
                break;
            case MenuItemId.YEAR_OVERVIEW:
                startYearOverviewActivity();
                break;
            default:
                handled = false;
                break;
        }
        return handled;
    }

    protected void startDayEditorActivity() {
        startActivity(DayEditorActivity.class);
    }

    protected void startWeekOverviewActivity() {
        startActivity(WeekOverviewActivity.class);
    }

    protected void startYearOverviewActivity() {
        startActivity(YearOverviewActivity.class);
    }

    protected void startActivity(Class<? extends BaseActivity> activityClass) {
        Intent intent = new Intent(this, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
