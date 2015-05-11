import java.io.File;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class STRutils {
	
	public static String getStringBetween( String sIn, String sPred, String sZa, Integer count) {
		String result = "";
		Integer posPred = 0;
		for ( Integer i = 0; i <= count; i++ ) {
			posPred = sIn.indexOf(sPred, posPred);
			posPred += sPred.length();
		}
		Integer posZa   = sIn.indexOf(sZa, posPred);
		if ( posPred >= 0 && posZa >= 0 ) {
			result = sIn.substring(posPred, posZa);
		} 
		result = result.replaceAll("[\\r\\n]+", "");
		return result;
	}
		
	public static String getModifiedFileName( String sFileName ) {
		sFileName = getCapitalized( sFileName );
		sFileName = Normalizer.normalize(sFileName, Normalizer.Form.NFD);
		sFileName = sFileName.replaceAll("[^A-Za-z0-9 ]", "");
		sFileName = sFileName.replaceAll(" ", "_");
		return sFileName;
	}
	
	private static String getCapitalized (String pWord) {
	    StringBuilder sb = new StringBuilder();
	    String[] words = pWord.replaceAll("_", " ").split("\\s");
	    for (int i = 0; i < words.length; i++)
	    {
	        if (i > 0)
	            sb.append(" ");
	        if (words[i].length() > 0)
	        {
	            sb.append(Character.toUpperCase(words[i].charAt(0)));
	            if (words[i].length() > 1)
	            {
	                sb.append(words[i].substring(1));
	            }
	        }
	    }
	    return sb.toString();
	}
	
	public static String getCurrentDate() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date today = Calendar.getInstance().getTime();         
		String reportDate = df.format(today);
		return reportDate;
	}
	
	public static String getPathToSave( String sNaz ) {
		String result = "";
		if ( sNaz.length() > 0 )
			result = sNaz.charAt(0) + File.separator + sNaz;
		return result;
	}
	
	public static String Quote( String s ) {
		return "'"+s+"'";
	}
	
}
