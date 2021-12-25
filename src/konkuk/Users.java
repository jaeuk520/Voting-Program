package konkuk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Users {
	static File file = new File(Constant.usersFile); // �� Ŭ�������� ������ Users.txt�� �ٷ�
	static ArrayList<Users> list = new ArrayList<>(); // Users ��ü ����
	String phone;// ��ȭ��ȣ
	String password;// ��й�ȣ
	String name;// �̸�

	Users() throws IOException, InterruptedException {
		startMenu();
	}

	// ������(��ȭ��ȣ, ��й�ȣ, �̸�)
	Users(String phone, String password, String name) {
		this.phone = phone;
		this.password = password;
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	// ���۸޴�
	public void startMenu() throws IOException, InterruptedException {
		Constant.clearConsole();
		updateList();
		System.out.println("<��ǥ ���α׷�>");
		System.out.println("�α��� �޴� ����");
		System.out.println("1. �α���");
		System.out.println("2. �ű԰���");
		System.out.println("3. ����");
		System.out.print("����: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String select = br.readLine().trim();
		if (select.equals("1")) {
			signIn();
		} else if (select.equals("2")) {
			signUp();
		} else if (select.equals("3")) {
			System.out.println("\n���α׷��� �����մϴ�.");
			System.exit(0);
		} else if (select.equals("4")) {
			admin();
		} else {
			System.out.println(Constant.inputErrorMsg);
			Thread.sleep(Constant.gotoMenuSleepTime);
			startMenu();
		}
	}

	public void admin() throws IOException {
		System.out.println("****Users.txt****");
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")));
		String str = "";
		int count = 1;
		while ((str = br.readLine()) != null) {
			System.out.println("User #" + count + " : " + str);
			count++;
		}
		System.out.println("\n*****ArrayList*****");
		for (int i = 0; i < list.size(); i++) {
			System.out.println("index #" + (i + 1) + " : " + list.get(i).getPhone() + " " + list.get(i).getPassword()
					+ " " + list.get(i).getName());
		}
		br.close();
	}

	// ��ȭ��ȣ �Է�
	public String inputPhone() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("��ȭ��ȣ �Է�: ");
		String phone;
		while (true) {
			try {
				String str = br.readLine();
				str = str.trim();
				if (str.equals(Constant.quitChar)) {
					startMenu();
				}
				phone = str;
				break;
			} catch (NumberFormatException | IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				br = new BufferedReader(new InputStreamReader(System.in));
				System.out.println(Constant.inputErrorMsg);
				System.out.print("��ȭ��ȣ �Է�: ");
			}
		}
		// ���� 8 �Ҹ���
		if (phone.length() != Constant.phoneNumberLength) {
			System.out.println(Constant.inputErrorMsg);
			return inputPhone();
		}
		if (!(phone.matches(Constant.phoneNumberRegex))) {
			System.out.println(Constant.inputErrorMsg);
			return inputPhone();
		}
		return phone;
	}

	// ��й�ȣ �Է�
	public String inputPassword() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("��й�ȣ �Է�: ");
		String password;
		while (true) {
			try {
				password = br.readLine();
				if (password.equals(Constant.quitChar)) {
					startMenu();
				}
				break;
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				br = new BufferedReader(new InputStreamReader(System.in));
				System.out.println(Constant.inputErrorMsg);
				System.out.print("��й�ȣ �Է�: ");
			}
		}
		// ����,���ĺ� �̿� �Է°� OR ����+���ĺ� ������
		if (!password.matches(Constant.pwRegex)) {
	         System.out.println(Constant.inputErrorMsg);
	         return inputPassword();
	      }
		// ���� 5~12 �Ҹ���
		if (!(password.length() >= Constant.pwMinLength && password.length() <= Constant.pwMaxLength)) {
			System.out.println(Constant.inputErrorMsg);
			return inputPassword();
		}
		return password;
	}

	// �̸� �Է�
	public String inputName() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("�̸� �Է�: ");
		String name;
		while (true) {
			try {
				name = br.readLine();
				if (name.equals(Constant.quitChar)) {
					startMenu();
				}
				break;
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				br = new BufferedReader(new InputStreamReader(System.in));
				System.out.println(Constant.inputErrorMsg);
				System.out.print("�̸� �Է�: ");
			}
		}
		// �ѱ� �ƴ� ���
		if (!(name.matches(Constant.koreanRegex))) {
			System.out.println(Constant.inputErrorMsg);
			return inputName();
		}
		// ���� 2~5 �Ҹ���
		if (!(name.length() >= Constant.nameMinLength && name.length() <= Constant.nameMaxLength)) {
			System.out.println(Constant.inputErrorMsg);
			return inputName();
		}
		return name;
	}

	// �α���
	public void signIn() throws IOException, InterruptedException {
		String phone;
		while (true) {
			Constant.clearConsole();
			System.out.println("<��ǥ ���α׷�>");
			System.out.println("<�α��� �޴�>");
			phone = inputPhone(); // �������� phone �ʱ�ȭ
			// false: phone ��ġ�ż� ��й�ȣ �Է� �ܰ�� ��� ����
			if (!searchInfo(phone)) {
				break;
			} else {
				System.out.println("�������� �ʴ� �����Դϴ�. �ٽ� �Է����ּ���.");
				System.out.println("3�� �Ŀ� �ǵ��� ���ϴ�.");
				Thread.sleep(Constant.gotoMenuSleepTime);
			}
		}
		String password = inputPassword(); // �������� password �ʱ�ȭ
		String name = "";
		// -- Users.txt�� ���� ��--
		// �α��� ����
		if (checkSignIn(phone, password)) {
			for (int i = 0; i < list.size(); i++) {
				if (phone.equals(list.get(i).getPhone())) {
					name = list.get(i).getName();
					System.out.println("�α��� ����!");
				}
			}
			System.out.println("\n3�� �Ŀ� ���θ޴��� ���ϴ�...");
			Thread.sleep(Constant.gotoMenuSleepTime);
			Users user = new Users(phone, password, name);
			VotesHandler vh = new VotesHandler(user);
			vh.startMainMenu(); //���θ޴� ����
		} else {
			System.out.println("�������� �ʴ� �����Դϴ�. �ٽ� �Է����ּ���.");
			System.out.println("3���Ŀ� �ǵ��� ���ϴ�.");
			Thread.sleep(Constant.gotoMenuSleepTime);
			signIn();
		}
		
		
	}

	// �ű԰���
	public void signUp() throws IOException, InterruptedException {
		String phone;
		while (true) {
			Constant.clearConsole();
			System.out.println("<��ǥ ���α׷�>");
			System.out.println("<�ű԰���>");
			phone = inputPhone(); // �������� phone �ʱ�ȭ
			// true: phone �ߺ��ƴ϶� �ű԰��԰���
			if (searchInfo(phone)) {
				break;
			} else {
				System.out.println("�̹� �����ϴ� ��ȣ�Դϴ�. �ٽ� �Է����ּ���.");
				System.out.println("3���Ŀ� �ǵ��� ���ϴ�.");
				Thread.sleep(Constant.gotoMenuSleepTime);
			}
		}
		String password = inputPassword(); // �������� password �ʱ�ȭ
		String name = inputName(); // �������� name �ʱ�ȭ
		addInfo(new Users(phone, password, name)); // Users.txt�� �߰�

		startMenu();
//		Timer timer = new Timer();
//		System.out.println("(3�� �Ŀ� �Ѿ�ϴ�.)");
//		timer.schedule(/*TimerTask tt(���θ޴�)*/, 3000);
	}

	// Users.txt�� ��ȭ��ȣ, ��й�ȣ, �̸� ������ ���� + ArrayList�� ����
	void addInfo(Users user) throws NumberFormatException, InterruptedException, IOException {
		try {
			BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(file, true), Charset.forName("UTF8")));
			bw.write(user.getPhone() + Constant.fieldTokenizerDelim + user.getPassword() + Constant.fieldTokenizerDelim
					+ user.getName() + "\n");
			bw.flush();
			System.out.println("���� �Ϸ�!");
			System.out.println("\n3�� �Ŀ� �α��� �޴��� �ǵ��� ���ϴ�.");
			Thread.sleep(Constant.gotoMenuSleepTime);
			list.add(user);
			bw.close();
		} catch (IOException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Users.txt�� �Է��� ��ȭ��ȣ ���� �ִ��� Ȯ�� (������ false, ������ true)
	boolean searchInfo(String phone) throws IOException {
		if (list.size() == 0) {
			return true;
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (phone.equals(list.get(i).getPhone())) {
					return false;
				}
			}
		}
		return true;
	}

	// �α��� ���� (��ȭ��ȣ, ��й�ȣ ��� Users.txt�� ��ġ�ϸ� true, �ƴϸ� false)
	boolean checkSignIn(String phone, String password) throws IOException {
		if (list.size() == 0) {
			return false;
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (phone.equals(list.get(i).getPhone())) {
					if (password.equals(list.get(i).getPassword())) {
						// ã������ ����
						this.phone = list.get(i).getPhone();
						this.password = list.get(i).getPassword();
						this.name = list.get(i).getName();
						return true;
					}
				}
			}
		}
		return false;
	}

	// ���α׷� ���� �� Users.txt ���� ���� ��� ����
	// Users.txt���� ���� �о ArrayList �ʱ�ȭ
	void updateList() throws IOException, InterruptedException {
		list.clear();
		FileWriter fw = new FileWriter(file, true);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), Charset.forName("UTF8")));
		String str = "";
		while ((str = br.readLine()) != null) {
			/*********************************************/
			//���� ���� Ȯ��
			if (!str.matches(Constant.usersFileRegex)) {
				//���� ������ �� �� ����
				Constant.printFileError(Constant.usersFile);
				Thread.sleep(2000);
			} else {
				String phone = str.split(Constant.fieldTokenizerDelim)[0];
				String password = str.split(Constant.fieldTokenizerDelim)[1];
				String name = str.split(Constant.fieldTokenizerDelim)[2];
				list.add(new Users(phone, password, name));
			}
			/*********************************************/

		}
		fw.close();
		br.close();
		fr.close();
	}

}
