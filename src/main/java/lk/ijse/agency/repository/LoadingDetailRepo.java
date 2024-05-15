package lk.ijse.agency.repository;

import lk.ijse.agency.db.DbConnection;
import lk.ijse.agency.model.LoadingDetail;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class LoadingDetailRepo {
    public static boolean save(List<LoadingDetail> ldList) throws SQLException {
        for (LoadingDetail ld : ldList) {
            System.out.println(ld);
            System.out.println(ldList);
            if(!save(ld)) {
                System.out.println("hridA");
                return false;

            }
        }
        System.out.println("AI ME");
        return true;
    }

    private static boolean save(LoadingDetail ld) throws SQLException {
        String sql = "INSERT INTO van_item_loading VALUES(?, ?,?,?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setString(1, ld.getVanId());
        pstm.setString(2, ld.getRepoId());
        pstm.setString(3, ld.getItemCode());
        pstm.setInt(4, ld.getQty());
        System.out.println("loading detail save!!!");

        return pstm.executeUpdate() > 0;
    }
}
