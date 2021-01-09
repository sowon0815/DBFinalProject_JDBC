import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Song {
	//column of song table
	public static String title;
	public static String album;
	public static String singer;
	public static String genre;
	public static int highestRanking;
	
	//static function that shows all the information of the song
	public static void printAllSong(Connection myConn, Statement myState, ResultSet myResSet) throws SQLException {
		String sql="";

		//get the number of song which is stored in the song table
		sql="SELECT count(*) FROM Song";
		myResSet=myState.executeQuery(sql);
		myResSet.next();
		int num=myResSet.getInt(1);
		//print out number of song
		System.out.println("("+num+" Songs)");
		//get all the information from the song table
		sql="SELECT * FROM Song";
		myResSet=myState.executeQuery(sql);
		
		System.out.println(String.format("Title %21s | Album %21s | Singer %21s | Genre %21s | HighestRanking %10s", "","","","",""));
		System.out.println(String.format("%146s", "").replace(' ', '-'));
		//print out the whole information of the song table
		while(myResSet.next()) {
			title=myResSet.getString("title");
			album=myResSet.getString("album");
			singer=myResSet.getString("singer");
			genre=myResSet.getString("genre");
			highestRanking=myResSet.getInt("highestRanking");
			System.out.println(String.format("title: %20s | album: %20s | singer: %20s | genre: %20s | highestRanking: %9s", title, album, singer, genre, highestRanking));
		}
		System.out.println();

	}
	//staic function that updates song's highest ranking
	public static void updateSongRanking(Connection myConn, Statement myState, ResultSet myResSet) throws SQLException {
		Song song=new Song();
		Scanner scanner=new Scanner(System.in);
		String sql="";
		//update the song's highest ranking with the input value
		sql="update Song set highestRanking=? where title=? and singer=?";
		PreparedStatement ps=myConn.prepareStatement(sql);
		//get the value of song title
		System.out.print("song title: ");
		song.title=scanner.nextLine();
		//get the value of singer
		System.out.print("singer: ");
		song.singer=scanner.nextLine();
		//get the value of highest ranking (change the table's highest ranking value into this)
		System.out.print("highest ranking: ");
		song.highestRanking=scanner.nextInt();
		scanner=new Scanner(System.in);
		ps.setInt(1,song.highestRanking);
		ps.setString(2, song.title);
		ps.setString(3, song.singer);
		//execute update
		ps.executeUpdate();
		
		//if the user want to see the changed song list, print all
		System.out.print("Do you want to see the song list?(y/n) ");
		String ans=scanner.nextLine();
		if (ans.equals("y")) song.printAllSong(myConn, myState, myResSet);
		System.out.println();
	}
	//delete song 
	public static void deleteSong(Connection myConn, Statement myState, ResultSet myResSet) throws SQLException {
		//make a song object and store the input data at it
		Song song=new Song();
		Scanner scanner=new Scanner(System.in);
		String sql="";
		
		//title that tue user want to delete
		System.out.print("song title: ");
		song.title=scanner.nextLine();
		//get an input about the singer who sings that song
		System.out.print("singer: ");
		song.singer=scanner.nextLine();
		
		//delete the song which has same value as the value of input value
		sql="delete from Song where title=? and singer=?";
		PreparedStatement ps=myConn.prepareStatement(sql);
		
		ps.setString(1, song.title);
		ps.setString(2, song.singer);
		ps.executeUpdate();
		
		//if the user want to see the changed song list, print all
		System.out.print("Do you want to see the song list?(y/n) ");
		String ans=scanner.nextLine();
		if (ans.equals("y")) song.printAllSong(myConn, myState, myResSet);
		System.out.println();
	}
	//print all the songs that is 00genre
	public static void searchGenre(Connection myConn, Statement myState, ResultSet myResSet) throws SQLException {
		//make an object to store all the input value
		Song song=new Song();
		String sql="";
		Scanner scanner=new Scanner(System.in);
		
		//get an input value from user about which genre's song does the user want to get
		System.out.print("Which genre do you want?(ballad/dance/R&B) ");
		song.genre=scanner.nextLine();
		//get the value of the song which has the genre of the input value
		sql="select singer, title from Song where genre=?";
		PreparedStatement ps=myConn.prepareStatement(sql);
		
		ps.setString(1, song.genre);
		myResSet=ps.executeQuery();
		
		System.out.println(String.format("Singer %21s | Title %21s","",""));
		System.out.println(String.format("%58s", "").replace(' ', '-'));
		
		//print all the value
		while(myResSet.next()) {
			song.singer=myResSet.getString("singer");
			song.title=myResSet.getString("title");
			System.out.println(String.format("singer: %20s | title: %20s", song.singer, song.title));
		}
		System.out.println();
	}
	//print all the album that the debut year of album's singer is equal to the input year value
	public static void seachYear(Connection myConn, Statement myState, ResultSet myResSet) throws SQLException {
		Scanner scanner=new Scanner(System.in);
		String sql="";
		//make a new song object and store input value here
		Song song=new Song();
		
		//get an input of debut year
		System.out.print("Which debut year do you want? ");
		int year=scanner.nextInt();
		//print the singer, album and release date of the album
		//if the debut year of song's singer is what the user want
		sql="select Song.singer, Song.album, Album.releaseDate " + 
				"from Song, Album " + 
				"where Song.album=Album.album " + 
				"and Song.singer=Album.singer " + 
				"and Song.singer in(select singer from Singer where debut=?);";
		PreparedStatement ps=myConn.prepareStatement(sql);
		
		ps.setInt(1, year);
		myResSet=ps.executeQuery();
		
		System.out.println(String.format("Singer %21s | Album %21s | ReleaseDate %21s","","",""));
		System.out.println(String.format("%93s", "").replace(' ', '-'));
		//print all the result value
		while(myResSet.next()) {
			song.singer=myResSet.getString("Song.singer");
			song.album=myResSet.getString("Song.album");
			String date=myResSet.getString("Album.releaseDate");
			System.out.println(String.format("singer: %20s | album: %20s | releaseDate %20s", song.singer, song.album, date));
		}
		System.out.println();


	}
}
