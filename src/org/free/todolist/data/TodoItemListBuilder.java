package org.free.todolist.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.free.todolist.model.TodoItem;

public class TodoItemListBuilder {
	private List<TodoItem> list;
	public TodoItemListBuilder() {
		list = new LinkedList<TodoItem>();
		accessDataBase();
	}

	private void accessDataBase() {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager
					.getConnection("jdbc:sqlite:stodoitem");
			Statement stat = con.createStatement();
			String sql = "SELECT itemid, type, desc, timeout, period, note, status FROM stodoitem";
			ResultSet rs = stat.executeQuery(sql);
			parse(rs);
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<TodoItem> getTodoItems() {
		return list;
	}

	private void parse(ResultSet rs) {
		try {
			while (rs.next()) {
				TodoItem node = new TodoItem();
				node.setId(String.valueOf(rs.getInt("itemid")));
				node.setType(rs.getString("type"));
				node.setDesc(rs.getString("desc"));
				node.setTimeout(rs.getString("timeout"));
				node.setPeriod(rs.getString("period"));
				node.setNote(rs.getString("note"));
				node.setStatus(rs.getString("status"));
				list.add(node);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
