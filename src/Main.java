import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner=new Scanner(System.in);
		//make the object of Album, Singer, Song class
		Album album=new Album();
		Singer singer=new Singer();
		Song song=new Song();
		
		//variables needed for connection
		String userID="dbuser";
		String userPW="dbpwd";
		String dbName="dbprj";
		String url="jdbc:mysql://localhost:3306/"+dbName+"?&serverTimezone=UTC";
		
		Connection myConn=null;
		Statement myState=null;
		ResultSet myResSet=null;
		
		String sql="";

		
		try {
			//connect with jdbc
			myConn=DriverManager.getConnection(url,userID, userPW);
			myState=myConn.createStatement();
			
			//variables that gets an input from an user about the menu
			int op;
			while(true) {//do it repeatedly until the input value is 6
				scanner=new Scanner(System.in);
				//print the menu
				System.out.println("==============================================================");
				System.out.println("1.print information 2.insert a new singer 3.update information");
				System.out.println("4.delete a song 5.search 6.exit");
				System.out.print(">>>");
				op=scanner.nextInt();
				
				if (op==1) {//print the information
					//print every information, or only one of album, singer, song tables
					System.out.println("(1)print everything (2)print all albums (3)print all singers (4)print all songs");
					System.out.print(">>>");
					//get an input from user 1~4
					int subop=scanner.nextInt();
					System.out.println();
					if (subop==1) {//show all tables to user
						album.printAllAlbum(myConn, myState, myResSet); //static function that prints every information about album 
						singer.printAllSinger(myConn, myState, myResSet); //static function that prints information of singer
						song.printAllSong(myConn, myState, myResSet); //static function that prints information of song
					}
					else if (subop==2) {//show only the information of album
						album.printAllAlbum(myConn, myState, myResSet);
					}
					else if (subop==3) {//show only the information of the singer
						singer.printAllSinger(myConn, myState, myResSet);						
					}
					else if (subop==4) {//show only the information of the song
						song.printAllSong(myConn, myState, myResSet);	
					}else {//if the input is not one of 1,2,3,4
						System.out.println("유효한 번호를 입력해주세요.");
					}

				}
				else if (op==2) {//insert new singer into singer table
					singer.insertNewSinger(myConn, myState, myResSet); //static function that insert singer name, debut year, latest album(optionally)
				}
				else if (op==3) {//update the information
					//user can update the information about the song's highest ranking or album's name
					System.out.println("(1)update highest ranking of the song (2)update name of the album");
					System.out.print(">>>");
					//get an input about which information the user want to update
					int subop=scanner.nextInt();
					if (subop==1) {//update song's highest ranking
						song.updateSongRanking(myConn, myState, myResSet); //get the information of song, then update the highest ranking
					}
					else if (subop==2){//update the name of album
						scanner=new Scanner(System.in);
						//get an input of album name that the user want to change
						System.out.print("album: ");
						album.album=scanner.nextLine();
						//get an input of singer name that the user want to change
						System.out.print("singer: ");
						album.singer=scanner.nextLine();
						//get an input of new name that the user want to change into
						System.out.print("change album name into: ");
						String newAlbumName=scanner.nextLine();
						try {//use transaction to update two tables at one time
							//turn off the auto commit
							myConn.setAutoCommit(false);
							//update the album name at the album table
							sql="update album set album=? where album=? and singer=?";
							PreparedStatement ps=myConn.prepareStatement(sql);
							
							ps.setString(1, newAlbumName);
							ps.setString(2, album.album);
							ps.setString(3, album.singer);
							ps.executeUpdate();
							
							//we need to update all the column that contains album
							//change the latest album name of the singer, if the value is updated
							sql="update singer set latestAlbum=? where latestAlbum=? and singer=?";
							ps=myConn.prepareStatement(sql);
							
							ps.setString(1, newAlbumName);
							ps.setString(2, album.album);
							ps.setString(3, album.singer);
							ps.executeUpdate();	
							//commit
							myConn.commit();
						}
						catch(SQLException e) {
							e.printStackTrace();
							if (myConn!=null) {
								try {//if commit didn't work, then roll back
									System.out.println("Transaction is being rolled back");
									myConn.rollback();
								}
								catch(SQLException e1) {
									e1.printStackTrace();
								}
							}
						}
						finally {
							//change auto commit into true
							myConn.setAutoCommit(true);	
						}
						System.out.println();
					}
					else {//if the value is not one of 1,2
						System.out.println("유효한 번호를 입력해주세요.");
					}
				}
				else if (op==4) {//song deletion
					song.deleteSong(myConn, myState, myResSet);//static function that delete the song
				}
				else if (op==5) {//get an input from user, then show the information it
					//genre, debut year, song title are the three things that user can search
					System.out.println("(1)genre (2)debut year (3)song title");
					System.out.print(">>>");
					//get an input about which information does user want to get
					int subop=scanner.nextInt();
					if (subop==1) {//get an input about genre, and show the song which is that genre
						song.searchGenre(myConn, myState, myResSet); //static function that shows that genre
					}
					else if (subop==2) {//get an input about singer's debut year, show every album of the singer who have that debut year
						song.seachYear(myConn, myState, myResSet);//static function
					}
					else if (subop==3) {//get an input of song's title, then print information about the song
						scanner=new Scanner(System.in);
						//get an input of the song title
						System.out.print("Which song title do you want? ");
						String title=scanner.nextLine();
						//show the singer of the song, singer's debut year, release date of the album which contains the song
						sql="select title, singer, releaseDate, debut from singer_song where title=?";
						PreparedStatement ps=myConn.prepareStatement(sql);
						
						ps.setString(1, title);
						myResSet=ps.executeQuery();
						
						System.out.println(String.format("Title %21s | Singer %21s | ReleaseDate %21s | Debut %11s","","","",""));
						System.out.println(String.format("%114s", "").replace(' ', '-'));
						
						while(myResSet.next()) {//show all values that we get from sql
							song.title=myResSet.getString("title");
							song.singer=myResSet.getString("singer");
							String date=myResSet.getString("ReleaseDate");
							int debut=myResSet.getInt("Debut");
							System.out.println(String.format("title: %20s | singer: %20s | releaseDate: %20s | debut: %10d",song.title, song.singer, date, debut));
						}
						System.out.println();

					}
					else {//if input is not one of 1,2,3
						System.out.println("유효한 번호를 입력해주세요.");
					}
				}
				else if (op==6) break;//if the input is 6, exit the program
				else System.out.println("유효한 번호를 입력해주세요.");//if the input is not one of 1,2,3,4,5,6
			}
			System.out.println("bye");

			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {//when the program end, disconnect all the connection
			if(myResSet!=null) {
				try {
					myResSet.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
			
			if (myState!=null) {
				try {
					myState.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
			
			if (myConn!=null) {
				try {
					myConn.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}