package lk.ijse.agency.repository;

import lk.ijse.agency.db.DbConnection;
import lk.ijse.agency.model.Customer;
import lk.ijse.agency.model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepo {
    public static boolean save(Customer customer) throws SQLException {
//        In here you can now save your customer
        String sql = "INSERT INTO customer VALUES(?, ?, ?, ?,?,?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, customer.getId());
        pstm.setObject(2, customer.getName());
        pstm.setObject(3, customer.getShopName());
        pstm.setObject(4, customer.getContact());
        pstm.setObject(5, customer.getAddress());
        pstm.setObject(6, customer.getRouteId());

        return pstm.executeUpdate() > 0;


    }

    public static boolean update(Customer customer) throws SQLException {
        String sql = "UPDATE customer SET name = ?, shop_name = ?, contact = ?, address = ?, route_id = ? WHERE cus_id = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, customer.getId());
        pstm.setObject(2, customer.getName());
        pstm.setObject(3, customer.getShopName());
        pstm.setObject(4, customer.getContact());
        pstm.setObject(5, customer.getAddress());
        pstm.setObject(6, customer.getRouteId());

        return pstm.executeUpdate() > 0;
    }

    public static Customer searchById(String id) throws SQLException {
        String sql = "SELECT * FROM customer WHERE cus_id = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, id);
        ResultSet resultSet = pstm.executeQuery();

        Customer customer = null;

        if (resultSet.next()) {
            String cus_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String shop_name = resultSet.getString(3);
            String contact = resultSet.getString(4);
            String address = resultSet.getString(5);
            String route_id = resultSet.getString(6);

            customer = new Customer(cus_id, name, shop_name,contact,address,route_id);
        }
        return customer;
    }



    public static List<Customer> getAll() throws SQLException {
        String sql = "SELECT * FROM customer";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Customer> customersList = new ArrayList<>();
        while (resultSet.next()) {
            String cus_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String shop_name = resultSet.getString(3);
            String contact = resultSet.getString(4);
            String address = resultSet.getString(5);
            String route_id = resultSet.getString(6);

            Customer customer = new Customer(cus_id, name, shop_name, contact, address, route_id);
            customersList.add(customer);
        }
        return customersList;
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT id FROM customers";

        Connection connection = DbConnection.getInstance().getConnection();
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        List<String> idList = new ArrayList<>();

        while (resultSet.next()) {
            idList.add(resultSet.getString(1));
        }
        return idList;
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM customer WHERE cus_id = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static List<String> getCusId() throws SQLException {
        String sql = "SELECT cus_id FROM customer";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        List<String> vanList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while(resultSet.next()) {
            vanList.add(resultSet.getString(1));
        }
        return vanList;
    }

    public static Customer searchByCusId(String id) throws SQLException {
        String sql = "SELECT * FROM customer WHERE cus_id = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);
        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            );
        }
        return null;
    }
}
