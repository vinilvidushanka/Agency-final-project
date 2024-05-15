package lk.ijse.agency.repository;

import lk.ijse.agency.db.DbConnection;
import lk.ijse.agency.model.Expenses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExpensesRepo {
    public static boolean save(Expenses expenses) throws SQLException {
//        In here you can now save your customer
        String sql = "INSERT INTO expenses VALUES(?, ?, ?, ?,?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, expenses.getCode());
        pstm.setObject(2, expenses.getVanId());
        pstm.setObject(4, expenses.getAmount());
        pstm.setObject(3, expenses.getDescription());
        pstm.setObject(5, expenses.getDate());

        return pstm.executeUpdate() > 0;

        /*int affectedRows = pstm.executeUpdate();
        if (affectedRows > 0) {
            return true;
        } else {
            return false;
        }*/
    }

    public static boolean update(Expenses expenses) throws SQLException {
        String sql = "UPDATE expenses SET van_id = ?, amount = ?, description = ?, date = ? WHERE code = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, expenses.getCode());
        pstm.setObject(2, expenses.getVanId());
        pstm.setObject(4, expenses.getAmount());
        pstm.setObject(3, expenses.getDescription());
        pstm.setObject(5, expenses.getDate());

        return pstm.executeUpdate() > 0;
    }

    public static Expenses searchById(String id) throws SQLException {
        String sql = "SELECT * FROM expenses WHERE code = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, id);
        ResultSet resultSet = pstm.executeQuery();

        Expenses expenses = null;

        if (resultSet.next()) {
            String code = resultSet.getString(1);
            String van_id = resultSet.getString(2);
            Double amount = resultSet.getDouble(4);
            String description = resultSet.getString(3);
            String date = String.valueOf(resultSet.getDate(5));

            expenses = new Expenses(code, van_id, amount,description,date);
        }
        return expenses;
    }



    public static List<Expenses> getAll() throws SQLException {
        String sql = "SELECT * FROM expenses";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Expenses> expensesList = new ArrayList<>();
        while (resultSet.next()) {
            String code = resultSet.getString(1);
            String van_id = resultSet.getString(2);
            Double amount = resultSet.getDouble(4);
            String description = resultSet.getString(3);
            String  date = String.valueOf(resultSet.getDate(5));

            Expenses expenses = new Expenses(code, van_id, amount,description,date);
            expensesList.add(expenses);
        }
        return expensesList;
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT code FROM expenses";

        Connection connection = DbConnection.getInstance().getConnection();
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        List<String> idList = new ArrayList<>();

        while (resultSet.next()) {
            idList.add(resultSet.getString(1));
        }
        return idList;
    }

    public static boolean delete(String code) throws SQLException {
        String sql = "DELETE FROM expenses WHERE code = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, code);

        return pstm.executeUpdate() > 0;
    }
}
