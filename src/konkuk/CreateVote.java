package konkuk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CreateVote {
	String phoneNum;
	
	CreateVote (String phoneNum) {
		this.phoneNum = phoneNum;
	}
	
	//투표 만들기
	public void makeVote() {
		//투표 이름 입력
		String name = inputVoteName();
		if (name.equals(Constant.quitChar)) {
			return;
		}
		
		//투표 선택지 입력
		String[] options = inputOptions();
		if (options[0].equals(Constant.quitChar)) {
			return;
		}
		
		//몰표 한도 입력
		int multipleVote = inputMultipleVote();
		if (multipleVote == Constant.quitInt) {
			return;
		}
		
		//투표 선택지 추가 허용 입력
		boolean addAllow;
		boolean increaseVoteNumAllow;
		int _addAllow = inputAllowAdd();
		if (_addAllow == 1) {
			addAllow = true;
			int _increaseVoteNumAllow = inputIncreaseVoteNumAllow();
			
			if (_increaseVoteNumAllow == Constant.quitInt) {
				return;
			}
			
			increaseVoteNumAllow = 
					(_increaseVoteNumAllow == 1 ? true : false);
		} else if (_addAllow == 2) {
			addAllow = false;
			increaseVoteNumAllow = false;
		} else {
			return;
		}
		
		//투표 선택 횟수 입력
		int numAllow = inputAllowNum(multipleVote, options.length);
		if (numAllow == Constant.quitInt) {
			return;
		}
		
		//파일 생성
		makeVoteFile(name, this.phoneNum, options, addAllow,
				numAllow, multipleVote, increaseVoteNumAllow);
		
		completeMake();
	}
	
	//투표 이름 입력
	String inputVoteName() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("<투표 만들기>");
		System.out.println("*모든 선택지 입력 후에 'end'입력*");
		String name = null;
		while (true) {
			try {
				System.out.print("투표 이름 입력: ");
				name = br.readLine();
				name = name.trim();
				
				if (name.equals("")) {
					System.out.println("입력 형식에 맞지 않습니다. 다시 입력해주세요.");
					continue;
				}
				
				if (name.equals(Constant.quitChar)) {
					return Constant.quitChar;
				}
				
				if (!(name.matches(Constant.voteNameRegex))) {
					System.out.println("입력 형식에 맞지 않습니다. 다시 입력해주세요.");
					continue;
				}
				
				// 길이 20 초과일 경우
				if (name.length() > 20) {
					System.out.println("입력 형식에 맞지 않습니다. 다시 입력해주세요.");
					continue;
				}
				
				break;
			} catch (Exception e) {
				System.out.println("입력 형식에 맞지 않습니다. 다시 입력해주세요.");
			}
		}
		return name;
	}
	
	//투표 선택지 입력
	String[] inputOptions() {
		ArrayList<String> arr = new ArrayList<>();
		int n = 1;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = "";
		while (true) {
			try {
				System.out.print("투표 선택지 입력 "+ n + "번째: ");
				line = br.readLine();
				line = line.trim();
				
				if(line.equals("")) {
					System.out.println("입력 형식에 맞지 않습니다. 다시 입력해주세요.");
					continue;
				}
				
				if (line.equals(Constant.quitChar)) {
					String[] str = {Constant.quitChar};
					return str;
				}
				if (line.equals(Constant.endAddOptionChar)) {
					if (n <= Constant.minOptionNum) {
						System.out.println("최소 선택지는 2개 입니다.");
						continue;
					}
					break;
				}
				
				//확인
				if (!(line.matches(Constant.voteOptionsRegex))) {
					System.out.println("입력 형식에 맞지 않습니다. 다시 입력해주세요.");
					continue;
				}
				
				//길이 20 초과인 경우
				if(line.length() > Constant.maxOptionLength) {
					System.out.println("입력 형식에 맞지 않습니다. 다시 입력해주세요.");
					continue;
				}
				
				n++;
				arr.add(line);
				
				if (n>Constant.maxOptionNum) {
					System.out.println("최대 선택지 개수는 " + Constant.maxOptionNum + "개 입니다.");
					break;
				}
				
			} catch (Exception e) {
				System.out.println("입력 형식에 맞지 않습니다. 다시 입력해주세요.");
			}
		} 
		
		n--;
		String[] str = new String[arr.size()];
		int size = 0;
		for(String temp: arr) {
			str[size++] = temp;
		}
		return str;
	}
	
	//몰표 한도 입력
	int inputMultipleVote() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line ="";
		int multipleVote;
		
		while (true) {
			try {
				System.out.print("한 선택지 중복 투표 허용 횟수 (몰표 한도, " 
						+ Constant.minMultipleVoteNum + "~" 
						+ Constant.maxMultipleVoteNum + "의 값): ");
				
				line = br.readLine();
				line = line.trim();
				
				if (line.equals("")) {
					System.out.println(Constant.inputErrorMsg);
					continue;
				}
				
				if (line.equals(Constant.quitChar)) {
					System.out.println(Constant.inputErrorMsg);
					return Constant.quitInt;
				}
				
				if (!line.matches(Constant.numRegex)) {
					System.out.println(Constant.inputErrorMsg);
					continue;
				}
				
				multipleVote = Integer.parseInt(line);
				
				//범위 확인
				if (!(multipleVote <= Constant.maxMultipleVoteNum
						&& multipleVote >= Constant.minMultipleVoteNum)) {
					System.out.println(Constant.inputErrorMsg);
					continue;
				}
				
				break;
			}
			catch (Exception e) {
				System.out.println(Constant.inputErrorMsg);
				continue;
			}
		}
		
		return multipleVote;
	}
	
	//투표 선택지 추가 허용 여부 입력
	int inputAllowAdd() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = "";
		int allow;
		while (true) {
			try {
				System.out.println("투표자 선택지 추가 허용");
				System.out.println("1. 예\n2. 아니요");
				System.out.print("선택(1 or 2): ");
				
				line = br.readLine();
				line = line.trim();
				
				if(line.equals("")) {
					System.out.println("입력 형식에 맞지 않습니다. 다시 입력해주세요.");
					continue;
				}
				
				if (line.equals(Constant.quitChar)) {
					return Constant.quitInt;
				}
				
				if (!(line.matches(Constant.voteAllowAddRegex))) {
					System.out.println("입력 형식에 맞지 않습니다. 다시 입력해주세요.");
					continue;
				}
				
				//1 or 2 확인
				if (line.equals(Constant.addAllowTrue)) {
					allow = 1;
					break;
				} else if (line.equals(Constant.addAllowFalse)) {
					allow = 2;
					break;
				} else {
					System.out.println("입력 형식에 맞지 않습니다. 다시 입력해주세요.");
					continue;
				}
			} catch (Exception e) {
				System.out.println("입력 형식에 맞지 않습니다. 다시 입력해주세요.");
				continue;
			}
		}
		return allow;
	}
	
	//선택지 추가시 총 투표 횟수 늘리기 여부 입력
	int inputIncreaseVoteNumAllow() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int increaseVoteNumAllow;
		String line ="";
		
		while (true) {
			try {
				System.out.println("투표자 선택지 추가 시 총 투표 허용 횟수 늘리기");
				System.out.println("('예'를 선택하면 총 투표 가능 횟수가 선택지가 추가될 때 마다 몰표 한도만큼 늘어나게 됩니다.)");
				System.out.println("1. 예");
				System.out.println("2. 아니요");
				System.out.print("선택: ");
				
				line = br.readLine();
				line = line.trim();
				
				if (line.equals("")) {
					System.out.println(Constant.inputErrorMsg);
					continue;
				}
				
				//'q' 확인
				if (line.equals(Constant.quitChar)) {
					return Constant.quitInt;
				}
				
				if (!line.matches(Constant.increaseVoteNumRegex)) {
					System.out.println(Constant.inputErrorMsg);
					continue;
				}
				
				if (line.equals(Constant.increaseVoteNumTrue) ) {
					increaseVoteNumAllow = 1;
					break;
				}
				
				else if (line.equals(Constant.increaseVoteNumFalse)) {
					increaseVoteNumAllow = 2;
					break;
				}
				
				System.out.println(Constant.inputErrorMsg);
				
			}
			catch (Exception e) {
				System.out.println(Constant.inputErrorMsg);
				continue;
			}
		}
		return increaseVoteNumAllow;
	}
	
	//총 투표 허용 횟수 입력
	int inputAllowNum(int multipleVoteNum, int optionsNum) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int allowNum = 0;
		String line = "";
		int maxNum = multipleVoteNum*optionsNum;
		while (true) {
			try {
				//n-1: 모두 하나씩 투표 하는걸 방지
				System.out.print("총 투표 허용 횟수(" + multipleVoteNum + " 이상 " + 
				maxNum + "미만): ");
				line = br.readLine();
				line = line.trim();
				
				if(line.equals("")) {
					System.out.println("입력 형식에 맞지 않습니다. 다시 입력해주세요.");
					continue;
				}
				
				if (line.equals(Constant.quitChar)) {
					return Constant.quitInt;
				}
				
				if (!(line.matches(Constant.voteAllowNumRegex))) {
					System.out.println("입력 형식에 맞지 않습니다. 다시 입력해주세요.");
					continue;
				}
				
				allowNum = Integer.parseInt(line);
				
				//n이하인지 확인
				if(!(allowNum >= multipleVoteNum && allowNum < maxNum)) {
					System.out.println("입력 형식에 맞지 않습니다. 다시 입력해주세요.");
					continue;
				}
				
				break;
				
			} catch (Exception e) {
				System.out.println("입력 형식에 맞지 않습니다. 다시 입력해주세요.");
			}
		}
		return allowNum;
	}
	
	//투표 파일 생성
	void makeVoteFile(String voteName, String makerNum, 
			String[] options, boolean addAllow , int numAllow,
			int multipleVote, boolean increaseVoteNumAllow) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		String fileName = sdf.format(new Date())+".txt";
		File file = new File(Constant.voteDirRoute+fileName);
		
		Votes vote = new Votes(fileName+".txt", voteName, makerNum, 
				numAllow, addAllow, options, multipleVote, increaseVoteNumAllow);
		
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, false),Charset.forName("UTF8")));
			
			StringBuilder sb = new StringBuilder("");
			String line="";
			
			//line1: 투표이름 투표자  저장
			line = vote.getVoteName() 
					+ Constant.fieldTokenizerDelim + vote.getMakerNum()
					+ Constant.fieldTokenizerDelim + vote.getUpdateVer();
			sb.append(line+"\n");
			
			//line2: 투표종료여부 투표허용횟수 선택지추가가능여부 저장
			line = Integer.toString(vote.getEndOfVotesInt())
					+ Constant.fieldTokenizerDelim + Integer.toString(vote.getNumAllow())
					+ Constant.fieldTokenizerDelim + Integer.toString(vote.getAddAllowInt())
					+ Constant.fieldTokenizerDelim + Integer.toString(vote.getMultipleVote())
					+ Constant.fieldTokenizerDelim + Integer.toString(vote.getIncreaseVoteNumALlowInt());
			sb.append(line+"\n");
			
			//line3-n: 선택지이름 투표자 저장
			for (int i=0; i<vote.getOptionName().length; i++) {
				line = vote.getOptions().get(i).getOptionName()
						+ Constant.fieldTokenizerDelim + vote.getOptions().get(i).getVoter();
				sb.append(line+"\n");
			}
			
			bw.write(sb.toString());
			bw.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bw!=null) try { bw.close(); } catch (IOException e) {}
		}
	}
	
	//투표 생성 완료
	void completeMake() {
		System.out.println("투표 생성 완료!");
		System.out.println("3초 후에 메인 메뉴로 돌아갑니다...");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
