package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws SQLException {
        Database.init(parsingFile());
    }

    public static ArrayList<Country> parsingFile() {
        ArrayList<Country> countries = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("src/main/resources/Country.csv"))) {
            reader.readLine();
            while (reader.ready()) {
                String[] data = reader.readLine().split(",");
                countries.add(new Country(data));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return countries;
    }
}