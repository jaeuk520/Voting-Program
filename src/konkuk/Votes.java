package konkuk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.StringTokenizer;

//투표 만들기
public class Votes {
	boolean normal; // 파일 정상 여부
	
	String voteCode; // 투표 코드 (=파일 이름)
	String voteName; // 투표 이름
	String makerNum; // 만든유저
	boolean endOfVotes; // 투표 종료 여부
	int numAllow; // 투표 가능 횟수
	boolean addAllow; // 사용자 선택지 추가 허용
	int updateVer; //투표 업데이트 버전
	int multipleVote; //한 선택지당 투표 가능한 횟수(몰표 한도)
	boolean increaseVoteNumAllow; //선택지가 추가될 시 총 투표 횟수 증가 여부
	ArrayList<VoteOptions> options = new ArrayList<>(); // 선택지 및 선택자, 득표 수
	
	static ArrayList<String> userPhones = getUserPhones(); //전화 번호 유효성 검사를 위해 추가한 static 변수
	
	static ArrayList<String> getUserPhones() {
		//System.out.println("static");
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

	/*새로운 투표 만들시에 사용
	 * 투표자가 없을 경우  voter에 null 값을 가짐*/
	Votes(String voteCode, String voteName, String makerNum, 
			int numAllow, boolean addAllow, String[] options,
			int multipleVote, boolean increaseVoteNumAllow) {
		this.voteCode = voteCode;
		this.voteName = voteName;
		this.makerNum = makerNum;
		this.addAllow = addAllow;    				
		this.endOfVotes = false; // 새로 만든 것이므로 false
		this.numAllow = numAllow;
		this.multipleVote = multipleVote;
		this.increaseVoteNumAllow = increaseVoteNumAllow;
		
		this.updateVer = 0; //default

		for (int i = 0; i < options.length; i++) {
			VoteOptions vo = new VoteOptions(options[i], Constant.haveNoVoter);
			this.options.add(vo);
		}

	}

	// fileName에는 .txt 확장자 까지 붙어있음
	Votes(String fileName) throws InterruptedException {
		this.normal = loadVote(fileName);
		
		/*
		 * for (int i=0; i<userPhones.size(); i++) {
		 * System.out.println(userPhones.get(i)); }
		 */
	}
	
	//파일 읽어서 vote에 저장
	public boolean loadVote(String fileName) throws InterruptedException {
		//옵션 초기화
		options.clear();
		
		this.voteCode = fileName;

		File file = new File(Constant.voteDirRoute + fileName);
		// for test
		if (!(file.exists())) {
			System.out.println("error");
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), Charset.forName("UTF8")));
			String line = "";
			/*************************************************/

			// line1: 투표 이름, 만든 유저
			line = br.readLine();
			if (!line.matches(Constant.voteLine1Regex)) {
				Constant.printFileError(fileName);
				return false;
			}
			StringTokenizer st = new StringTokenizer(line, Constant.fieldTokenizerDelim);
			this.voteName = st.nextToken();
			this.makerNum = st.nextToken();
			this.updateVer = Integer.parseInt(st.nextToken());
			if (!userPhones.contains(this.makerNum)) {
				System.out.println(fileName + ": 파일 내부의 유저 정보가 잘못되어있습니다. : "
						+ this.makerNum);
				return false;
			}

			// line2: 투표종료여부, 허용투표횟수, 선택지추가가능여부
			line = br.readLine();
			if (!line.matches(Constant.voteLine2Regex)) {
				Constant.printFileError(fileName);
				return false;
			}
			st = new StringTokenizer(line, Constant.fieldTokenizerDelim);
			this.endOfVotes = (st.nextToken().equals(Constant.endOfVoteTrue) ? true : false);
			this.numAllow = Integer.parseInt(st.nextToken());
			this.addAllow = (st.nextToken().equals(Constant.addAllowTrue) ? true : false);
			this.multipleVote = Integer.parseInt(st.nextToken());
			this.increaseVoteNumAllow = (st.nextToken().equals(Constant.increaseVoteNumTrue) ? true : false);

			// line3-n: 선택지이름, 선택자들
			
