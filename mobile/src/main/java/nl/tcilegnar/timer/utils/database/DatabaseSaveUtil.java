package nl.tcilegnar.timer.utils.database;

import com.orm.SugarRecord;

import android.os.AsyncTask;
import android.widget.Toast;

import nl.tcilegnar.timer.App;

import static android.widget.Toast.LENGTH_SHORT;

public class DatabaseSaveUtil extends AsyncTask<SugarRecord, Void, Long> {
    private AsyncResponse listener = null;
    private Long id;

    public DatabaseSaveUtil(AsyncResponse listener) {
        this.listener = listener;
    }

    @Override
    protected Long doInBackground(SugarRecord[] params) {
        SugarRecord param = params[0];
        try {
            param.save();
            id = param.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    protected void onPostExecute(Long savedId) {
        listener.processFinish(savedId, isSuccess());

        if (!isSuccess()) {
            Toast.makeText(App.getContext(), "Could not save values, invalid input!", LENGTH_SHORT).show();
        } else {
            Toast.makeText(App.getContext(), "Saved success (id = " + id + ")!", LENGTH_SHORT).show();
        }
    }

    private boolean isSuccess() {
        return id > 0;
    }

    public interface AsyncResponse {
        void processFinish(Long savedId, boolean success);
    }
}
