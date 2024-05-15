package lk.ijse.agency.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Loading {
    private String itemCode;
    private String itemName;
    private int qty;
    private String vanId;
    private String repoId;
    private String date;
}
