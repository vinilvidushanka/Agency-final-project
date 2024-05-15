package lk.ijse.agency.repository;

import lk.ijse.agency.db.DbConnection;
import lk.ijse.agency.model.Expenses;
import lk.ijse.agency.model.Salary;
import lk.ijse.agency.model.Stock;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SalaryRepo {
    public static List<Salary> getAll() throws SQLException {
        String sql = "SELECT * FROM salary";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Salary> salaryList = new ArrayList<>();
        while (resultSet.next()) {
            String salary_id = resultSet.getString(1);
            String employee_id = resultSet.getString(2);
            String name = resultSet.getString(3);
            String month = resultSet.getString(4);
            Double amount = resultSet.getDouble(5);
            String date = resultSet.getString(6);


            Salary salary = new Salary(salary_id, employee_id, name, month,amount,date);
            salaryList.add(salary);
        }
        return salaryList;
    }
    public static boolean save(Salary salary) throws SQLException {
        String sql = "INSERT INTO salary VALUES(?, ?, ?, ?,?,?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, salary.getSalaryId());
        pstm.setObject(2, salary.getEmpId());
        pstm.setObject(3, salary.getName());
        pstm.setObject(4, salary.getMonth());
        pstm.setObject(5, salary.getAmount());
        pstm.setObject(6, salary.getDate());

        return pstm.executeUpdate() > 0;


    }
    public static boolean update(Salary salary) throws SQLException {
        String sql = "UPDATE salary SET employee_id = ?, name = ?, month = ?, amount = ?,date = ? WHERE salary_id = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, salary.getSalaryId());
        pstm.setObject(2, salary.getEmpId());
        pstm.setObject(3, salary.getName());
        pstm.setObject(4, salary.getMonth());
        pstm.setObject(5, salary.getAmount());
        pstm.setObject(6, salary.getDate());

        return pstm.executeUpdate() > 0;
    }
    public static Salary searchById(String id) throws SQLException {
        String sql = "SELECT * FROM salary WHERE salary_id = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, id);
        ResultSet resultSet = pstm.executeQuery();

        Salary salary = null;

        if (resultSet.next()) {
            String salary_id = resultSet.getString(1);
            String employee_id = resultSet.getString(2);
            String name = resultSet.getString(3);
            String month = resultSet.getString(4);
            Double amount = Double.valueOf(String.valueOf(resultSet.getDate(5)));
            String date = String.valueOf(resultSet.getDate(5));


            salary = new Salary(salary_id, employee_id, name,month,amount,date);
        }
        return salary;
    }
}
