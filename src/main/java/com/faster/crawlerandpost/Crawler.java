package com.faster.crawlerandpost;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thinhdd on 8/15/2016.
 */
public class Crawler {
    public static void main(String[] args) throws IOException {
        Crawler crawler = new Crawler();
        crawler.getAllBookInfo();
    }

    private void getAllBookInfo() throws IOException {
        String url = "http://www.allitebooks.com/page/";
        List<BookInfo> bookInfos = new ArrayList<BookInfo>();
        for (int i = 1; i < 627; i++) {
            Document doc = Jsoup.connect(url + i).get();
            Elements elements = doc.select("a[rel=bookmark]");
            for (int j = 0; j < elements.size(); j++) {
                if(j%2==0)
                {
                    continue;
                }
                Element element = elements.get(i);
                String linkDetailBook = element.attr("href");
                Document subDoc = Jsoup.connect(linkDetailBook).get();
                bookInfos.add(getInfoBook(subDoc));
                System.out.println("j="+j);
            }
            System.out.println("i="+i);
        }
        System.out.println(bookInfos.size());
    }

    private BookInfo getInfoBook(Document document)
    {
        BookInfo bookInfo = new BookInfo();
        Elements elements = document.select(".single-title");
        bookInfo.setName(elements.get(0).text());
        Elements elementsDetail = document.select("dd");
        bookInfo.setAuthorLink(elementsDetail.get(0).childNode(1).attr("href"));
        bookInfo.setAuthor(elementsDetail.get(0).childNode(1).childNode(0).toString());
        bookInfo.setYear(elementsDetail.get(2).text());
        bookInfo.setPages(Integer.parseInt(elementsDetail.get(3).text()));
        bookInfo.setLanguage(elementsDetail.get(4).text());
        bookInfo.setFileSize(elementsDetail.get(5).text());
        bookInfo.setType(elementsDetail.get(6).text());
        bookInfo.setCategory(elementsDetail.get(7).childNode(1).attr("href"));
        bookInfo.setDes(document.select(".entry-content").get(0).childNode(3).childNode(0).toString());
        bookInfo.setDownloadLink(document.select(".download-links").get(0).childNode(1).attr("href"));
        return bookInfo;
    }

}
