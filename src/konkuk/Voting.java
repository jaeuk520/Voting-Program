package konkuk;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;


public class Voting {
    Users users;
    ArrayList<Votes> voteslist;
    int maxVote=0;

    Voting(Users User, ArrayList<Votes> Voteslist) {
        this.users = User;
        this.voteslist = Voteslist;
    }

    //투표 가능한 Votes들을 출력
    public void DoVoting() throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        Constant.clearConsole();

        ArrayList<Votes> ValidVotes = new ArrayList<Votes>();
        ValidVotes.clear();
        Votes SelectedVote = null;  //사용자가 선택할 Votes

        //투표 가능한 Votes들 담아놓기
        for (int i = 0; i < voteslist.size(); i++) {
            if (!voteslist.get(i).getEndOfVotes()) {
                //투표가 끝나지 않았는지
                if (CountVoted(voteslist.get(i)) != voteslist.get(i).getNumAllow() //수정
                		|| voteslist.get(i).checkUserUpdate((users.phone))) {
                    //유저가 투표를 할 수 있는지
                    ValidVotes.add(voteslist.get(i));
                }
            }
        }

        if(ValidVotes.size() == 0){
            System.out.println("현재 가능한 투표가 없습니다.");
            System.out.println("3초 후에 메인 메뉴로 돌아갑니다...");
            try {
                Thread.sleep(Constant.gotoMenuSleepTime);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return;  //DoVoting 종료, 변경사항 없으므로 저장 안해도 됨
        }

        //투표가 가능한 투표 목록 출력
        String line = "";
        System.out.println("<투표하기>");
        System.out.println("(취소하려면 '"+Constant.quitChar+"' 입력)");
        System.out.println("***현재 진행중인 투표 목록***");                       
        for (int i = 0; i < ValidVotes.size(); i++) {
           if(ValidVotes.get(i).checkUserUpdate(users.phone)) {
              System.out.println((i + 1) + ". " + ValidVotes.get(i).voteName + " (선택지 추가되어 재투표 가능)");
            }
           else System.out.println((i + 1) + ". " + ValidVotes.get(i).voteName);
        }
        //어떤 투표를 할건지 선택
        while (true) {
            System.out.print("선택: ");

            line = sc.nextLine();
            line = line.trim();

            if (line.equals(Constant.quitChar)) {
            	System.out.println("3초 후에 메인 메뉴로 돌아갑니다...");
                try {
                    Thread.sleep(Constant.gotoMenuSleepTime);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return;
            }
            else if (!(line.matches("^[0-" + ValidVotes.size() + "]$"))) {
                //숫자가 아닌경우
                System.out.println(Constant.inputErrorMsg);
                continue;
            }
            else if (Integer.parseInt(line) > ValidVotes.size() || Integer.parseInt(line) < 1) {
                //범위 밖의 숫자를 입력한 경우
                System.out.println(Constant.inputErrorMsg);
                continue;
            }
            //입력형식에 위배되지 않는경우
            SelectedVote = ValidVotes.get(Integer.parseInt(line) - 1);
            break;
        }
        
        //입력받은 투표가 최신화가 되어 재투표가 가능한 경우
       if(SelectedVote.checkUserUpdate(users.phone)) {
          Constant.clearConsole();
            System.out.println("\n***" + SelectedVote.getVoteName() + "***");
            System.out.println("선택지가 추가되어 재투표가 가능합니다. 재투표 하시겠습니까?");
            System.out.println("(\"예\"를 선택하면 기존의 투표는 무효가 됩니다.)");
            System.out.println("1. 예\n2. 아니요");
            System.out.print("선택: ");
            line = sc.nextLine();
            line = line.trim();
            if (line.equals(Constant.quitChar)) {
                System.out.println("3초 후에 메인 메뉴로 돌아갑니다...");
                try {
                    Thread.sleep(Constant.gotoMenuSleepTime);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return;
            }
            else if (line.equals(Constant.reVoteTrue)) {
            //기존의 투표 무효화
               
               String fileName = SelectedVote.getVoteCode();
               File file = new File(Constant.voteDirRoute + fileName);
               
               BufferedWriter bw = null;
               try {
                   bw = new BufferedWriter(new OutputStreamWriter(
                         new FileOutputStream(file, false), Charset.forName("UTF8")));
                   StringBuilder sb = new StringBuilder("");
                   String print = "";

                   //line1: 투표이름 투표자  저장
                   print = SelectedVote.getVoteName() + Constant.fieldTokenizerDelim + SelectedVote.getMakerNum() + Constant.fieldTokenizerDelim + SelectedVote.getUpdateVer();
                   sb.append(print + "\n");

                   //line2: 투표종료여부 투표허용횟수 선택지추가가능여부 저장
                   print = Integer.toString(SelectedVote.getEndOfVotesInt())
                           + Constant.fieldTokenizerDelim + Integer.toString(SelectedVote.getNumAllow())
                           + Constant.fieldTokenizerDelim + Integer.toString(SelectedVote.getAddAllowInt())
                           + Constant.fieldTokenizerDelim + Integer.toString(SelectedVote.getMultipleVote())
                           + Constant.fieldTokenizerDelim + Integer.toString(SelectedVote.getIncreaseVoteNumALlowInt());
                   sb.append(print + "\n");

                   //line3-n: 선택지이름 투표자 저장
                   for (int i = 0; i < SelectedVote.getOptionName().length; i++) {
                	   String voter="";
                	   String temp1="";
                    	   for(String temp2 : SelectedVote.getOptions().get(i).getVoterArray()) {                                      		   
                    		   temp1 = temp2.substring(0, temp2.length()-2);
                    		   if(!temp1.equals(users.getPhone())) {
                    			   voter += temp2 + Constant.voterTokenizerDelim;
                    		   }
                    	   }
                       if(voter.length()!=0) voter = voter.substring(0, voter.length()-1);
                       else voter = null;
                       print = SelectedVote.getOptions().get(i).getOptionName()
                   			+ Constant.fieldTokenizerDelim + voter;
                       sb.append(print + "\n");
                   }
                   bw.write(sb.toString());
                   bw.flush();

               } catch (Exception e) {
                   e.printStackTrace();
               } finally {
                   if (bw != null) try {
                       bw.close();
                   } catch (IOException e) {
                   }
               }
               //투효 무효화 적용
               String code = SelectedVote.getVoteCode();
               boolean suc = SelectedVote.loadVote(code);
               if(!suc) {
               	Constant.printFileError(SelectedVote.getVoteCode());
               }
               //투표 후 사용자가 투표한 시기의 투표 업데이트 버전 = 투표 업데이트 버전
               //투표를 마친 후(투표 선택지를 select하는 것을 성공한 후)에는 무조건 적으로 사용자가 투표한 시기의 투표 업데이트 버전 변수에 현 투표 업데이트 버전 값을 대입해야함.
         } else if (line.equals(Constant.reVoteFalse)) {
            return;
         }
         else {
            System.out.println(Constant.inputErrorMsg+"\n");
            return;
         }
       }

        //투표횟수 안넘을때까지 투표가능
        while (CountVoted(SelectedVote) != SelectedVote.getNumAllow()) {
            Constant.clearConsole();
          
            //선택지 출력
            System.out.println("\n***" + SelectedVote.getVoteName() + "***");
            System.out.println("(한 선택지 당 투표 가능 횟수: " + SelectedVote.getMultipleVote() + "회)");
            System.out.println("남은 투표 횟수: " + (SelectedVote.getNumAllow()-CountVoted(SelectedVote)) + "회");
            if (SelectedVote.getAddAllow() && SelectedVote.getOptionName().length < Constant.maxOptionNum)  {
               System.out.println("0. 선택지 추가");
            }

            //투표 가능한 옵션 리스트
            ArrayList<VoteOptions> ValidOptionList = new ArrayList<VoteOptions>();
            int countVote = 0;
            int idx=0;
            //각 옵션마다 투표했는지 확인
            for (int i = 0; i < SelectedVote.getOptions().size(); i++) {
               countVote = 0;
                for (String voter : SelectedVote.getOptions().get(i).getVoterArray()) {
                	voter = voter.substring(0, voter.length()-2);
                    if (voter.equals(users.getPhone())) {
                        countVote++;
                    } 
                }
                //몰표횟수만큼 투표한 선택지를 제외하고 가져옴
                if (countVote < SelectedVote.multipleVote) {             
                    ValidOptionList.add(SelectedVote.getOptions().get(i));
                    System.out.println((idx + 1) + ". " + ValidOptionList.get(idx).getOptionName() + "(" + countVote + "회)");
                    idx++;
                }
            }

            //투표 옵션 선택 시작
            while (true) {
                try{
                   int ver = SelectedVote.getUpdateVer();  //해당 투표의 업데이트 버전을 가져와 초기화 함
                    System.out.print("선택: ");
                    line = sc.nextLine();
                    line = line.trim();
                    if (line.equals(Constant.quitChar)) {
                    	//동표 허용 안함
                    	for(int i = 0; i < SelectedVote.getOptions().size(); i++) {
                    		countVote=0;
                    		
                    		for(String voter : SelectedVote.getOptions().get(i).getVoterArray()) {
                    			voter = voter.substring(0, voter.length()-2);
                                if (voter.equals(users.getPhone())) {
                                    countVote++;
                                }                                
                    		}
                    		
                    		if(i==0) 
                    			maxVote = countVote;
                    		
                    		else if((i!=0 && maxVote != countVote)
                    				|| (i==SelectedVote.getOptions().size()-1 
                    				&& maxVote ==0 && countVote==0)) {
                    			System.out.println("3초 후에 메인 메뉴로 돌아갑니다...");
                    			//SaveToTxt();  //변경사항 저장
                    			try {
                    				Thread.sleep(Constant.gotoMenuSleepTime);
                    			} catch (InterruptedException e) {
                    				// TODO Auto-generated catch block
                    				e.printStackTrace();
                    			}
                    			return;
                    		}
                    	}
                    	System.out.println("모든 선택지가 동표를 가질 수는 없습니다.");
                    	continue;
                    }
                    else if (isInteger(line)==false) {
                        //숫자가 아닌경우
                        System.out.println(Constant.inputErrorMsg);
                        continue;
                    }
                    else if (Integer.parseInt(line) > ValidOptionList.size() || Integer.parseInt(line) < 0) {
                        System.out.println(Constant.inputErrorMsg);    
                        continue;
                    }
                    else if (Integer.parseInt(line) == 0) {
                        if (SelectedVote.getAddAllow() && SelectedVote.getOptionName().length < Constant.maxOptionNum) {
                            //선택지 추가가 가능한 경우 추가
                            while (true) {
                                try{    
                                    System.out.println("\n---선택지 추가---");
                                    System.out.print("입력: ");
                                    line = sc.nextLine();
                                    line = line.trim();
                                    if (line.equals(Constant.quitChar)) {
                                    	return;
                                    } else if (line.equals("")) {
                                       System.out.println(Constant.inputErrorMsg);
                                       continue;
                                    }
                                    else if (!(line.matches(Constant.voteOptionsRegex))) {
                                        //특수문자를 포함한 경우
                                        System.out.println(Constant.inputErrorMsg+"\n");
                                        continue;
                                    }
                                    else if (line.length() > Constant.maxVoteOptionLength) {
                                        //20글자를 벗어나 입력한 경우
                                        System.out.println(Constant.inputErrorMsg+"\n");
                                        continue;
                                    }
                                    else {
                                        //중복된 옵션이 아닌지 확인
                                        if (IfThereIsNoOption(SelectedVote, line)) {
                                            SelectedVote.getOptions().add(new VoteOptions(line, Constant.haveNoVoter));
                                            
                                            //선택지의 개수가 늘어날 때 투표허용 횟수가 늘어 나는지 확인
                                            if(SelectedVote.getIncreaseVoteNumAllow()) {
                                               SelectedVote.numAllow = changeVoteNumAllow(SelectedVote);
                                            }
                                            
                                            SelectedVote.updateVer++;

                                            //해당 Votes의 정보 업데이트
                                            String fileName = SelectedVote.getVoteCode();
                                            File file = new File(Constant.voteDirRoute + fileName);
                                            
                                            BufferedWriter bw = null;
                                            try {
                                                bw = new BufferedWriter(new OutputStreamWriter(
                                                      new FileOutputStream(file, false), Charset.forName("UTF8")));
                                                StringBuilder sb = new StringBuilder("");
                                                String print = "";

                                                //line1: 투표이름 투표자  저장
                                                print = SelectedVote.getVoteName() + Constant.fieldTokenizerDelim + SelectedVote.getMakerNum() + Constant.fieldTokenizerDelim + SelectedVote.getUpdateVer();
                                                sb.append(print + "\n");

                                                //line2: 투표종료여부 투표허용횟수 선택지추가가능여부 저장
                                                print = Integer.toString(SelectedVote.getEndOfVotesInt())
                                                        + Constant.fieldTokenizerDelim + Integer.toString(SelectedVote.getNumAllow())
                                                        + Constant.fieldTokenizerDelim + Integer.toString(SelectedVote.getAddAllowInt())
                                                        + Constant.fieldTokenizerDelim + Integer.toString(SelectedVote.getMultipleVote())
                                                        + Constant.fieldTokenizerDelim + Integer.toString(SelectedVote.getIncreaseVoteNumALlowInt());
                                                sb.append(print + "\n");

                                                //line3-n: 선택지이름 투표자 저장
                                                for(int i = 0; i < SelectedVote.getOptionName().length; i++) {
                                                	if(SelectedVote.getOptions().get(i).voter.size()==0) {
                                                		print = SelectedVote.getOptions().get(i).getOptionName()
                                                    			+ Constant.fieldTokenizerDelim + SelectedVote.getOptions().get(i).getVoter();
                                                    	sb.append(print + "\n");
                                                	}
                                                	else {
                                                	print = SelectedVote.getOptions().get(i).getOptionName()
                                                			+ Constant.fieldTokenizerDelim + SelectedVote.getOptions().get(i).getVoter();
                                                	sb.append(print + "\n");
                                                	}
                                                }

                                                bw.write(sb.toString());
                                                bw.flush();

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            } finally {
                                                if (bw != null) try {
                                                    bw.close();
                                                } catch (IOException e) {
                                                }
                                            }
                                            
                                            System.out.println("\n추가 완료!");
                                            System.out.println("3초 후에 투표로 돌아갑니다...");
                                            try {
                                                Thread.sleep(Constant.gotoMenuSleepTime);
                                            } catch (InterruptedException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }

                                            Constant.clearConsole();
                                            System.out.println("\n***" + SelectedVote.getVoteName() + "***");
                                            System.out.println("(한 선택지 당 투표 가능 횟수: " + SelectedVote.getMultipleVote() + "회");
                                            System.out.println("남은 투표 횟수: " + (SelectedVote.getNumAllow()-CountVoted(SelectedVote)) + "회");
                                            if (SelectedVote.getAddAllow() && SelectedVote.getOptionName().length < Constant.maxOptionNum) {
                                               System.out.println("0. 선택지 추가");
                                            }

                                            //투표 가능한 옵션 리스트 업데이트
                                            ValidOptionList.clear();
                                            //각 옵션마다 투표했는지 확인
                                            idx=0;
                                            int index=0;
                                            for (int i = 0; i < SelectedVote.getOptions().size(); i++) {  
                                               countVote=0;
                                               String voter1;
                                               String temp[];
                                               voter1 = SelectedVote.getOptions().get(i).getVoter();
                                               String voter2[] = voter1.split("\\-(.*?)\\+");
                                               temp = voter2[voter2.length-1].split(Constant.updateVerTokenizerDelim);
                                               voter2[voter2.length-1]=temp[0];
                                                for (int j=0; j<voter2.length; j++) {
                                                	System.out.println("voter: " + voter2[j]);
                                                    if (voter2[j].equals(users.getPhone())) {                                                    	
                                                        countVote++;
                                                    }
                                                }
                                                //몰표횟수만큼 투표한 선택지를 제외하고 가져옴
                                                //투표를 안한 선택지인 경우 해당 선택지 가져옴
                                                if (countVote < SelectedVote.multipleVote) {             
                                                    ValidOptionList.add(SelectedVote.getOptions().get(i));
                                                    System.out.println((idx + 1) + ". " + ValidOptionList.get(idx).getOptionName() + "(" + countVote + "회)");
                                                    idx++;
                                                }
                                            }
                                            break;
                                        }
                                        else {
                                            System.out.println(Constant.inputErrorMsg+"\n");
                                            continue;
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println(Constant.inputErrorMsg);
                                }
                            }
                        } else {
                           System.out.println(Constant.inputErrorMsg);
                            continue;
                        }

                    }
                    else {
                    	//동표 처리 관련 수정   
                    	int flag=0;
                    	if (CountVoted(SelectedVote) == SelectedVote.getNumAllow()-1) {
                    		for(int i = 0; i < SelectedVote.getOptions().size(); i++) {
                        		countVote=0;
                        		for(String voter : SelectedVote.getOptions().get(i).getVoterArray()) {
                        			voter = voter.substring(0, voter.length()-2);
                        			if(voter.equals(users.getPhone())) {
                    					countVote++;
                    				}
                    				if(SelectedVote.getOptions().get(i) == ValidOptionList.get(Integer.parseInt(line)-1)) {
                    					countVote++;
                    				}                              
                        		}
                        		if(i==0) maxVote = countVote;
                        		else if(i!=0 && maxVote != countVote) {
                        			flag=1;
                        			break;
                        		}
                        	}
                    	if (flag==0) {
                    		System.out.println("모든 선택지가 동표를 가질 수는 없습니다.");
                    		continue;
                    	}
                    	}
                    	                    	
                        //기존의 선택지를 올바르게 입력한 경우
                        ValidOptionList.get(Integer.parseInt(line) - 1).AddVoter(users.phone + Constant.updateVerTokenizerDelim + Integer.toString(ver));  //부분 수정
                        ValidOptionList.get(Integer.parseInt(line) - 1).num += 1;

                        //파일을 다시 기록
                        String fileName = SelectedVote.getVoteCode();
                        File file = new File(Constant.voteDirRoute + fileName);

                        BufferedWriter bw = null;
                        try {
                            bw = new BufferedWriter(new OutputStreamWriter(   
                                  new FileOutputStream(file, false), Charset.forName("UTF8")));
                            StringBuilder sb = new StringBuilder("");
                            String print = "";

                          //line1: 투표이름 투표자  저장
                            print = SelectedVote.getVoteName() + Constant.fieldTokenizerDelim + SelectedVote.getMakerNum() + Constant.fieldTokenizerDelim + SelectedVote.getUpdateVer();
                            sb.append(print + "\n");

                            //line2: 투표종료여부 투표허용횟수 선택지추가가능여부 저장
                            print = Integer.toString(SelectedVote.getEndOfVotesInt())
                                    + Constant.fieldTokenizerDelim + Integer.toString(SelectedVote.getNumAllow())
                                    + Constant.fieldTokenizerDelim + Integer.toString(SelectedVote.getAddAllowInt())
                                    + Constant.fieldTokenizerDelim + Integer.toString(SelectedVote.getMultipleVote())
                                    + Constant.fieldTokenizerDelim + Integer.toString(SelectedVote.getIncreaseVoteNumALlowInt());
                            sb.append(print + "\n");

                            //line3-n: 선택지이름 투표자 저장                            
                            for(int i = 0; i < SelectedVote.getOptionName().length; i++) {
                            	if(SelectedVote.getOptions().get(i).voter.size()==0) {
                            		print = SelectedVote.getOptions().get(i).getOptionName()
                                			+ Constant.fieldTokenizerDelim + SelectedVote.getOptions().get(i).getVoter();
                                	sb.append(print + "\n");
                            	}
                            	else {
                            	print = SelectedVote.getOptions().get(i).getOptionName()
                            			+ Constant.fieldTokenizerDelim + SelectedVote.getOptions().get(i).getVoter();
                            	sb.append(print + "\n");
                            	}
                            }

                            bw.write(sb.toString());
                            bw.flush();

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        finally {
                            if (bw != null) try {
                                bw.close();
                            } catch (IOException e) {
                            }
                        }

                        //투표를 더이상 못하는 경우 메인 메뉴로 돌아감
                        if (CountVoted(SelectedVote) == SelectedVote.getNumAllow()) {
                            System.out.println("3초 후에 메인 메뉴로 돌아갑니다...");
                            try {
                                Thread.sleep(Constant.gotoMenuSleepTime);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            return;
                        }
                        break;
                    }
                } catch (Exception e) {
                    System.out.println(Constant.inputErrorMsg);
                }
            }

        }
        sc.close();
    }

    //name이 이미 있는 투표 옵션인지 확인
    boolean IfThereIsNoOption(Votes vote, String name) {
        for (String o_name : vote.getOptionName()) {
            if (o_name.equals(name)) return false;
        }
        return true;
    }

    //해당 Votes에서 사용자가 투표한 횟수를 반환
    int CountVoted(Votes votes) {
        int count = 0;
        String[] Votedpersons;
        for (int i = 0; i < votes.getOptions().size(); i++) {
            Votedpersons = votes.getVoter(votes.getOptionName()[i]);
            for (int j = 0; j < Votedpersons.length; j++) {
            	//수정된 부분
            	Votedpersons[j] = Votedpersons[j].substring(0, Votedpersons[j].length()-2);
                if (Votedpersons[j].equals(users.getPhone())) count++;
            }
        }
        return count;            
    }
    
    
    private int changeVoteNumAllow(Votes votes) {
       votes.numAllow+=votes.getMultipleVote();
       return votes.numAllow;
    }
    
    public static boolean isInteger(String s) {
    	try {
    		Integer.parseInt(s);
    	}
    	catch (NumberFormatException e) {
    		return false;
    	}
    	return true;
    }
    
}