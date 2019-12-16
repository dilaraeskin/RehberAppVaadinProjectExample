package com.uniyaz.db;

import com.uniyaz.domain.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBTransaction {

    final String JDBC_CONNECTION_STR = "jdbc:mysql://localhost:3306/rehber";
    final String USERNAME = "admin";
    final String PASSWORD = "admin";

    public void addPerson(Person person) {

        String sql = "insert into person (name, surname, phone) " +
                "values (?, ?, ?) ";

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (
                Connection conn = DriverManager.getConnection(JDBC_CONNECTION_STR, USERNAME, PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, person.getName());
            preparedStatement.setString(2, person.getSurname());
            preparedStatement.setString(3, person.getPhone());

            int affectedRows = preparedStatement.executeUpdate();
            System.out.println(affectedRows + " satır eklendi.");
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Person> personList() {

        List<Person> personList = new ArrayList<>();
        String sql = "select * from person";

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (
                Connection conn = DriverManager.getConnection(JDBC_CONNECTION_STR, USERNAME, PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {


            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                String phone = resultSet.getString("phone");

                Person person = new Person();
                person.setId(id);
                person.setName(name);
                person.setSurname(surname);
                person.setPhone(phone);
                personList.add(person);
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return personList;
    }

    public void deletePerson(Person person){

        String sql = "delete from person where id=? ";

        try (Connection conn = DriverManager.getConnection(JDBC_CONNECTION_STR, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, person.getId());

            int affectedRows = preparedStatement.executeUpdate();
            System.out.println(affectedRows + " satır silindi");
        } catch (SQLException e) {
            System.err.format("SQL State: %s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updatePerson(Person person) {

        String sql = "UPDATE person SET surname=?,phone=? WHERE name=?";

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (
                Connection conn = DriverManager.getConnection(JDBC_CONNECTION_STR, USERNAME, PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(3, person.getName());
            preparedStatement.setString(1, person.getSurname());
            preparedStatement.setString(2, person.getPhone());

            int affectedRows = preparedStatement.executeUpdate();
            System.out.println(affectedRows + " satır eklendi.");
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Person> searchPerson(Person person) {

        List<Person> personList = new ArrayList<>();
            String sql = "select * from person where name='"+person.getName()+"'";

            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            try (
                    Connection conn = DriverManager.getConnection(JDBC_CONNECTION_STR, USERNAME, PASSWORD);
                    PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

      //         preparedStatement.setString(1, person.getName());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String surname = resultSet.getString("surname");
                    String phone = resultSet.getString("phone");


                    person.setId(id);
                    person.setName(name);
                    person.setSurname(surname);
                    person.setPhone(phone);
                    personList.add(person);
                }

            } catch (SQLException e) {
                System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return personList;
        }




}