			while (!((line = br.readLine()) == null)) {
				if (!line.matches(Constant.voteLine3Regex)) {
					Constant.printFileError(fileName);
					return false;
				}
				st = new StringTokenizer(line, Constant.fieldTokenizerDelim);
				String _name = st.nextToken();
				String voterList = st.nextToken();
				//null값인지 확인. 아니라면 전화번호 형식이 맞는지 확인
				if (!voterList.equals(Constant.haveNoVoter)) {
					StringTokenizer st2 = new StringTokenizer(voterList, Constant.voterTokenizerDelim);
					while (st2.hasMoreTokens()) {
						String t_voter = st2.nextToken();
						if (!t_voter.matches(Constant.voterRegex)) {
							Constant.printFileError(fileName);
							return false;
						}
						else if (!userPhones.contains(t_voter.split(Constant.updateVerTokenizerDelim)[0])) {
							System.out.println(fileName + ": 파일 내부의 유저 정보가 잘못되어있습니다.2 : "
						+ t_voter.split(Constant.updateVerTokenizerDelim)[0]);
							return false;
						}
					}
				}
				options.add(new VoteOptions(_name, voterList));
			}
			/********************************************/
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
				}
		}
		
		return true;
		
		
	}

	public String getVoteCode() {
		return this.voteCode;
	}

	public String getVoteName() {
		return this.voteName;
	}

	public String getMakerNum() {
		return this.makerNum;
	}

	public boolean getEndOfVotes() {
		return this.endOfVotes;
	}

	public int getEndOfVotesInt() {
		return (this.endOfVotes == true ? 1 : 2);
	}

	public int getNumAllow() {
		return this.numAllow;
	}

	public boolean getAddAllow() {
		return this.addAllow;
	}

	public int getAddAllowInt() {
		return (this.addAllow == true ? 1 : 2);
	}
	
	public int getMultipleVote() {
		return this.multipleVote;
	}
	
	public int getUpdateVer() {
		return this.updateVer;
	}
	
	public int getIncreaseVoteNumALlowInt() {
		return (this.increaseVoteNumAllow ? 
				Integer.parseInt(Constant.increaseVoteNumTrue) 
				: Integer.parseInt(Constant.increaseVoteNumFalse));
	}
	
	public boolean getIncreaseVoteNumAllow() {
		return this.increaseVoteNumAllow;
	}
	
	public boolean getNormal() {
		return this.normal;
	}
	
	public int getUserUpdateVer(String userNum) {
		StringTokenizer st = null;
		
		for (int i=0; i<this.options.size(); i++) {
			for (int j=0; j<this.options.get(i).getVoterArray().length; j++) {
				//null 값이면 pass
				if (this.options.get(i).getVoterArray()[0].equals(Constant.haveNoVoter)) {
					continue;
				}
				st = new StringTokenizer(this.options.get(i).getVoterArray()[j], Constant.updateVerTokenizerDelim);
				if (userNum.equals(st.nextToken())) {
					return Integer.parseInt(st.nextToken());
				}
			}
		}
		//못 찾았을 경우
		return -1;
	}
	
	/*userNum에 대한 유저가 재투표가 가능한지 확인해주는 함수
	 * 투표 선택지가 추가가 안돼서 재투표가 불가하거나,
	 * 해당 유저가 없을 경우 false 반환*/
	public boolean checkUserUpdate(String userNum) {
		int ver = this.getUserUpdateVer(userNum);
		
		if (ver == -1) {
			return false;
		}
		
		return (this.updateVer > ver ? true : false);
	}

	/* 투표 선택지들만 배열로 반환하는 함수 */
	public String[] getOptionName() {
		String[] str = new String[options.size()];
		for (int i = 0; i < str.length; i++) {
			str[i] = options.get(i).getOptionName();
		}
		return str;
	}

	/* 투표자들을 배열로 반환하는 함수 
	 * String optionName: 선택지 이름 
	 * 해당 선택지를 찾지 못할 경우 null값 반환*/
	public String[] getVoter(String optionName) {
		for (int i = 0; i < options.size(); i++) {
			if (optionName.equals(options.get(i).getOptionName())) {
				String[] str = options.get(i).getVoterArray();
				return str;
			}
		}
		return null;
	}

	public ArrayList<VoteOptions> getOptions() {
		return this.options;
	}
	

	//테스트 함수
	public void showVote() { 
		System.out.println(this.voteCode);
		System.out.println(this.voteName); 
		System.out.println(this.makerNum);
		System.out.println(this.updateVer);
		
		System.out.println(this.endOfVotes); 
		System.out.println(this.numAllow);
		System.out.println(this.addAllow);
		System.out.println(this.multipleVote);
		System.out.println(this.increaseVoteNumAllow);
		
		for(int i=0; i<options.size(); i++) {
			System.out.println(options.get(i).getOptionName());
			System.out.println(options.get(i).getVoter()); 
		}
	}
}
