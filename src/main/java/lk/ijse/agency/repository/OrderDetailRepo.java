package lk.ijse.agency.repository;

import lk.ijse.agency.db.DbConnection;
import lk.ijse.agency.model.OrderDetail;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderDetailRepo {
    public static boolean save(List<OrderDetail> odList) throws SQLException {
        for (OrderDetail od : odList) {
            if(!save(od)) {
                return false;
            }
        }
        return true;
    }
    public static boolean save(OrderDetail od) throws SQLException {
        System.out.println(od);
        System.out.println("od");
        String sql = "INSERT INTO order_details VALUES(?, ?,?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);
        pstm.setString(1, od.getOrderId());
        pstm.setString(2, od.getItemCode());
        pstm.setString(3, String.valueOf(od.getQty()));



        return pstm.executeUpdate() > 0;
    }
}
