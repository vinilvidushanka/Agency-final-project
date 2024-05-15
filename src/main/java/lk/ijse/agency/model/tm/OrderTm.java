package lk.ijse.agency.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class OrderTm {
    private String orderId;
    private String itemCode;
    private String itemName;
    private int qty;
    private String date;
}
