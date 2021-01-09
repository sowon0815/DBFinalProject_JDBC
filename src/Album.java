import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Album {
	//column of Album table
	public static String album;
	public static String singer;
	public static String releaseDate;
	
	//static function that shows all the information of the album
	static void printAllAlbum(Connection myConn, Statement myState, ResultSet myResSet) throws SQLException {
		String sql="";
		
		//get the number of album which is stored in the database
		sql="SELECT count(*) FROM album";
		myResSet=myState.executeQuery(sql);
		myResSet.next();
		int num=myResSet.getInt(1);
		//print out the number of album
		System.out.println("("+num+" albums)");
		//get all the information of the albums
		sql="SELECT * FROM album";
		myResSet=myState.executeQuery(sql);
		
		System.out.println(String.format("Album %21s | Singer %21s | ReleaseDate %15s", "","",""));
		System.out.println(String.format("%89s", "").replace(' ', '-'));
		//print out the whole information about the album
		while(myResSet.next()) {
			album=myResSet.getString("album");
			singer=myResSet.getString("singer");
			releaseDate=myResSet.getString("releaseDate");
			
			System.out.println(String.format("album: %20s | singer: %20s | releaseDate: %15s", album, singer, releaseDate));
		}
		System.out.println();
	}
	

}
