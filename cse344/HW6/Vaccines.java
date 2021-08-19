import java.sql.*;

public class Vaccines {

    public SqlConnectionManager sqlClient;
    public String vaccineName;

    public Vaccines(String vaccineName, String vaccineSupplier,
                    int totalDoses, int dosesPerPatient, int daysBetweenDoses) throws Exception {
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

        vaccineName = "";
        try {
            ResultSet rs = sqlClient.executeQuery(
                    "INSERT INTO Vaccines (VaccineName, VaccineSupplier, AvailableDoses, ReservedDoses, " +
                            "TotalDoses, DosesPerPatient, DaysBetweenDoses) VALUES (" +
                            "'" + vaccineName + "'" + ", " + "'" + vaccineSupplier + "'" + ", " +
                            totalDoses + ", " + 0 + ", " + totalDoses + ", " + dosesPerPatient + ", " + daysBetweenDoses
                            + ");" + "SELECT @@IDENTITY AS 'Identity'; ");
            rs.next();
            vaccineName = (rs.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void AddDoses(int stock){
        try {
            sqlClient.executeUpdate(
                    "UPDATE Vaccines SET TotalDoses = TotalDoses + " + stock +
                            ", " + "AvailableDoses = AvailableDoses + " + stock +
                            " WHERE VaccineName = " + vaccineName +
                            ";");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ReserveDoses(int stock){
        try {
            sqlClient.executeUpdate(
                    "UPDATE Vaccines SET AvailableDoses = AvailableDoses - " + stock +
                            ", " + "ReservedDoses = ReservedDoses + " + stock +
                            " WHERE VaccineName = " + vaccineName +
                            " AND AvailableDoses > " + stock +
                            ";");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}