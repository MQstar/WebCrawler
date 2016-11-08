import java.io.*;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by qx on 16/11/8.
 */
public class Test {
    public static void main(String[] args) {
        OutputStream mOutputStream = null;
        PrintWriter mWriter = null;
        //生成文件地址
        File mFile = new File("/Users/qx/Desktop/BookList");
        ExecutorService threadPool = Executors.newCachedThreadPool();
        List<Record> records = new Vector<>();
        //互联网
        threadPool.execute(new Crawler("https://www.douban.com/j/search?q=%E4%BA%92%E8%81%94%E7%BD%91&start=", records));
        //算法
        threadPool.execute(new Crawler("https://www.douban.com/j/search?q=%E7%AE%97%E6%B3%95&start=", records));
        //编程
        threadPool.execute(new Crawler("https://www.douban.com/j/search?q=%E7%BC%96%E7%A8%8B&start=", records));
        //主线程等待
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
            //根据评分排序
            records.sort((o1, o2) -> (int) ((o2.getRank() - o1.getRank()) * 10));
            //写入文件
            mOutputStream = new FileOutputStream(mFile);
            mWriter = new PrintWriter(new OutputStreamWriter(mOutputStream));
            for (Record record : records) {
                mWriter.write(record.getTitle() + "\n");
                mWriter.flush();
            }
        } catch (InterruptedException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (mOutputStream != null)
                    mOutputStream.close();
                if (mWriter != null)
                    mWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}