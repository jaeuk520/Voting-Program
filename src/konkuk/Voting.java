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

    //��ǥ ������ Votes���� ���
    public void DoVoting() throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        Constant.clearConsole();

        ArrayList<Votes> ValidVotes = new ArrayList<Votes>();
        ValidVotes.clear();
        Votes SelectedVote = null;  //����ڰ� ������ Votes

        //��ǥ ������ Votes�� ��Ƴ���
        for (int i = 0; i < voteslist.size(); i++) {
            if (!voteslist.get(i).getEndOfVotes()) {
                //��ǥ�� ������ �ʾҴ���
                if (CountVoted(voteslist.get(i)) != voteslist.get(i).getNumAllow() //����
                		|| voteslist.get(i).checkUserUpdate((users.phone))) {
                    //������ ��ǥ�� �� �� �ִ���
                    ValidVotes.add(voteslist.get(i));
                }
            }
        }

        if(ValidVotes.size() == 0){
            System.out.println("���� ������ ��ǥ�� �����ϴ�.");
            System.out.println("3�� �Ŀ� ���� �޴��� ���ư��ϴ�...");
            try {
                Thread.sleep(Constant.gotoMenuSleepTime);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return;  //DoVoting ����, ������� �����Ƿ� ���� ���ص� ��
        }

        //��ǥ�� ������ ��ǥ ��� ���
        String line = "";
        System.out.println("<��ǥ�ϱ�>");
        System.out.println("(����Ϸ��� '"+Constant.quitChar+"' �Է�)");
        System.out.println("***���� �������� ��ǥ ���***");                       
        for (int i = 0; i < ValidVotes.size(); i++) {
           if(ValidVotes.get(i).checkUserUpdate(users.phone)) {
              System.out.println((i + 1) + ". " + ValidVotes.get(i).voteName + " (������ �߰��Ǿ� ����ǥ ����)");
            }
           else System.out.println((i + 1) + ". " + ValidVotes.get(i).voteName);
        }
        //� ��ǥ�� �Ұ��� ����
        while (true) {
            System.out.print("����: ");

            line = sc.nextLine();
            line = line.trim();

            if (line.equals(Constant.quitChar)) {
            	System.out.println("3�� �Ŀ� ���� �޴��� ���ư��ϴ�...");
                try {
                    Thread.sleep(Constant.gotoMenuSleepTime);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return;
            }
            else if (!(line.matches("^[0-" + ValidVotes.size() + "]$"))) {
                //���ڰ� �ƴѰ��
                System.out.println(Constant.inputErrorMsg);
                continue;
            }
            else if (Integer.parseInt(line) > ValidVotes.size() || Integer.parseInt(line) < 1) {
                //���� ���� ���ڸ� �Է��� ���
                System.out.println(Constant.inputErrorMsg);
                continue;
            }
            //�Է����Ŀ� ������� �ʴ°��
            SelectedVote = ValidVotes.get(Integer.parseInt(line) - 1);
            break;
        }
        
        //�Է¹��� ��ǥ�� �ֽ�ȭ�� �Ǿ� ����ǥ�� ������ ���
       if(SelectedVote.checkUserUpdate(users.phone)) {
          Constant.clearConsole();
            System.out.println("\n***" + SelectedVote.getVoteName() + "***");
            System.out.println("�������� �߰��Ǿ� ����ǥ�� �����մϴ�. ����ǥ �Ͻðڽ��ϱ�?");
            System.out.println("(\"��\"�� �����ϸ� ������ ��ǥ�� ��ȿ�� �˴ϴ�.)");
            System.out.println("1. ��\n2. �ƴϿ�");
            System.out.print("����: ");
            line = sc.nextLine();
            line = line.trim();
            if (line.equals(Constant.quitChar)) {
                System.out.println("3�� �Ŀ� ���� �޴��� ���ư��ϴ�...");
                try {
                    Thread.sleep(Constant.gotoMenuSleepTime);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return;
            }
            else if (line.equals(Constant.reVoteTrue)) {
            //������ ��ǥ ��ȿȭ
               
               String fileName = SelectedVote.getVoteCode();
               File file = new File(Constant.voteDirRoute + fileName);
               
               BufferedWriter bw = null;
               try {
                   bw = new BufferedWriter(new OutputStreamWriter(
                         new FileOutputStream(file, false), Charset.forName("UTF8")));
                   StringBuilder sb = new StringBuilder("");
                   String print = "";

                   //line1: ��ǥ�̸� ��ǥ��  ����
                   print = SelectedVote.getVoteName() + Constant.fieldTokenizerDelim + SelectedVote.getMakerNum() + Constant.fieldTokenizerDelim + SelectedVote.getUpdateVer();
                   sb.append(print + "\n");

                   //line2: ��ǥ���Ῡ�� ��ǥ���Ƚ�� �������߰����ɿ��� ����
                   print = Integer.toString(SelectedVote.getEndOfVotesInt())
                           + Constant.fieldTokenizerDelim + Integer.toString(SelectedVote.getNumAllow())
                           + Constant.fieldTokenizerDelim + Integer.toString(SelectedVote.getAddAllowInt())
                           + Constant.fieldTokenizerDelim + Integer.toString(SelectedVote.getMultipleVote())
                           + Constant.fieldTokenizerDelim + Integer.toString(SelectedVote.getIncreaseVoteNumALlowInt());
                   sb.append(print + "\n");

                   //line3-n: �������̸� ��ǥ�� ����
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
               //��ȿ ��ȿȭ ����
               String code = SelectedVote.getVoteCode();
               boolean suc = SelectedVote.loadVote(code);
               if(!suc) {
               	Constant.printFileError(SelectedVote.getVoteCode());
               }
               //��ǥ �� ����ڰ� ��ǥ�� �ñ��� ��ǥ ������Ʈ ���� = ��ǥ ������Ʈ ����
               //��ǥ�� ��ģ ��(��ǥ �������� select�ϴ� ���� ������ ��)���� ������ ������ ����ڰ� ��ǥ�� �ñ��� ��ǥ ������Ʈ ���� ������ �� ��ǥ ������Ʈ ���� ���� �����ؾ���.
         } else if (line.equals(Constant.reVoteFalse)) {
            return;
         }
         else {
            System.out.println(Constant.inputErrorMsg+"\n");
            return;
         }
       }

        //��ǥȽ�� �ȳ��������� ��ǥ����
        while (CountVoted(SelectedVote) != SelectedVote.getNumAllow()) {
            Constant.clearConsole();
          
            //������ ���
            System.out.println("\n***" + SelectedVote.getVoteName() + "***");
            System.out.println("(�� ������ �� ��ǥ ���� Ƚ��: " + SelectedVote.getMultipleVote() + "ȸ)");
            System.out.println("���� ��ǥ Ƚ��: " + (SelectedVote.getNumAllow()-CountVoted(SelectedVote)) + "ȸ");
            if (SelectedVote.getAddAllow() && SelectedVote.getOptionName().length < Constant.maxOptionNum)  {
               System.out.println("0. ������ �߰�");
            }

            //��ǥ ������ �ɼ� ����Ʈ
            ArrayList<VoteOptions> ValidOptionList = new ArrayList<VoteOptions>();
            int countVote = 0;
            int idx=0;
            //�� �ɼǸ��� ��ǥ�ߴ��� Ȯ��
            for (int i = 0; i < SelectedVote.getOptions().size(); i++) {
               countVote = 0;
                for (String voter : SelectedVote.getOptions().get(i).getVoterArray()) {
                	voter = voter.substring(0, voter.length()-2);
                    if (voter.equals(users.getPhone())) {
                        countVote++;
                    } 
                }
                //��ǥȽ����ŭ ��ǥ�� �������� �����ϰ� ������
                if (countVote < SelectedVote.multipleVote) {             
                    ValidOptionList.add(SelectedVote.getOptions().get(i));
                    System.out.println((idx + 1) + ". " + ValidOptionList.get(idx).getOptionName() + "(" + countVote + "ȸ)");
                    idx++;
                }
            }

            //��ǥ �ɼ� ���� ����
            while (true) {
                try{
                   int ver = SelectedVote.getUpdateVer();  //�ش� ��ǥ�� ������Ʈ ������ ������ �ʱ�ȭ ��
                    System.out.print("����: ");
                    line = sc.nextLine();
                    line = line.trim();
                    if (line.equals(Constant.quitChar)) {
                    	//��ǥ ��� ����
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
                    			System.out.println("3�� �Ŀ� ���� �޴��� ���ư��ϴ�...");
                    			//SaveToTxt();  //������� ����
                    			try {
                    				Thread.sleep(Constant.gotoMenuSleepTime);
                    			} catch (InterruptedException e) {
                    				// TODO Auto-generated catch block
                    				e.printStackTrace();
                    			}
                    			return;
                    		}
                    	}
                    	System.out.println("��� �������� ��ǥ�� ���� ���� �����ϴ�.");
                    	continue;
                    }
                    else if (isInteger(line)==false) {
                        //���ڰ� �ƴѰ��
                        System.out.println(Constant.inputErrorMsg);
                        continue;
                    }
                    else if (Integer.parseInt(line) > ValidOptionList.size() || Integer.parseInt(line) < 0) {
                        System.out.println(Constant.inputErrorMsg);    
                        continue;
                    }
                    else if (Integer.parseInt(line) == 0) {
                        if (SelectedVote.getAddAllow() && SelectedVote.getOptionName().length < Constant.maxOptionNum) {
                            //������ �߰��� ������ ��� �߰�
                            while (true) {
                                try{    
                                    System.out.println("\n---������ �߰�---");
                                    System.out.print("�Է�: ");
                                    line = sc.nextLine();
                                    line = line.trim();
                                    if (line.equals(Constant.quitChar)) {
                                    	return;
                                    } else if (line.equals("")) {
                                       System.out.println(Constant.inputErrorMsg);
                                       continue;
                                    }
                                    else if (!(line.matches(Constant.voteOptionsRegex))) {
                                        //Ư�����ڸ� ������ ���
                                        System.out.println(Constant.inputErrorMsg+"\n");
                                        continue;
                                    }
                                    else if (line.length() > Constant.maxVoteOptionLength) {
                                        //20���ڸ� ��� �Է��� ���
                                        System.out.println(Constant.inputErrorMsg+"\n");
                                        continue;
                                    }
                                    else {
                                        //�ߺ��� �ɼ��� �ƴ��� Ȯ��
                                        if (IfThereIsNoOption(SelectedVote, line)) {
                                            SelectedVote.getOptions().add(new VoteOptions(line, Constant.haveNoVoter));
                                            
                                            //�������� ������ �þ �� ��ǥ��� Ƚ���� �þ� ������ Ȯ��
                                            if(SelectedVote.getIncreaseVoteNumAllow()) {
                                               SelectedVote.numAllow = changeVoteNumAllow(SelectedVote);
                                            }
                                            
                                            SelectedVote.updateVer++;

                                            //�ش� Votes�� ���� ������Ʈ
                                            String fileName = SelectedVote.getVoteCode();
                                            File file = new File(Constant.voteDirRoute + fileName);
                                            
                                            BufferedWriter bw = null;
                                            try {
                                                bw = new BufferedWriter(new OutputStreamWriter(
                                                      new FileOutputStream(file, false), Charset.forName("UTF8")));
                                                StringBuilder sb = new StringBuilder("");
                                                String print = "";

                                                //line1: ��ǥ�̸� ��ǥ��  ����
                                                print = SelectedVote.getVoteName() + Constant.fieldTokenizerDelim + SelectedVote.getMakerNum() + Constant.fieldTokenizerDelim + SelectedVote.getUpdateVer();
                                                sb.append(print + "\n");

                                                //line2: ��ǥ���Ῡ�� ��ǥ���Ƚ�� �������߰����ɿ��� ����
                                                print = Integer.toString(SelectedVote.getEndOfVotesInt())
                                                        + Constant.fieldTokenizerDelim + Integer.toString(SelectedVote.getNumAllow())
                                                        + Constant.fieldTokenizerDelim + Integer.toString(SelectedVote.getAddAllowInt())
                                                        + Constant.fieldTokenizerDelim + Integer.toString(SelectedVote.getMultipleVote())
                                                        + Constant.fieldTokenizerDelim + Integer.toString(SelectedVote.getIncreaseVoteNumALlowInt());
                                                sb.append(print + "\n");

                                                //line3-n: �������̸� ��ǥ�� ����
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
                                            
                                            System.out.println("\n�߰� �Ϸ�!");
                                            System.out.println("3�� �Ŀ� ��ǥ�� ���ư��ϴ�...");
                                            try {
                                                Thread.sleep(Constant.gotoMenuSleepTime);
                                            } catch (InterruptedException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }

                                            Constant.clearConsole();
                                            System.out.println("\n***" + SelectedVote.getVoteName() + "***");
                                            System.out.println("(�� ������ �� ��ǥ ���� Ƚ��: " + SelectedVote.getMultipleVote() + "ȸ");
                                            System.out.println("���� ��ǥ Ƚ��: " + (SelectedVote.getNumAllow()-CountVoted(SelectedVote)) + "ȸ");
                                            if (SelectedVote.getAddAllow() && SelectedVote.getOptionName().length < Constant.maxOptionNum) {
                                               System.out.println("0. ������ �߰�");
                                            }

                                            //��ǥ ������ �ɼ� ����Ʈ ������Ʈ
                                            ValidOptionList.clear();
                                            //�� �ɼǸ��� ��ǥ�ߴ��� Ȯ��
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
                                                //��ǥȽ����ŭ ��ǥ�� �������� �����ϰ� ������
                                                //��ǥ�� ���� �������� ��� �ش� ������ ������
                                                if (countVote < SelectedVote.multipleVote) {             
                                                    ValidOptionList.add(SelectedVote.getOptions().get(i));
                                                    System.out.println((idx + 1) + ". " + ValidOptionList.get(idx).getOptionName() + "(" + countVote + "ȸ)");
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
                    	//��ǥ ó�� ���� ����   
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
                    		System.out.println("��� �������� ��ǥ�� ���� ���� �����ϴ�.");
                    		continue;
                    	}
                    	}
                    	                    	
                        //������ �������� �ùٸ��� �Է��� ���
                        ValidOptionList.get(Integer.parseInt(line) - 1).AddVoter(users.phone + Constant.updateVerTokenizerDelim + Integer.toString(ver));  //�κ� ����
                        ValidOptionList.get(Integer.parseInt(line) - 1).num += 1;

                        //������ �ٽ� ���
                        String fileName = SelectedVote.getVoteCode();
                        File file = new File(Constant.voteDirRoute + fileName);

                        BufferedWriter bw = null;
                        try {
                            bw = new BufferedWriter(new OutputStreamWriter(   
                                  new FileOutputStream(file, false), Charset.forName("UTF8")));
                            StringBuilder sb = new StringBuilder("");
                            String print = "";

                          //line1: ��ǥ�̸� ��ǥ��  ����
                            print = SelectedVote.getVoteName() + Constant.fieldTokenizerDelim + SelectedVote.getMakerNum() + Constant.fieldTokenizerDelim + SelectedVote.getUpdateVer();
                            sb.append(print + "\n");

                            //line2: ��ǥ���Ῡ�� ��ǥ���Ƚ�� �������߰����ɿ��� ����
                            print = Integer.toString(SelectedVote.getEndOfVotesInt())
                                    + Constant.fieldTokenizerDelim + Integer.toString(SelectedVote.getNumAllow())
                                    + Constant.fieldTokenizerDelim + Integer.toString(SelectedVote.getAddAllowInt())
                                    + Constant.fieldTokenizerDelim + Integer.toString(SelectedVote.getMultipleVote())
                                    + Constant.fieldTokenizerDelim + Integer.toString(SelectedVote.getIncreaseVoteNumALlowInt());
                            sb.append(print + "\n");

                            //line3-n: �������̸� ��ǥ�� ����                            
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

                        //��ǥ�� ���̻� ���ϴ� ��� ���� �޴��� ���ư�
                        if (CountVoted(SelectedVote) == SelectedVote.getNumAllow()) {
                            System.out.println("3�� �Ŀ� ���� �޴��� ���ư��ϴ�...");
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

    //name�� �̹� �ִ� ��ǥ �ɼ����� Ȯ��
    boolean IfThereIsNoOption(Votes vote, String name) {
        for (String o_name : vote.getOptionName()) {
            if (o_name.equals(name)) return false;
        }
        return true;
    }

    //�ش� Votes���� ����ڰ� ��ǥ�� Ƚ���� ��ȯ
    int CountVoted(Votes votes) {
        int count = 0;
        String[] Votedpersons;
        for (int i = 0; i < votes.getOptions().size(); i++) {
            Votedpersons = votes.getVoter(votes.getOptionName()[i]);
            for (int j = 0; j < Votedpersons.length; j++) {
            	//������ �κ�
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