package applicationmanager.com.example.a123.worker;

import applicationmanager.com.example.a123.util.Constants;

public class CalculateWorker extends BaseWorker {
    @Override
    public int work() {
        if (mTasks == null || mTasks.isEmpty()) {
            return Constants.ERROR_QUEUE_EMPTY;
        }

        Object work = mTasks.poll();
        if (work == null) {
            return Constants.ERROR_NULL_NODE;
        }

        if (work instanceof Integer) {
            int number = (Integer)work;
            return number * number * number * number;
        }

        return Constants.ERROR_WRONG_TYPE;
    }
}
