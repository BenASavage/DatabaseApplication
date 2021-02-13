package Sql;

public class SqlPlayers {

    public static final String fillTable = "INSERT INTO Players" +
            "(FirstName, LastName, Position, TeamID, PPG)" +
            "VALUES " +
            "('Lebron', 'James', 'SF', 1, 25),"+
            "('Anthony', 'Davis', 'PF', 1, 26.6),"+
            "('Danny', 'Green', 'SG', 1, 25),"+
            "('Quinn', 'Cook', 'PG', 1, 5),"+
            "('Avery', 'Bradley', 'PG', 1, 8.5),"+
            "('Paul', 'Millsap', 'PF', 2, 12.1)," +
            "('Nikota', 'Jokic', 'C', 2, 20.6)," +
            "('Gary', 'Harris', 'SG', 2, 10.2)," +
            "('Mason', 'Plumlee', 'PF', 2, 7.4)," +
            "('Will', 'Barton', 'SF', 2,  15.1)," +
            "('Kawhi', 'Leonard', 'SF', 3, 27.2),"+
            "('Paul', 'George', 'SG', 3, 21.7),"+
            "('Marcus', 'Morris', 'SF', 3, 11),"+
            "('Patrick', 'Beverley', 'PG', 3, 8.5),"+
            "('Lou', 'Williams', 'SG', 3, 19.5),"+
            "('Mike', 'Conley', 'PG', 4, 13.5),"+
            "('Rudy', 'Gobert', 'C', 4, 15.6),"+
            "('Bojan', 'Bodanovic', 'SF', 4, 21),"+
            "('Jodan', 'Clarkson', 'PG', 4, 16.2),"+
            "('Joe', 'Ingles', 'SG', 4, 10),"+
            "('Russell', 'Westbrok', 'PG', 5,  27.2)," +
            "('James', 'Harden', 'SG', 5,  35.3)," +
            "('Eric', 'Gordon', 'SG', 5,  15.3)," +
            "('Robert', 'Covington', 'PF', 5,  27.2)," +
            "('P.J.', 'Tucket', 'PF', 5,  7.4)";

    public static final String createTable = "CREATE TABLE Players ("+
            " ID int not null primary key" +
            " GENERATED ALWAYS AS IDENTITY" +
            " (START WITH 1, INCREMENT BY 1)," +
            " FirstName varchar(255)," +
            " LastName varchar(255)," +
            " Position varchar(255)," +
            " TeamID int," +
            " PPG float" +
            ")";

    public static final String dropTable =
            "DROP TABLE Players";

    // ========== Q U E R I E S ==========

    public static final String selectAll =
            "SELECT * FROM Players";

    public static final String selectLastNamePositionPPGWherePositionPG =
            "SELECT ID, LastName, Position, PPG" +
                    " FROM Players" +
                    " WHERE Position = 'PG'" +
                    " ORDER BY PPG DESC";

    public static final String selectAllOrderByTeamID =
            "SELECT * FROM Players ORDER BY TeamID, ID";

    public static final String selectAllOrderByPointPerGame =
            "SELECT * FROM Players ORDER BY PPG DESC";
}
