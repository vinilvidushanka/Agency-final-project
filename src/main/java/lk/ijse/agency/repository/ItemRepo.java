package lk.ijse.agency.repository;

import lk.ijse.agency.db.DbConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemRepo {
    public static List<String> getCodes() throws SQLException {
        String sql = "SELECT item_code FROM stock";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        List<String> codeList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while(resultSet.next()) {
            codeList.add(resultSet.getString(1));
        }
        return codeList;
    }
}
