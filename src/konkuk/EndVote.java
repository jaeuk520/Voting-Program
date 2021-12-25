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
	String user_Phone_number;								//�α��ε� ���� ��ȭ��ȣ
	String user_name;										//�α��ε� ���� �̸�
	File directory;											//���ϵ��� ����ִ� ���丮
	File[] underDir; 										//���丮 ���� ���� ���
	Vector<HashMap<File, String>> this_userMade_FileList;  	//�α��ε� ������ ���� ��ǥ ���
	Vector<HashMap<File, String>> endedVotes;				//����� ��ǥ ���
	BufferedReader iterFileReader;							//���� �Է��� ���� ����
	BufferedWriter iterFileWriter;							//���� ����� ���� ����
	Scanner scan = new Scanner(System.in);
	
	static ArrayList<String> userPhones = getUserPhones(); //��ȭ ��ȣ ��ȿ�� �˻縦 ���� �߰��� static ����
	
	static ArrayList<String> getUserPhones() {
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
	
	//������
	public EndVote(String userPhoneNumber, String userName) {
		user_Phone_number = userPhoneNumber;
		user_name = userName;
		directory = new File(Constant.voteDirRoute);
		this_userMade_FileList = new Vector<>();
		endedVotes = new Vector<>();
	}
	
	//�޴� ���
	/*
	 * public void menu() { try { Users.clearConsole(); } catch (IOException |
	 * InterruptedException e1) { e1.printStackTrace(); }
	 * 
	 * System.out.println("4. �����ǥ ������"); System.out.println("5. ��ǥ ��� Ȯ��");
	 * System.out.println("6. ����");
	 * 
	 * // System.out.println("�����˻�!!!!! " + endedVotes.size());
	 * System.out.print("����: "); String actual_select = scan.nextLine(); String
	 * select = actual_select.trim(); try { if (select.equals("4")) {
	 * refreshFiles(); showMyVotes(); } else if (select.equals("5")) {
	 * refreshFiles(); showEndedVotes(); // System.out.println("������"); �����˻� } else
	 * if (select.equals("6")) { System.out.println("\n���α׷��� �����մϴ�");
	 * scan.nextLine(); System.exit(0); } else {
	 * System.out.println("�Է� ������ ���� �ʽ��ϴ�. �ٽ� �Է��� �ּ���"); scan.nextLine(); menu(); }
	 * } catch(IOException e) { e.printStackTrace(); }
	 * 
	 * }
	 */
	
	//���丮 ���� �ؽ�Ʈ���� ��� �ֽ�ȭ (����� ȣ�� �ʿ�)
	private void refreshFiles() throws IOException, InterruptedException {
		this_userMade_FileList = new Vector<>();
		endedVotes = new Vector<>(); // ���� �ʱ�ȭ
		underDir = directory.listFiles();
		
		for (int i = 0; i < underDir.length; i++) {
			iterFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(underDir[i]), "UTF8"));
			verifyVote(iterFileReader, underDir[i]);
	//		System.out.println(endedVotes.get(0).get(underDir[i])); 
	//		for (File k : endedVotes.get(0).keySet()) {
	//			System.out.println("Ű : " + k);
	//		} //�����˻�
			iterFileReader.close();
		}
	}
	//���丮 ������ �ؽ�Ʈ ���ϵ��� Ȯ���ϸ鼭 ������ ���� ��ǥ�� this_userMade_FileList��, ����� ��ǥ�� endedVotes�� �߰�
	private void verifyVote(BufferedReader br, File curFile) throws IOException, InterruptedException {
		String curLine = "";
		boolean isMyVote = false;
		HashMap<File, String> map = new HashMap<>();
		
		
		//ù��° ��
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
			System.out.println(curFile + ": ���� ������ ���� ������ �߸��Ǿ��ֽ��ϴ�. : "
					+ arr[1]);
			return;
		}
		
		//�ι�° ��
		curLine = br.readLine();
		/*****/
		if (!curLine.matches(Constant.voteLine2Regex)) {
			Constant.printFileError(curFile.getName());
			return;
		}
		arr = curLine.split("\\/");
		
		//3��° �� ���� ���� �´��� Ȯ�� - > 2��°�� ���� ������ �ʿ� �߰� arr �ǵ��� ����
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
							System.out.println(curFile + ": ���� ������ ���� ������ �߸��Ǿ��ֽ��ϴ�. : "
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
	 		System.out.println("<��ǥ �����ϱ�>");
			System.out.println("***" + user_name+"���� ���� ��ǥ***");
			for (int i = 0; i < this_userMade_FileList.size(); i++) {			
				for (File s : this_userMade_FileList.get(i).keySet()) {
					System.out.println(i + 1 + ". " + this_userMade_FileList.get(i).get(s));
				}
			}
			System.out.println("0. ���");
			System.out.print("������ ��ǥ ����: ");
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
					System.out.println("3�� �Ŀ� �ǵ��� ���ϴ�.");
					Thread.sleep(Constant.gotoMenuSleepTime);
					continue;
				}
				for (File s : this_userMade_FileList.get(selectedNum).keySet()) {
					System.out.println("'" + this_userMade_FileList.get(selectedNum).get(s) + "'��(��) ���� �����ðڽ��ϱ�?");
					System.out.println("1. ��");
					System.out.println("2. �ƴϿ�");
					System.out.print("����: ");
					
					String original_confirm = scan.nextLine();
					String confirm = original_confirm.trim();
					if (confirm.equals("1")) {
						terminateVotes(selectedNum);
						System.out.println("\n��ǥ�� �����߽��ϴ�.");
						System.out.println("<'" + this_userMade_FileList.get(selectedNum).get(s) + "' ��ǥ ���>");
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
						System.out.println("3�� �Ŀ� �ǵ��� ���ϴ�.");
						Thread.sleep(Constant.gotoMenuSleepTime);
						continue;
					}
				}
			}
			else {
				System.out.println(Constant.inputErrorMsg);
				System.out.println("3�� �Ŀ� �ǵ��� ���ϴ�.");
				Thread.sleep(Constant.gotoMenuSleepTime);
				continue;			
			}
		}
	}
	
	private void terminateVotes(int num) throws IOException {
		String oldPath = ""; 		//�� ���� ���
		for (File s : this_userMade_FileList.get(num).keySet()) {
			oldPath = s.getPath();
		}
		String curLine = ""; 		//���� ���� ��
		int lineCnt = 1; 			// ���� ����
		
		File oldFile = new File(oldPath);
		File newFile = new File(directory + "/temp.txt");
		newFile.createNewFile(); 	//temp.txt ���� ����
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
			System.out.println("<����� ��ǥ ��� Ȯ��>");
		//	System.out.println("�����˻�!!! " + endedVotes.size());
			for (int i = 0; i < endedVotes.size(); i++) {
				for (File s : endedVotes.get(i).keySet()) {
					System.out.println(i + 1 + ". " + endedVotes.get(i).get(s));
				}
			}
			System.out.println("0. ���");
			System.out.print("��� Ȯ���� ��ǥ ����: ");
			String actual_select = scan.nextLine();
			String selec = actual_select.trim();
			if (selec.equals(Constant.quitChar) || selec.equals("0")) {
				return;
			}
			else if (isNumeric(selec)) {
				int selecNum = Integer.parseInt(selec) - 1;
				if (endedVotes.size() <= selecNum || selecNum < 0) {
					System.out.println(Constant.inputErrorMsg);
					System.out.println("3�� �Ŀ� �ǵ��� ���ϴ�.");
					Thread.sleep(Constant.gotoMenuSleepTime);
					continue;
				}
				for (File s : endedVotes.get(selecNum).keySet()) {
					System.out.println();
					System.out.println("<'" + endedVotes.get(selecNum).get(s) + "' ��ǥ ���>");
				}
				printEndedVotes(selecNum);
				return;
			}
			else {
				System.out.println(Constant.inputErrorMsg);
				System.out.println("3�� �Ŀ� �ǵ��� ���ϴ�.");
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
		
		int lineCnt = 1;		//���° �����ΰ�
		int number = 1;			//���� ��ǥ ǥ���� ���� �ѹ�
		int obtained = 0;		//�ش� ��ǥ�� ���� ��ǥ��
		int total_obtained = 0;	//�� ��ǥ��
		int max_obtained = 0;	//�ִ� ��ǥ��
		Vector<String> candidate = new Vector<>(); //�ĺ�����
		
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
				System.out.println(number + "�� " + arr[0] + " " + obtained + "ǥ");
				lineCnt++;
				number++;
			}
		}
		
		System.out.println("-------");
		System.out.println("�δ� �� ��ǥ ��� Ƚ�� : " + secondLine[1] + "��");
		System.out.println("�� �������� ���� �ִ� ��ǥ ���� Ƚ��: " + secondLine[3] + "ǥ");
		System.out.println("�� ��ǥ�� : " + total_obtained + "ǥ");		
		System.out.println("�ִ� ��ǥ : ");
		while (!candidate.isEmpty()) {
			System.out.println("***" + candidate.remove(0) + "***");
		}
		
		candidate = null;
		System.out.println("(���� Ű�� ������ ���� �޴��� ���ư��ϴ�.)");
		scan.nextLine();
		
		iterFileReader.close();
	}
}
