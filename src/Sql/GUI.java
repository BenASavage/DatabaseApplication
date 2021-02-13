package Sql;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import java.sql.*;
import java.util.Enumeration;

public class GUI extends JFrame {

    private JPanel contentPane;

    private JPanel teamControlPanel;
    private JTable teamTable;

    private JPanel playerControlPanel;
    private JTable playerTable;

    private final ButtonGroup buttonGroup_1 = new ButtonGroup();
    private final ButtonGroup buttonGroup_2 = new ButtonGroup();
    private Statement statement;


    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                GUI frame = new GUI();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public GUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(50, 50, 700, 600);

        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:DemoDatabase;create=true");
            statement = connection.createStatement();

            statement.execute(SqlTeams.dropTable);
            statement.execute(SqlTeams.createTable);
            statement.execute(SqlTeams.fillTable);

            statement.execute(SqlPlayers.dropTable);
            statement.execute(SqlPlayers.createTable);
            statement.execute(SqlPlayers.fillTable);
        } catch (SQLException e) {
            try {
                statement.execute(SqlTeams.createTable);
                statement.execute(SqlTeams.fillTable);

                statement.execute(SqlPlayers.createTable);
                statement.execute(SqlPlayers.fillTable);
            } catch (SQLException f) {
                f.printStackTrace();
            }
        }

        createMenu();
        createTeamControlPanel();
        teamTable = createTable(SqlTeams.selectAll, "Teams");

        createPlayerControlPanel();
        playerTable = createTable(SqlPlayers.selectAll, "Players");
        createContentPane();
    }

    private void createContentPane() {
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        contentPane.setFont(new Font("Verdana", Font.PLAIN, 26));
        setContentPane(contentPane);

        contentPane.add(playerControlPanel, BorderLayout.WEST);
        contentPane.add(playerTable, BorderLayout.CENTER);

        contentPane.add(teamControlPanel, BorderLayout.WEST);
        contentPane.add(teamTable, BorderLayout.CENTER);
    }

    private JTable createTable(String query, String tableName) {
        try {
            String tableText = displayResults(statement.executeQuery(query));

            String[] tableLines = tableText.split("\\n");
            int columnCount = tableLines[0].split("\\s+").length;

            JTable table = new JTable(tableLines.length, columnCount);

            for (int i = 0; i < tableLines.length; i++) {
                String[] tableCells = tableLines[i].split("\\s+");
                for (int j = 0; j < tableCells.length; j++) {
                    table.getModel().setValueAt(tableCells[j],i,j);
                }
            }

            table.getModel().addTableModelListener(tableModelEvent -> {
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();

                String columnName = table.getModel().getValueAt(0, column).toString();
                columnName = columnName.substring(0,1) + columnName.substring(1).toLowerCase();

                String result = table.getValueAt(row, column).toString();
                if (result.matches("\\D+")) {
                    result = "'" + result + "'";
                }

                String id = (String) table.getValueAt(row, 0);

                try {
                    String update =
                            "UPDATE " + tableName +
                            " SET " + columnName +
                            " = " + result +
                            " WHERE ID = " + id;
                    statement.execute(update);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            return table;
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void createPlayerControlPanel() {
        playerControlPanel = new JPanel();
        playerControlPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        playerControlPanel.setLayout(new GridLayout(12, 1, 0, 0));


        JRadioButton radioTeamID = new JRadioButton("Order by TeamID");
        radioTeamID.addActionListener(e -> {
            contentPane.remove(playerTable);
            playerTable = createTable(SqlPlayers.selectAllOrderByTeamID, "Players");
            contentPane.add(playerTable, BorderLayout.CENTER);
            contentPane.repaint();
            contentPane.revalidate();
        });
        radioTeamID.setSelected(true);
        buttonGroup_1.add(radioTeamID);
        playerControlPanel.add(radioTeamID);


        JRadioButton radioPPG = new JRadioButton("Order by Points Per Game");
        radioPPG.addActionListener(e -> {
            contentPane.remove(playerTable);
            playerTable = createTable(SqlPlayers.selectAllOrderByPointPerGame, "Players");
            contentPane.add(playerTable, BorderLayout.CENTER);
            contentPane.repaint();
            contentPane.revalidate();
        });
        buttonGroup_1.add(radioPPG);
        playerControlPanel.add(radioPPG);

        JRadioButton radioPointGuards = new JRadioButton("Point Guards");
        radioPointGuards.addActionListener(e -> {
            contentPane.remove(playerTable);
            playerTable = createTable(SqlPlayers.selectLastNamePositionPPGWherePositionPG, "Players");
            contentPane.add(playerTable, BorderLayout.CENTER);
            contentPane.repaint();
            contentPane.revalidate();
        });
        buttonGroup_1.add(radioPointGuards);
        playerControlPanel.add(radioPointGuards);

        JButton addPlayerButton = new JButton("Add a Player");
        addPlayerButton.addActionListener(e -> {
            try {
                statement.execute("INSERT INTO Players" +
                        "(FirstName, LastName, Position, TeamID, PPG)" +
                        "VALUES ('null', 'null', 'null', null, 0)");

                for (Enumeration<AbstractButton> buttons = buttonGroup_1.getElements(); buttons.hasMoreElements();) {
                    AbstractButton button = buttons.nextElement();
                    if (button.isSelected()) {
                        button.doClick();
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        playerControlPanel.add(addPlayerButton);

        JButton removePlayerButton = new JButton("Remove a Player");
        removePlayerButton.addActionListener(e -> {
            try {
                int idNum = Integer.parseInt(JOptionPane.showInputDialog(contentPane, "Enter the ID number"));
                statement.execute("DELETE FROM Players WHERE ID = " + idNum);
                for (Enumeration<AbstractButton> buttons = buttonGroup_1.getElements(); buttons.hasMoreElements();) {
                    AbstractButton button = buttons.nextElement();
                    if (button.isSelected()) {
                        button.doClick();
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        playerControlPanel.add(removePlayerButton);
    }

    private void createTeamControlPanel() {
        teamControlPanel = new JPanel();
        teamControlPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        teamControlPanel.setLayout(new GridLayout(12, 1, 0, 0));

        JRadioButton radioOrderByID = new JRadioButton("Order by ID");
        radioOrderByID.addActionListener(e -> {
            contentPane.remove(teamTable);
            teamTable = createTable(SqlTeams.selectAllOrderByID, "Teams");
            contentPane.add(teamTable, BorderLayout.CENTER);
            contentPane.repaint();
            contentPane.revalidate();
        });
        radioOrderByID.setSelected(true);
        buttonGroup_2.add(radioOrderByID);
        teamControlPanel.add(radioOrderByID);

        JRadioButton radioOrderByWins = new JRadioButton("Order by Wins");
        radioOrderByWins.addActionListener(e -> {
            contentPane.remove(teamTable);
            teamTable = createTable(SqlTeams.selectAllOrderByWin, "Teams");
            contentPane.add(teamTable, BorderLayout.CENTER);
            contentPane.repaint();
            contentPane.revalidate();
        });
        buttonGroup_2.add(radioOrderByWins);
        teamControlPanel.add(radioOrderByWins);

        JButton addTeamButton = new JButton("Add a Team");
        addTeamButton.addActionListener(e -> {
            try {
                statement.execute("INSERT INTO Teams" +
                        "(Name, City, Win, Loss)" +
                        "VALUES ('null', 'null', 0, 0)");

                for (Enumeration<AbstractButton> buttons = buttonGroup_2.getElements(); buttons.hasMoreElements();) {
                    AbstractButton button = buttons.nextElement();
                    if (button.isSelected()) {
                        button.doClick();
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        teamControlPanel.add(addTeamButton);

        JButton removeTeamButton = new JButton("Remove a Team");
        removeTeamButton.addActionListener(e -> {
            try {
                int idNum = Integer.parseInt(JOptionPane.showInputDialog(contentPane, "Enter the ID number"));
                statement.execute("DELETE FROM Teams WHERE ID = " + idNum);
                for (Enumeration<AbstractButton> buttons = buttonGroup_2.getElements(); buttons.hasMoreElements();) {
                    AbstractButton button = buttons.nextElement();
                    if (button.isSelected()) {
                        button.doClick();
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        teamControlPanel.add(removeTeamButton);
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenuItem mntnTeams = new JMenuItem("Teams");
        mntnTeams.addActionListener(arg0 -> {
            contentPane.removeAll();
            contentPane.add(teamControlPanel, BorderLayout.WEST);
            contentPane.add(teamTable, BorderLayout.CENTER);
            revalidate();
            repaint();
        });
        menuBar.add(mntnTeams);

        JMenuItem mntmPlayers = new JMenuItem("Players");
        mntmPlayers.addActionListener(arg0 -> {
            contentPane.removeAll();
            contentPane.add(playerControlPanel, BorderLayout.WEST);
            contentPane.add(playerTable, BorderLayout.CENTER);
            revalidate();
            repaint();
        });
        menuBar.add(mntmPlayers);


        JMenuItem mntnExit = new JMenuItem("Exit");
        mntnExit.addActionListener(arg0 -> System.exit(0));
        menuBar.add(mntnExit);
    }

    public static String displayResults(ResultSet results) {
        String table = "";
        try{
            ResultSetMetaData metaData = results.getMetaData();
            int largestText = getLargestText(metaData);
            int columnCount = metaData.getColumnCount();

            table = table.concat(printHeader(metaData)).concat("\n");

            while(results.next()){
                for(int i = 1; i <= columnCount; i++){
                    table = table.concat(String.format("%-"+largestText+"s",results.getObject(i)));
                    table = table.concat(" ");
                }
                table = table.concat("\n");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return table;
    }

    private static String printHeader(ResultSetMetaData metaData) {
        String header = "";
        int largestText = getLargestText(metaData);
        try {
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnText = metaData.getColumnLabel(i);
                if (columnText.length() > largestText)
                    largestText = columnText.length();
            }

            for (int i = 1; i <= columnCount; i++) {
                String columnText = metaData.getColumnLabel(i);

                header = header.concat(String.format("%-" + largestText + "s", columnText));
                header = header.concat(" ");
            }
            header = header.concat("\n");
            return header;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    private static int getLargestText(ResultSetMetaData metaData) {
        int largestText = 0;
        try {
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnText = metaData.getColumnLabel(i);
                if(columnText.length() > largestText)
                    largestText = columnText.length();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return largestText;
    }
}