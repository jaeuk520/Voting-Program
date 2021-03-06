package konkuk;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public final class Constant {
	static final String usersFile = "Users.txt"; //유저 정보 저장 텍스트 파일 이름
	static final String voteDirRoute = "Votes"+File.separator; //투표 저장 지정 루트
	
	static final String endOfVoteTrue = "1"; //투표 종료 여부 true
	static final String endOfVoteFalse = "2"; //투표 종료 여부 false
	static final String addAllowTrue = "1"; //선택지 추가 가능 여부 true
	static final String addAllowFalse = "2"; //선택지 추가 가능 여부 false
	
	static final String fieldTokenizerDelim = "/"; //파일에서 필드 구분자
	static final String voterTokenizerDelim = "+"; //투표자들 구분자
	static final String updateVerTokenizerDelim = "-"; //유저 전화번호와 업데이트 버전 구분자
	
	
	static final String quitChar = "q"; //메뉴로 되돌아가기 문자
	static final String haveNoVoter = "null"; //투표한 사람이 없는 선택지의 투표자형식
	static final int quitInt = -1; //메뉴 되돌아가기 int
	static final int minOptionNum = 2; //선택지 최소 갯수
	static final int maxOptionNum = 100; //선택지 최대 갯수
	static final int maxOptionLength = 20; //선택지 최대 글자 길이
	static final int minMultipleVoteNum = 1; //몰표 최소 한도
	static final int maxMultipleVoteNum = 100; //몰표 최대 한도
	static final int phoneNumberLength = 8; //전화번호 자릿수
	static final int nameMinLength = 2; //이름 최소 글자 수
	static final int nameMaxLength = 5; //이름 최대 글자 수
	static final int pwMinLength = 5; //비밀번호 최소 글자 수
	static final int pwMaxLength = 12; //비밀번호 최대 글자 수
	static final String reVoteTrue = "1"; //재투표 여부 true
	static final String reVoteFalse = "2"; //재투표 여부 false
	static final String increaseVoteNumTrue = "1";
	static final String increaseVoteNumFalse = "2";
	
	static final String endAddOptionChar= "end"; //선택지 만드는거 끝났을 때의 문자
	
	//파일 형식 확인 정규표현식
	static final String voteFileNameRegex =
			"^(\\d{12})+.(?i)(txt)$";
	static final String voteLine1Regex =
			"^([A-Za-z0-9가-힣\\s]{1,20})/(\\d{8})/(\\d{1})$";
	static final String voteLine2Regex =
			"^[1-2]/([0-9]+)/[1-2]/([0-9]+)/[1-2]$";
	static final String voteLine3Regex = 
			"^([A-Za-z0-9가-힣\\s]{1,20})/([0-9\\+\\-|null]*)$";//투표자들 부분은 다시 확인해주어야됨
	static final String voterRegex = 
			"^(\\d{8})-([0-9]+)$";
	static final String usersFileRegex = 
			"^(\\d{8})/(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{5,12}/([가-힣]{2,5})$";
	static final String updateVerRegex = "^[0-9]+$"; //업데이트 버전 표현 정규식
	
	
	static final String pwRegex = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{5,12}$"; //비밀번호 정규표현식4
	static final String koreanRegex = "^[가-힣]*$"; //한국어 정규표현식
	static final String voteNameRegex = "^[A-Za-z0-9가-힣\\s]*$"; //투표 이름 정규표현식
	static final String voteOptionsRegex = "^[A-Za-z0-9가-힣\\s]*$"; //선택지 정규표현식
	static final String voteAllowAddRegex = "^[1-2]$"; //선택지 추가 가능 여부 정규표현식
	static final String voteAllowNumRegex = "^[\\d]+$"; //선택지 선택 가능 횟수 정규표현식
	static final String numRegex = "^[\\d]+$"; //숫자 정규식
	static final String increaseVoteNumRegex = "^[1-2]$"; //투표 한도 늘리기 여부 정규표현식
	static final String menuOptionRegex = "^[1-6]$"; //메뉴 선택 정규표현식
	static final String phoneNumberRegex = "^[0-9]*$"; //전화번호 정규표현식
	
	static final int maxVoteNameLength = 20; //투표이름 최대 글자 수
	static final int maxVoteOptionLength = 20; //선택지 최대 글자 수
	
	static final int gotoMenuSleepTime = 3000; //n초후에 ~로 넘어갑니다
	
	//에러 메세지 문자열
	static final String fileErrorMsg = "파일이 잘못되었습니다.";
	static final String inputErrorMsg = "입력 형식에 맞지 않습니다. 다시 입력해주세요.";
	
	static void printFileError(String fileName) throws InterruptedException {
		System.out.println(fileName + ": " + fileErrorMsg);
		System.out.println("엔터 키를 누르면 계속 진행합니다.");
		Scanner sc = new Scanner(System.in);
		sc.nextLine();
		//Thread.sleep(2000);
	}
	
	// 콘솔창 지우기
	static void clearConsole() throws IOException, InterruptedException {
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
	}
}
