package com.example.songzeceng.firstjd;

import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        Scanner input = new Scanner(System.in);
        try {
            Socket server = new Socket("localhost", 12345);
            BufferedInputStream inputStream = new BufferedInputStream(server.getInputStream());
            BufferedOutputStream outputStream = new BufferedOutputStream(server.getOutputStream());
            boolean isOver = false;
            while (!isOver) { // 只要不结束，这个线程一直运行
                outputStream = new BufferedOutputStream(server.getOutputStream());

                String toServer = input.next();

                outputStream.write(toServer.getBytes());
                outputStream.flush();

                while (inputStream.available() <= 0); // 过滤空消息

                byte[] bytes = new byte[1024];
                int len;
                StringBuffer stringBuffer = new StringBuffer();
                while (inputStream.available() > 0 && (len = inputStream.read(bytes)) != -1) {
                    stringBuffer.append(new String(bytes, 0, len));
                }
                String fromServer = stringBuffer.toString();
                System.out.println(fromServer);
                isOver = fromServer.equals("over");
            }
            System.out.println("over..");
            inputStream.close();
            outputStream.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}