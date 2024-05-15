package lk.ijse.agency.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StockTm {
    private String itemCode;
    private String name;
    private int Qty;
    private double unitPrice;

}
