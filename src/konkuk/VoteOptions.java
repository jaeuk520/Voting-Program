package konkuk;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class VoteOptions {
	int num; //득표 수
	String optionName; //선택지 이름
	ArrayList<String> voter = new ArrayList<>(); //투표자들
	
	VoteOptions(String optionName, String voter) {
		this.optionName = optionName;
		if (voter.equals(Constant.haveNoVoter)) {
			num = 0;
		} else {
			StringTokenizer st = new StringTokenizer(voter, Constant.voterTokenizerDelim);
			this.num = st.countTokens();
			while (st.hasMoreTokens()) {
				this.voter.add(st.nextToken());
			}
		}
	}
	
	public String getOptionName() {
		return this.optionName;
	}
	
	public int getNum() {
		return this.num;
	}
	
	//투표자들을 배열 형태로 반환 없으면 { "null" } 배열 반환
	public String[] getVoterArray() {
		if (this.num==0) {
			String[] str = { Constant.haveNoVoter };
			return str;
		}
		String[] str = new String[voter.size()];
		int size = 0;
		for (String temp: voter) {
			str[size++] = temp;
		}
		return str;
	}
	
	//투표자들을 반환 (구분자는 +, 투표자가 없을 경우 null(String) 값 반환)
	public String getVoter() {
		if (this.num==0) {
			return Constant.haveNoVoter;
		}
		String temp = "";
		for (int i=0; i<voter.size(); i++) {
			temp+=voter.get(i)+Constant.voterTokenizerDelim;
		}
		temp = temp.substring(0, temp.length()-1); //"+"지우기
		return temp;
	}

	public void AddVoter(String voter){
		this.voter.add(voter);
		return;
	}
	
	public String[] getVoterUser() {
		if (this.num == 0) {
			String[] str = { Constant.haveNoVoter };
			return  str;
		}
		String[] voteUsers = new String[this.voter.size()];
		String[] temp;
		
		for (int i=0; i<this.voter.size(); i++) {
			temp = this.voter.get(i).split(Constant.updateVerTokenizerDelim);
			voteUsers[i] = temp[0];
		}
		return voteUsers;
	}
}
