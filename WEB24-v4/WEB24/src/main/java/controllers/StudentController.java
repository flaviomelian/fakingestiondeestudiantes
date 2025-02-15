/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import connections.ConnectionToDB;
import models.Student;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Flavio
 */
public class StudentController {
    
    private ConnectionToDB connection;
    private Connection con;
    
    public StudentController() {
        this.connection = new ConnectionToDB();
    }
    
    //CRUD
    public boolean insertStudent(String name, String surnames, int age, int mark, String address, int course, String familyData, boolean picture) {
        var inserted = true;
        try {
            this.con = this.connection.getConnection();
            //if(!this.con.isClosed()) this.con.close();
            //this.con = this.connection.getConnection();
            var SQLinput = this.con.prepareStatement(
                            "insert into students(name, surnames, age, grade, course, family_data, picture, address)"
                            + " values(?, ?, ?, ?, ?, ?, ?, ?);");
            SQLinput.setString(1, name);
            SQLinput.setString(2, surnames);
            SQLinput.setInt(3, age);
            SQLinput.setInt(4, mark);
            SQLinput.setInt(5, course);
            SQLinput.setString(6, familyData);
            SQLinput.setBoolean(7, picture);
            SQLinput.setString(8, address);
            SQLinput.execute();
            SQLinput.close();
        } catch (SQLException e) {
            e.printStackTrace();
            inserted = !inserted;
        } finally {
            return inserted;
        }
    }
    
    public boolean updateStudent(String name, String surnames, int age, int mark, String address, int course, String familyData, boolean picture, int id) {
        var updated = true;
        try (var con = this.connection.getConnection();
            var SQLinput = con.prepareStatement("update students set name = ?, surnames = ?, age = ?, grade = ?, address = ?, course = ?,"
                            + "family_data = ?, picture = ? where id = ?;")) {
            SQLinput.setString(1, name);
            SQLinput.setString(2, surnames);
            SQLinput.setInt(3, age);
            SQLinput.setInt(4, mark);
            SQLinput.setString(5, address);
            SQLinput.setInt(6, course);
            SQLinput.setString(7, familyData);
            SQLinput.setBoolean(8, picture);
            SQLinput.setInt(9, id);
            SQLinput.executeUpdate();
        } catch (SQLException e) {
            updated = !updated;
            e.printStackTrace();
        } finally {
            return updated;
        }
    }
    
    public boolean deleteStudent(int id) {
        var deleted = true;
        try {
        System.out.println("Intentando borrar al estudiante con id: " + id);
            // Obtienes otra conexión libre del pool
            this.con = this.connection.getConnection();  // Esta es otra conexión
            var SQLinput = this.con.prepareStatement("DELETE FROM students WHERE id = ?;");
            SQLinput.setInt(1, id);
            SQLinput.execute();
            SQLinput.close();
            this.con.close(); // Y la cierras después de usarla
        System.out.println("Estudiante con id " + id + " borrado exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            deleted = false;
        }
        return deleted;
    }

 
    
    public Student getStudent(int id) {
        Student student = null;
        try {
            if(!this.con.isClosed()) this.con.close();
            this.con = this.connection.getConnection();
            var SQLinput = this.con.prepareStatement("select * from students where id = ?;");
            SQLinput.setInt(1, id);
            var resultSet = SQLinput.executeQuery();
            if (resultSet.next()) {
                System.out.println("data.StudentManagement.getStudent()");
                student = new Student(resultSet.getInt("id"),
                                    resultSet.getString("name"),
                                    resultSet.getString("surnames"),
                                    resultSet.getInt("age"), 
                                    resultSet.getInt("grade"), 
                                    resultSet.getInt("course"), 
                                    resultSet.getString("family_data"),
                                    resultSet.getString("address"), 
                                    resultSet.getBoolean("picture"));
            }
            SQLinput.close();
            resultSet.close();
            this.con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }

    public ArrayList<Student> getStudents() {
        var students = new ArrayList<Student>();
        try {
        System.out.println("Obteniendo lista de estudiantes.");
            // Aquí obtienes una conexión libre
            this.con = this.connection.getConnection(); // Esta es una conexión libre
            try (var SQLinput = this.con.prepareStatement("SELECT * FROM students;");
                 var resultSet = SQLinput.executeQuery()) {

                while (resultSet.next()) {
                    students.add(new Student(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("surnames"),
                            resultSet.getInt("age"),
                            resultSet.getInt("grade"),
                            resultSet.getInt("course"),
                            resultSet.getString("family_data"),
                            resultSet.getString("address"),
                            resultSet.getBoolean("picture")
                    ));
                }
            }
            // Después de usar la conexión, se debería cerrar
            this.con.close(); 
        System.out.println("lista de estudiantes obtenida.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }


}
