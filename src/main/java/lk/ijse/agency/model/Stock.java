package lk.ijse.agency.model;

import  lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Stock {
    private String itemCode;
    private String name;
    private int Qty;
    private double unitPrice;

}
