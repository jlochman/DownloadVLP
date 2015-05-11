import java.io.*;

public class printResults {
	
	private static File mFile = null;
	private static FileWriter fw = null;
	private static BufferedWriter bw = null;
	
	public printResults( String filePath ) {
		Init( filePath );
	}
	
	static void Init( String filePath ) {
		mFile = new File( filePath );
		try {
			mFile.createNewFile( );
			fw = new FileWriter( mFile.getAbsoluteFile( ) );
			bw = new BufferedWriter( fw );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void Print( String text ) {
		try {
			bw.write( text );
			bw.write( "\n" );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void Finalize() {
	    try {
			bw.close( );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
