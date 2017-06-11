package nl.tcilegnar.timer.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.text.format.DateFormat;

import java.util.Calendar;

import nl.tcilegnar.timer.utils.TimerCalendar;

public class TimePickerFragment extends DialogFragment {
    private OnTimeSetListener onTimeSetListener;
    private Calendar timeToShow = TimerCalendar.getCurrent();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int hour = timeToShow.get(Calendar.HOUR_OF_DAY);
        int minute = timeToShow.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), onTimeSetListener, hour, minute, DateFormat.is24HourFormat
                (getActivity()));
    }

    public void setOnTimeSetListener(OnTimeSetListener onTimeSetListener) {
        this.onTimeSetListener = onTimeSetListener;
    }

    public void show(FragmentManager fragmentManager, String tag, Calendar timeToShow) {
        super.show(fragmentManager, tag);
        this.timeToShow = timeToShow;
    }
}
