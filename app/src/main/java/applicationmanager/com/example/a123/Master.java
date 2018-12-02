package applicationmanager.com.example.a123;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Master {
    // 根据cpu核数，创建worker线程
    private LinkedList<Thread> workers = new LinkedList<>();

    // 任务列表
    private ConcurrentLinkedQueue<Object> tasks = new ConcurrentLinkedQueue<>();

    // 结果队列
    private ConcurrentLinkedQueue<Object> results = new ConcurrentLinkedQueue<>();

    public Master(int coresNumber) {
        if (coresNumber <= 0) {
            return;
        }

        for (int i = 0; i < coresNumber; i++) {
            CalculateWorker worker = new CalculateWorker();
            workers.add(new Thread(worker));
            worker.setTasks(tasks);
            worker.setResults(results);
        }
    }

    public void beginWork() {
        for (int i = 0; i < workers.size(); i++) {
            workers.get(i).start();
        }
    }

    public ConcurrentLinkedQueue<Object> getResults() {
        return results;
    }

    public void addTask(Object task) {
        if (task != null) {
            tasks.add(task);
        }
    }

    public boolean isComplete() {
        if (workers == null || workers.size() <= 0) {
            return true;
        }

        boolean isOver = true;
        for (int i = 0; i < workers.size(); i++) {
            Thread worker = workers.get(i);
            if (worker != null && worker.getState() != Thread.State.TERMINATED) {
                isOver = false;
            }
        }

        return isOver;
    }
}
