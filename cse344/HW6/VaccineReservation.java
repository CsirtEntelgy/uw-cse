import java.sql.*;

public class VaccineReservation {

    public static void main(String[] args) throws Exception {
        try {
            VaccineReservationScheduler vrs = new VaccineReservationScheduler();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class VaccineReservationScheduler {

    private int slotSchedulingId;
    private String getAppointmentSQL;
    private String updateAppointmentSQL;
    private SqlConnectionManager sqlClient;

    public VaccineReservationScheduler() throws Exception {
        this.sqlClient = new SqlConnectionManager(
                System.getenv("Server"),
                System.getenv("DBName"),
                System.getenv("UserID"),
                System.getenv("Password")
        );
        sqlClient.openConnection();
        this.slotSchedulingId = 0;
        this.getAppointmentSQL = "SELECT c.CaregiverSlotSchedulingId " +
                "FROM CaregiverSchedule AS c " +
                "WHERE c.SlotStatus = 0;";
        this.updateAppointmentSQL = "UPDATE CaregiverSchedule " +
                "SET SlotStatus = 1 " +
                "WHERE CaregiverSlotSchedulingId = " + slotSchedulingId + ";";
    }

    /*
    Method that reserves a CareGiver appointment slot &
    returns the unique scheduling slotid
    Should return 0 if no slot is available  or -1 if there is a database error
    */
    public int putHoldOnAppointmentSlot() throws Exception {
        try {
            ResultSet rs = sqlClient.executeQuery(getAppointmentSQL);
            boolean empty = rs.next();
            if(!empty){
                return 0;
            } else {
                rs.last();
                this.slotSchedulingId = (rs.getInt(1));
                sqlClient.executeQuery(updateAppointmentSQL);
                return this.slotSchedulingId;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /*
    method that marks a slot on Hold with a definite reservation
    slotid is the slot that is currently on Hold and whose status will be updated 
    returns the same slotid when the database update succeeds 
    returns 0 is there if the database update dails 
    returns -1 the same slotid when the database command fails
    returns -2 if the slotid parm is invalid
    */
    public int ScheduleAppointmentSlot(int slotId) {
        if (slotId < 1) {
            return -2;
        }
        try {
            sqlClient.executeUpdate("UPDATE CaregiverSchedule " +
                    "SET SlotStatus = 2 " +
                    "WHERE CaregiverSlotSchedulingId = " + slotId + ";");
            return slotId;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}