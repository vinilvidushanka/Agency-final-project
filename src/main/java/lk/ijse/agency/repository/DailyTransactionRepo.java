package lk.ijse.agency.repository;

import lk.ijse.agency.db.DbConnection;
import lk.ijse.agency.model.Customer;
import lk.ijse.agency.model.DailyTransaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DailyTransactionRepo {
    public static List<DailyTransaction> getAll() throws SQLException {
        String sql = "SELECT * FROM daily_transaction";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<DailyTransaction> dailyTransactionList = new ArrayList<>();
        while (resultSet.next()) {
            String bill_id = resultSet.getString(1);
            Double amount = resultSet.getDouble(2);
            String date = resultSet.getString(3);
            String van_id = resultSet.getString(4);


            DailyTransaction dailyTransaction = new DailyTransaction(bill_id, amount, date, van_id);
            dailyTransactionList.add(dailyTransaction);
        }
        return dailyTransactionList;
    }

    public static boolean save(DailyTransaction dailyTransaction) throws SQLException {
        String sql = "INSERT INTO daily_transaction VALUES(?, ?, ?, ?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, dailyTransaction.getBillId());
        pstm.setObject(2, dailyTransaction.getAmount());
        pstm.setObject(3, dailyTransaction.getDate());
        pstm.setObject(4, dailyTransaction.getVanId());


        return pstm.executeUpdate() > 0;
    }

    public static boolean update(DailyTransaction dailyTransaction) throws SQLException {
        String sql = "UPDATE daily_transaction SET amount = ?, date = ?, van_id = ?WHERE bill_id = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(4, dailyTransaction.getBillId());
        pstm.setObject(1, dailyTransaction.getAmount());
        pstm.setObject(2, dailyTransaction.getDate());
        pstm.setObject(3, dailyTransaction.getVanId());


        return pstm.executeUpdate() > 0;
    }

    public static DailyTransaction searchById(String billId) throws SQLException {
        String sql = "SELECT * FROM daily_transaction WHERE bill_id = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, billId);
        ResultSet resultSet = pstm.executeQuery();

        DailyTransaction dailyTransaction = null;

        if (resultSet.next()) {
            String bill_id = resultSet.getString(1);
            Double amount = resultSet.getDouble(2);
            String date = resultSet.getString(3);
            String van_id = resultSet.getString(4);


            dailyTransaction = new DailyTransaction(billId, amount, date,van_id);
        }
        return dailyTransaction;
    }

    public static boolean delete(String billId) throws SQLException {
        String sql = "DELETE FROM daily_transaction WHERE bill_id = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, billId);

        return pstm.executeUpdate() > 0;
    }
}
