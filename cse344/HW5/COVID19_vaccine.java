import java.util.*;
import java.sql.*;

public class COVID19_vaccine {

    public SqlConnectionManager sqlClient;
    public String vaccineId;

    /*
    List<COVID19_vaccine> vaccineList = new ArrayList<>();
    vaccineList.add(new COVID19_vaccine("Pfizer-BioNTech", 2, 100));
    vaccineList.add(new COVID19_vaccine("Moderna", 2, 100));
    vaccineList.add(new COVID19_vaccine("Johnson & Johnson’s Janssen", 1, 100));
    */
    public COVID19_vaccine(String name, int reqDose, int stock) throws Exception {
        this.sqlClient = new SqlConnectionManager(
                System.getenv("Server"),
                System.getenv("DBName"),
                System.getenv("UserID"),
                System.getenv("Password")
        );

        try {
            sqlClient.openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        vaccineId = "";
        try {
            ResultSet rs = sqlClient.executeQuery(
                    "INSERT INTO Vaccines (VaccineName, RequiredDose, Stock) VALUES (" +
                            "'" + name + "'" + ", " + reqDose + ", " + stock +
                            ");" + "SELECT @@IDENTITY AS 'Identity'; ");
            rs.next();
            vaccineId = (rs.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void AddDoses(int stock){
        try {
            sqlClient.executeUpdate(
                    "UPDATE Vaccines SET stock = stock + " + stock +
                            " WHERE VaccineId = " + Integer.parseInt(vaccineId) +
                            ";");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ReserveDoses(int stock){
        try {
            sqlClient.executeUpdate(
                    "UPDATE Vaccines SET stock = stock - " + stock +
                            " WHERE VaccineId = " + Integer.parseInt(vaccineId) +
                            " AND stock > " + stock +
                            ";");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}