package eg.edu.alexu.csd.datastructure.mailServer;

import eg.edu.alexu.csd.datastructure.com.LStack;

public interface ISort {
    public LStack<Mail> sort(LStack<Mail> l, IFolder folder, String attribute) throws Exception;
    public void	 setSelectedSort(String str);
    public String getSelectedSort();
}
