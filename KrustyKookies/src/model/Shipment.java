package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Shipment {
	public int order_id;
	public int pallet_id;
	public String date_of_delivery;
	
	public Shipment(ResultSet rs)throws SQLException {
		this.order_id = rs.getInt("order_id");
		this.pallet_id = rs.getInt("pallet_id");
		this.date_of_delivery = rs.getString("delivery_by_date");
		
	}

}
