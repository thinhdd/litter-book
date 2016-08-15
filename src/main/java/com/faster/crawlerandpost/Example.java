package com.faster.crawlerandpost;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@EnableAutoConfiguration
@ComponentScan
public class Example {

    Gson gson = new Gson();
    PrintWriter printWriter=null;

    @RequestMapping("/")
    String home() throws IOException {
        File file = new File("data.crawler");
        BufferedReader br = new BufferedReader(new FileReader("data.crawler"));
        return System.getProperty(br.readLine());
    }

    @RequestMapping("/reset")
    String reset() throws IOException {
        File file = new File("data.crawler");
        file.delete();
        BufferedReader br = new BufferedReader(new FileReader("data.crawler"));
        return System.getProperty(br.readLine());
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Example.class, args);
    }

}