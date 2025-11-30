package project212.phase2.AVL;

public class LinkedStak<T> {
	private Node<T> Top;	
	public LinkedStak () {
		Top  = null;
	}
	public boolean empty () {
		return Top == null;
        }
	public boolean full () {
		return false;
	}
	public void push(T x)
        {
        Node<T>p=new Node<>(x);
        if(Top==null)
            Top=p;
        else{
        p.next=Top;
        Top=p;
        }
        }
       public T pop()
       {
       T x=Top.data;
       Top=Top.next;
       return x;
       }
}
