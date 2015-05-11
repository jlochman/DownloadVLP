import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;

import org.apache.commons.io.FileUtils;


public class URLutils {

	public static String getURLContent( String sURL ) {
		URL url;
	    InputStream is = null;
	    BufferedReader br;
	    String line;
	    String Content = "";

	    try {
	        url = new URL( sURL );
	        is = url.openStream();  // throws an IOException
	        br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

	        while ((line = br.readLine()) != null) {
	            Content += line;
	            Content += "\n";
	        }
	        
	    } catch (MalformedURLException mue) {
	         mue.printStackTrace();
	    } catch (IOException ioe) {
	         ioe.printStackTrace();
	    } finally {
	        try {
	            if (is != null) is.close();
	        } catch (IOException ioe) {
				ioe.printStackTrace();
	        }
	    }
		return Content;
	}
		
	public static String getURLModifyDate( String sURL ) {
		String result = "";
		try {
			URL url = new URL( sURL );
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			long date = httpCon.getLastModified();
		    if (date == 0)
		      result = "";
		    else
		      result = new Date(date).toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void downloadFromURL( String sURL, String sFile ) {
		try {
			URL mURL = new URL(sURL);
			File mFile = new File(sFile);
			
			FileUtils.copyURLToFile( mURL, mFile );
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	

}
