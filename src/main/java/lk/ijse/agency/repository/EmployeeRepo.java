package lk.ijse.agency.repository;

import lk.ijse.agency.db.DbConnection;
import lk.ijse.agency.model.Employee;
import lk.ijse.agency.model.Stock;
import lk.ijse.agency.model.tm.EmployeeTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepo {
    public static boolean save(EmployeeTm employee) throws SQLException {
//        In here you can now save your customer
        String sql = "INSERT INTO employee VALUES(?, ?, ?, ?,?,?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, employee.getId());
        pstm.setObject(2, employee.getName());
        pstm.setObject(3, employee.getNic());
        pstm.setObject(4, employee.getAddress());
        pstm.setObject(5, employee.getContact());
        pstm.setObject(6, employee.getVanId());

        return pstm.executeUpdate() > 0;

        /*int affectedRows = pstm.executeUpdate();
        if (affectedRows > 0) {
            return true;
        } else {
            return false;
        }*/
    }

    public static boolean update(Employee employee) throws SQLException {
        String sql = "UPDATE employee SET name = ?, NIC = ?, address = ?, contact = ?, van_id = ? WHERE employee_id = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(6, employee.getId());
        pstm.setObject(1, employee.getName());
        pstm.setObject(2, employee.getNic());
        pstm.setObject(3, employee.getAddress());
        pstm.setObject(4, employee.getContact());
        pstm.setObject(5, employee.getVanId());

        return pstm.executeUpdate() > 0;
    }

    public static Employee searchById(String id) throws SQLException {
        String sql = "SELECT * FROM employee WHERE employee_id = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, id);
        ResultSet resultSet = pstm.executeQuery();

        Employee employee = null;

        if (resultSet.next()) {
            String employee_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String NIC = resultSet.getString(3);
            String address = resultSet.getString(4);
            int contact = resultSet.getInt(5);
            String van_id = resultSet.getString(6);

            employee = new Employee(employee_id, name, NIC,address,contact,van_id);
        }
        return employee;
    }



    public static List<Employee> getAll() throws SQLException {
        String sql = "SELECT * FROM employee";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Employee> employeeList = new ArrayList<>();
        while (resultSet.next()) {
            String employee_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String NIC = resultSet.getString(3);
            String address = resultSet.getString(4);
            int contact = resultSet.getInt(5);
            String van_id = resultSet.getString(6);

            Employee employee = new Employee(employee_id, name, NIC,address,contact,van_id);
            employeeList.add(employee);
        }
        return employeeList;
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT employee_id FROM employee";

        Connection connection = DbConnection.getInstance().getConnection();
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        List<String> idList = new ArrayList<>();

        while (resultSet.next()) {
            idList.add(resultSet.getString(1));
        }
        return idList;
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM employee WHERE employee_id = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static List<String> getEmpId() throws SQLException {
        String sql = "SELECT employee_id FROM employee";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        List<String> vanList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while(resultSet.next()) {
            vanList.add(resultSet.getString(1));
        }
        return vanList;
    }

    public static Employee searchByEmpId(String id) throws SQLException {
        String sql = "SELECT * FROM employee WHERE employee_id = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);
        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return new Employee(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getInt(5),
                    resultSet.getString(6)
            );
        }
        return null;
    }
}
