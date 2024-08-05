package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateUsers {

  public static void main(String[] args) {
    String[] names = {"John", "Jane", "Tom", "Anna", "Chris", "Katie", "Mike", "Sara", "James", "Laura"};
    String[] domains = {"example.com", "test.com", "sample.com"};
    Random random = new Random();

    try (FileWriter writer = new FileWriter("users.csv")) {
      writer.append("id,email,phone,username,password,name,contact_info,preferences\n");

      for (int i = 1; i <= 50; i++) {
        String name = names[random.nextInt(names.length)];
        String email = name.toLowerCase() + i + "@" + domains[random.nextInt(domains.length)];
        String phone = "123-456-78" + String.format("%02d", i);
        String username = name.toLowerCase() + i;
        String password = "password" + i;
        String contactInfo = "Contact" + i;
        String preferences = "Preferences" + i;

        writer.append(i + "," + email + "," + phone + "," + username + "," + password + "," + name + "," + contactInfo + "," + preferences + "\n");
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("50 users generated and saved to users.csv");
  }
}
