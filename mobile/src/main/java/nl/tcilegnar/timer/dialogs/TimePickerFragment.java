package nl.tcilegnar.timer.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import nl.tcilegnar.timer.utils.TimerCalendar;

public class TimePickerFragment extends DialogFragment {
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;
    private Calendar timeToShow = TimerCalendar.getCurrentDay();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int hour = timeToShow.get(Calendar.HOUR_OF_DAY);
        int minute = timeToShow.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), onTimeSetListener, hour, minute, DateFormat.is24HourFormat
                (getActivity()));
    }

    public void setOnTimeSetListener(TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        this.onTimeSetListener = onTimeSetListener;
    }

    public void show(FragmentManager fragmentManager, String tag, Calendar timeToShow) {
        super.show(fragmentManager, tag);
        this.timeToShow = timeToShow;
    }
}
