import java.sql.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class VaccinePatient {

    public SqlConnectionManager sqlClient;

    public String firstDose;
    public String secondDose;
    private int firstDoseSchedule;
    private int secondDoseSchedule;

    private int patientId;
    private int caregiverId;
    private Vaccines vac;

    public VaccinePatient(int patientId, int caregiverId) throws Exception {
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

        this.patientId = patientId;
        this.caregiverId = caregiverId;
    }

    public void ReserveAppointment(int CaregiverSchedulingID, Vaccines vaccine){
        this.vac = vaccine;
        // finding date hour minute info from given CaregiverScheduleID...?
        ResultSet datetime = null;
        try {
            datetime = sqlClient.executeQuery(
                    "SELECT c.WorkDay, c.SlotHour, c.SlotMinute " +
                            "FROM CareGiverSchedule as c " +
                            "WHERE c.CaregiverSlotSchedulingId = " + CaregiverSchedulingID + ";"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        // adding first dose
        firstDose = "";
        firstDoseSchedule = CaregiverSchedulingID;
        try {
            ResultSet rs = sqlClient.executeQuery(
                    "INSERT INTO VaccineAppointments " +
                            "(VaccineName, PatientId, CaregiverId, ReservationDate, ReservationStartHour, " +
                            "ReservationStartMinute, AppointmentDuration, SlotStatus) " +
                            "VALUES (" +
                            "'" + vaccine.vaccineName + "'" + ", " + "'" + this.patientId + "'" + ", " + "'" + this.caregiverId + "'" + ", " +
                            datetime.getDate(1) + ", " + datetime.getString(2) + ", " + datetime.getString(3) + ", " +
                            15 + ", " + 1
                            + ");" + "SELECT @@IDENTITY AS 'Identity'; ");
            rs.next();
            firstDose = rs.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // adding second dose
        secondDose = "";
        try {
            Instant instant = datetime.getDate(1).toInstant().plus(3, ChronoUnit.WEEKS);
            ResultSet rs = sqlClient.executeQuery(
                    "INSERT INTO VaccineAppointments " +
                            "(VaccineName, PatientId, CaregiverId, ReservationDate, ReservationStartHour, " +
                            "ReservationStartMinute, AppointmentDuration, SlotStatus) " +
                            "VALUES (" +
                            "'" + vaccine.vaccineName + "'" + ", " + "'" + this.patientId + "'" + ", " + "'" + this.caregiverId + "'" + ", " +
                            Date.from(instant) + ", " + datetime.getString(2) + ", " + datetime.getString(3) + ", " +
                            15 + ", " + 1
                            + ");" + "SELECT @@IDENTITY AS 'Identity'; ");
            rs.next();
            secondDose = rs.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // finding second schedule...? (3 weeks later)
        try {
            Instant instant = datetime.getDate(1).toInstant().plus(3, ChronoUnit.WEEKS);
            ResultSet rs = sqlClient.executeQuery("SELECT c.CaregiverSlotSchedulingId " +
                    "FROM CaregiverSchedule AS c " +
                    "WHERE c.WorkDay = " + Date.from(instant) + ";");
            secondDoseSchedule = (rs.getInt(1));
            sqlClient.executeUpdate("UPDATE CaregiverSchedule " +
                    "SET SlotStatus = 1 " +
                    "WHERE CaregiverSlotSchedulingId = " + secondDoseSchedule + ";");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ScheduleAppointment(int amount){
        try {
            // mark "Scheduled"
            VaccineReservationScheduler vrs = new VaccineReservationScheduler();
            vrs.ScheduleAppointmentSlot(firstDoseSchedule);
            vrs.ScheduleAppointmentSlot(secondDoseSchedule);

            // update patient
            sqlClient.executeUpdate(
                    "UPDATE Patients SET VaccineStatus = " + 1 +
                            " WHERE PatientId = " + this.patientId +
                            ";");

            // update vaccine
            vac.ReserveDoses(amount);

            // add schedule to CaregiverSchedule...?
            sqlClient.executeUpdate("UPDATE CaregiverSchedule " +
                    "SET VaccineAppointmentId = " + firstDose +
                    "WHERE CaregiverSlotSchedulingId = " + firstDoseSchedule + ";");
            sqlClient.executeUpdate("UPDATE CaregiverSchedule " +
                    "SET VaccineAppointmentId = " + secondDose +
                    "WHERE CaregiverSlotSchedulingId = " + secondDoseSchedule + ";");

            // I can't understand the structure... It seems weird in Java, or I don't know anymore...
            // I'm sorry about the messy code, I tried to follow the lab spec as much as possible
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
