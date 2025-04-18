package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Appointments{
    private Connection connection;
    public Appointments(Connection connection){
        this.connection = connection;
    }
    public void viewAppointments(){
        String query = "SELECT* from appointments";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Appointments : ");
            System.out.println("+------------+---------------+-------------+--------------+");
            System.out.println("| Id         |  patient_Id   | doctor_Id   |      date    |");
            System.out.println("+------------+---------------+-------------+--------------+");
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                int patient_id = resultSet.getInt("patient_id");
                int doctor_id = resultSet.getInt("doctor_id");
                String appointment_date = resultSet.getString("appointment_date");
                System.out.printf("|%-12s|%-15s|%-13s|%-14s|\n", id, patient_id, doctor_id, appointment_date);
                System.out.println("+------------+---------------+-------------+--------------+");

            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
