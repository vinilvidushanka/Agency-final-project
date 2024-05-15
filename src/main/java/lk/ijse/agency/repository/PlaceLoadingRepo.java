package lk.ijse.agency.repository;

import lk.ijse.agency.db.DbConnection;
import lk.ijse.agency.model.PlaceLoading;

import java.sql.Connection;
import java.sql.SQLException;

public class PlaceLoadingRepo {
    public static boolean placeLoading(PlaceLoading pl) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
            boolean isLoadSaved = LoadingRepo.save(pl.getLoading());
            System.out.println(isLoadSaved);
            if (isLoadSaved) {
                System.out.println(isLoadSaved);
                System.out.println("methnt awa");
                boolean isLoadingDetailSaved = LoadingDetailRepo.save(pl.getLdList());
                System.out.println(isLoadingDetailSaved);
                if (isLoadingDetailSaved) {
                    boolean isStockQtyUpdate = StockRepo.updateLoadingQty(pl.getLdList());
                    System.out.println(isStockQtyUpdate);
                    if (isStockQtyUpdate) {
                        connection.commit();
                        return true;
                    }
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
