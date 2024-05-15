package lk.ijse.agency.repository;

import lk.ijse.agency.db.DbConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VanRepo {
    public static List<String> getId() throws SQLException {
        String sql = "SELECT van_id FROM van";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        List<String> vanList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while(resultSet.next()) {
            vanList.add(resultSet.getString(1));
        }
        return vanList;
    }

    public static List<String> getVanId() throws SQLException {
        String sql = "SELECT van_id FROM van";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        List<String> vanList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while(resultSet.next()) {
            vanList.add(resultSet.getString(1));
        }
        return vanList;
    }
}
