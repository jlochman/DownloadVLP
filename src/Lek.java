import java.io.File;



public class Lek {
	private String URLtext;
	
	private String Nazev;
	private String NazevZkr;
	private String PathToSave;
	private String Sila;
	private String Jednotka;
	private String LekovaForma;
	private String RegC;
	
	private String fileURL;
	private String fileMod;
	private long fileSize;
	private String fileMD5;
	
	Lek( String sURL ) {
		setURLtext(sURL);
		extractFromURL();
	}
	
	public String getNazev() {
		return Nazev;
	}
	public void setNazev(String nazev) {
		Nazev = nazev;
	}
	
	public String getNazevZkr() {
		return NazevZkr;
	}
	public void setNazevZkr(String nazevZkr) {
		NazevZkr = nazevZkr;
	}

	public String getPathToSave() {
		return PathToSave;
	}
	public void setPathToSave(String pathToSave) {
		PathToSave = pathToSave;
	}

	public String getSila() {
		return Sila;
	}
	public void setSila(String sila) {
		Sila = sila;
	}

	public String getJednotka() {
		return Jednotka;
	}
	public void setJednotka(String jednotka) {
		Jednotka = jednotka;
	}

	public String getLekovaForma() {
		return LekovaForma;
	}
	public void setLekovaForma(String lekovaForma) {
		LekovaForma = lekovaForma;
	}

	public String getRegC() {
		return RegC;
	}
	public void setRegC(String regC) {
		RegC = regC;
	}

	public String getFileMod() {
		return fileMod;
	}
	public void setFileMod(String fileMod) {
		this.fileMod = fileMod;
	}

	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileMD5() {
		return fileMD5;
	}
	public void setFileMD5(String fileMD5) {
		this.fileMD5 = fileMD5;
	}
	
	public String getFileURL() {
		return fileURL;
	}
	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}

	public String getURLtext() {
		return URLtext;
	}	
	public void setURLtext(String uRLtext) {
		URLtext = uRLtext;
	}	
		
	public void print() {
		System.out.println(URLtext);
		System.out.println("fileURL:  		"+fileURL);
		System.out.println("Nazev:      	"+Nazev);
		System.out.println("NazevZkr: 		"+NazevZkr);
		System.out.println("Sila:     		"+Sila);
		System.out.println("Jednotka: 		"+Jednotka);
		System.out.println("LekovaForma:	"+LekovaForma);
		System.out.println("RegC:			"+RegC);
		System.out.println("fileMod:		"+fileMod);
		System.out.println("fileSize:		"+fileSize);
		System.out.println("fileMD5: 		"+fileMD5);
	}
	
	public String getString() {
		String result = "";
		result += Nazev;
		result += " ; ";
		result += RegC;
		result += " ; ";
		result += "Modifikace: ";
		result += fileMod;
		result += " ; ";
		result += "Velikost: ";
		result += fileSize;
		result += " ; ";
		result += "Cesta: ";
		result += PathToSave;
		return result;
	}
	
	private void extractFromURL() {
		if ( URLtext == "" ) return;
		fileURL     = STRutils.getStringBetween(URLtext, "a href=\"", "\">", 0);
		Nazev       = STRutils.getStringBetween(URLtext, "\">",  "</a>",  0);
		Sila        = STRutils.getStringBetween(URLtext, "<td>", "</td>", 1);
		Jednotka    = STRutils.getStringBetween(URLtext, "<td>", "</td>", 2);
		LekovaForma = STRutils.getStringBetween(URLtext, "<td>", "</td>", 3);
		RegC        = STRutils.getStringBetween(URLtext, "<td>", "</td>", 4);
		NazevZkr   = STRutils.getModifiedFileName( Nazev );
		PathToSave = STRutils.getPathToSave( NazevZkr );
	}
	
	public void extractFromWEB( String fixedPath, String date, Integer index ) {
		PathToSave += "_" + String.format("%05d", index) + ".doc";
		String fileName = fixedPath + File.separator + date + File.separator + PathToSave;
		
		fileMod  = URLutils.getURLModifyDate( fileURL );
		URLutils.downloadFromURL( fileURL, fileName);
		
		fileSize = myFileUtils.getFileSize( fileName );
		fileMD5 = myFileUtils.getMd5( fileName );
	}
}
