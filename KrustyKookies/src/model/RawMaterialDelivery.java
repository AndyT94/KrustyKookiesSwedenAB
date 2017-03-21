package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RawMaterialDelivery {
	public String material;
	public double amount;
	public String date;
	
	public RawMaterialDelivery(ResultSet rs) throws SQLException {
		this.material = rs.getString("material_name");
		this.amount = rs.getFloat("delivery_amount");
		this.date = rs.getString("delivery_date");
	}
}
