package lk.ijse.agency.repository;

import lk.ijse.agency.db.DbConnection;
import lk.ijse.agency.model.Order;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderRepo {

    public static boolean save(Order order) throws SQLException {

        System.out.println(order.toString());

        String sql = "INSERT INTO orders VALUES(?, ?, ?,?,?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);
        System.out.println("orderRepo");

        pstm.setString(1, order.getOrderId());
        pstm.setString(2, order.getItemCode());
        pstm.setString(3, order.getItemName());
        pstm.setInt(4, order.getQty());
        pstm.setString(5,order.getDate());

        System.out.println("orderRepo 2nd");

        return pstm.executeUpdate() > 0;
    }

}
