package lk.ijse.agency.repository;

import lk.ijse.agency.db.DbConnection;
import lk.ijse.agency.model.PlaceSalesReport;
import lk.ijse.agency.model.SalesReport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class PlaceSalesRepo {
    public static boolean placeReport(PlaceSalesReport ps) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
            boolean isSaleSaved = SalesRepo.save(ps.getSales());
            if (isSaleSaved) {

                    boolean isStockQtyUpdate = StockRepo.updateSaleQty(ps.getSales());
                    if (isStockQtyUpdate) {
                        connection.commit();
                        return true;
                    }

            }
            connection.rollback();
            return false;
        } catch (Exception e) {
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }




}
