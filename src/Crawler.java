import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qx on 16/11/8.
 */
public class Crawler implements Runnable {
    String mAddress;
    URL mURL;
    List<Record> mSort;

    /**Constructor
     *
     * @param address 带有搜索关键字的豆瓣网址
     * @param sort 存放评论数大于2000的书籍信息(Vector)
     */
    public Crawler(String address,List<Record> sort) {
        mAddress = address;
        mSort=sort;
    }

    @Override
    public void run() {
        try {
            //豆瓣只提供了2000条搜索记录
            for (int i = 0; i < 2000; i += 20) {
                //cat的值表示了查询的类型,1001是书籍
                mURL = new URL(mAddress + i + "&cat=1001");
                parser(mURL);
                System.out.println("解析了"+i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**爬网址,先解析Json,再解析HTML,分别用了Gson和Jsoup
     *
     * @param url 要爬的网址
     * @throws IOException
     */
    private void parser(URL url) throws IOException {
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader(url.openStream()));
        JsonObject rootobj = root.getAsJsonObject();
        JsonArray list = rootobj.getAsJsonArray("items");
        float rank = 0;
        int number = 0;
        for (int i = 0; i < list.size(); i++) {
            JsonElement element = list.get(i);
            String html = element.getAsString();
            Document document = Jsoup.parse(html);
            Elements title = document.select("div.title");//获取书籍信息
            Elements rate = document.select(".rating_nums");//获取书籍评分
            Elements numbers=document.select("span:contains(评价)");//获取书籍评论数
            if (rate.text()!=null&&rate.text().length()!=0)
            rank = Float.parseFloat(rate.text());
            number = getNumbers(numbers.text());
            Record tempRecord=new Record(title.text(), rank, number);
            //将评论大于2000的书籍存放起来
            if (number>2000){
                mSort.add(tempRecord);
            }
        }
    }

    /**
     * 获取字符串中的数字
     * @param content 含有数字的字符串
     * @return
     */
    private int getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return Integer.parseInt(matcher.group(0));
        }
        return 0;
    }
}
