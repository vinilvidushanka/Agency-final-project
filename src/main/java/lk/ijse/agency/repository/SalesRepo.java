package lk.ijse.agency.repository;

import lk.ijse.agency.db.DbConnection;
import lk.ijse.agency.model.SalesReport;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SalesRepo {

    public static boolean save(SalesReport salesReport) throws SQLException {
        System.out.println(salesReport);
        String sql = "INSERT INTO sales_report VALUES(?, ?, ?,?,?,?,?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);
        pstm.setString(1, salesReport.getBillCode());
        pstm.setString(2, salesReport.getItemCode());
        pstm.setInt(3, salesReport.getQty());
        //pstm.setInt(4, salesReport.getFreeQty());
        pstm.setString(4, salesReport.getDate());
        pstm.setString(5, salesReport.getItemName());
        pstm.setString(6, salesReport.getVanId());
        pstm.setDouble(7, salesReport.getAmount());

        System.out.println("sale save?");
        return pstm.executeUpdate() > 0;

    }
}
