package org.free.todolist.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.free.todolist.manager.TaskService;
import org.free.todolist.model.TodoItem;
import org.free.todolist.plugin.Plugin;
import org.free.todolist.plugin.TodoPluginManager;

/**
 * This is the data-service of <code>sTodo</code>, it provides
 * operations on data such as insert, update, delete, and search
 * it a single instance in this <code>sTodo</code>application.
 * 
 * @author juntao.qiu@gmail.com
 *
 */
public class DataService {
	private String message;
	private boolean status;
	
	/*
	 * sTodo using the embedded database sqlite as inner
	 * data-set, DataService can provide data to other module
	 * of sTodo. 
	 */
	private DataService(){
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static DataService instance;
	
	public static DataService getInstance(){
		synchronized(DataService.class){
			if(instance == null){
				instance = new DataService();
			}
		}
		return instance;
	}
	
	/**
	 * insert new <code>TodoItem</code> in to database <code>stodoitem</code>
	 * 
	 * @param todo the entity of a todo-bean
	 * @return status of whether the operation is success or faild.
	 */
	public boolean addItem(TodoItem todo){
		String query = "INSERT INTO stodoitem (type, desc, timeout, period, note, status) VALUES (?, ?, ?, ?, ?, ?)";
		Connection con = null;
		PreparedStatement pstat = null;
		
		try {
			con = DriverManager.getConnection("jdbc:sqlite:stodoitem");
			pstat = con.prepareStatement(query);
			
			pstat.setString(1, todo.getType());
			pstat.setString(2, todo.getDesc());
			pstat.setString(3, todo.getTimeout());
			pstat.setString(4, todo.getPeriod());
			pstat.setString(5, todo.getNote());
			pstat.setString(6, todo.getStatus());
			
			pstat.execute();
			
			//get last item id of the todo
			ResultSet rs = pstat.getGeneratedKeys();
			while(rs.next()){
				int id = rs.getInt(1);
				todo.setId(String.valueOf(id));
			}

			pstat.close();
			con.close();
			status = true;
		} catch (SQLException e) {
			e.printStackTrace();
			message = e.getMessage();
			status = false;
		} finally {
			if(pstat != null){
				try{pstat.close();}catch(Exception e){}
			}
			if(con != null){
				try{con.close();}catch(Exception e){}
			}
		}
		
		return status;
	}
	
	/**
	 * remove <code>TodoItem</code> entity from database
	 * 
	 * @param todo todo-item entity
	 * @return
	 */
	public boolean removeItem(TodoItem todo){
		String query = "DELETE FROM stodoitem WHERE itemid="+todo.getId();
		Connection con = null;
		Statement stat = null;
		
		try{
			con = DriverManager.getConnection("jdbc:sqlite:stodoitem");
			stat = con.createStatement();

			stat.execute(query);

			stat.close();
			con.close();
			status = true;
		}catch(Exception e){
			e.printStackTrace();
			message = e.getMessage();
			status = false;
		} finally {
			if(stat != null){
				try{stat.close();}catch(Exception e){}
			}
			if(con != null){
				try{con.close();}catch(Exception e){}
			}
		}
		
		return status;
	}
	
	/**
	 * update all fields of a <code>todoitem</code> in database.
	 * @param todo
	 * @return
	 */
	public boolean updateItem(TodoItem todo){
		String query = "UPDATE stodoitem SET type=?, desc=?, timeout=?, period=?, note=?, status=? WHERE itemid="+todo.getId();
		Connection con = null;
		PreparedStatement pstat = null;
		try{
			con = DriverManager.getConnection("jdbc:sqlite:stodoitem");
			pstat = con.prepareStatement(query);
			pstat.setString(1, todo.getType());
			pstat.setString(2, todo.getDesc());
			pstat.setString(3, todo.getTimeout());
			pstat.setString(4, todo.getPeriod());
			pstat.setString(5, todo.getNote());
			pstat.setString(6, todo.getStatus());
			
			pstat.execute();

			pstat.close();
			con.close();
			status = true;
		}catch(Exception e){
			e.printStackTrace();
			message = e.getMessage();
			status = false;
		} finally {
			if(pstat != null){
				try{pstat.close();}catch(Exception e){}
			}
			if(con != null){
				try{con.close();}catch(Exception e){}
			}
		}
		
		return status;
	}
	
	/**
	 * search the database, by <code>by</code> (which is the column name in database),
	 * and value, the real text like 123, tomorrow, etc.
	 * 
	 * @param by the column in database
	 * @param value the value of the column
	 * @return
	 */
	public List<TodoItem> searchList(String by, String value){
		List<TodoItem> list = new LinkedList<TodoItem>();
		String query = 
			"SELECT itemid, type, desc, timeout, period, status, note FROM stodoitem WHERE "+
			by+
			" LIKE \""+
			value+
			"\"";
		Connection con = null;
		Statement stat = null;
		try{
			con = DriverManager.getConnection("jdbc:sqlite:stodoitem");
			stat = con.createStatement();
			
			ResultSet rs = stat.executeQuery(query);
			while(rs.next()){
				TodoItem item = new TodoItem();
				item.setId(String.valueOf(rs.getInt("itemid")));
				item.setDesc(rs.getString("desc"));
				item.setType(rs.getString("type"));
				item.setTimeout(rs.getString("timeout"));
				item.setPeriod(rs.getString("period"));
				item.setStatus(rs.getString("status"));
				item.setNote(rs.getString("note"));
				
				list.add(item);
			}
			stat.close();
			con.close();
			status = true;
		}catch(Exception e){
			e.printStackTrace();
			message = e.getMessage();
			status = false;
		} finally {
			if(stat != null){
				try{stat.close();}catch(Exception e){}
			}
			if(con != null){
				try{con.close();}catch(Exception e){}
			}
		}
		
		return list;
	}
	
	/**
	 * get all items from database, used for init the UI.
	 * @return
	 */
	public List<TodoItem> getAllItems(){
		List<TodoItem> list = new LinkedList<TodoItem>();
		String sql = "SELECT itemid, type, desc, timeout, period, note, status FROM stodoitem";
		Plugin plUtil = TodoPluginManager.getInstance().getPlugin("util");
		
		String date = null;
		Statement stat = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:sqlite:stodoitem");
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				TodoItem node = new TodoItem();
				node.setId(String.valueOf(rs.getInt("itemid")));
				node.setType(rs.getString("type"));
				node.setDesc(rs.getString("desc"));
				
				date = (String)plUtil.execute("parseTimeout", rs.getString("timeout"));
				node.setTimeout(date);
				
				node.setPeriod(rs.getString("period"));
				node.setNote(rs.getString("note"));
				node.setStatus(rs.getString("status"));
				list.add(node);
			}
			stat.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(stat != null){
				try{stat.close();}catch(Exception e){}
			}
			if(con != null){
				try{con.close();}catch(Exception e){}
			}
		}
		
		return list;
	}
	
	/**
	 * get error message
	 * @return null if no error, the error message otherwise
	 */
	public String getMessage(){
		return message;
	}
}
