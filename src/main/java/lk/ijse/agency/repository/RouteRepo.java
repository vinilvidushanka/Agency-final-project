package lk.ijse.agency.repository;

import lk.ijse.agency.db.DbConnection;
import lk.ijse.agency.model.Route;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RouteRepo {
    public static List<Route> getAll() throws SQLException {
        String sql = "SELECT * FROM route";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Route> routeList = new ArrayList<>();
        while (resultSet.next()) {
            String route_id = resultSet.getString(1);
            String Name = resultSet.getString(2);
            String van_id = resultSet.getString(3);
            String day = resultSet.getString(4);

            Route route = new Route(route_id, Name, van_id,day);
            routeList.add(route);
        }
        return routeList;
    }

    public static List<String> getId() throws SQLException {
        String sql = "SELECT route_id FROM route";

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
