package eg.edu.alexu.csd.datastructure.mailServer;

import eg.edu.alexu.csd.datastructure.com.AQueue;
import eg.edu.alexu.csd.datastructure.com.LStack;
import eg.edu.alexu.csd.datastructure.com.SingleLinked;

public class Filter implements IFilter {
    SingleLinked <String>filterlist = new SingleLinked<>();

    public LStack<Mail> filter(LStack<Mail> l, IFolder folder, String attribute, String required) throws Exception {

        LStack<Mail> temp = new LStack<>();
        LStack<Mail> filteredmail = new LStack<>();
        if(required==null)
            return l;

        Sort Sorting = new Sort();


        if(!attributeInt(attribute)) {//if required data is string we will pass by all mails
            LStack<Mail> s = Sorting.sort(l,folder,"date");//just get a copy of inbox and
            if(attribute.equals("sender"))//if searching sender
                while(!s.isEmpty())
                    if(s.top().getSender().getEmail().equals(required))
                        filteredmail.push(s.pop());
                    else
                        s.pop();
            else if(attribute.equals("receivers")) {//if searching receivers
                AQueue<Contact> t = s.top().getReceivers();
                while(!s.isEmpty()){
                    if(t.isEmpty())
                        t = s.pop().getReceivers();
                    if(t.dequeue().getEmail().equals(required)) {
                        filteredmail.push(s.pop());
                        if(!s.isEmpty())
                            t = s.top().getReceivers();
                    }
                }
            }
            else//else search body or head
                while(!s.isEmpty())
                    if(mailString(s.top(),attribute).contains(required))
                        filteredmail.push(s.pop());
                    else
                        s.pop();
            l = filteredmail;
            return filteredmail;
        }





        //else comparing integer attribute so using sort and use binary search
        LStack<Mail> s = Sorting.sort(l,folder,attribute);
        int U = s.size();//upper pointer
        int L = 0;//lower pointer
        boolean found = false;


        while(L<=U &&!found) {
            while(s.size()!=(U+L)/2) {
                if(s.size()<(U+L)/2)
                    temp.push(s.pop());
                else
                    s.push(temp.pop());
            }
            if(mailIndex(s.top(),attribute)==Integer.valueOf(required))
                found = true;
            else if(mailIndex(s.top(),attribute)>Integer.valueOf(required))
                L = (U+L)/2 + 1;
            else
                U = (U+L)/2 - 1 ;
        }

        if(found) {//if found index with required data check before it and after it if they also have required data
            filteredmail.push(s.top());
            while(!s.isEmpty()&&mailIndex(s.top(),attribute)==Integer.valueOf(required))
                filteredmail.push(s.pop());
            while(!temp.isEmpty()&&mailIndex(temp.top(),attribute)==Integer.valueOf(required))
                filteredmail.push(temp.pop());
        }
        l = filteredmail;
        return filteredmail;
    }



    public static String mailString(Mail mail,String attribute) {
        String index="";
        switch(attribute) {
            case("subject"):
                index = mail.getSubject();
            case("body"):
                index = mail.getBody();
            case("sender"):
                index = mail.getSender().toString();
            case("receivers"):
                index = mail.getReceivers().toString();
        }
        return index;
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

    public static boolean attributeInt(String attribute) {
        boolean intger = false ;
        switch(attribute) {
            case("priority"):
                intger = true;//if he choose priority the required is number of priority
            case("subject"):
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

    public SingleLinked <String> getFilterList() {
        return filterlist;
    }

    public void setFilterList(SingleLinked <String> Filterlist) {
        filterlist = Filterlist;
    }
}
