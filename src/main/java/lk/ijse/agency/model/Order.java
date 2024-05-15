package lk.ijse.agency.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data

public class Order{
    private String orderId;
    private String itemCode;
    private String itemName;
    private int qty;
    private String date;
}
