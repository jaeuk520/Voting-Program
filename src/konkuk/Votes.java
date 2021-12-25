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

//��ǥ �����
public class Votes {
	boolean normal; // ���� ���� ����
	
	String voteCode; // ��ǥ �ڵ� (=���� �̸�)
	String voteName; // ��ǥ �̸�
	String makerNum; // ��������
	boolean endOfVotes; // ��ǥ ���� ����
	int numAllow; // ��ǥ ���� Ƚ��
	boolean addAllow; // ����� ������ �߰� ���
	int updateVer; //��ǥ ������Ʈ ����
	int multipleVote; //�� �������� ��ǥ ������ Ƚ��(��ǥ �ѵ�)
	boolean increaseVoteNumAllow; //�������� �߰��� �� �� ��ǥ Ƚ�� ���� ����
	ArrayList<VoteOptions> options = new ArrayList<>(); // ������ �� ������, ��ǥ ��
	
	static ArrayList<String> userPhones = getUserPhones(); //��ȭ ��ȣ ��ȿ�� �˻縦 ���� �߰��� static ����
	
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
					// ���� ������ �� �� ����
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

	/*���ο� ��ǥ ����ÿ� ���
	 * ��ǥ�ڰ� ���� ���  voter�� null ���� ����*/
	Votes(String voteCode, String voteName, String makerNum, 
			int numAllow, boolean addAllow, String[] options,
			int multipleVote, boolean increaseVoteNumAllow) {
		this.voteCode = voteCode;
		this.voteName = voteName;
		this.makerNum = makerNum;
		this.addAllow = addAllow;    				
		this.endOfVotes = false; // ���� ���� ���̹Ƿ� false
		this.numAllow = numAllow;
		this.multipleVote = multipleVote;
		this.increaseVoteNumAllow = increaseVoteNumAllow;
		
		this.updateVer = 0; //default

		for (int i = 0; i < options.length; i++) {
			VoteOptions vo = new VoteOptions(options[i], Constant.haveNoVoter);
			this.options.add(vo);
		}

	}

	// fileName���� .txt Ȯ���� ���� �پ�����
	Votes(String fileName) throws InterruptedException {
		this.normal = loadVote(fileName);
		
		/*
		 * for (int i=0; i<userPhones.size(); i++) {
		 * System.out.println(userPhones.get(i)); }
		 */
	}
	
	//���� �о vote�� ����
	public boolean loadVote(String fileName) throws InterruptedException {
		//�ɼ� �ʱ�ȭ
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

			// line1: ��ǥ �̸�, ���� ����
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
				System.out.println(fileName + ": ���� ������ ���� ������ �߸��Ǿ��ֽ��ϴ�. : "
						+ this.makerNum);
				return false;
			}

			// line2: ��ǥ���Ῡ��, �����ǥȽ��, �������߰����ɿ���
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

			// line3-n: �������̸�, �����ڵ�
			
			while (!((line = br.readLine()) == null)) {
				if (!line.matches(Constant.voteLine3Regex)) {
					Constant.printFileError(fileName);
					return false;
				}
				st = new StringTokenizer(line, Constant.fieldTokenizerDelim);
				String _name = st.nextToken();
				String voterList = st.nextToken();
				//null������ Ȯ��. �ƴ϶�� ��ȭ��ȣ ������ �´��� Ȯ��
				if (!voterList.equals(Constant.haveNoVoter)) {
					StringTokenizer st2 = new StringTokenizer(voterList, Constant.voterTokenizerDelim);
					while (st2.hasMoreTokens()) {
						String t_voter = st2.nextToken();
						if (!t_voter.matches(Constant.voterRegex)) {
							Constant.printFileError(fileName);
							return false;
						}
						else if (!userPhones.contains(t_voter.split(Constant.updateVerTokenizerDelim)[0])) {
							System.out.println(fileName + ": ���� ������ ���� ������ �߸��Ǿ��ֽ��ϴ�.2 : "
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
				//null ���̸� pass
				if (this.options.get(i).getVoterArray()[0].equals(Constant.haveNoVoter)) {
					continue;
				}
				st = new StringTokenizer(this.options.get(i).getVoterArray()[j], Constant.updateVerTokenizerDelim);
				if (userNum.equals(st.nextToken())) {
					return Integer.parseInt(st.nextToken());
				}
			}
		}
		//�� ã���� ���
		return -1;
	}
	
	/*userNum�� ���� ������ ����ǥ�� �������� Ȯ�����ִ� �Լ�
	 * ��ǥ �������� �߰��� �ȵż� ����ǥ�� �Ұ��ϰų�,
	 * �ش� ������ ���� ��� false ��ȯ*/
	public boolean checkUserUpdate(String userNum) {
		int ver = this.getUserUpdateVer(userNum);
		
		if (ver == -1) {
			return false;
		}
		
		return (this.updateVer > ver ? true : false);
	}

	/* ��ǥ �������鸸 �迭�� ��ȯ�ϴ� �Լ� */
	public String[] getOptionName() {
		String[] str = new String[options.size()];
		for (int i = 0; i < str.length; i++) {
			str[i] = options.get(i).getOptionName();
		}
		return str;
	}

	/* ��ǥ�ڵ��� �迭�� ��ȯ�ϴ� �Լ� 
	 * String optionName: ������ �̸� 
	 * �ش� �������� ã�� ���� ��� null�� ��ȯ*/
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
	

	//�׽�Ʈ �Լ�
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
