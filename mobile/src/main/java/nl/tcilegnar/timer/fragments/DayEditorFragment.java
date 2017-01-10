package nl.tcilegnar.timer.fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.adapters.DayEditorAdapter;
import nl.tcilegnar.timer.dialogs.TimePickerFragment;

import static nl.tcilegnar.timer.views.DayEditorItemView.TimePickerDialogListener;

public class DayEditorFragment extends Fragment implements TimePickerDialogListener {

    private ListView dayEditorList;
    private DayEditorAdapter dayEditorAdapter;

    public DayEditorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day_editor, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        dayEditorList = (ListView) view.findViewById(R.id.day_editor_list);
        dayEditorAdapter = new DayEditorAdapter(getActivity(), this);
        dayEditorList.setAdapter(dayEditorAdapter);
        dayEditorAdapter.startNewDay();

        initFab(view);
    }

    @Override
    public void showTimePickerDialog(TimePickerDialog.OnTimeSetListener onTimeSetListener, String tag) {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setOnTimeSetListener(onTimeSetListener);
        timePickerFragment.show(getActivity().getFragmentManager(), tag);
    }

    private void initFab(View view) {
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetCurrentDay();
            }
        });
    }

    private void resetCurrentDay() {
        dayEditorAdapter.reset();
    }
}
