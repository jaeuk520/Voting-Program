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
	
	//��ǥ �����
	public void makeVote() {
		//��ǥ �̸� �Է�
		String name = inputVoteName();
		if (name.equals(Constant.quitChar)) {
			return;
		}
		
		//��ǥ ������ �Է�
		String[] options = inputOptions();
		if (options[0].equals(Constant.quitChar)) {
			return;
		}
		
		//��ǥ �ѵ� �Է�
		int multipleVote = inputMultipleVote();
		if (multipleVote == Constant.quitInt) {
			return;
		}
		
		//��ǥ ������ �߰� ��� �Է�
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
		
		//��ǥ ���� Ƚ�� �Է�
		int numAllow = inputAllowNum(multipleVote, options.length);
		if (numAllow == Constant.quitInt) {
			return;
		}
		
		//���� ����
		makeVoteFile(name, this.phoneNum, options, addAllow,
				numAllow, multipleVote, increaseVoteNumAllow);
		
		completeMake();
	}
	
	//��ǥ �̸� �Է�
	String inputVoteName() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("<��ǥ �����>");
		System.out.println("*��� ������ �Է� �Ŀ� 'end'�Է�*");
		String name = null;
		while (true) {
			try {
				System.out.print("��ǥ �̸� �Է�: ");
				name = br.readLine();
				name = name.trim();
				
				if (name.equals("")) {
					System.out.println("�Է� ���Ŀ� ���� �ʽ��ϴ�. �ٽ� �Է����ּ���.");
					continue;
				}
				
				if (name.equals(Constant.quitChar)) {
					return Constant.quitChar;
				}
				
				if (!(name.matches(Constant.voteNameRegex))) {
					System.out.println("�Է� ���Ŀ� ���� �ʽ��ϴ�. �ٽ� �Է����ּ���.");
					continue;
				}
				
				// ���� 20 �ʰ��� ���
				if (name.length() > 20) {
					System.out.println("�Է� ���Ŀ� ���� �ʽ��ϴ�. �ٽ� �Է����ּ���.");
					continue;
				}
				
				break;
			} catch (Exception e) {
				System.out.println("�Է� ���Ŀ� ���� �ʽ��ϴ�. �ٽ� �Է����ּ���.");
			}
		}
		return name;
	}
	
	//��ǥ ������ �Է�
	String[] inputOptions() {
		ArrayList<String> arr = new ArrayList<>();
		int n = 1;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = "";
		while (true) {
			try {
				System.out.print("��ǥ ������ �Է� "+ n + "��°: ");
				line = br.readLine();
				line = line.trim();
				
				if(line.equals("")) {
					System.out.println("�Է� ���Ŀ� ���� �ʽ��ϴ�. �ٽ� �Է����ּ���.");
					continue;
				}
				
				if (line.equals(Constant.quitChar)) {
					String[] str = {Constant.quitChar};
					return str;
				}
				if (line.equals(Constant.endAddOptionChar)) {
					if (n <= Constant.minOptionNum) {
						System.out.println("�ּ� �������� 2�� �Դϴ�.");
						continue;
					}
					break;
				}
				
				//Ȯ��
				if (!(line.matches(Constant.voteOptionsRegex))) {
					System.out.println("�Է� ���Ŀ� ���� �ʽ��ϴ�. �ٽ� �Է����ּ���.");
					continue;
				}
				
				//���� 20 �ʰ��� ���
				if(line.length() > Constant.maxOptionLength) {
					System.out.println("�Է� ���Ŀ� ���� �ʽ��ϴ�. �ٽ� �Է����ּ���.");
					continue;
				}
				
				n++;
				arr.add(line);
				
				if (n>Constant.maxOptionNum) {
					System.out.println("�ִ� ������ ������ " + Constant.maxOptionNum + "�� �Դϴ�.");
					break;
				}
				
			} catch (Exception e) {
				System.out.println("�Է� ���Ŀ� ���� �ʽ��ϴ�. �ٽ� �Է����ּ���.");
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
	
	//��ǥ �ѵ� �Է�
	int inputMultipleVote() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line ="";
		int multipleVote;
		
		while (true) {
			try {
				System.out.print("�� ������ �ߺ� ��ǥ ��� Ƚ�� (��ǥ �ѵ�, " 
						+ Constant.minMultipleVoteNum + "~" 
						+ Constant.maxMultipleVoteNum + "�� ��): ");
				
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
				
				//���� Ȯ��
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
	
	//��ǥ ������ �߰� ��� ���� �Է�
	int inputAllowAdd() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = "";
		int allow;
		while (true) {
			try {
				System.out.println("��ǥ�� ������ �߰� ���");
				System.out.println("1. ��\n2. �ƴϿ�");
				System.out.print("����(1 or 2): ");
				
				line = br.readLine();
				line = line.trim();
				
				if(line.equals("")) {
					System.out.println("�Է� ���Ŀ� ���� �ʽ��ϴ�. �ٽ� �Է����ּ���.");
					continue;
				}
				
				if (line.equals(Constant.quitChar)) {
					return Constant.quitInt;
				}
				
				if (!(line.matches(Constant.voteAllowAddRegex))) {
					System.out.println("�Է� ���Ŀ� ���� �ʽ��ϴ�. �ٽ� �Է����ּ���.");
					continue;
				}
				
				//1 or 2 Ȯ��
				if (line.equals(Constant.addAllowTrue)) {
					allow = 1;
					break;
				} else if (line.equals(Constant.addAllowFalse)) {
					allow = 2;
					break;
				} else {
					System.out.println("�Է� ���Ŀ� ���� �ʽ��ϴ�. �ٽ� �Է����ּ���.");
					continue;
				}
			} catch (Exception e) {
				System.out.println("�Է� ���Ŀ� ���� �ʽ��ϴ�. �ٽ� �Է����ּ���.");
				continue;
			}
		}
		return allow;
	}
	
	//������ �߰��� �� ��ǥ Ƚ�� �ø��� ���� �Է�
	int inputIncreaseVoteNumAllow() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int increaseVoteNumAllow;
		String line ="";
		
		while (true) {
			try {
				System.out.println("��ǥ�� ������ �߰� �� �� ��ǥ ��� Ƚ�� �ø���");
				System.out.println("('��'�� �����ϸ� �� ��ǥ ���� Ƚ���� �������� �߰��� �� ���� ��ǥ �ѵ���ŭ �þ�� �˴ϴ�.)");
				System.out.println("1. ��");
				System.out.println("2. �ƴϿ�");
				System.out.print("����: ");
				
				line = br.readLine();
				line = line.trim();
				
				if (line.equals("")) {
					System.out.println(Constant.inputErrorMsg);
					continue;
				}
				
				//'q' Ȯ��
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
	
	//�� ��ǥ ��� Ƚ�� �Է�
	int inputAllowNum(int multipleVoteNum, int optionsNum) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int allowNum = 0;
		String line = "";
		int maxNum = multipleVoteNum*optionsNum;
		while (true) {
			try {
				//n-1: ��� �ϳ��� ��ǥ �ϴ°� ����
				System.out.print("�� ��ǥ ��� Ƚ��(" + multipleVoteNum + " �̻� " + 
				maxNum + "�̸�): ");
				line = br.readLine();
				line = line.trim();
				
				if(line.equals("")) {
					System.out.println("�Է� ���Ŀ� ���� �ʽ��ϴ�. �ٽ� �Է����ּ���.");
					continue;
				}
				
				if (line.equals(Constant.quitChar)) {
					return Constant.quitInt;
				}
				
				if (!(line.matches(Constant.voteAllowNumRegex))) {
					System.out.println("�Է� ���Ŀ� ���� �ʽ��ϴ�. �ٽ� �Է����ּ���.");
					continue;
				}
				
				allowNum = Integer.parseInt(line);
				
				//n�������� Ȯ��
				if(!(allowNum >= multipleVoteNum && allowNum < maxNum)) {
					System.out.println("�Է� ���Ŀ� ���� �ʽ��ϴ�. �ٽ� �Է����ּ���.");
					continue;
				}
				
				break;
				
			} catch (Exception e) {
				System.out.println("�Է� ���Ŀ� ���� �ʽ��ϴ�. �ٽ� �Է����ּ���.");
			}
		}
		return allowNum;
	}
	
	//��ǥ ���� ����
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
			
			//line1: ��ǥ�̸� ��ǥ��  ����
			line = vote.getVoteName() 
					+ Constant.fieldTokenizerDelim + vote.getMakerNum()
					+ Constant.fieldTokenizerDelim + vote.getUpdateVer();
			sb.append(line+"\n");
			
			//line2: ��ǥ���Ῡ�� ��ǥ���Ƚ�� �������߰����ɿ��� ����
			line = Integer.toString(vote.getEndOfVotesInt())
					+ Constant.fieldTokenizerDelim + Integer.toString(vote.getNumAllow())
					+ Constant.fieldTokenizerDelim + Integer.toString(vote.getAddAllowInt())
					+ Constant.fieldTokenizerDelim + Integer.toString(vote.getMultipleVote())
					+ Constant.fieldTokenizerDelim + Integer.toString(vote.getIncreaseVoteNumALlowInt());
			sb.append(line+"\n");
			
			//line3-n: �������̸� ��ǥ�� ����
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
	
	//��ǥ ���� �Ϸ�
	void completeMake() {
		System.out.println("��ǥ ���� �Ϸ�!");
		System.out.println("3�� �Ŀ� ���� �޴��� ���ư��ϴ�...");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
