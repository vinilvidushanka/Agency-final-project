package lk.ijse.agency.db;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
public class DbConnection {
    private static DbConnection dbConnection;
    private  Connection connection;

    private DbConnection() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ice_cream_agency",
                "root",
                "ijse@123"
        );
    }
    public  static DbConnection getInstance() throws SQLException {
        if (dbConnection == null){
            return dbConnection =new DbConnection();
        }
        return dbConnection;
    }
    public Connection getConnection() {
        return connection;
    }
}
