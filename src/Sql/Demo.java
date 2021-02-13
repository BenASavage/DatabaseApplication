package Sql;

import java.sql.*;

public class Demo {

    public static void main(String[] args){
        try{
            Connection connection = DriverManager.getConnection("jdbc:derby:DemoDatabase;create=true");
            Statement statement = connection.createStatement();

            statement.execute(SqlTeams.dropTable);
            statement.execute(SqlTeams.createTable);
            statement.execute(SqlTeams.fillTable);

            statement.execute(SqlPlayers.dropTable);
            statement.execute(SqlPlayers.createTable);
            statement.execute(SqlPlayers.fillTable);


            ResultSet results = statement.executeQuery(SqlTeams.selectAll);
            displayResults(results);
            System.out.println();

            results = statement.executeQuery(SqlPlayers.selectAll);
            displayResults(results);
            System.out.println();

            //results = statement.executeQuery(SqlPlayers.selectAllOrderByTeamID);
            //displayResults(results);
            //System.out.println();


        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void displayResults(ResultSet results) {
        try{
            ResultSetMetaData metaData = results.getMetaData();
            int largestText = printHeader(metaData);
            int columnCount = metaData.getColumnCount();
            while(results.next()){
                for(int i = 1; i <= columnCount; i++){
                    System.out.printf("%-"+largestText+"s",results.getObject(i));
                    System.out.print(" ");
                }
                System.out.println("");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static int printHeader(ResultSetMetaData metaData) {
        int largestText = 0;
        try {
            int columnCount = metaData.getColumnCount();
            int dashCount = 0;
            for (Integer i = 1; i <= columnCount; i++) {
                String columnText = metaData.getColumnLabel(i);
                if(columnText.length() > largestText)
                    largestText = columnText.length();
            }
            for (Integer i = 1; i <= columnCount; i++) {
                String columnText = metaData.getColumnLabel(i);

                System.out.printf("%-"+largestText+"s",columnText);
                System.out.print(" ");
                dashCount += largestText;
            }
            System.out.println("");
            for(int i=0; i<dashCount; i++){
                System.out.print("-");
            }
            System.out.println("");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return largestText;
    }
}
