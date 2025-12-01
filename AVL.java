package project212.phase2.AVL;

class BSTNode<T> {
	public int key;
	public T data;
	public BSTNode<T> left,right;
	
public BSTNode(int key, T data) {
	this.key = key;
	this.data = data;
	left = right = null;
}
}
public class AVL<T>{
	private BSTNode<T> root, current;
	// int num_comp = 0; for للمقارنة
	public AVL() {
		current = root = null;
	}
	public boolean empty() {
		return root == null;
	}
	public boolean full() {
		return false;
	}
	public boolean findKey(int k) { //FIND
		BSTNode<T> p = root;
		while (p!= null) {
			current = p;
			if(k == p.key) {
				return true;}
				else if(k < p.key) {
					p = p.left;
				}
				else {
					p = p.right;
				}
			}
		return false;
		}
	public boolean insert(int k, T val) { //INSERT AVL
		    if (findKey(k)) return false; // key exists
		    root = insertAVL(root, k, val);
		    return true;
		}
		private BSTNode<T> insertAVL(BSTNode<T> root2, int k, T val) {
			if (root2 == null)
		        return new BSTNode<T>(k, val);

		    if (k < root2.key)
		    	root2.left = insertAVL(root2.left, k, val);
		    else
		    	root2.right = insertAVL(root2.right, k, val);

		    // rebalance
		    return rebalance(root2);
	}
		public boolean insert1(int k, T val) {

	        // check existing key
	        BSTNode<T> backup = current;
	        if (findKey(k)) {
	            current = backup;
	            return false;
	        }

	        BSTNode<T> newNode = new BSTNode<>(k, val);

	   
	        if (root == null) {
	            root = current = newNode;
	            return true;
	        }

	     
	       LinkedStak<BSTNode<T>> stack = new  LinkedStak<>();
	        BSTNode<T> p = root;
	        while (true) {
	            stack.push(p);
	            if (k < p.key) {
	                if (p.left == null) {
	                    p.left = newNode;
	                    break;
	                }
	                p = p.left;
	            } else {
	                if (p.right == null) {
	                    p.right = newNode;
	                    break;
	                }
	                p = p.right;
	            }
	        }

	        current = newNode;
	        while (!stack.empty()) { //avl reballance upward

	            BSTNode<T> parent = stack.pop();
	            BSTNode<T> newParent = rebalance(parent);

	            if (stack.empty()) {
	                // parent was the original root
	                root = newParent;
	            } 
	            else {
	                // pop grandparent 
	                BSTNode<T> grand = stack.pop();

	                if (grand.left == parent)
	                    grand.left = newParent;
	                else
	                    grand.right = newParent;

	               
	                stack.push(grand);
	            }
	        }


	                return true;
	            }
		 private BSTNode<T> rotateRight(BSTNode<T> y) {
		        BSTNode<T> x = y.left;
		        BSTNode<T> T2 = x.right;

		        x.right = y;
		        y.left = T2;
		        return x;
		    }
		    private BSTNode<T> rotateLeft(BSTNode<T> y) {
		        BSTNode<T> x = y.right;
		        BSTNode<T> T2 = x.left;

		        x.left = y;
		        y.right = T2;

		        return x;
		    }

		    private BSTNode<T> rotateLeftRight(BSTNode<T> node) {
		        node.left = rotateLeft(node.left);
		        return rotateRight(node);
		    }

		    private BSTNode<T> rotateRightLeft(BSTNode<T> node) {
		        node.right = rotateRight(node.right);
		        return rotateLeft(node);
		    }
		    private int height(BSTNode<T> node) {
		        if (node == null) return 0;
		        return 1 + Math.max(height(node.left), height(node.right));
		    }

		    
		    private int balanceFactor(BSTNode<T> node) {
		        if (node == null) return 0;
		        return height(node.right) - height(node.left);
		    }

		    
		    private BSTNode<T> rebalance(BSTNode<T> node) {
		        int bf = balanceFactor(node);
		        // ---- RIGHT HEAVY ----
		        if (bf > 1) {
		            if (balanceFactor(node.right) >= 0)
		                return rotateLeft(node);       // RR
		            else
		                return rotateRightLeft(node);  // RL
		        }

		    
		        if (bf < -1) {
		            if (balanceFactor(node.left) <= 0)
		                return rotateRight(node);      // LL
		            else
		                return rotateLeftRight(node);  // LR
		        }

		        return node; 
		    }

		    public boolean removeKey(int key) {

		        BSTNode<T> parent = null;
		        BSTNode<T> current = root;

		        // 1) ابحث عن الـ node
		        while (current != null && current.key != key) {
		            parent = current;
		            if (key < current.key)
		                current = current.left;
		            else
		                current = current.right;
		        }

		        // لم يتم العثور عليه
		        if (current == null)
		            return false;

		        // 2) إذا كان للـ node طفل واحد أو لا أطفال
		        if (current.left == null || current.right == null) {

		            BSTNode<T> child;
		            if (current.left != null)
		                child = current.left;
		            else
		                child = current.right;

		            if (parent == null) {
		                // حذف الـ root
		                root = child;
		            } else if (current == parent.left) {
		                parent.left = child;
		            } else {
		                parent.right = child;
		            }

		            return true;
		        }

		        // 3) في حالة وجود طفلين (نستخدم الأصغر في اليمين)
		        BSTNode<T> successorParent = current;
		        BSTNode<T> successor = current.right;

		        while (successor.left != null) {
		            successorParent = successor;
		            successor = successor.left;
		        }

		        // نسخ البيانات
		        current.key = successor.key;
		        current.data = successor.data;

		        // حذف successor
		        if (successorParent.left == successor)
		            successorParent.left = successor.right;
		        else
		            successorParent.right = successor.right;

		        return true;
		    }


		//INORDER
		public void inOrder() {
			if(root == null)
				System.out.println("empty tree");
			else
				inOrder(root);
		}
		private void inOrder(BSTNode<T>p) {
			if (p == null) return;
	        inOrder(p.left);
	        System.out.println("key= " + p.key + " , data=" + p.data);
	        inOrder(p.right);
	    }

	    public BSTNode<T> getRoot() {
	        return root;
	    }
		//HELPING METHODS
		public void findRoot() {
			current = root;
	}
		public int curkey() {
			return current.key;
		}
		
		public T retrieve() {
		    if (current == null)
		        return null;
		    return current.data;
		}

		public void delete(Product p) {
	        if (p == null) return;
	        removeKey(p.getProductId());
	    }
	
	}
	
