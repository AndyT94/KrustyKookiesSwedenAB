package model;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Database is a class that specifies the interface to the movie database. Uses
 * JDBC.
 */
public class Database {
	public static final String[] FACTORY = { "Deep-freeze storage", "Ramp" };
	public static final int DEEP_FREEZE = 0;
	public static final int RAMP = 1;

	/**
	 * The database connection.
	 */
	private Connection conn;

	/**
	 * Create the database interface object. Connection to the database is
	 * performed later.
	 */
	public Database() {
		conn = null;
	}

	/**
	 * Open a connection to the database, using the specified user name and
	 * password.
	 */
	public boolean openConnection(String filename) {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + filename);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Close the connection to the database.
	 */
	public void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Check if the connection to the database has been established
	 * 
	 * @return true if the connection has been established
	 */
	public boolean isConnected() {
		return conn != null;
	}

	public List<RawMaterial> getRawMaterials() {
		LinkedList<RawMaterial> materials = new LinkedList<RawMaterial>();
		try {
			String sql = "SELECT material_name, material_amount, unit FROM RawMaterials ORDER BY material_name";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				materials.add(new RawMaterial(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return materials;
	}

	public Pallet getPallet(String pallet_id) {
		try {
			String sql = "SELECT * FROM pallets WHERE pallet_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, pallet_id);
			ResultSet rs = ps.executeQuery();
			return new Pallet(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<RawMaterialDelivery> getRawMaterialsDeliveries() {
		LinkedList<RawMaterialDelivery> deliveries = new LinkedList<RawMaterialDelivery>();
		try {
			String sql = "SELECT delivery_date, material_name, delivery_amount FROM RawDeliveries ORDER BY delivery_date DESC";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				deliveries.add(new RawMaterialDelivery(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return deliveries;
	}

	public ArrayList<Pallet> getPallets() {
		ArrayList<Pallet> list = new ArrayList<Pallet>();
		try {
			String sql = "SELECT * FROM pallets";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Pallet(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	private boolean hasRawMaterial(String material) {
		try {
			String sql = "SELECT * FROM RawMaterials WHERE material_name = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, material);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void addDelivery(String date, String material, String amount) throws DatabaseException {
		if (date.isEmpty() || material.isEmpty() || amount.isEmpty()) {
			throw new DatabaseException("Please fill in all fields!");
		}
		if (!isDate(date)) {
			throw new DatabaseException("Invalid date (Format: yyyy-MM-dd HH:mm:ss)!");
		}
		try {
			double delivAmount = Double.parseDouble(amount);
			if (delivAmount <= 0) {
				throw new DatabaseException("Amount delivered must be > 0!");
			}
		} catch (NumberFormatException ne) {
			throw new DatabaseException("Amount must be a number!");
		}

		try {
			conn.setAutoCommit(false);

			if (!hasRawMaterial(material)) {
				conn.setAutoCommit(true);
				throw new DatabaseException("No such raw material!");
			}

			String sql = "INSERT INTO RawDeliveries (delivery_date, material_name, delivery_amount) VALUES (?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, date);
			ps.setString(2, material);
			ps.setString(3, amount);
			ps.executeUpdate();

			sql = "SELECT material_amount FROM RawMaterials WHERE material_name = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, material);
			ResultSet rs = ps.executeQuery();
			double currAmount = rs.getFloat("material_amount");
			Double newAmount = currAmount + Double.parseDouble(amount);

			sql = "UPDATE RawMaterials SET material_amount = ? WHERE material_name = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, newAmount.toString());
			ps.setString(2, material);
			ps.executeUpdate();

			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean isDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setLenient(false);
		try {
			format.parse(date);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	public ArrayList<Pallet> getAllBlockedPallets() {
		ArrayList<Pallet> list = new ArrayList<Pallet>();
		try {
			String sql = "SELECT * FROM pallets WHERE blocked = 1";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Pallet(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Order> getOrders() {
		List<Order> orders = new LinkedList<Order>();
		try {
			String sql = "SELECT o.order_id, recipe_name, amount, customer_name, delivery_by_date FROM Orders o JOIN AmountOrdered a ON o.order_id = a.order_id ORDER BY o.order_id";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				orders.add(new Order(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}

	public List<Shipment> getShipments() {
		List<Shipment> shipments = new LinkedList<Shipment>();
		try {
			String sql = "SELECT * FROM Shipments ORDER BY date_of_delivery DESC";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				shipments.add(new Shipment(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return shipments;
	}

	public void addOrder(String customer, String order, String date) throws DatabaseException {
		if (date.isEmpty() || customer.isEmpty() || order.isEmpty()) {
			throw new DatabaseException("Please fill in all fields!");
		}
		if (!isDate(date)) {
			throw new DatabaseException("Invalid date (Format: yyyy-MM-dd HH:mm:ss)!");
		}

		try {
			conn.setAutoCommit(false);

			if (!hasCustomer(customer)) {
				conn.setAutoCommit(true);
				throw new DatabaseException("No such customer!");
			}

			Map<String, String> cookies = null;
			String[] amount = order.split(",");
			cookies = new HashMap<String, String>();
			for (int i = 0; i < amount.length; i++) {
				String[] data = amount[i].trim().split("\\s");
				String nbr = data[0];
				String recipe = "";
				for (int j = 1; j < data.length; j++) {
					recipe += data[j] + " ";
				}
				cookies.put(recipe.trim(), nbr);
			}
			for (String key : cookies.keySet()) {
				if (!hasRecipe(key)) {
					conn.setAutoCommit(true);
					throw new DatabaseException("No such recipe!");
				}
			}

			String sql = "INSERT INTO Orders (customer_name, delivery_by_date) VALUES (?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, customer);
			ps.setString(2, date);
			ps.executeUpdate();

			sql = "SELECT order_id FROM Orders WHERE customer_name = ? AND delivery_by_date = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, customer);
			ps.setString(2, date);
			ResultSet rs = ps.executeQuery();
			int id = rs.getInt("order_id");

			for (String key : cookies.keySet()) {
				sql = "INSERT INTO AmountOrdered (order_id, recipe_name, amount) VALUES (?, ?, ?)";
				ps = conn.prepareStatement(sql);
				ps.setString(1, Integer.toString(id));
				ps.setString(2, key);
				ps.setString(3, cookies.get(key));
				ps.executeUpdate();
			}

			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean hasCustomer(String customer) {
		try {
			String sql = "SELECT * FROM Customers WHERE customer_name = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, customer);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean hasRecipe(String recipe) {
		try {
			String sql = "SELECT * FROM Recipes WHERE recipe_name = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, recipe);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	public void insertShipment(String order, String pallet, String delivery) throws DatabaseException {
		if (order.isEmpty() || pallet.isEmpty() || delivery.isEmpty()) {
			throw new DatabaseException("Please fill in all fields!");
		}
		if (!isDate(delivery)) {
			throw new DatabaseException("Invalid date (Format: yyyy-MM-dd HH:mm:ss)!");
		}

		try {
			conn.setAutoCommit(false);

			
			if (!hasPallet(pallet)) {
				conn.setAutoCommit(true);
				throw new DatabaseException("No such pallet id!");
			}
			if (!hasOrder(order)) {
				conn.setAutoCommit(true);
				throw new DatabaseException("No such order id!");
			}
			if (isBlocked(pallet)) {
				conn.setAutoCommit(true);
				throw new DatabaseException("The pallet has been blocked!");
			}
			if (isShippedPallet(pallet)) {
				conn.setAutoCommit(true);
				throw new DatabaseException("Pallet already shipped!");
			}

			String sql = "INSERT INTO Shipments (order_id, pallet_id, date_of_delivery) VALUES (?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, order);
			ps.setString(2, pallet);
			ps.setString(3, delivery);
			ps.executeUpdate();

			sql = "UPDATE Pallets SET location = (SELECT customer_name FROM Orders WHERE order_id = ?) WHERE pallet_id = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, order);
			ps.setString(2, pallet);
			ps.executeUpdate();

			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean isShippedPallet(String pallet_id) {
		try {
			String sql = "SELECT * FROM Shipments WHERE pallet_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, pallet_id);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean hasPallet(String pallet_id) {
		try {
			String sql = "SELECT * FROM Pallets WHERE pallet_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, pallet_id);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean hasOrder(String order_id) {
		try {
			String sql = "SELECT * FROM Orders WHERE order_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, order_id);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean isBlocked(String pallet_id) {
		try {
			String sql = "SELECT pallet_id FROM Pallets WHERE pallet_id = ? AND blocked = 1";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, pallet_id);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<Order> getOrders(String from, String to) throws DatabaseException {
		if (from.isEmpty() || to.isEmpty()) {
			throw new DatabaseException("Please fill in all fields!");
		}
		if (!isDate(from) || !isDate(to)) {
			throw new DatabaseException("Invalid date (Format: yyyy-MM-dd HH:mm:ss)!");
		}

		List<Order> orders = new LinkedList<Order>();
		try {
			String sql = "SELECT o.order_id, recipe_name, amount, customer_name, delivery_by_date FROM Orders o JOIN AmountOrdered a ON o.order_id = a.order_id WHERE delivery_by_date >= ? AND delivery_by_date <= ? ORDER BY o.order_id";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, from);
			ps.setString(2, to);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				orders.add(new Order(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}

	public List<Pallet> getPalletsInProduction() {
		List<Pallet> list = new LinkedList<Pallet>();
		try {
			String sql = "SELECT * FROM pallets WHERE location <> ? AND location NOT IN (SELECT customer_name FROM Customers) AND blocked = 0";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, FACTORY[RAMP]);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Pallet(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	

	public List<Pallet> searchRecipe(String recipe) {
		List<Pallet> list = new LinkedList<Pallet>();
		try {
			String sql = "SELECT * FROM pallets WHERE recipe_name = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, recipe);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Pallet(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;

	}

	public List<Pallet> searchPalletId(String pallet_id) {
		List<Pallet> list = new LinkedList<Pallet>();
		try {
			String sql = "SELECT * FROM pallets WHERE pallet_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, pallet_id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Pallet(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
		
	}

	public void createPallet(String recipe) throws DatabaseException {
		if (recipe.isEmpty()) {
			throw new DatabaseException("Please fill in all fields!");
		}
		recipe = recipe.trim();
		if (!hasRecipe(recipe)) {
			throw new DatabaseException("No such recipe!");
		}

		try {
			conn.setAutoCommit(false);
			List<Ingredient> ingredients = new LinkedList<Ingredient>();

			String sql = "SELECT material_name, quantity FROM Ingredients WHERE recipe_name = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, recipe);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ingredients.add(new Ingredient(rs));
			}

			if (!hasEnoughMaterial(ingredients)) {
				conn.setAutoCommit(true);
				throw new DatabaseException("Not enough raw material!");
			}

			insertPallet(recipe);
			useIngredients(ingredients);

			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void useIngredients(List<Ingredient> ingredients) throws SQLException {
		for (Ingredient i : ingredients) {
			String sql = "SELECT material_amount FROM RawMaterials WHERE material_name = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, i.material);
			ResultSet rs = ps.executeQuery();

			Double inStorage = rs.getDouble("material_amount");
			double required = 54 * i.quantity;
			double newAmount = inStorage - required;

			sql = "UPDATE RawMaterials SET material_amount = ? WHERE material_name = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, Double.toString(newAmount));
			ps.setString(2, i.material);
			ps.executeUpdate();
		}
	}

	private void insertPallet(String recipe) throws SQLException {
		String sql = "INSERT INTO Pallets (location, production_date, blocked, recipe_name) VALUES (?, ?, ?, ?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, FACTORY[DEEP_FREEZE]);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		ps.setString(2, format.format(date));
		ps.setString(3, "0");
		ps.setString(4, recipe);
		ps.executeUpdate();
	}

	// 5400 cookies per pallet
	private boolean hasEnoughMaterial(List<Ingredient> ingredients) throws SQLException {
		for (Ingredient i : ingredients) {
			String sql = "SELECT material_amount FROM RawMaterials WHERE material_name = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, i.material);
			ResultSet rs = ps.executeQuery();
			Double inStorage = rs.getDouble("material_amount");
			double required = 54 * i.quantity;

			if (required > inStorage) {
				return false;
			}
		}
		return true;
	}

	public void palletToRamp(String pallet_id) {
		try {
			conn.setAutoCommit(false);

			String sql = "UPDATE Pallets SET location = ? WHERE pallet_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, FACTORY[RAMP]);
			ps.setString(2, pallet_id);
			ps.executeUpdate();

			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void blockPallet(String pallet_id) {
		try {
			conn.setAutoCommit(false);

			String sql = "UPDATE Pallets SET blocked = ? WHERE pallet_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, "1");
			ps.setString(2, pallet_id);
			ps.executeUpdate();

			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Pallet> searchDate(String from, String to) {
		// TODO Auto-generated method stub
		return null;
	}

}