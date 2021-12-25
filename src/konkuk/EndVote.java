package konkuk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;


public class EndVote {
	//
	String user_Phone_number;								//로그인된 유저 전화번호
	String user_name;										//로그인된 유저 이름
	File directory;											//파일들이 들어있는 디렉토리
	File[] underDir; 										//디렉토리 내부 파일 목록
	Vector<HashMap<File, String>> this_userMade_FileList;  	//로그인된 유저가 만든 투표 목록
	Vector<HashMap<File, String>> endedVotes;				//종료된 투표 목록
	BufferedReader iterFileReader;							//파일 입력을 위한 버퍼
	BufferedWriter iterFileWriter;							//파일 출력을 위한 버퍼
	Scanner scan = new Scanner(System.in);
	
	static ArrayList<String> userPhones = getUserPhones(); //전화 번호 유효성 검사를 위해 추가한 static 변수
	
	static ArrayList<String> getUserPhones() {
		ArrayList<String> phonesList = new ArrayList<>();
		File file = new File(Constant.usersFile);
		String line = "";
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(file), Charset.forName("UTF8")));
			
			while((line = br.readLine()) != null) {
				if (!line.matches(Constant.usersFileRegex)) {
					// 오류 있으면 그 줄 무시
					Constant.printFileError(Constant.usersFile);
					Thread.sleep(2000);
				} else {
					String phone = line.split(Constant.fieldTokenizerDelim)[0];
					phonesList.add(phone);
				}
			}
			
			br.close();
					
		} catch (Exception e) {
			return null;
		}
		
		return phonesList;
	}
	
	//생성자
	public EndVote(String userPhoneNumber, String userName) {
		user_Phone_number = userPhoneNumber;
		user_name = userName;
		directory = new File(Constant.voteDirRoute);
		this_userMade_FileList = new Vector<>();
		endedVotes = new Vector<>();
	}
	
	//메뉴 출력
	/*
	 * public void menu() { try { Users.clearConsole(); } catch (IOException |
	 * InterruptedException e1) { e1.printStackTrace(); }
	 * 
	 * System.out.println("4. 등록투표 끝내기"); System.out.println("5. 투표 결과 확인");
	 * System.out.println("6. 종료");
	 * 
	 * // System.out.println("단위검사!!!!! " + endedVotes.size());
	 * System.out.print("선택: "); String actual_select = scan.nextLine(); String
	 * select = actual_select.trim(); try { if (select.equals("4")) {
	 * refreshFiles(); showMyVotes(); } else if (select.equals("5")) {
	 * refreshFiles(); showEndedVotes(); // System.out.println("정상동작"); 단위검사 } else
	 * if (select.equals("6")) { System.out.println("\n프로그램을 종료합니다");
	 * scan.nextLine(); System.exit(0); } else {
	 * System.out.println("입력 형식이 맞지 않습니다. 다시 입력해 주세요"); scan.nextLine(); menu(); }
	 * } catch(IOException e) { e.printStackTrace(); }
	 * 
	 * }
	 */
	
	//디렉토리 내부 텍스트파일 목록 최신화 (빈번한 호출 필요)
	private void refreshFiles() throws IOException, InterruptedException {
		this_userMade_FileList = new Vector<>();
		endedVotes = new Vector<>(); // 벡터 초기화
		underDir = directory.listFiles();
		
		for (int i = 0; i < underDir.length; i++) {
			iterFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(underDir[i]), "UTF8"));
			verifyVote(iterFileReader, underDir[i]);
	//		System.out.println(endedVotes.get(0).get(underDir[i])); 
	//		for (File k : endedVotes.get(0).keySet()) {
	//			System.out.println("키 : " + k);
	//		} //단위검사
			iterFileReader.close();
		}
	}
	//디렉토리 내부의 텍스트 파일들을 확인하면서 본인이 만든 투표면 this_userMade_FileList에, 종료된 투표면 endedVotes에 추가
	private void verifyVote(BufferedReader br, File curFile) throws IOException, InterruptedException {
		String curLine = "";
		boolean isMyVote = false;
		HashMap<File, String> map = new HashMap<>();
		
		
		//첫번째 줄
		curLine = br.readLine();
		/*****/
		if (!curLine.matches(Constant.voteLine1Regex)) {
			Constant.printFileError(curFile.getName());
			return;
		}
		String[] arr = curLine.split("\\/");
		String voteName = arr[0];
		
		
		if (arr[1].equals(user_Phone_number)) {
			isMyVote = true;
		}
		
		if (!userPhones.contains(arr[1])) {
			System.out.println(curFile + ": 파일 내부의 유저 정보가 잘못되어있습니다. : "
					+ arr[1]);
			return;
		}
		
		//두번째 줄
		curLine = br.readLine();
		/*****/
		if (!curLine.matches(Constant.voteLine2Regex)) {
			Constant.printFileError(curFile.getName());
			return;
		}
		arr = curLine.split("\\/");
		
		//3번째 줄 이후 형식 맞는지 확인 - > 2번째줄 정보 맞으면 맵에 추가 arr 건들지 말것
		while ((curLine=br.readLine())!= null) {
			if (curLine.matches(Constant.voteLine3Regex)) {
				String[] temp = curLine.split(Constant.fieldTokenizerDelim);
				if (!temp[1].equals(Constant.haveNoVoter)) {
					StringTokenizer st = new StringTokenizer(temp[1], Constant.voterTokenizerDelim);
					while (st.hasMoreTokens()) {
						String t_voter = st.nextToken();
						if (!t_voter.matches(Constant.voterRegex)) {
							Constant.printFileError(curFile.toString());
							return;
						}
						else if (!userPhones.contains(t_voter.split(Constant.updateVerTokenizerDelim)[0])) {
							System.out.println(curFile + ": 파일 내부의 유저 정보가 잘못되어있습니다. : "
						+ t_voter.split(Constant.updateVerTokenizerDelim)[0]);
							return;
						}
					}
				}
			} else {
				Constant.printFileError(curFile.getName());
				return;
			}
		}
		
		
		if (arr[0].equals("1")) {
			map.put(curFile, voteName);
			endedVotes.add(map);
		}
		else if (isMyVote) {
			map.put(curFile, voteName);
			this_userMade_FileList.add(map);
		}
	}
	
	public void showMyVotes() throws IOException, InterruptedException {
		refreshFiles();
		
		while (true) {
			try {
				Constant.clearConsole();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
	 		System.out.println("<투표 종료하기>");
			System.out.println("***" + user_name+"님이 만든 투표***");
			for (int i = 0; i < this_userMade_FileList.size(); i++) {			
				for (File s : this_userMade_FileList.get(i).keySet()) {
					System.out.println(i + 1 + ". " + this_userMade_FileList.get(i).get(s));
				}
			}
			System.out.println("0. 취소");
			System.out.print("종료할 투표 선택: ");
			String actual_select = scan.nextLine();
			String selec = actual_select.trim();
			if (selec.equals(Constant.quitChar) || selec.equals("0")) {
				return;
			}
			else if (isNumeric(selec)) {
				int selectedNum = Integer.parseInt(selec) - 1;
				if ((this_userMade_FileList.size() <= selectedNum) ||
						(selectedNum < 0)) {
					System.out.println(Constant.inputErrorMsg);
					System.out.println("3초 후에 되돌아 갑니다.");
					Thread.sleep(Constant.gotoMenuSleepTime);
					continue;
				}
				for (File s : this_userMade_FileList.get(selectedNum).keySet()) {
					System.out.println("'" + this_userMade_FileList.get(selectedNum).get(s) + "'을(를) 정말 끝내시겠습니까?");
					System.out.println("1. 예");
					System.out.println("2. 아니오");
					System.out.print("선택: ");
					
					String original_confirm = scan.nextLine();
					String confirm = original_confirm.trim();
					if (confirm.equals("1")) {
						terminateVotes(selectedNum);
						System.out.println("\n투표를 종료했습니다.");
						System.out.println("<'" + this_userMade_FileList.get(selectedNum).get(s) + "' 투표 결과>");
						HashMap<File, String> newMap = new HashMap<>();
						newMap.put(s, this_userMade_FileList.get(selectedNum).get(s));
						endedVotes.add(newMap);
						printEndedVotes(endedVotes.size() - 1);
						return;
					}
					else if (confirm.equals("2")) {
						continue;
					}
					else if (confirm.equals(Constant.quitChar)) {
						return;
					}
					else {
						System.out.println(Constant.inputErrorMsg);
						System.out.println("3초 후에 되돌아 갑니다.");
						Thread.sleep(Constant.gotoMenuSleepTime);
						continue;
					}
				}
			}
			else {
				System.out.println(Constant.inputErrorMsg);
				System.out.println("3초 후에 되돌아 갑니다.");
				Thread.sleep(Constant.gotoMenuSleepTime);
				continue;			
			}
		}
	}
	
	private void terminateVotes(int num) throws IOException {
		String oldPath = ""; 		//구 파일 경로
		for (File s : this_userMade_FileList.get(num).keySet()) {
			oldPath = s.getPath();
		}
		String curLine = ""; 		//현재 읽은 줄
		int lineCnt = 1; 			// 현재 라인
		
		File oldFile = new File(oldPath);
		File newFile = new File(directory + "/temp.txt");
		newFile.createNewFile(); 	//temp.txt 파일 생성
		iterFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(oldFile), "UTF8"));
		iterFileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile), "UTF8"));
		
		while ((curLine = iterFileReader.readLine()) != null) {
			if (lineCnt == 2) {
				StringBuilder sb = new StringBuilder(curLine);
				sb.setCharAt(0, '1');
				curLine = sb.toString();
			}
			iterFileWriter.write(curLine + "\r\n");
			lineCnt++;
		}
		
		
		iterFileReader.close();
		iterFileWriter.flush();
		iterFileWriter.close();
		
		oldFile.delete();
		newFile.renameTo(new File(oldPath));
	}
	
	public void showEndedVotes() throws IOException, InterruptedException {
		refreshFiles();
		while (true) {
			try {
				Constant.clearConsole();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			} 
			System.out.println("<종료된 투표 결과 확인>");
		//	System.out.println("단위검사!!! " + endedVotes.size());
			for (int i = 0; i < endedVotes.size(); i++) {
				for (File s : endedVotes.get(i).keySet()) {
					System.out.println(i + 1 + ". " + endedVotes.get(i).get(s));
				}
			}
			System.out.println("0. 취소");
			System.out.print("결과 확인할 투표 선택: ");
			String actual_select = scan.nextLine();
			String selec = actual_select.trim();
			if (selec.equals(Constant.quitChar) || selec.equals("0")) {
				return;
			}
			else if (isNumeric(selec)) {
				int selecNum = Integer.parseInt(selec) - 1;
				if (endedVotes.size() <= selecNum || selecNum < 0) {
					System.out.println(Constant.inputErrorMsg);
					System.out.println("3초 후에 되돌아 갑니다.");
					Thread.sleep(Constant.gotoMenuSleepTime);
					continue;
				}
				for (File s : endedVotes.get(selecNum).keySet()) {
					System.out.println();
					System.out.println("<'" + endedVotes.get(selecNum).get(s) + "' 투표 결과>");
				}
				printEndedVotes(selecNum);
				return;
			}
			else {
				System.out.println(Constant.inputErrorMsg);
				System.out.println("3초 후에 되돌아 갑니다.");
				Thread.sleep(Constant.gotoMenuSleepTime);
				continue;
			}
		}
	}
	private boolean isNumeric(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	private void printEndedVotes(int num) throws IOException {
				
		String selectedFilePath = "";
		
		for (File s : endedVotes.get(num).keySet()) {
			selectedFilePath = s.getPath();
		}
		
		String curLine = "";
		String[] secondLine = new String[3];
		
		int lineCnt = 1;		//몇번째 라인인가
		int number = 1;			//실제 투표 표시할 라인 넘버
		int obtained = 0;		//해당 투표가 얻은 득표수
		int total_obtained = 0;	//총 득표수
		int max_obtained = 0;	//최다 득표수
		Vector<String> candidate = new Vector<>(); //후보군들
		
		File selectedFile = new File(selectedFilePath);
		iterFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(selectedFile), "UTF8"));
		
		while ((curLine = iterFileReader.readLine()) != null) {
			if (lineCnt == 1) {
				lineCnt++;
				continue;
			}
			else if (lineCnt == 2) {
				lineCnt++;
				secondLine = curLine.split("\\/");
			}
			else if (curLine.equals("")) {
				continue;
			}
			else {
				String[] arr = curLine.split("\\/");
				String[] votedPerson = arr[1].split("\\+");
				
				if (votedPerson[0].equals(Constant.haveNoVoter)) {
					obtained = 0;
				}
				else {
					obtained = votedPerson.length;					
				}
				
				total_obtained += obtained;
				if (max_obtained == obtained) {
					candidate.add(arr[0]);
				}
				else if (max_obtained <= obtained) {
					max_obtained = obtained;
					//candidate.clear();
					candidate.add(arr[0]);
				}
				System.out.println(number + "번 " + arr[0] + " " + obtained + "표");
				lineCnt++;
				number++;
			}
		}
		
		System.out.println("-------");
		System.out.println("인당 총 투표 허용 횟수 : " + secondLine[1] + "번");
		System.out.println("한 선택지에 대해 최대 투표 가능 횟수: " + secondLine[3] + "표");
		System.out.println("총 투표수 : " + total_obtained + "표");		
		System.out.println("최다 득표 : ");
		while (!candidate.isEmpty()) {
			System.out.println("***" + candidate.remove(0) + "***");
		}
		
		candidate = null;
		System.out.println("(엔터 키를 누르면 메인 메뉴로 돌아갑니다.)");
		scan.nextLine();
		
		iterFileReader.close();
	}
}
