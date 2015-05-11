import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MySQLite {
	
	private Connection c;
	private String dbFile = "";
	private Integer lastDateID = -1;
	private String lastDateStr = "";
	private Integer newDateID = -1;
	private String newDateStr = "";
	
	printResults myPrinter = null;
	
	MySQLite( String sFile ) {
		dbFile = sFile;
		c = null;
	}
	
	public void setDBName( String sName ) {
		dbFile = sName;
	}
	
	public void Init() {
		this.Connect();
		this.CreateTables();
		this.Connect();
		this.getDateID();
	}
	
	public void getResults( String path ) {
		
		myPrinter = new printResults( path+newDateStr+File.separator+"souhrn.csv" );
		myPrinter.Print("Nove soubory");
		this.getNew();
		
		myPrinter.Print("Zrusene soubory");
		this.getDeleted();
		
		myPrinter.Print("Modifikovane soubory");
		this.getModifications();
		
		myPrinter.Print("Shodne soubory");
		this.getShoda();
		
		myPrinter.Finalize();
		
		this.getToDelete( path );
		this.writeChanges();
		
	}
	
	public void saveDate( String sDate ) {
		try {
			Statement MyStmt = c.createStatement();
			newDateStr = sDate;
			String sql = "INSERT INTO DATASTAZENI (ID,DatumStazen) " +
						 "VALUES ("+newDateID+","+STRutils.Quote(sDate)+");"; 
			MyStmt.executeUpdate(sql);
			MyStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public void saveLek( Lek myLek ) {
		try {
			Statement MyStmt = c.createStatement();
			String sql = "INSERT INTO LEKY "+
						"(Nazev,NazevZkr,RegC,Sila,Jednotka,LekovaForma,"+
					     "FileMD5,FileCesta,FileVelikost,FileModDate,DatumID) " +
						 "VALUES "+
					    "("+STRutils.Quote(myLek.getNazev())+","+
						 	STRutils.Quote(myLek.getNazevZkr())+","+
						 	STRutils.Quote(myLek.getRegC())+","+
						 	STRutils.Quote(myLek.getSila())+","+
						 	STRutils.Quote(myLek.getJednotka())+","+
						 	STRutils.Quote(myLek.getLekovaForma())+","+
						 	STRutils.Quote(myLek.getFileMD5())+","+
						 	STRutils.Quote(myLek.getPathToSave())+","+
						 	myLek.getFileSize()+","+
						 	STRutils.Quote(myLek.getFileMod())+","+
						 	newDateID+");";			    		
			MyStmt.executeUpdate(sql);
			MyStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void writeChanges() {
		try {
			c.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getLastDownDate() {
		String result = "";
		try {
			Statement MyStmt = c.createStatement();
			ResultSet rs = MyStmt.executeQuery("SELECT max(ID) AS maxID, DatumStazen FROM DATASTAZENI");
			rs.next();
			result = rs.getString("DatumStazen");
			rs.close() ;
			MyStmt.close();
		} catch ( Exception e ) {
		    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}
		
		if (result != null ) return result;
		else return "";
	}
	
	private void getDateID() {
		try {
			Statement MyStmt = c.createStatement();
			ResultSet rs = MyStmt.executeQuery("SELECT max(ID) AS maxID, DatumStazen FROM DATASTAZENI");
			rs.next();
			lastDateID = rs.getInt("maxID");
			lastDateStr = rs.getString("DatumStazen");
			newDateID = lastDateID + 1;
			
			rs.close() ;
			MyStmt.close();
		} catch ( Exception e ) {
		    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}
		System.out.println("Last downlad date ID: "+lastDateID+"  "+lastDateStr);
		//System.out.println("New downlad date ID: "+newDateID);
	}
	
	private void Connect() {
		try {
			Class.forName("org.sqlite.JDBC");
		    c = DriverManager.getConnection("jdbc:sqlite:"+dbFile);
		    c.setAutoCommit( false );
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}
		//System.out.println("Database opened successfully");		
	}
	
	private void CreateTables() {
		try {
			Statement MyStmt = c.createStatement();
		    String sql1 = "CREATE TABLE IF NOT EXISTS LEKY " +
		                 " (ID INTEGER PRIMARY KEY          NOT NULL," +
		                 "  Nazev               TEXT    NOT NULL, " + 
		                 "  NazevZkr            TEXT    NOT NULL, " + 
		                 "  RegC                TEXT    NOT NULL, " + 
		                 "  Sila                TEXT, " +
		                 "  Jednotka            TEXT, " +
		                 "  LekovaForma         TEXT, " +
		                 "  FileMD5             TEXT, " +
		                 "  FileCesta           TEXT, " +
		                 "  FileVelikost        INT,  " +
		                 "  FileModDate         TEXT, " +
		                 "  DatumID             INT     NOT NULL)"; 
		    MyStmt.executeUpdate(sql1);
		    
		    String sql2 = "CREATE TABLE IF NOT EXISTS DATASTAZENI " +
	                 " (ID INT PRIMARY KEY     NOT NULL," +
	                 "  DatumStazen         TEXT    NOT NULL)";
		    MyStmt.executeUpdate(sql2);
		    
		    MyStmt.close();
		    c.commit();
    	} catch ( Exception e ) {
    		System.err.println( e.getClass().getName() + ": " + e.getMessage() );
    		System.exit(0);
        }
		//System.out.println("Table created successfully");
	}
	
	private void getNew() {
		try {
			Statement MyStmt = c.createStatement();
			ResultSet rs = MyStmt.executeQuery( 	"SELECT ID FROM LEKY WHERE DatumID = "+newDateID+" AND RegC IN "+
					"(SELECT RegC FROM LEKY WHERE DatumID = "+newDateID+" OR DatumID = "+lastDateID+" GROUP BY RegC HAVING COUNT(RegC) = 1)");
			while ( rs.next() ) {
		        myPrinter.Print( getLekFromID( rs.getInt("ID") ).getString() );
			}
		    rs.close();
		    MyStmt.close();
		} catch ( Exception e ) {
		    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}
	}
	
	private void getDeleted() {
		try {
			Statement MyStmt = c.createStatement();
			ResultSet rs = MyStmt.executeQuery( 	"SELECT ID FROM LEKY WHERE DatumID = "+lastDateID+" AND RegC IN "+
					"(SELECT RegC FROM LEKY WHERE DatumID = "+newDateID+" OR DatumID = "+lastDateID+" GROUP BY RegC HAVING COUNT(RegC) = 1)");
			while ( rs.next() ) {
				myPrinter.Print( getLekFromID( rs.getInt("ID") ).getString() );
			}
		    rs.close();
		    MyStmt.close();
		} catch ( Exception e ) {
		    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}
	}
	
	private void getShoda() {
		try {
			Statement MyStmt = c.createStatement();
			ResultSet rs = MyStmt.executeQuery( 	"SELECT ID FROM LEKY WHERE DatumID = "+newDateID+" AND RegC IN "+
					"(SELECT RegC FROM LEKY WHERE DatumID = "+newDateID+" OR DatumID = "+lastDateID+" GROUP BY RegC HAVING COUNT(RegC) = 2) "+
					"AND RegC IN "+
					"(SELECT RegC FROM LEKY WHERE DatumID = "+newDateID+" OR DatumID = "+lastDateID+" GROUP BY RegC HAVING COUNT(FileMD5) = 2);");
			
			while ( rs.next() ) {
				myPrinter.Print( getLekFromID( rs.getInt("ID") ).getString() );
			}
		    rs.close();
		    MyStmt.close();
		} catch ( Exception e ) {
		    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}		
	}
	
	private void getModifications() {
		try {
			Statement MyStmt = c.createStatement();
			ResultSet rs = MyStmt.executeQuery( 	"SELECT ID FROM LEKY WHERE DatumID = "+newDateID+" AND RegC IN "+
					"(SELECT RegC FROM LEKY WHERE DatumID = "+newDateID+" OR DatumID = "+lastDateID+" GROUP BY RegC HAVING COUNT(RegC) = 2) "+
					"AND RegC IN "+
					"(SELECT RegC FROM LEKY WHERE DatumID = "+newDateID+" OR DatumID = "+lastDateID+" GROUP BY RegC HAVING COUNT(FileMD5) = 1)");
			while ( rs.next() ) {
		        myPrinter.Print( getLekFromID( rs.getInt("ID") ).getString() );
			}
		    rs.close();
		    MyStmt.close();
		} catch ( Exception e ) {
		    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}		
	}
	
	private void getToDelete( String path ) {
		try {
			Statement MyStmt = c.createStatement();
			ResultSet rs = MyStmt.executeQuery( 	"SELECT ID FROM LEKY WHERE DatumID = "+lastDateID+" AND RegC IN "+
					"(SELECT RegC FROM LEKY WHERE DatumID = "+newDateID+" OR DatumID = "+lastDateID+" GROUP BY RegC HAVING COUNT(RegC) = 2) "+
					"AND RegC IN "+
					"(SELECT RegC FROM LEKY WHERE DatumID = "+newDateID+" OR DatumID = "+lastDateID+" GROUP BY RegC HAVING COUNT(FileMD5) = 2)");
			while ( rs.next() ) {
				deleteLekByID( rs.getInt("ID"), path );
			}
		    rs.close();
		    MyStmt.close();
		} catch ( Exception e ) {
		    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}			
	}
	
	private Lek getLekFromID( Integer id ) {
		Lek myLek = new Lek( "" );
		try {
			Statement myStmt = c.createStatement();
			ResultSet rs = myStmt.executeQuery( "SELECT * FROM LEKY, DATASTAZENI WHERE LEKY.DatumID = DATASTAZENI.ID and LEKY.ID = "+id );
			rs.next();
			myLek.setNazev( rs.getString( "Nazev" ));
			myLek.setNazevZkr( rs.getString( "NazevZkr" ));
			myLek.setSila( rs.getString( "Sila" ));
			myLek.setRegC( rs.getString( "RegC"));
			myLek.setSila( rs.getString( "Sila"));
			myLek.setJednotka( rs.getString( "Jednotka"));
			myLek.setLekovaForma( rs.getString( "LekovaForma"));
			myLek.setFileMD5( rs.getString( "FileMD5"));
			myLek.setPathToSave( rs.getString( "FileCesta"));
			myLek.setFileSize( rs.getLong( "FileVelikost"));
			myLek.setFileMod( rs.getString( "FileModDate"));
			myLek.setPathToSave( rs.getString( "DatumStazen") + File.separator + rs.getString( "FileCesta"));
		    rs.close();
		    myStmt.close();
		} catch ( Exception e ) {
		    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}
		return myLek; 
	}
	
	private void deleteLekByID( Integer id, String path) {
		String mPath = path;
		try {
			Statement myStmt = c.createStatement();
			ResultSet rs = myStmt.executeQuery( "SELECT * FROM LEKY, DATASTAZENI WHERE LEKY.DatumID = DATASTAZENI.ID and LEKY.ID = "+id );
			rs.next();
			mPath += rs.getString( "DatumStazen") + File.separator + rs.getString( "FileCesta");
		    rs.close();
		    myStmt.close();
		} catch ( Exception e ) {
		    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}
		
		File file = new File( mPath );
		file.delete();
		
		try {
			Statement myStmt = c.createStatement();
			myStmt.executeUpdate( "DELETE FROM LEKY WHERE ID = "+id );
		    myStmt.close();
		} catch ( Exception e ) {
		    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}
	}

}