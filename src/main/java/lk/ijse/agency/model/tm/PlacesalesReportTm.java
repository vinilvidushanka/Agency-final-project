package lk.ijse.agency.model.tm;

import lk.ijse.agency.model.SalesReport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlacesalesReportTm {
    private SalesReport sales;
    private List<SalesReport> slList;
}
