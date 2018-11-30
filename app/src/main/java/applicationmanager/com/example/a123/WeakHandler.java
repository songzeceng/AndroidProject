package applicationmanager.com.example.a123;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public class WeakHandler extends Handler {
    private WeakReference<IHandler> handlerRef;

    public WeakHandler(IHandler handler) {
        if (handler == null) {
            return;
        }
        handlerRef = new WeakReference(handler);
    }

    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (handlerRef == null || handlerRef.get() == null) {
            return;
        }
        IHandler handler = handlerRef.get();
        handler.handleMessage(msg);
    }
}
