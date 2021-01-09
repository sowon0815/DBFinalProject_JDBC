import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Singer {
	//column of singer table
	public static String singer;
	public static int debut;
	public static String latestAlbum;
	
	//static function that shows all the information of the singer
	public static void printAllSinger(Connection myConn, Statement myState, ResultSet myResSet) throws SQLException {
		String sql="";
		//get the number of singer which is stored in the database
		sql="SELECT count(*) FROM Singer";
		myResSet=myState.executeQuery(sql);
		myResSet.next();
		int num=myResSet.getInt(1);
		//print out the number of singer
		System.out.println("("+num+" Singers)");
		//get all the value of singer
		sql="SELECT * FROM Singer";
		myResSet=myState.executeQuery(sql);
		
		System.out.println(String.format("Singer %21s | debutYear %10s | latestAlbum %21s", "","",""));
		System.out.println(String.format("%87s", "").replace(' ', '-'));
		//print out the whole information of the singer table
		while(myResSet.next()) {
			singer=myResSet.getString("singer");
			debut=myResSet.getInt("debut");
			latestAlbum=myResSet.getString("latestAlbum");
			
			System.out.println(String.format("singer: %20s | debutYear: %9s | latestAlbum: %20s", singer, debut, latestAlbum));
		}
		System.out.println();
	}
	//insert a new singer into singer table
	public static void insertNewSinger(Connection myConn, Statement myState, ResultSet myResSet) throws SQLException {
		Scanner scanner=new Scanner(System.in);
		String sql="";
		
		//insert a new singer into the singer table
		//we are going to get the value from the user
		sql="insert into Singer(singer, debut, latestAlbum) values(?,?,?)";
		PreparedStatement ps=myConn.prepareStatement(sql);
		Singer singer=new Singer(); //make a new object to store the input value
		//get an input of the new singer name
		System.out.print("singer name: ");
		singer.singer=scanner.nextLine();
		//get an input of the singer's debut year
		System.out.print("debut year: ");
		singer.debut=scanner.nextInt();
		//get an input of the singer's latest album (but it's optional)
		scanner=new Scanner(System.in);
		System.out.print("Do you know their latest album?(y/n) ");
		String ans=scanner.nextLine();

		if (ans.equals("y")) {//if user wants to insert the value of the latest album
			System.out.print("lattest album: ");
			singer.latestAlbum=scanner.nextLine();
			ps.setString(1, Singer.singer);
			ps.setInt(2, singer.debut);
			ps.setString(3, singer.latestAlbum);
		}
		else {//if user don't want know the value of the latest album
			//fill this with null value
			ps.setString(1, Singer.singer);
			ps.setInt(2, singer.debut);
			ps.setString(3, null);
		}
		ps.executeUpdate();
		
		//if user wants to get the value of changed singer table
		//print all
		System.out.print("Do you want to see the singer list?(y/n) ");
		ans=scanner.nextLine();
		if (ans.equals("y")) singer.printAllSinger(myConn, myState, myResSet);
		System.out.println();

	}

}
