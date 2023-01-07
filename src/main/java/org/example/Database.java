package org.example;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {

    private static Connection connection;
    private static Statement statement;
    private static Map<String, Double> internetUsers;

    public static void init(ArrayList<Country> countries) throws SQLException {

        connect();

        createTableCountries();

        for (Country country : countries) {
            putDataIntoCountries(country);
        }

        getInternetUsersBySubregions();
        EventQueue.invokeLater(() -> {
            Graphics g = new Graphics(internetUsers);
            g.setVisible(true);
        });

        System.out.println("Страна с наименьшим кол-вом интернет пользователей в Восточной Европе:\n" + smallestNumberOfInternetUsersInEasternEurope() + "\n" + "\n");
        System.out.println("Страны, в которых процент интернет пользователей находится в промежутке от 75% до 85%:\n" + internetUsersPercentInRange75And85());

        disconnect();
    }

    private static void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/country.db");
        statement = connection.createStatement();
    }

    private static void disconnect() throws SQLException {
        statement.close();
        connection.close();
    }

    private static void createTableCountries() throws SQLException {
        statement.execute("DROP TABLE IF EXISTS Countries");
        statement.execute("CREATE TABLE Countries (" +
                "country VARCHAR, " +
                "subregion VARCHAR, " +
                "region VARCHAR, " +
                "internetUsers INTEGER, " +
                "population INTEGER);"
        );
    }

    private static void putDataIntoCountries(Country country) throws SQLException {
        String query = String.format(
                "INSERT INTO Countries VALUES ('%s', '%s', '%s', '%s', '%s');",
                country.getCountry(),
                country.getSubregion(),
                country.getRegion(),
                country.getInternetUsers(),
                country.getPopulation()
        );

        statement.executeUpdate(query);
    }

    private static void getInternetUsersBySubregions() throws SQLException {
        internetUsers = new HashMap<>();
        String sql =
                "SELECT subregion, SUM(internetUsers), SUM(population) " +
                        "FROM Countries " +
                        "GROUP BY subregion;";
        ResultSet res = statement.executeQuery(sql);
        while(res.next()) {
            internetUsers.put(res.getString("subregion"),
                    (res.getDouble("SUM(internetUsers)") / res.getDouble("SUM(population)") * 100));
        }
    }

    private static String smallestNumberOfInternetUsersInEasternEurope() throws SQLException {
        return statement.executeQuery(
                "SELECT country, min(internetUsers) " +
                        "FROM Countries " +
                        "WHERE subregion ='Eastern Europe' ;"
        ).getString("country");
    }

    private static ArrayList<String> internetUsersPercentInRange75And85() throws SQLException {
        ArrayList<String> arrayList = new ArrayList<>();
        String sql =
                "SELECT country " +
                        "FROM Countries " +
                        "WHERE (internetUsers * 1.0) / (population * 1.0) * 100 BETWEEN 75 and 85;";
        ResultSet res = statement.executeQuery(sql);
        while (res.next()) {
            arrayList.add(res.getString("country"));
        }

        return arrayList;
    }

}