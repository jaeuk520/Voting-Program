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
	static File file = new File(Constant.usersFile); // 이 클래스에서 파일은 Users.txt만 다룸
	static ArrayList<Users> list = new ArrayList<>(); // Users 객체 저장
	String phone;// 전화번호
	String password;// 비밀번호
	String name;// 이름

	Users() throws IOException, InterruptedException {
		startMenu();
	}

	// 생성자(전화번호, 비밀번호, 이름)
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


	// 시작메뉴
	public void startMenu() throws IOException, InterruptedException {
		Constant.clearConsole();
		updateList();
		System.out.println("<투표 프로그램>");
		System.out.println("로그인 메뉴 선택");
		System.out.println("1. 로그인");
		System.out.println("2. 신규가입");
		System.out.println("3. 종료");
		System.out.print("선택: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String select = br.readLine().trim();
		if (select.equals("1")) {
			signIn();
		} else if (select.equals("2")) {
			signUp();
		} else if (select.equals("3")) {
			System.out.println("\n프로그램을 종료합니다.");
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

	// 전화번호 입력
	public String inputPhone() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("전화번호 입력: ");
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
				System.out.print("전화번호 입력: ");
			}
		}
		// 길이 8 불만족
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

	// 비밀번호 입력
	public String inputPassword() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("비밀번호 입력: ");
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
				System.out.print("비밀번호 입력: ");
			}
		}
		// 숫자,알파벳 이외 입력값 OR 숫자+알파벳 미조합
		if (!password.matches(Constant.pwRegex)) {
	         System.out.println(Constant.inputErrorMsg);
	         return inputPassword();
	      }
		// 길이 5~12 불만족
		if (!(password.length() >= Constant.pwMinLength && password.length() <= Constant.pwMaxLength)) {
			System.out.println(Constant.inputErrorMsg);
			return inputPassword();
		}
		return password;
	}

	// 이름 입력
	public String inputName() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("이름 입력: ");
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
				System.out.print("이름 입력: ");
			}
		}
		// 한글 아닐 경우
		if (!(name.matches(Constant.koreanRegex))) {
			System.out.println(Constant.inputErrorMsg);
			return inputName();
		}
		// 길이 2~5 불만족
		if (!(name.length() >= Constant.nameMinLength && name.length() <= Constant.nameMaxLength)) {
			System.out.println(Constant.inputErrorMsg);
			return inputName();
		}
		return name;
	}

	// 로그인
	public void signIn() throws IOException, InterruptedException {
		String phone;
		while (true) {
			Constant.clearConsole();
			System.out.println("<투표 프로그램>");
			System.out.println("<로그인 메뉴>");
			phone = inputPhone(); // 전역변수 phone 초기화
			// false: phone 매치돼서 비밀번호 입력 단계로 통과 가능
			if (!searchInfo(phone)) {
				break;
			} else {
				System.out.println("존재하지 않는 정보입니다. 다시 입력해주세요.");
				System.out.println("3초 후에 되돌아 갑니다.");
				Thread.sleep(Constant.gotoMenuSleepTime);
			}
		}
		String password = inputPassword(); // 전역변수 password 초기화
		String name = "";
		// -- Users.txt와 정보 비교--
		// 로그인 성공
		if (checkSignIn(phone, password)) {
			for (int i = 0; i < list.size(); i++) {
				if (phone.equals(list.get(i).getPhone())) {
					name = list.get(i).getName();
					System.out.println("로그인 성공!");
				}
			}
			System.out.println("\n3초 후에 메인메뉴로 갑니다...");
			Thread.sleep(Constant.gotoMenuSleepTime);
			Users user = new Users(phone, password, name);
			VotesHandler vh = new VotesHandler(user);
			vh.startMainMenu(); //메인메뉴 실행
		} else {
			System.out.println("존재하지 않는 정보입니다. 다시 입력해주세요.");
			System.out.println("3초후에 되돌아 갑니다.");
			Thread.sleep(Constant.gotoMenuSleepTime);
			signIn();
		}
		
		
	}

	// 신규가입
	public void signUp() throws IOException, InterruptedException {
		String phone;
		while (true) {
			Constant.clearConsole();
			System.out.println("<투표 프로그램>");
			System.out.println("<신규가입>");
			phone = inputPhone(); // 전역변수 phone 초기화
			// true: phone 중복아니라서 신규가입가능
			if (searchInfo(phone)) {
				break;
			} else {
				System.out.println("이미 존재하는 번호입니다. 다시 입력해주세요.");
				System.out.println("3초후에 되돌아 갑니다.");
				Thread.sleep(Constant.gotoMenuSleepTime);
			}
		}
		String password = inputPassword(); // 전역변수 password 초기화
		String name = inputName(); // 전역변수 name 초기화
		addInfo(new Users(phone, password, name)); // Users.txt에 추가

		startMenu();
//		Timer timer = new Timer();
//		System.out.println("(3초 후에 넘어갑니다.)");
//		timer.schedule(/*TimerTask tt(메인메뉴)*/, 3000);
	}

	// Users.txt에 전화번호, 비밀번호, 이름 순으로 쓰기 + ArrayList에 저장
	void addInfo(Users user) throws NumberFormatException, InterruptedException, IOException {
		try {
			BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(file, true), Charset.forName("UTF8")));
			bw.write(user.getPhone() + Constant.fieldTokenizerDelim + user.getPassword() + Constant.fieldTokenizerDelim
					+ user.getName() + "\n");
			bw.flush();
			System.out.println("가입 완료!");
			System.out.println("\n3초 후에 로그인 메뉴로 되돌아 갑니다.");
			Thread.sleep(Constant.gotoMenuSleepTime);
			list.add(user);
			bw.close();
		} catch (IOException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Users.txt에 입력한 전화번호 정보 있는지 확인 (있으면 false, 없으면 true)
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

	// 로그인 검증 (전화번호, 비밀번호 모두 Users.txt과 일치하면 true, 아니면 false)
	boolean checkSignIn(String phone, String password) throws IOException {
		if (list.size() == 0) {
			return false;
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (phone.equals(list.get(i).getPhone())) {
					if (password.equals(list.get(i).getPassword())) {
						// 찾았으면 저장
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

	// 프로그램 실행 시 Users.txt 파일 없을 경우 생성
	// Users.txt에서 정보 읽어서 ArrayList 초기화
	void updateList() throws IOException, InterruptedException {
		list.clear();
		FileWriter fw = new FileWriter(file, true);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), Charset.forName("UTF8")));
		String str = "";
		while ((str = br.readLine()) != null) {
			/*********************************************/
			//파일 형식 확인
			if (!str.matches(Constant.usersFileRegex)) {
				//오류 있으면 그 줄 무시
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
