package lk.ijse.agency.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class LoadingDetail {
    private String vanId;
    private String repoId;
    private String itemCode;
    private int qty;

}
