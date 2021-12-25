package konkuk;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public final class Constant {
	static final String usersFile = "Users.txt"; //À¯Àú Á¤º¸ ÀúÀå ÅØ½ºÆ® ÆÄÀÏ ÀÌ¸§
	static final String voteDirRoute = "Votes"+File.separator; //ÅõÇ¥ ÀúÀå ÁöÁ¤ ·çÆ®
	
	static final String endOfVoteTrue = "1"; //ÅõÇ¥ Á¾·á ¿©ºÎ true
	static final String endOfVoteFalse = "2"; //ÅõÇ¥ Á¾·á ¿©ºÎ false
	static final String addAllowTrue = "1"; //¼±ÅÃÁö Ãß°¡ °¡´É ¿©ºÎ true
	static final String addAllowFalse = "2"; //¼±ÅÃÁö Ãß°¡ °¡´É ¿©ºÎ false
	
	static final String fieldTokenizerDelim = "/"; //ÆÄÀÏ¿¡¼­ ÇÊµå ±¸ºĞÀÚ
	static final String voterTokenizerDelim = "+"; //ÅõÇ¥ÀÚµé ±¸ºĞÀÚ
	static final String updateVerTokenizerDelim = "-"; //À¯Àú ÀüÈ­¹øÈ£¿Í ¾÷µ¥ÀÌÆ® ¹öÀü ±¸ºĞÀÚ
	
	
	static final String quitChar = "q"; //¸Ş´º·Î µÇµ¹¾Æ°¡±â ¹®ÀÚ
	static final String haveNoVoter = "null"; //ÅõÇ¥ÇÑ »ç¶÷ÀÌ ¾ø´Â ¼±ÅÃÁöÀÇ ÅõÇ¥ÀÚÇü½Ä
	static final int quitInt = -1; //¸Ş´º µÇµ¹¾Æ°¡±â int
	static final int minOptionNum = 2; //¼±ÅÃÁö ÃÖ¼Ò °¹¼ö
	static final int maxOptionNum = 100; //¼±ÅÃÁö ÃÖ´ë °¹¼ö
	static final int maxOptionLength = 20; //¼±ÅÃÁö ÃÖ´ë ±ÛÀÚ ±æÀÌ
	static final int minMultipleVoteNum = 1; //¸ôÇ¥ ÃÖ¼Ò ÇÑµµ
	static final int maxMultipleVoteNum = 100; //¸ôÇ¥ ÃÖ´ë ÇÑµµ
	static final int phoneNumberLength = 8; //ÀüÈ­¹øÈ£ ÀÚ¸´¼ö
	static final int nameMinLength = 2; //ÀÌ¸§ ÃÖ¼Ò ±ÛÀÚ ¼ö
	static final int nameMaxLength = 5; //ÀÌ¸§ ÃÖ´ë ±ÛÀÚ ¼ö
	static final int pwMinLength = 5; //ºñ¹Ğ¹øÈ£ ÃÖ¼Ò ±ÛÀÚ ¼ö
	static final int pwMaxLength = 12; //ºñ¹Ğ¹øÈ£ ÃÖ´ë ±ÛÀÚ ¼ö
	static final String reVoteTrue = "1"; //ÀçÅõÇ¥ ¿©ºÎ true
	static final String reVoteFalse = "2"; //ÀçÅõÇ¥ ¿©ºÎ false
	static final String increaseVoteNumTrue = "1";
	static final String increaseVoteNumFalse = "2";
	
	static final String endAddOptionChar= "end"; //¼±ÅÃÁö ¸¸µå´Â°Å ³¡³µÀ» ¶§ÀÇ ¹®ÀÚ
	
	//ÆÄÀÏ Çü½Ä È®ÀÎ Á¤±ÔÇ¥Çö½Ä
	static final String voteFileNameRegex =
			"^(\\d{12})+.(?i)(txt)$";
	static final String voteLine1Regex =
			"^([A-Za-z0-9°¡-ÆR\\s]{1,20})/(\\d{8})/(\\d{1})$";
	static final String voteLine2Regex =
			"^[1-2]/([0-9]+)/[1-2]/([0-9]+)/[1-2]$";
	static final String voteLine3Regex = 
			"^([A-Za-z0-9°¡-ÆR\\s]{1,20})/([0-9\\+\\-|null]*)$";//ÅõÇ¥ÀÚµé ºÎºĞÀº ´Ù½Ã È®ÀÎÇØÁÖ¾î¾ßµÊ
	static final String voterRegex = 
			"^(\\d{8})-([0-9]+)$";
	static final String usersFileRegex = 
			"^(\\d{8})/(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{5,12}/([°¡-ÆR]{2,5})$";
	static final String updateVerRegex = "^[0-9]+$"; //¾÷µ¥ÀÌÆ® ¹öÀü Ç¥Çö Á¤±Ô½Ä
	
	
	static final String pwRegex = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{5,12}$"; //ºñ¹Ğ¹øÈ£ Á¤±ÔÇ¥Çö½Ä4
	static final String koreanRegex = "^[°¡-ÆR]*$"; //ÇÑ±¹¾î Á¤±ÔÇ¥Çö½Ä
	static final String voteNameRegex = "^[A-Za-z0-9°¡-ÆR\\s]*$"; //ÅõÇ¥ ÀÌ¸§ Á¤±ÔÇ¥Çö½Ä
	static final String voteOptionsRegex = "^[A-Za-z0-9°¡-ÆR\\s]*$"; //¼±ÅÃÁö Á¤±ÔÇ¥Çö½Ä
	static final String voteAllowAddRegex = "^[1-2]$"; //¼±ÅÃÁö Ãß°¡ °¡´É ¿©ºÎ Á¤±ÔÇ¥Çö½Ä
	static final String voteAllowNumRegex = "^[\\d]+$"; //¼±ÅÃÁö ¼±ÅÃ °¡´É È½¼ö Á¤±ÔÇ¥Çö½Ä
	static final String numRegex = "^[\\d]+$"; //¼ıÀÚ Á¤±Ô½Ä
	static final String increaseVoteNumRegex = "^[1-2]$"; //ÅõÇ¥ ÇÑµµ ´Ã¸®±â ¿©ºÎ Á¤±ÔÇ¥Çö½Ä
	static final String menuOptionRegex = "^[1-6]$"; //¸Ş´º ¼±ÅÃ Á¤±ÔÇ¥Çö½Ä
	static final String phoneNumberRegex = "^[0-9]*$"; //ÀüÈ­¹øÈ£ Á¤±ÔÇ¥Çö½Ä
	
	static final int maxVoteNameLength = 20; //ÅõÇ¥ÀÌ¸§ ÃÖ´ë ±ÛÀÚ ¼ö
	static final int maxVoteOptionLength = 20; //¼±ÅÃÁö ÃÖ´ë ±ÛÀÚ ¼ö
	
	static final int gotoMenuSleepTime = 3000; //nÃÊÈÄ¿¡ ~·Î ³Ñ¾î°©´Ï´Ù
	
	//¿¡·¯ ¸Ş¼¼Áö ¹®ÀÚ¿­
	static final String fileErrorMsg = "ÆÄÀÏÀÌ Àß¸øµÇ¾ú½À´Ï´Ù.";
	static final String inputErrorMsg = "ÀÔ·Â Çü½Ä¿¡ ¸ÂÁö ¾Ê½À´Ï´Ù. ´Ù½Ã ÀÔ·ÂÇØÁÖ¼¼¿ä.";
	
	static void printFileError(String fileName) throws InterruptedException {
		System.out.println(fileName + ": " + fileErrorMsg);
		System.out.println("¿£ÅÍ Å°¸¦ ´©¸£¸é °è¼Ó ÁøÇàÇÕ´Ï´Ù.");
		Scanner sc = new Scanner(System.in);
		sc.nextLine();
		//Thread.sleep(2000);
	}
	
	// ÄÜ¼ÖÃ¢ Áö¿ì±â
	static void clearConsole() throws IOException, InterruptedException {
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
	}
}
