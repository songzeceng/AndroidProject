package applicationmanager.com.example.a123;

import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class BaseWorker implements Runnable{
    protected ConcurrentLinkedQueue<Object> mTasks ;
    ConcurrentLinkedQueue<Object> mResults ;

    public void setTasks(ConcurrentLinkedQueue<Object> mTasks) {
        this.mTasks = mTasks;
    }

    public void setResults(ConcurrentLinkedQueue<Object> results) {
        this.mResults = results;
    }

    public abstract int work();

    @Override
    public void run() {
        while (true) {
            int workResult = work();
            if (workResult > 0) {
                mResults.add(workResult);
            } else if (workResult == Constants.ERROR_QUEUE_EMPTY
                    || workResult == Constants.ERROR_NULL_NODE) {
                return;
            }
        }
    }
}
