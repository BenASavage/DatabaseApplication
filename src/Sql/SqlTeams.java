package Sql;

public class SqlTeams {

    public static final String fillTable = "INSERT INTO Teams" +
            "(Name, City, Win, Loss)" +
            "VALUES " +
            "('Lakers', 'LA', 41, 12),"+
            "('Nuggets', 'Denver', 38, 17),"+
            "('Clippers', 'LA', 37, 18),"+
            "('Jazz', 'SLC', 38, 18)," +
            "('Rockets', 'Houston', 34, 20)";

    public static final String createTable = "CREATE TABLE Teams ("+
            " ID int not null primary key" +
            " GENERATED ALWAYS AS IDENTITY" +
            " (START WITH 1, INCREMENT BY 1)," +
            " Name varchar(255)," +
            " City varchar(255)," +
            " Win int," +
            " Loss int" +
            ")";

    public static final String dropTable =
            "DROP TABLE Teams";

    // ========== Q U E R I E S ==========

    public static final String selectAll =
            "SELECT * FROM Teams";

    public static final String selectAllOrderByWin =
            "SELECT * FROM Teams ORDER BY Win DESC";

    public static final String selectAllOrderByID =
            "SELECT * FROM Teams ORDER BY ID";
}

