package commandLineArgs;

import com.beust.jcommander.Parameter;

public class commandArgs {

	@Parameter(names = {"--help", "-h"}, help = true)
	private boolean help;
	
	@Parameter(names = {"--path", "-p"}, description = "Cesta k ulozeni souboru")
	private String pathToSave = "/Volumes/Work HD/veterina/";
	
	@Parameter(names = {"--database", "-d"}, description = "Nazev DB. DB je striktne umistena vedle spousteciho souboru")
	private String dbFile = "SQLite.db";
	
	@Parameter(names = {"--url", "-u"}, description = "URL ke stazeni")
	private String sURL = "http://eagri.cz/public/app/uskvbl/cs/registrace-a-schvalovani/"+
							"registrace-vlp/seznam-vlp/registrovane-vnitrostatnim-postupem-a-mrpdcp";
	
	@Parameter(names = {"--beforeTable", "-bt"}, description = "Charakteristicky tag pred tabulkou s leky")
	private String sBeforeTable = "<table class=\"uskvbl_table\" id=\"uskvbl_table\"";
	
	@Parameter(names = {"--beforeLek", "-bl"}, description = "Charakteristicky tag pred lekem")
	private String sBeforeLek = "<td><a href=";
	
	@Parameter(names = {"--afterLek", "-al"}, description = "Charakteristicky tag za lekem")
	private String sAfterLek = "</tr>";
		
	@Override
	public String toString() {
		return "CommandArgs [dataBase=" + dbFile + ", pathToSave=" + pathToSave + ", help="
				+ help + ", URL=" + sURL + ", beforeTable=" + sBeforeTable 
				+", beforeLek=" + sBeforeLek + ", afterLek=" + sAfterLek + "]";
	}

	public boolean isHelp() {
		return help;
	}
	public String getPath() {
		return pathToSave;
	}
	public String getDB() {
		return dbFile;
	}
	public String getURL() {
		return sURL;
	}
	public String getBeforeTable() {
		return sBeforeTable;
	}
	public String getBeforeLek() {
		return sBeforeLek;
	}
	public String getAfterLek() {
		return sAfterLek;
	}
	
}
