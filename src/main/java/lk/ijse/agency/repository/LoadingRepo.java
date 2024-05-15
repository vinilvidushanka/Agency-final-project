package lk.ijse.agency.repository;

import lk.ijse.agency.db.DbConnection;
import lk.ijse.agency.model.Loading;

import java.sql.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoadingRepo {
    public static boolean save(Loading loading) throws SQLException {
        String sql = "INSERT INTO daily_item_loading_report VALUES(?,?,?,?,?,?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        System.out.println("loading save?");

        pstm.setObject(1, loading.getRepoId());
        pstm.setObject(2, loading.getItemCode());
        pstm.setObject(3, loading.getQty());
        pstm.setObject(4, loading.getDate());
        pstm.setObject(5, loading.getVanId());
        pstm.setObject(6, loading.getItemName());

        System.out.println("loading save!!!!");

        return pstm.executeUpdate() > 0;
    }
}
