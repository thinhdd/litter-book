package com.faster.crawlerandpost;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Component
public class BootstrapOnAppReady implements ApplicationListener<ApplicationReadyEvent> {

    File file = new File("data.crawler");
    Gson gson = new Gson();
    PrintWriter printWriter=null;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        try {
            getAllBookInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private List<BookInfo> getAllBookInfo() throws IOException {
        printWriter = new PrintWriter(file);
        String url = "http://www.allitebooks.com/page/";
        List<BookInfo> bookInfos = new ArrayList<BookInfo>();
        printWriter.write("[");
        for (int i = 1; i < 267; i++) {
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
                String s = gson.toJson(getInfoBook(subDoc));
                printWriter.write(","+s);
            }
            System.out.println("i="+i);
        }
        printWriter.write("]");
        printWriter.close();
        return bookInfos;
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