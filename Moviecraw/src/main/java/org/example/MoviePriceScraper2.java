package org.example;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MoviePriceScraper2 {
    public static void main(String[] args) {
      String url = "https://www.money101.com.tw/blog/%E5%85%A8%E5%8F%B0%E7%A7%80%E6%B3%B0%E5%BD%B1%E5%9F%8E%E7%A5%A8%E5%83%B9-%E4%BF%A1%E7%94%A8%E5%8D%A1%E6%8E%A8%E8%96%A6-%E6%9C%83%E5%93%A1%E5%88%B6%E5%BA%A6";

      try {
        // 設置User-Agent以模擬瀏覽器
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
                .timeout(10000) // 設置連接超時
                .get();

        // 根據具體的HTML結構來選擇需要的元素
        Elements priceTables = doc.select("table"); // 假設票價資訊在<table>元素內
        for (Element table : priceTables) {
          // 抓取表格內的所有行
          Elements rows = table.select("tr");
          for (Element row : rows) {
            // 抓取每行內的所有單元格
            Elements cells = row.select("td");
            for (Element cell : cells) {
              // 打印每個單元格的文本內容
              System.out.print(cell.text() + "\t");
            }
            System.out.println();
          }
          System.out.println("===================================");
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

