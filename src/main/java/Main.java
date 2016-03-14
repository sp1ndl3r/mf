import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* Viktor Spindler, lösningsförslag till modularfinance */

public class Main {

  public static void main(String[] args){
    try {
      String gsonString = downloadURL("http://www.modularfinance.se/api/challenges/buy-sell.json"); // Funktion för att läsa in all data ifrån URL:EN
      ApiCall a = new Gson().fromJson(gsonString, ApiCall.class); // Använder Gson-biblioteket för att parsa
      Collections.reverse(a.data); // Ändrar ordning på den inlästa datan
      List<DailyStatus> resultOfoN2 =  ordoN2(a.data);  // Funktion som löser det med tidskomplexiteten O(N^2), returnerar en lista med köp(pos[0]) och sälj(pos[1])
      List<DailyStatus> resultOfoN =  ordoN(a.data);    // Funktion som löser det med tidskomplexiteten O(N), returnerar en lista med köp(pos[0]) och sälj(pos[1])


      System.out.println("O(N^2): ");
      System.out.println("Buy day: " + resultOfoN2.get(0).quote_date);
      System.out.println("Lowest Price: " + resultOfoN2.get(0).low);
      System.out.println("Sell day: " + resultOfoN2.get(1).quote_date);
      System.out.println("Highest Price: " + resultOfoN2.get(1).high + "\n");

      System.out.println("-------------------------------");

      System.out.println("\nO(N): ");
      System.out.println("Buy day: " + resultOfoN.get(0).quote_date);
      System.out.println("Lowest Price: " + resultOfoN.get(0).low);
      System.out.println("Sell day: " + resultOfoN.get(1).quote_date);
      System.out.println("Highest Price: " + resultOfoN.get(1).high + "\n");


    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static List<DailyStatus> ordoN2(List<DailyStatus> listStatus){
    List<DailyStatus> result = new ArrayList<DailyStatus>();
    DailyStatus sellDay = null;
    DailyStatus buyDay;
    double maxProfit = 0;


    buyDay = listStatus.get(0);

    for (int i = 1; i < listStatus.size(); i++) {
      for (int j = i; j < listStatus.size(); j++) {
        double compare = listStatus.get(j).high - listStatus.get(i).low;
        if(maxProfit < compare) {
          maxProfit = compare;
          buyDay = listStatus.get(i);
          sellDay = listStatus.get(j);
        }
      }
    }

    result.add(buyDay);  // Köp position[0]
    result.add(sellDay); // Sälj position[1]
    return result;
  }

  public static List<DailyStatus> ordoN(List<DailyStatus> listStatus){
    List<DailyStatus> result = new ArrayList<DailyStatus>();
    DailyStatus sellDay = null;
    DailyStatus buyDay = listStatus.get(0);
    DailyStatus cheapestDay = buyDay;
    double maxProfit = 0;

    for (int i = 1; i < listStatus.size(); i++){
      if(cheapestDay.low > listStatus.get(i).low) {
        cheapestDay = listStatus.get(i);
      }
      double compare = listStatus.get(i).high - cheapestDay.low;
      if(maxProfit < compare){
        maxProfit = compare;
        buyDay = cheapestDay;
        sellDay = listStatus.get(i);
      }
    }

    result.add(buyDay);  // Köp position[0]
    result.add(sellDay);  // Sälj position[1]
    return result;
  }

  public static String downloadURL(String url) throws Exception {
    BufferedReader in = null;
    try{
      URLConnection connection = (new URL(url)).openConnection();
      connection.addRequestProperty("User-Agent", "Mozilla/4.76");
      in = new BufferedReader(new InputStreamReader(
              connection.getInputStream()));
      StringBuilder sb = new StringBuilder();
      String input;
      while ((input = in.readLine()) != null) {
        sb.append(input);
      }
      return sb.toString();
    }
    finally {
      if (in != null)
        in.close();
    }
  }
}
