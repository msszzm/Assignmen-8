package eg.edu.alexu.csd.datastructure.mailServer;

import eg.edu.alexu.csd.datastructure.com.LStack;

public class Sort implements ISort{//s
    String selectedSort;

    public Sort() {
        selectedSort=null;
    }

    public  LStack<Mail>  sort(LStack<Mail> l,IFolder folder,String attribute) throws Exception {

        if(folder.noofMails()==0&&folder.noofDeletedMails()==0) {
            System.out.println("It's zero in sort");
            return null;
        }
        if(selectedSort!=null)
            attribute = selectedSort;
        LStack<Mail> s = new LStack<>();;

        int test =folder.noofMails();
        if(l==null||l.size()==0) {
            for(int i =0;i<folder.noofMails();i++) {
                s.push(folder.getMail(i));
            }
            for(int i =0;i<folder.noofDeletedMails();i++) {
                s.push(folder.getDeletedMail(i).getDeletedMail());
            }
        }
        else
            s=l;


        LStack<Mail> bigger = new LStack<>();//stack for bigger than temp
        LStack<Mail> smaller = new LStack<>();//stack for smaller than temp
        LStack<Mail> extraSmaller = new LStack<>();//extra stack for smaller than temp
        LStack<Integer> numbers = new LStack<>();//stack for no of bigger than of each of pivot
        Mail temp = s.pop();//pivot the top of the stack
        int noBigger =0;
        boolean attributeInt = attributeInt(attribute);
        while(!s.isEmpty())//will empty the s for first time and return it ordered
            if(attributeInt)//compare integer attribute
                if(mailIndex(s.top(),attribute)<mailIndex(temp,attribute))
                    smaller.push(s.pop());
                else {
                    bigger.push(s.pop());
                    noBigger++;
                }
            else {//compare string attribute
                if(mailString(s.top(),attribute).compareTo(mailString(temp,attribute))<0)
                    smaller.push(s.pop());
                else {
                    bigger.push(s.pop());
                    noBigger++;
                }
            }
        numbers.push(noBigger);
        bigger.push(temp);


        while((bigger.size()!=0)||(smaller.size()!=0)){
            noBigger=0;
            if(smaller.size()==1||smaller.size()==0) {
                if(smaller.size()==1)
                    s.push(smaller.pop());
                if(bigger.size()!=0) {
                    s.push(bigger.pop());
                    int i = 0;
                    while(i++<numbers.top())
                        smaller.push(bigger.pop());
                    numbers.pop();
                }
            }
            else {
                temp = smaller.pop();
                while(!smaller.isEmpty())
                    extraSmaller.push(smaller.pop());
                while(!extraSmaller.isEmpty())
                    if(attributeInt) {
                        if(mailIndex(extraSmaller.top(),attribute)<mailIndex(temp,attribute))
                            smaller.push(extraSmaller.pop());
                        else {
                            bigger.push(extraSmaller.pop());
                            noBigger++;
                        }
                    }
                    else {
                        if(mailString(extraSmaller.top(),attribute).compareTo(mailString(temp,attribute))<0)
                            smaller.push(extraSmaller.pop());
                        else {
                            bigger.push(extraSmaller.pop());
                            noBigger++;
                        }
                    }
                numbers.push(noBigger);
                bigger.push(temp);
            }
        }

        if(l==null) {
            LStack<Mail> s1 = new LStack<>();
            while(!s.isEmpty())
                s1.push(s.pop());
            while(!s1.isEmpty()) {
                s.push(s.top());
            }
        }
        l=s;
        return s;
    }

    public static int mailIndex(Mail mail,String attribute) {
        int index=0;
        switch(attribute) {
            case("priority"):
                index = mail.getPriority();
            case("date"):
                index = (int) (mail.getDate().getTime() - 2629746*1000);
        }
        return index;
    }

    public static String mailString(Mail mail,String attribute) {
        String index="";
        switch(attribute) {
            case("head"):
                index = mail.getSubject();
            case("body"):
                index = mail.getBody();
            case("sender"):
                index = mail.getReceivers().toString();
            case("receivers"):
                index = mail.getReceivers().toString();
        }
        return index;
    }

    public static boolean attributeInt(String attribute) {
        boolean intger = false ;
        switch(attribute) {
            case("priority"):
                intger = true;
            case("head"):
                intger = false;
            case("body"):
                intger = false;
            case("sender"):
                intger = false;
            case("receivers"):
                intger = true;
            case("date"):
                intger = true;
        }
        return intger;
    }

    public 	String getSelectedSort() {
        return selectedSort;
    }

    public void	 setSelectedSort(String str) {
        selectedSort = str;
    }
}