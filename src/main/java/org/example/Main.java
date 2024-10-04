package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import java.sql.*;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/Library";
        String user = "postgres";
        String password = "123";

        // Попытка подключения к базе данных
        try {
            // Явная загрузка драйвера (можно опустить в новых версиях)

            // Подключение к базе данных
            Connection conn = DriverManager.getConnection(url, user, password);
            //System.out.println("Connected to the PostgreSQL server successfully.");

            Scanner in = new Scanner(System.in);
            boolean flag = true;
            while(flag){
                System.out.println("Введите число - 1 для вывода книг");
                System.out.println("Введите число - 2 для добавления книги");
                System.out.println("Введите число - 3 для редактирования данных о книге");
                System.out.println("Введите число - 4 для удаления книги");
                System.out.println("Введите число - 0 для выхода из программы");
                int input = in.nextInt();
                in.nextLine();
                switch (input){
                    case(1):{
                        Output(conn);
                        break;
                    }
                    case(2):{
                        addBook(conn, in);
                        break;
                    }
                    case(3):{
                        changeBook(conn, in);
                        break;
                    }
                    case(4):{
                        deleteBook(conn, in);
                        break;
                    }
                    case(0):{
                        flag = false;
                        break;
                    }
                }
            }
            // Закрытие подключения
            conn.close();

        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    public static void Output(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("Select * from Book");
        while(resultSet.next()){
            int id = resultSet.getInt("id");
            String title = resultSet.getString("title");
            String author = resultSet.getString("author");
            int year = resultSet.getInt("year");
            System.out.printf("%d. %s автор - %s год: %d\n", id, title, author, year);
        }
    }

    public static void addBook(Connection conn, Scanner in){
        try{
            System.out.println("Введите название книги");
            String title = in.nextLine();
            System.out.println("Введите автора книги");
            String author = in.nextLine();
            System.out.println("Введите дату публикации");
            int year = in.nextInt();
            in.nextLine();

            String querry = "Insert into Book(title, author, year) Values (?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(querry);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author);
            preparedStatement.setInt(3, year);
            preparedStatement.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public  static void deleteBook(Connection conn, Scanner in){
        System.out.println("Введите id книги, которую хотите удалить");
        int id = in.nextInt();
        in.nextLine();
        try{
            String querry = "Delete from Book where id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(querry);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Книга успешно удалена");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void changeBook(Connection conn, Scanner in){
        System.out.println("Введите id книги, которую хотите редактировать");
        int id = in.nextInt();
        in.nextLine();
        System.out.println("Введите название книги");
        String title = in.nextLine();
        System.out.println("Введите автора книги");
        String author = in.nextLine();
        System.out.println("Введите год публикации книги");
        int year = in.nextInt();
        in.nextLine();
        try{
            String querry = "Update Book Set title = ?, author = ?, year = ? where id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(querry);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author);
            preparedStatement.setInt(3, year);
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}