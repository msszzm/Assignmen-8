  
package eg.edu.alexu.csd.datastructure.mailServer;

import eg.edu.alexu.csd.datastructure.com.LStack;
import eg.edu.alexu.csd.datastructure.com.SingleLinked;

public interface IFilter {
    public SingleLinked<String> getFilterList();
    public LStack<Mail> filter(LStack<Mail> l, IFolder folder, String attribute, String required) throws Exception;
}