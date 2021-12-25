package konkuk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class VotesHandler {
	Users user;
	ArrayList<Votes> votesList = new ArrayList<>(); // Votes 객체 저장

	VotesHandler(Users user) throws InterruptedException {
		this.user = user;
		votesMkdir();
		initVotes();
	}

	/* Votes폴더 없으면 만들기 */
	void votesMkdir() {
		File votesDir = new File(Constant.voteDirRoute);
		if (!(votesDir.isDirectory())) {
			votesDir.mkdir(); // 폴더 생성
		}
	}

	/* list에 Votes 객체 담기 */
	void initVotes() throws InterruptedException {
		File path = new File(Constant.voteDirRoute);
		File[] fileList = path.listFiles();
		votesList.clear(); // 추가
		// 모든 파일 이름들로 리스트에 객체 저장
		if (fileList.length > 0) {
			for (int i = 0; i < fileList.length; i++) {
				/*****************************************/
				if (fileList[i].getName().matches(Constant.voteFileNameRegex) ) {
					Votes v = new Votes(fileList[i].getName());
					//파일이 정상일경우 리스트에 추가
					if (v.getNormal()) {
						votesList.add(new Votes(fileList[i].getName()));
					}
				} else {
					System.out.println(fileList[i].getName() + ": " +Constant.fileErrorMsg);
					//Thread.sleep(1000);
					continue;
				}
				/****************************************/
			}
		}
		//votesList.get(0).showVote();
		/*
		 * for (int i=0; i<votesList.size(); i++) { for (int j=0;
		 * j<votesList.get(i).options.size(); j++) { String[] str =
		 * votesList.get(i).options.get(j).getVoterUser(); for (int k=0; k<str.length;
		 * k++) { System.out.println(str[k]); }
		 * //System.out.println(votesList.get(i).options.get(j).getVoter()); } }
		 */
		
		//System.out.println("check: " +votesList.get(0).checkUserUpdate("23232323"));
	}

	// 사용자 정보 확인
	public void userInfo() throws IOException, InterruptedException {
		System.out.println("<사용자 정보>");
		System.out.println("이름: " + user.getName());
		System.out.println("전화번호: 010-" + user.getPhone().substring(0, 4) + "-" + user.getPhone().substring(4, 8));
		System.out.println("(3초 후에 메인 메뉴로 돌아갑니다…)");
		try {
			Thread.sleep(Constant.gotoMenuSleepTime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Check Exception");
		}
	}

	// 투표가 비어있는지 확인
	public boolean isEmpty() {
		if (votesList.size() == 0) {
			return true;
		}
		return false;
	}

	// 투표 메뉴
	void startMainMenu() throws IOException, InterruptedException {
		Scanner sc = new Scanner(System.in);

		String line = "";
		boolean exitFlag = false;
		while (!exitFlag) {
			initVotes(); // 투표 정보 갱신
			Constant.clearConsole(); //---추가구문
			System.out.println("<메인메뉴>");
			System.out.println("안녕하세요." + user.getName() + "님");
			System.out.println("1. 사용자 정보");
			System.out.println("2. 투표하기");
			System.out.println("3. 투표 만들기");
			System.out.println("4. 등록투표 끝내기");
			System.out.println("5. 투표 결과 확인");
			System.out.println("6. 종료");
			while (true) {
				System.out.print("선택: ");

				line = sc.nextLine();
				line = line.trim();

				// 유효 범위 확인 
				if (!(line.matches(Constant.menuOptionRegex))) {
					System.out.println(Constant.inputErrorMsg);
					continue;
				}
				break;
			}

			Constant.clearConsole();
			switch (Integer.parseInt(line)) {
			case 1:
				// 사용자정보 확인
				userInfo();
				break;
			case 2:
				// 투표하기
				Voting voting = new Voting(user, votesList);
				voting.DoVoting();
				break;
			case 3:
				// 투표 만들기
				CreateVote createVote = new CreateVote(user.getPhone());
				createVote.makeVote();
				break;
			case 4:
				// 등록 투표 끝내기
				EndVote endVote = new EndVote(user.getPhone(), user.getName());
				endVote.showMyVotes();
				break;
			case 5:
				// 투표 결과 확인
				EndVote endVote2 = new EndVote(user.getPhone(), user.getName());
				endVote2.showEndedVotes();
				break;
			case 6:
				// 종료
				exitFlag = true;
				break;
			}
			Constant.clearConsole();
		}
		sc.close();
	}
}
