package nl.tcilegnar.timer.utils.database;

import com.orm.SugarRecord;

import android.os.AsyncTask;

public class DatabaseUtil extends AsyncTask<SugarRecord, Void, Boolean> {
    private AsyncResponse listener = null;

    public DatabaseUtil(AsyncResponse listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(SugarRecord[] params) {
        params[0].save();
        return true;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        listener.processFinish(success);
    }

    public interface AsyncResponse {
        void processFinish(Boolean success);
    }
}
