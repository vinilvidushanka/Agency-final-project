package lk.ijse.agency.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class LoadingDetailTm {
    private String vanId;
    private String repoId;
    private String itemCode;
    private int qty;
}
