package lk.ijse.agency.repository;

import lk.ijse.agency.db.DbConnection;
import lk.ijse.agency.model.CreditBill;
import lk.ijse.agency.model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreditBillRepo {
    public static List<CreditBill> getAll() throws SQLException {
        String sql = "SELECT * FROM credit_bill";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<CreditBill> creditBillList = new ArrayList<>();
        while (resultSet.next()) {
            String bill_id = resultSet.getString(1);
            String cus_id = resultSet.getString(2);
            String route_id = resultSet.getString(3);
            Double amount = Double.valueOf(resultSet.getString(4));
            String date = resultSet.getString(5);

            CreditBill creditBill = new CreditBill(bill_id, cus_id, route_id, amount, date);
            creditBillList.add(creditBill);
        }
        return creditBillList;
    }

    public static boolean save(CreditBill creditBill) throws SQLException {
        String sql = "INSERT INTO credit_bill VALUES(?, ?, ?, ?,?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, creditBill.getBillId());
        pstm.setObject(2, creditBill.getCusId());
        pstm.setObject(3, creditBill.getRouteId());
        pstm.setObject(4, creditBill.getAmount());
        pstm.setObject(5, creditBill.getDate());

        return pstm.executeUpdate() > 0;


    }

    public static boolean update(CreditBill creditBill) throws SQLException {
        String sql = "UPDATE credit_bill SET cus_id = ?, route_id = ?, amount = ?, date = ? WHERE bill_id = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(5, creditBill.getBillId());
        pstm.setObject(1, creditBill.getCusId());
        pstm.setObject(2, creditBill.getRouteId());
        pstm.setObject(3, creditBill.getAmount());
        pstm.setObject(4, creditBill.getDate());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String billId) throws SQLException {
        String sql = "DELETE FROM credit_bill WHERE bill_id = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, billId);

        return pstm.executeUpdate() > 0;
    }

    public static CreditBill searchById(String billId) throws SQLException {
        String sql = "SELECT * FROM credit_bill WHERE bill_id = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, billId);
        ResultSet resultSet = pstm.executeQuery();

        CreditBill creditBill = null;

        if (resultSet.next()) {
            String bill_id = resultSet.getString(1);
            String cus_id = resultSet.getString(2);
            String route_id = resultSet.getString(3);
            Double amount = Double.valueOf(resultSet.getString(4));
            String date = resultSet.getString(5);


            creditBill = new CreditBill(bill_id, cus_id, route_id,amount,date);
        }
        return creditBill;
    }
}
