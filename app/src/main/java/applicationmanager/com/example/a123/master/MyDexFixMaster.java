package applicationmanager.com.example.a123.master;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.BaseDexClassLoader;

public class MyDexFixMaster {
    private WeakReference<Context> contextRef;

    public MyDexFixMaster(Context context) {
        if (context != null) {
            contextRef = new WeakReference<>(context);
        }
    }

    public void hotFix(String patchPath) throws Exception {
        if (patchPath == null || patchPath.isEmpty()) {
            throw new Exception("补丁路径为空");
        }
        Context context = contextRef.get();
        if (context == null) {
             throw new Exception("MyDexFixManager没有被初始化");
        }
        BaseDexClassLoader contextClassLoader = (BaseDexClassLoader) context.getClassLoader();

        // 获取原dexElements
        Object dexElements = getDexElementsFromClassLoader(contextClassLoader);

        // 复制补丁文件到应用目录下
        String destDirPath = contextRef.get().getDir("myPatch", Context.MODE_PRIVATE).getAbsolutePath();
        copyFile(patchPath, destDirPath);

        // 获取新的BaseDexClassLoader
        File optimizedDir = new File(destDirPath, "optimization");
        if  (!optimizedDir.exists()) {
            optimizedDir.mkdirs();
        }
        BaseDexClassLoader baseDexClassLoader = new BaseDexClassLoader(patchPath, optimizedDir,
                null, context.getClassLoader());
        Object newDexElements = combineDexElements(baseDexClassLoader, dexElements);

        // 更新BaseClassLoader的dexElements
        setDexElementsForClassLoader(contextClassLoader, newDexElements);
    }

    private Object combineDexElements(BaseDexClassLoader classLoader, Object dexElements) throws Exception {
        Object fixedDexElements = getDexElementsFromClassLoader(classLoader);

        int oldLength = Array.getLength(dexElements);
        int newLength = Array.getLength(fixedDexElements);
        int totalLength = oldLength + newLength;
        Object newDexElements = Array.newInstance(dexElements.getClass().getComponentType(), totalLength);

        for (int i = 0; i < totalLength; i++) {
            if (i < newLength) {
                Array.set(newDexElements, i, Array.get(newDexElements, i));
            } else {
                Array.set(newDexElements, i, Array.get(dexElements, i - newLength));
            }
        }
        return newDexElements;
    }

    private void copyFile(String patchPath, String destDirPath) throws Exception {
        File patchFile = new File(patchPath);
        if (!patchFile.exists()) {
             throw new FileNotFoundException(patchFile.getAbsolutePath());
        }

        File destFile = new File(destDirPath, patchFile.getName());
        if (destFile.exists()) {
            throw new Exception("补丁文件已经存在");
        }
        destFile.createNewFile();

        BufferedOutputStream bufferedOutputStream= new BufferedOutputStream(new FileOutputStream(destFile));
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(patchFile));

        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = bufferedInputStream.read(bytes)) != -1) {
            bufferedOutputStream.write(bytes, 0, len);
            bufferedOutputStream.flush();
        }

        bufferedOutputStream.close();
        bufferedInputStream.close();
    }

    private Object getDexElementsFromClassLoader(ClassLoader classLoader) throws Exception{
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        if (pathListField == null) {
             throw new Exception("BaseDexClassLoader没有pathList属性");
        }
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);

        if (pathList == null) {
            throw new Exception("BaseDexClassLoader没有pathList对象");
        }

        Field dexElementField = pathList.getClass().getDeclaredField("dexElements");
        if (dexElementField == null) {
            throw new Exception("pathList没有dexElementField属性");
        }
        dexElementField.setAccessible(true);
        Object dexElements = dexElementField.get(pathList);
        if (dexElements == null) {
            throw new Exception("pathList没有dexElementField对象");
        }
        return dexElements;
    }

    private void setDexElementsForClassLoader(ClassLoader classLoader, Object newDexElements) throws Exception{
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        if (pathListField == null) {
            throw new Exception("BaseDexClassLoader没有pathList属性");
        }
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);

        if (pathList == null) {
            throw new Exception("BaseDexClassLoader没有pathList对象");
        }

        Field dexElementField = pathList.getClass().getDeclaredField("dexElements");
        if (dexElementField == null) {
            throw new Exception("pathList没有dexElementField属性");
        }
        dexElementField.setAccessible(true);
        dexElementField.set(classLoader, newDexElements);
    }
}
