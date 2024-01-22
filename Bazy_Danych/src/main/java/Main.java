import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    private static Connection connection;
    private static JTextArea commandTextArea;

    public static void main(String[] args) {
        // Ustawienia do połączenia z bazą danych
        String url = "jdbc:oracle:thin:@192.168.0.13:1521:xe";
        String user = "system";
        String password = "system";

        // Rejestracja sterownika (może być konieczne w niektórych przypadkach)
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Brak sterownika JDBC: " + e.getMessage());
            return;
        }

        // Nawiązanie połączenia
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Połączono z bazą danych!");
        } catch (SQLException e) {
            System.err.println("Błąd połączenia z bazą danych: " + e.getMessage());
            return;
        }

        createAndShowGUI();
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Baza danych");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        commandTextArea = new JTextArea();
        commandTextArea.setBounds(10, 20, 350, 150);
        panel.add(commandTextArea);

        JButton createTableButton = new JButton("CREATE TABLE");
        createTableButton.setBounds(10, 180, 120, 25);
        panel.add(createTableButton);

        JButton insertDataButton = new JButton("INSERT DATA");
        insertDataButton.setBounds(140, 180, 120, 25);
        panel.add(insertDataButton);

        JButton dropTableButton = new JButton("DROP TABLE");
        dropTableButton.setBounds(270, 180, 120, 25);
        panel.add(dropTableButton);

        JButton closeButton = new JButton("Zamknij program");
        closeButton.setBounds(10, 220, 150, 25);
        panel.add(closeButton);

        JButton executeButton = new JButton("Wykonaj komendy");
        executeButton.setBounds(180, 220, 200, 25);
        panel.add(executeButton);

        createTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createTable();
            }
        });

        insertDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertData();
            }
        });

        dropTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dropTable();
            }
        });

        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeCommands();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeConnection();
                System.exit(0);
            }
        });
    }

    private static void createTable() {
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE Przyklad (ID INT PRIMARY KEY, NAME VARCHAR(255))"; // TUTAJ MOZNA ZMIENIC CREATE TABLE
            statement.execute(sql);
            System.out.println("Pomyslnie utworzono tablice");
        } catch (SQLException e) {
            System.err.println("Blad w tworzeniu tablicy: " + e.getMessage());
        }
    }

    private static void insertData() {
        try {
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO Przyklad (ID, NAME) VALUES (1, 'Janek')"; // TUTAJ MOZNA ZMIENIC INSERT INTO
            statement.execute(sql);
            System.out.println("Dane zostaly pomyslnie wprowadzone");
        } catch (SQLException e) {
            System.err.println("Blad przy wprowadzaniu danych: " + e.getMessage());
        }
    }

    private static void dropTable() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DROP TABLE Przyklad"; // TUTAJ MOZNA ZMIENIC DROP TABLE
            statement.execute(sql);
            System.out.println("Pomyslnie usunieto tabele");
        } catch (SQLException e) {
            System.err.println("Blad przy usuwaniu tabeli: " + e.getMessage());
        }
    }

    private static void executeCommands() {
        try {
            Statement statement = connection.createStatement();
            String[] commands = commandTextArea.getText().split(";");

            for (String command : commands) {
                if (!command.trim().isEmpty()) {
                    statement.execute(command);
                    System.out.println("Komenda zostala pomyslnie wykonana: " + command);
                }
            }

        } catch (SQLException e) {
            System.err.println("Blad przy wykonywaniu komendy: " + e.getMessage());
        }
    }

    private static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Zamknieto polaczenie z baza");
            }
        } catch (SQLException e) {
            System.err.println("Blad przy zamykaniu z baza: " + e.getMessage());
        }
    }
}
