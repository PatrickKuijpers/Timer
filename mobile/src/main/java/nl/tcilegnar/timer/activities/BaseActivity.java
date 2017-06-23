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

    protected class MenuItemId {
        public static final int DAY_EDITOR = 0;
        public static final int WEEK_OVERVIEW = 1;
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
        return showMenu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = true;
        int itemId = item.getItemId();
        switch (itemId) {
            case DayEditorActivity.MenuItemId.DAY_EDITOR:
                startDayEditorActivity();
                break;
            case DayEditorActivity.MenuItemId.WEEK_OVERVIEW:
                startWeekOverviewActivity();
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

    protected void startActivity(Class<? extends BaseActivity> activityClass) {
        Intent intent = new Intent(this, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
