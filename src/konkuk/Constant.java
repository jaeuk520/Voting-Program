package konkuk;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public final class Constant {
	static final String usersFile = "Users.txt"; //���� ���� ���� �ؽ�Ʈ ���� �̸�
	static final String voteDirRoute = "Votes"+File.separator; //��ǥ ���� ���� ��Ʈ
	
	static final String endOfVoteTrue = "1"; //��ǥ ���� ���� true
	static final String endOfVoteFalse = "2"; //��ǥ ���� ���� false
	static final String addAllowTrue = "1"; //������ �߰� ���� ���� true
	static final String addAllowFalse = "2"; //������ �߰� ���� ���� false
	
	static final String fieldTokenizerDelim = "/"; //���Ͽ��� �ʵ� ������
	static final String voterTokenizerDelim = "+"; //��ǥ�ڵ� ������
	static final String updateVerTokenizerDelim = "-"; //���� ��ȭ��ȣ�� ������Ʈ ���� ������
	
	
	static final String quitChar = "q"; //�޴��� �ǵ��ư��� ����
	static final String haveNoVoter = "null"; //��ǥ�� ����� ���� �������� ��ǥ������
	static final int quitInt = -1; //�޴� �ǵ��ư��� int
	static final int minOptionNum = 2; //������ �ּ� ����
	static final int maxOptionNum = 100; //������ �ִ� ����
	static final int maxOptionLength = 20; //������ �ִ� ���� ����
	static final int minMultipleVoteNum = 1; //��ǥ �ּ� �ѵ�
	static final int maxMultipleVoteNum = 100; //��ǥ �ִ� �ѵ�
	static final int phoneNumberLength = 8; //��ȭ��ȣ �ڸ���
	static final int nameMinLength = 2; //�̸� �ּ� ���� ��
	static final int nameMaxLength = 5; //�̸� �ִ� ���� ��
	static final int pwMinLength = 5; //��й�ȣ �ּ� ���� ��
	static final int pwMaxLength = 12; //��й�ȣ �ִ� ���� ��
	static final String reVoteTrue = "1"; //����ǥ ���� true
	static final String reVoteFalse = "2"; //����ǥ ���� false
	static final String increaseVoteNumTrue = "1";
	static final String increaseVoteNumFalse = "2";
	
	static final String endAddOptionChar= "end"; //������ ����°� ������ ���� ����
	
	//���� ���� Ȯ�� ����ǥ����
	static final String voteFileNameRegex =
			"^(\\d{12})+.(?i)(txt)$";
	static final String voteLine1Regex =
			"^([A-Za-z0-9��-�R\\s]{1,20})/(\\d{8})/(\\d{1})$";
	static final String voteLine2Regex =
			"^[1-2]/([0-9]+)/[1-2]/([0-9]+)/[1-2]$";
	static final String voteLine3Regex = 
			"^([A-Za-z0-9��-�R\\s]{1,20})/([0-9\\+\\-|null]*)$";//��ǥ�ڵ� �κ��� �ٽ� Ȯ�����־�ߵ�
	static final String voterRegex = 
			"^(\\d{8})-([0-9]+)$";
	static final String usersFileRegex = 
			"^(\\d{8})/(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{5,12}/([��-�R]{2,5})$";
	static final String updateVerRegex = "^[0-9]+$"; //������Ʈ ���� ǥ�� ���Խ�
	
	
	static final String pwRegex = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{5,12}$"; //��й�ȣ ����ǥ����4
	static final String koreanRegex = "^[��-�R]*$"; //�ѱ��� ����ǥ����
	static final String voteNameRegex = "^[A-Za-z0-9��-�R\\s]*$"; //��ǥ �̸� ����ǥ����
	static final String voteOptionsRegex = "^[A-Za-z0-9��-�R\\s]*$"; //������ ����ǥ����
	static final String voteAllowAddRegex = "^[1-2]$"; //������ �߰� ���� ���� ����ǥ����
	static final String voteAllowNumRegex = "^[\\d]+$"; //������ ���� ���� Ƚ�� ����ǥ����
	static final String numRegex = "^[\\d]+$"; //���� ���Խ�
	static final String increaseVoteNumRegex = "^[1-2]$"; //��ǥ �ѵ� �ø��� ���� ����ǥ����
	static final String menuOptionRegex = "^[1-6]$"; //�޴� ���� ����ǥ����
	static final String phoneNumberRegex = "^[0-9]*$"; //��ȭ��ȣ ����ǥ����
	
	static final int maxVoteNameLength = 20; //��ǥ�̸� �ִ� ���� ��
	static final int maxVoteOptionLength = 20; //������ �ִ� ���� ��
	
	static final int gotoMenuSleepTime = 3000; //n���Ŀ� ~�� �Ѿ�ϴ�
	
	//���� �޼��� ���ڿ�
	static final String fileErrorMsg = "������ �߸��Ǿ����ϴ�.";
	static final String inputErrorMsg = "�Է� ���Ŀ� ���� �ʽ��ϴ�. �ٽ� �Է����ּ���.";
	
	static void printFileError(String fileName) throws InterruptedException {
		System.out.println(fileName + ": " + fileErrorMsg);
		System.out.println("���� Ű�� ������ ��� �����մϴ�.");
		Scanner sc = new Scanner(System.in);
		sc.nextLine();
		//Thread.sleep(2000);
	}
	
	// �ܼ�â �����
	static void clearConsole() throws IOException, InterruptedException {
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
	}
}
