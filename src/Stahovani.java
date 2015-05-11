import java.util.Vector;

import commandLineArgs.commandArgs;
import commandLineArgs.commandLineArgsService;


public class Stahovani {
	
	static String pathToSave;
	static String dbName;
	static String currentDate;
	static String sURLmain;
	static String sBeforeTable;
	static String sBeforeLek;
	static String sAfterLek;

	static long startTime;
	
	public static void main(String[] args) {
		startTime = System.nanoTime();
		currentDate = STRutils.getCurrentDate();
		
		commandLineArgsService myService = new commandLineArgsService();
		commandArgs myArgs = null;
		myArgs = myService.parseAndSaveArgs(args);
		pathToSave = myArgs.getPath();
		dbName = myArgs.getDB();
		sURLmain = myArgs.getURL();
		sBeforeTable = myArgs.getBeforeTable();
		sBeforeLek = myArgs.getBeforeLek();
		sAfterLek = myArgs.getAfterLek();
		System.out.println( myArgs.toString() );
		
		MySQLite MyDB = new MySQLite(dbName);
		Vector<Lek> Leky_vec = new Vector<Lek>(0);
		
		System.out.println( getElapsedTime()+"Pripojiji se k SQL" );
		MyDB.Init( );

		if ( MyDB.getLastDownDate().contains(currentDate) ) {
			System.out.println("Dnes se jiz stahovalo. Pockejte do zitra.");
			return;
		}
			
		System.out.println( getElapsedTime()+"Stahuji URL" );
		String URLcontent = URLutils.getURLContent( sURLmain );
		
		System.out.println( getElapsedTime()+"Extrahuji" );
		Integer posStart = URLcontent.indexOf( sBeforeTable );
		Integer posEnd = posStart;
		
		while ( ( posStart = URLcontent.indexOf(sBeforeLek, posEnd) ) > -1 ) {
			posEnd = URLcontent.indexOf(sAfterLek, posStart );
			Leky_vec.addElement( new Lek( URLcontent.substring(posStart, posEnd) ) );
		}

		System.out.println( getElapsedTime()+"Stahuji soubory" );
		for ( int i = 0; i < Leky_vec.size(); i++ ) {
		//for ( int i = 0; i < 10; i++ ) {
			if ( i % 10 == 0 ) {
				System.out.print( getElapsedTime()+"[ "+String.format("%04d", i)+" / "+Leky_vec.size()+" ] "+
									"("+(100*i/Leky_vec.size())+" %) ");
				System.out.println( Leky_vec.get(i).getNazev() );
			}
			Leky_vec.get(i).extractFromWEB( pathToSave, currentDate, i+1 );
		}	
		
		System.out.println( getElapsedTime()+"Ukladam do DB" );
		MyDB.saveDate( currentDate );
		for ( int i = 0; i < Leky_vec.size(); i++ ) {
		//for ( int i = 0; i < 10; i++ ) {
			MyDB.saveLek( Leky_vec.get(i) );			
		}
		MyDB.writeChanges();
		MyDB.getResults(pathToSave);
		System.out.println( getElapsedTime()+"Hotovo" );
		
	}
	
	private static String getElapsedTime() {
		String result = "";
		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1000000000;
		
		long hours = duration / 3600;
		long minutes = (duration % 3600) / 60;
		long seconds = duration % 60;

		result = String.format("%02d:%02d:%02d", hours, minutes, seconds);	
		result += " ";
		return result;
	}

}
