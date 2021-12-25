package konkuk;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class VoteOptions {
	int num; //��ǥ ��
	String optionName; //������ �̸�
	ArrayList<String> voter = new ArrayList<>(); //��ǥ�ڵ�
	
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
	
	//��ǥ�ڵ��� �迭 ���·� ��ȯ ������ { "null" } �迭 ��ȯ
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
	
	//��ǥ�ڵ��� ��ȯ (�����ڴ� +, ��ǥ�ڰ� ���� ��� null(String) �� ��ȯ)
	public String getVoter() {
		if (this.num==0) {
			return Constant.haveNoVoter;
		}
		String temp = "";
		for (int i=0; i<voter.size(); i++) {
			temp+=voter.get(i)+Constant.voterTokenizerDelim;
		}
		temp = temp.substring(0, temp.length()-1); //"+"�����
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
