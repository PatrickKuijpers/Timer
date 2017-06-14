package nl.tcilegnar.timer.utils.database;

import com.orm.SugarRecord;

import android.os.AsyncTask;

import nl.tcilegnar.timer.models.TimerError;

public class DatabaseSaveUtil extends AsyncTask<Object, Void, Long> {
    private static final long NOT_SAVED = 0L;

    private final AsyncResponse listener;
    private Long id = NOT_SAVED;
    private TimerError error = new TimerError();

    public DatabaseSaveUtil(AsyncResponse listener) {
        this.listener = listener;
    }

    @Override
    protected Long doInBackground(Object[] params) {
        Object param = params[0];
        try {
            boolean isSugarEntity = SugarRecord.isSugarEntity(param.getClass());
            if (isSugarEntity) {
                SugarRecord sugarRecord = (SugarRecord) param;
                sugarRecord.save();
                id = sugarRecord.getId();
            } else {
                error = new TimerError("No sugar record");
            }
        } catch (Exception e) {
            e.printStackTrace();
            error = new TimerError(e);
        }
        return id;
    }

    @Override
    protected void onPostExecute(Long savedId) {
        if (isSuccess()) {
            listener.savedSuccesfully(savedId);
        } else {
            listener.saveFailed(error);
        }
    }

    private boolean isSuccess() {
        return id != NOT_SAVED && id > 0;
    }

    public interface AsyncResponse {
        void savedSuccesfully(Long savedId);

        void saveFailed(TimerError error);
    }
}
