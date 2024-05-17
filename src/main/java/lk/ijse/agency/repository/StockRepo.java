package lk.ijse.agency.repository;

import lk.ijse.agency.db.DbConnection;
import lk.ijse.agency.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StockRepo {
    public static boolean save(Stock stock) throws SQLException {

        String sql = "INSERT INTO stock VALUES(?, ?, ?, ?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, stock.getItemCode());
        pstm.setObject(2, stock.getName());
        pstm.setObject(3, stock.getQty());
        pstm.setObject(4, stock.getUnitPrice());


        return pstm.executeUpdate() > 0;

        /*int affectedRows = pstm.executeUpdate();
        if (affectedRows > 0) {
            return true;
        } else {
            return false;
        }*/
    }

    public static boolean update(Stock stock) throws SQLException {
        String sql = "UPDATE stock SET name = ?, price = ?, qty = ? WHERE code = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, stock.getItemCode());
        pstm.setObject(2, stock.getName());
        pstm.setObject(3, stock.getQty());
        pstm.setObject(4, stock.getUnitPrice());


        return pstm.executeUpdate() > 0;
    }

    public static Stock searchById(String itemCode) throws SQLException {
        String sql = "SELECT * FROM stock WHERE code = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, itemCode);
        ResultSet resultSet = pstm.executeQuery();

        Stock stock = null;

        if (resultSet.next()) {
            String item_code = resultSet.getString(1);
            String name = resultSet.getString(2);
            Integer qty = resultSet.getInt(3);
            Double unit_price = resultSet.getDouble(4);


            stock = new Stock(item_code, name,qty, unit_price);
        }
        return stock;
    }



    public static List<Stock> getAll() throws SQLException {
        String sql = "SELECT * FROM stock";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Stock> stockList = new ArrayList<>();
        while (resultSet.next()) {
            String item_code = resultSet.getString(1);
            String name = resultSet.getString(2);
            Integer qty = resultSet.getInt(3);
            Double unit_price = resultSet.getDouble(4);


            Stock stock = new Stock(item_code, name, qty, unit_price);
            stockList.add(stock);
        }
        return stockList;
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT item_code FROM stock";

        Connection connection = DbConnection.getInstance().getConnection();
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        List<String> idList = new ArrayList<>();

        while (resultSet.next()) {
            idList.add(resultSet.getString(1));
        }
        return idList;
    }

    public static Stock searchByItemCode(String itemCode) throws SQLException {
        String sql = "SELECT * FROM stock WHERE code = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);
        pstm.setObject(1, itemCode);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return new Stock(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4)

                    );
        }
        return null;
    }





    public static boolean delete(String itemCode) throws SQLException {
        String sql = "DELETE FROM stock WHERE code = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, itemCode);

        return pstm.executeUpdate() > 0;
    }

    public static boolean updateQty(List<OrderDetail> odList) throws SQLException {
        for (OrderDetail od : odList) {
            if(!updateQty(od))  {
                return false;
            }
        }
        return true;
    }
    private static boolean updateQty(OrderDetail od) throws SQLException {
        System.out.println("stock save");
        String sql = "UPDATE stock SET qty = qty + ? WHERE item_code = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setInt(1, od.getQty());
        pstm.setString(2, od.getItemCode());

        return pstm.executeUpdate() > 0;
    }

    public static boolean updateLoadingQty(List<LoadingDetail> ldList) throws SQLException {
        for (LoadingDetail ld : ldList) {
            if(!updateLoadingQty(ld))  {
                return false;
            }
        }
        return true;
    }
    static boolean updateLoadingQty(LoadingDetail ld) throws SQLException {
        String sql = "UPDATE stock SET qty = qty - ? WHERE code = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);
        System.out.println("update qty?");
        pstm.setInt(1, ld.getQty());
        pstm.setString(2, ld.getItemCode());

        System.out.println("update qty!!!");
        return pstm.executeUpdate() > 0;
    }


    public static boolean updateSaleQty(SalesReport ps) throws SQLException {
        String sql = "UPDATE stock SET qty = qty - ? WHERE code = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);
        System.out.println("update qty?");
        pstm.setInt(1, ps.getQty()+ps.getFreeQty());
        pstm.setString(2, ps.getItemCode());

        System.out.println("update qty!!!");
        return pstm.executeUpdate() > 0;
    }
}
