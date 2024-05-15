package lk.ijse.agency.repository;

import lk.ijse.agency.db.DbConnection;
import lk.ijse.agency.model.OrderDetail;
import lk.ijse.agency.model.PlaceOrder;

import java.sql.Connection;
import java.sql.SQLException;

public class    PlaceOrderRepo {

    public static boolean placeOrder(PlaceOrder po) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
            boolean isOrderSaved = OrderRepo.save(po.getOrder());
            if (isOrderSaved) {
                System.out.println(isOrderSaved);
                System.out.println();
                boolean isOrderDetailSaved = OrderDetailRepo.save(po.getOdList());
                System.out.println("1"+isOrderDetailSaved);
                if (isOrderDetailSaved) {
                    boolean isItemQtyUpdate = StockRepo.updateQty(po.getOdList());
                    if (isItemQtyUpdate) {
                        connection.commit();
                        return true;
                    }
                }
            }
            connection.rollback();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
