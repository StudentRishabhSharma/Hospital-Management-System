package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementsystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username ="root";
    private static final String password = "13022001";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection);
            Appointments appointments = new Appointments(connection);
            while (true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. View Appointment");
                System.out.println("6. Exit");
                System.out.println("Enter Your Choice: ");
                int choice = scanner.nextInt();

                switch (choice){
                    case 1:
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        patient.viewPatient();
                        System.out.println();
                        break;
                    case 3:
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        bookAppointment(patient, doctor, connection, scanner);
                        System.out.println();
                        break;
                    case 5:
                        appointments.viewAppointments();
                        System.out.println();
                    case 6:
                        return;
                    default:
                        System.out.println("Enter valid Choice");
                }

            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner){
        System.out.println("Enter patient Id: ");
        int patientId = scanner.nextInt();
        System.out.println("Enter doctor Id: ");
        int doctorId = scanner.nextInt();
        System.out.println("Enter appointment date (yyyy-mm-dd): ");
        String appointmentDate = scanner.next();

        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
            if(checkDoctorAvailability(doctorId, appointmentDate, connection)){
                 String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES(?, ?, ?)";
                 try {
                     PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                     preparedStatement.setInt(1, patientId);
                     preparedStatement.setInt(2, doctorId);
                     preparedStatement.setString(3, appointmentDate);

                     int rowsAffected = preparedStatement.executeUpdate();
                     if(rowsAffected>0){
                         System.out.println("Appointment Booked");
                     }else {
                         System.out.println("failed to Book Appointment!");
                     }
                 }catch (SQLException e){
                     e.printStackTrace();
                 }
            }else{
                System.out.println("Not Available on this date");
            }
        }else{
            System.out.println("Either doctor or patient doesn't exist!!");
        }
    }
    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection){
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count==0){
                    return true;
                }else {
                    return false;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;

    }
}
