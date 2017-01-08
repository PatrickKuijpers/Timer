package nl.tcilegnar.timer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.adapters.DayEditorAdapter;

public class DayEditorFragment extends Fragment {

    private ListView abilitiesGrid;

    public DayEditorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day_editor, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        abilitiesGrid = (ListView) view.findViewById(R.id.day_editor_list);
        abilitiesGrid.setAdapter(new DayEditorAdapter(getActivity()));
    }
}
