package MohawkWebserverJava.MohawkWebserverJava;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WorklistItem {
	public int row, column;
	@JsonProperty("tube_barcode")
	public String tubeBarcode;
	@JsonProperty("rack_barcode")
	public String rackBarcode;
	
	public WorklistItem(int row, int column, String tubeBarcode, String rackBarcode) {
		this.row = row;
		this.column = column;
		this.tubeBarcode = tubeBarcode;
		this.rackBarcode = rackBarcode;
	}
}
