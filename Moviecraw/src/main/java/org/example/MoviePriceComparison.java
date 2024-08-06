package org.example;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoviePriceComparison {

  public static void main(String[] args) {
    try {
      List<MoviePrice> pricesFromSite1 = fetchPricesFromSite1();
      List<MoviePrice> pricesFromSite2 = fetchPricesFromSite2();

      // 比較票價並打印結果
      comparePrices(pricesFromSite1, pricesFromSite2);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static List<MoviePrice> fetchPricesFromSite1() throws IOException {
    String url = "https://www.money101.com.tw/blog/%E5%85%A8%E5%8F%B0%E7%A7%80%E6%B3%B0%E5%BD%B1%E5%9F%8E%E7%A5%A8%E5%83%B9-%E4%BF%A1%E7%94%A8%E5%8D%A1%E6%8E%A8%E8%96%A6-%E6%9C%83%E5%93%A1%E5%88%B6%E5%BA%A6";
    List<MoviePrice> prices = new ArrayList<>();

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
        if (cells.size() > 1) {
          String movieName = cells.get(0).text();
          double price = Double.parseDouble(cells.get(1).text().replaceAll("[^\\d.]", ""));
          prices.add(new MoviePrice(movieName, price));
        }
      }
    }
    return prices;
  }

  private static List<MoviePrice> fetchPricesFromSite2() throws IOException {
    String url = "https://roo.cash/blog/movie-vieshow-cinemas-ticket-price/";
    List<MoviePrice> prices = new ArrayList<>();

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
        if (cells.size() > 1) {
          String movieName = cells.get(0).text();
          double price = Double.parseDouble(cells.get(1).text().replaceAll("[^\\d.]", ""));
          prices.add(new MoviePrice(movieName, price));
        }
      }
    }
    return prices;
  }

  private static void comparePrices(List<MoviePrice> pricesFromSite1, List<MoviePrice> pricesFromSite2) {
    Map<String, Double> site1Prices = new HashMap<>();
    for (MoviePrice price : pricesFromSite1) {
      site1Prices.put(price.getMovieName(), price.getPrice());
    }

    System.out.println("比較結果：");
    for (MoviePrice price : pricesFromSite2) {
      String movieName = price.getMovieName();
      if (site1Prices.containsKey(movieName)) {
        double price1 = site1Prices.get(movieName);
        double price2 = price.getPrice();
        System.out.println(movieName + " - 網站1: " + price1 + " 元, 網站2: " + price2 + " 元");
        if (price1 < price2) {
          System.out.println("網站1便宜");
        } else if (price1 > price2) {
          System.out.println("網站2便宜");
        } else {
          System.out.println("價格相同");
        }
      } else {
        System.out.println(movieName + " - 只有在網站2找到價格：" + price.getPrice() + " 元");
      }
    }
  }

  // 自定義的類來儲存電影票價信息
  static class MoviePrice {
    private String movieName;
    private double price;

    public MoviePrice(String movieName, double price) {
      this.movieName = movieName;
      this.price = price;
    }

    public String getMovieName() {
      return movieName;
    }

    public double getPrice() {
      return price;
    }
  }
}

