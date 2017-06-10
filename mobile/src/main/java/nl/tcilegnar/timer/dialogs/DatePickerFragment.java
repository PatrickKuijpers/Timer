package nl.tcilegnar.timer.dialogs;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;

import java.util.Calendar;

import nl.tcilegnar.timer.utils.TimerCalendar;

public class DatePickerFragment extends DialogFragment {
    private OnDateSetListener onDateSetListener;
    private Calendar dateToShow = TimerCalendar.getCurrentDate();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year = dateToShow.get(Calendar.YEAR);
        int month = dateToShow.get(Calendar.MONTH);
        int dayOfMonth = dateToShow.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), onDateSetListener, year, month, dayOfMonth);
    }

    public void setOnDateSetListener(OnDateSetListener onDateSetListener) {
        this.onDateSetListener = onDateSetListener;
    }

    public void show(FragmentManager fragmentManager, String tag, Calendar dateToShow) {
        super.show(fragmentManager, tag);
        this.dateToShow = dateToShow;
    }
}
