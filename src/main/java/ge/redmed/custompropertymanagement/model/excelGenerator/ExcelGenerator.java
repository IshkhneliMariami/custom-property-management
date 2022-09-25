package ge.redmed.custompropertymanagement.model.excelGenerator;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class ExcelGenerator implements DataGenerator {

	private String sheetName;
	private Integer columnSize;
	private Integer startRow = 0;
	private Integer startColumn = 0;
	private final Sheet sheet;

	private CellStyle headerStyle;
	private CellStyle[] headerStyles;
	private CellStyle bodyStyle;
	private CellStyle[] bodyStyles;

	private final Workbook workbook;

	private final CellStyle csDate;
	private final CellStyle csDateTime;

	public ExcelGenerator(SXSSFWorkbook workbook, SXSSFSheet sheet) {
		sheet.trackAllColumnsForAutoSizing();
		this.workbook = workbook;
		this.sheet = sheet;
		XSSFDataFormat df = (XSSFDataFormat) this.workbook.createDataFormat();
		this.csDate = this.workbook.createCellStyle();
		this.csDateTime = this.workbook.createCellStyle();
		this.csDate.setDataFormat(df.getFormat("dd.MM.yyyy"));
		this.csDateTime.setDataFormat(df.getFormat("dd.MM.yyyy HH:mm"));
	}

	public ExcelGenerator(SXSSFWorkbook workbook) {
		this(workbook, workbook.createSheet());
	}

	public ExcelGenerator() {
		this(new SXSSFWorkbook());
		this.headerStyle = this.workbook.createCellStyle();
		this.headerStyle.setAlignment(HorizontalAlignment.CENTER);
		Font font = workbook.createFont();
		font.setBold(true);
		this.headerStyle.setFont(font);
	}


	@Override
	public Workbook generate(String[] header, List<Object[]> body) {

		if (columnSize == null && header != null) {
			columnSize = header.length;
		} else {
			this.columnSize = body.size();
		}

		int rowNum = startRow;

		if (header != null && header.length > 0) {
			if (getHeaderStyles() == null && headerStyle != null) {
				headerStyles = new CellStyle[header.length];
                headerStyle.setWrapText(true);
				for (int i = 0; i < header.length; i++) {
					headerStyles[i] = headerStyle;
				}
			}
			newRow(rowNum++, header, headerStyles, null);
			this.sheet.createFreezePane(0, this.startRow + 1);
			this.sheet.setAutoFilter(new CellRangeAddress(this.startRow, this.startRow + 1, this.startColumn,
														  this.startColumn + columnSize - 1));
		}

		if (bodyStyles == null && bodyStyle != null) {
			bodyStyles = new CellStyle[this.columnSize];
			for (int i = 0; i < this.columnSize; i++) {
				bodyStyles[i] = bodyStyle;
			}
		}
		for (Object[] b : body) {
			newRow(rowNum++, b, bodyStyles, null);
		}

		if (header != null) {
			for (int i = 0; i < header.length; i++) {
				sheet.autoSizeColumn(i);
			}
		}

		return workbook;
	}

	private Row newRow(int rowInd, Object[] vals, CellStyle[] cellStyles, CellStyle rowStyle) {
		Row row = sheet.createRow(rowInd);
		if (rowStyle != null) {
			row.setRowStyle(rowStyle);
		}
		for (int i = 0; i < vals.length; i++) {
			newCell(row, i, cellStyles != null ? cellStyles[i] : null, vals[i]);
		}
		return row;
	}

	private Cell newCell(Row row, int column, CellStyle cellStyle, Object value) {
		Cell cell = row.createCell(startColumn + column);
		if (cellStyle != null) {
			cell.setCellStyle(cellStyle);
		}
		if (value != null) {
			if (value instanceof java.sql.Date) {
				cell.setCellValue((java.sql.Date) value);
				cell.setCellStyle(csDate);
			} else if (value instanceof Timestamp) {
				cell.setCellValue((Timestamp) value);
				cell.setCellStyle(csDateTime);
			} else if (value instanceof Date) {
				cell.setCellValue(new Timestamp(((Date) value).getTime()));
				cell.setCellStyle(csDateTime);
			} else if (value instanceof Double) {
				cell.setCellValue((Double) value);
			} else if (value instanceof Integer) {
				cell.setCellValue((Integer) value);
			} else if (value instanceof BigDecimal) {
				cell.setCellValue(Double.parseDouble(value.toString()));
			} else if (value instanceof BigInteger) {
				cell.setCellValue(Integer.parseInt(value.toString()));
			} else {
				cell.setCellValue(value.toString());
			}
		}
		return cell;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public Integer getStartRow() {
		return startRow;
	}

	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}

	public Integer getStartColumn() {
		return startColumn;
	}

	public void setStartColumn(Integer startColumn) {
		this.startColumn = startColumn;
	}

	public CellStyle getHeaderStyle() {
		return headerStyle;
	}

	public void setHeaderStyle(CellStyle headerStyle) {
		this.headerStyle = headerStyle;
	}

	public CellStyle getBodyStyle() {
		return bodyStyle;
	}

	public void setBodyStyle(CellStyle bodyStyle) {
		this.bodyStyle = bodyStyle;
	}

	public CellStyle[] getBodyStyles() {
		return bodyStyles;
	}

	public void setBodyStyles(CellStyle[] bodyStyles) {
		this.bodyStyles = bodyStyles;
	}

	public CellStyle[] getHeaderStyles() {
		return headerStyles;
	}

	public void setHeaderStyles(CellStyle[] headerStyles) {
		this.headerStyles = headerStyles;
	}
}
