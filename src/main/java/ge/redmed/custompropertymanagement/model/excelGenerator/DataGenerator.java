package ge.redmed.custompropertymanagement.model.excelGenerator;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface
DataGenerator {

	Workbook generate(String[] header, List<Object[]> body);

    void setBodyStyle(CellStyle bodyStyle);
}
