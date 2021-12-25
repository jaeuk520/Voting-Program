package konkuk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class VotesHandler {
	Users user;
	ArrayList<Votes> votesList = new ArrayList<>(); // Votes ��ü ����

	VotesHandler(Users user) throws InterruptedException {
		this.user = user;
		votesMkdir();
		initVotes();
	}

	/* Votes���� ������ ����� */
	void votesMkdir() {
		File votesDir = new File(Constant.voteDirRoute);
		if (!(votesDir.isDirectory())) {
			votesDir.mkdir(); // ���� ����
		}
	}

	/* list�� Votes ��ü ��� */
	void initVotes() throws InterruptedException {
		File path = new File(Constant.voteDirRoute);
		File[] fileList = path.listFiles();
		votesList.clear(); // �߰�
		// ��� ���� �̸���� ����Ʈ�� ��ü ����
		if (fileList.length > 0) {
			for (int i = 0; i < fileList.length; i++) {
				/*****************************************/
				if (fileList[i].getName().matches(Constant.voteFileNameRegex) ) {
					Votes v = new Votes(fileList[i].getName());
					//������ �����ϰ�� ����Ʈ�� �߰�
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

	// ����� ���� Ȯ��
	public void userInfo() throws IOException, InterruptedException {
		System.out.println("<����� ����>");
		System.out.println("�̸�: " + user.getName());
		System.out.println("��ȭ��ȣ: 010-" + user.getPhone().substring(0, 4) + "-" + user.getPhone().substring(4, 8));
		System.out.println("(3�� �Ŀ� ���� �޴��� ���ư��ϴ١�)");
		try {
			Thread.sleep(Constant.gotoMenuSleepTime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Check Exception");
		}
	}

	// ��ǥ�� ����ִ��� Ȯ��
	public boolean isEmpty() {
		if (votesList.size() == 0) {
			return true;
		}
		return false;
	}

	// ��ǥ �޴�
	void startMainMenu() throws IOException, InterruptedException {
		Scanner sc = new Scanner(System.in);

		String line = "";
		boolean exitFlag = false;
		while (!exitFlag) {
			initVotes(); // ��ǥ ���� ����
			Constant.clearConsole(); //---�߰�����
			System.out.println("<���θ޴�>");
			System.out.println("�ȳ��ϼ���." + user.getName() + "��");
			System.out.println("1. ����� ����");
			System.out.println("2. ��ǥ�ϱ�");
			System.out.println("3. ��ǥ �����");
			System.out.println("4. �����ǥ ������");
			System.out.println("5. ��ǥ ��� Ȯ��");
			System.out.println("6. ����");
			while (true) {
				System.out.print("����: ");

				line = sc.nextLine();
				line = line.trim();

				// ��ȿ ���� Ȯ�� 
				if (!(line.matches(Constant.menuOptionRegex))) {
					System.out.println(Constant.inputErrorMsg);
					continue;
				}
				break;
			}

			Constant.clearConsole();
			switch (Integer.parseInt(line)) {
			case 1:
				// ��������� Ȯ��
				userInfo();
				break;
			case 2:
				// ��ǥ�ϱ�
				Voting voting = new Voting(user, votesList);
				voting.DoVoting();
				break;
			case 3:
				// ��ǥ �����
				CreateVote createVote = new CreateVote(user.getPhone());
				createVote.makeVote();
				break;
			case 4:
				// ��� ��ǥ ������
				EndVote endVote = new EndVote(user.getPhone(), user.getName());
				endVote.showMyVotes();
				break;
			case 5:
				// ��ǥ ��� Ȯ��
				EndVote endVote2 = new EndVote(user.getPhone(), user.getName());
				endVote2.showEndedVotes();
				break;
			case 6:
				// ����
				exitFlag = true;
				break;
			}
			Constant.clearConsole();
		}
		sc.close();
	}
}
