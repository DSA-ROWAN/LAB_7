import java.util.LinkedList;
import java.util.NoSuchElementException;

public class MinHeap<T extends Comparable> implements MyHeap {
	private Node root = null;
	public int size = 0;
	
	private Node root(){
		return root;
	}
	
	@Override
	public boolean isEmpty(){
		if(root == null) return true;
		return false;
	}
	
	public void clear(){
		root = null;
	}
	
	private Node search(Comparable item){
		if(this.isEmpty()) return null;
		return this.recursiveSearch(this.root(), item);
		
	}
	
	private Node recursiveSearch(Node node, Comparable item){
		if(node == null) return null;
		int comp = item.compareTo(node.getData());
		if(comp == 0) return node;
		
		if(comp < 0){
			return recursiveSearch(node.getLeftChild(), item);
		}else{
			return recursiveSearch(node.getRightChild(), item);
		}
	}	
	
	@Override
	public boolean insert(Comparable item){
		if(this.isEmpty()){
			this.root = new Node(item);
			size++;
			return true;
		}else{
			Node curr = this.root();
			boolean done = false;
			
			Node newNode = new Node(item);
			
			while(!done){
				int comp = item.compareTo(curr.getData());
				
				if(comp == 0){
					return false;
				}
				
				if(comp < 0){//TODO add under curr
					if(curr.getLeftChild() == null){
						curr.setLeftChild(newNode);
						done = true;
					}else{
						curr = curr.getLeftChild();
					}
				}else{//TODO add above curr
					if(curr.getRightChild() == null){
						curr.setRightChild(newNode);
						done = true;
					}else{
						curr = curr.getRightChild();
					}
				}
			}
			newNode.setParent(curr);
			size++;
			return true;
		}
	}
	
	public boolean delete(Node nd){
		if(this.isEmpty()){
			return false;
		}
		
		Node deleteNode = this.search(nd.getData());
		
		if(deleteNode == null){
			return false;
		}
		
		Node hold;
		
		if(deleteNode.getRightChild() != null && deleteNode.getLeftChild() != null){
			hold = this.findPredecessor(deleteNode);
			deleteNode.setData(hold.getData());
			deleteNode = hold;
		}
		
		if(deleteNode.getRightChild() == null && deleteNode.getLeftChild() == null){
			deleteHere(deleteNode, null);
			size--;
			return true;
		}
		
		if(deleteNode.getRightChild() != null){
			hold = deleteNode.getRightChild();
			deleteNode.setRightChild(null);
		}else{
			hold = deleteNode.getLeftChild();
			deleteNode.setLeftChild(null) ;
		}
		
		deleteHere(deleteNode, hold);
		
		if(this.root == deleteNode){

		}
		size--;
		return true;
	}
	
	private void deleteHere(Node deleteNode, Node attach){
		Node parent = deleteNode.getParent();
		
		if(parent == null){
			this.root = attach;
			if(attach != null){
				attach.setParent(null);
			}
			return;
		}
		
		if(deleteNode == parent.getLeftChild()){
			parent.setLeftChild(attach);
			if(attach != null){
				attach.setParent(parent);
			}
			deleteNode = null;
			return;
		}
		parent.setRightChild(attach);
		if(attach != null){
			attach.setParent(parent);
		}
		deleteNode = null;
		return;
	}
	
	private Node findPredecessor(Node node){
		if(node.getLeftChild() == null){
			return null;
		}
		Node pred = node.getLeftChild();
		while(pred.getRightChild() != null){
			pred = pred.getRightChild();
		}
		return pred;
	}
	
	public LinkedList<? extends Comparable> inOrder(){
		LinkedList<Comparable> lst = new LinkedList<Comparable>();
		this._inOrder(this.root, lst);
		return lst;
	}
	
	private void _inOrder(Node node, LinkedList<Comparable> lst){
		if(node != null){
			this._inOrder(node.getLeftChild(), lst);
			lst.add(node.getData());
			this._inOrder(node.getRightChild(), lst);
		}
	}	
	
	public int count(T val1, T val2){
		int[] count = new int[1];
		
		if(val1.compareTo(val2) > 0){//val1 > val2 = -1
			this._count(this.root, val2, val1, count);
		}else{
			this._count(this.root, val1, val2, count);
		}		
	
		return count[0];
	}
	
	public int _count(Node node, T minVal, T maxVal, int[] count){
		if(node != null){
			if(node.getLeftChild() != null && node.getLeftChild().getData().compareTo(node.getData()) < 0){
				this._count(node.getLeftChild(), minVal, maxVal, count);
			}
			
			if((node.getData().compareTo(minVal) > 0) && (node.getData().compareTo(maxVal) < 0)){
				count[0]++;
			}
			
			if(node.getRightChild() != null && node.getRightChild().getData().compareTo(node.getData()) > 0){
				this._count(node.getRightChild(), minVal, maxVal, count);
			}
		}
		return 0;
	}
	
	public MinHeap<T> clone(){
		MinHeap<T> newTree = new MinHeap<T>();
		try {
			if(this.root != null){
				//this._clone(this.root, newTree.root(this.root.getData()));
				newTree.size = this.size;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newTree;
	}
	
	private Node _clone(Node node, Node newNode) throws Exception{
		if(node != null){
			newNode.setData(node.getData());
			Node newLeft = null;
			Node newRight = null;
			
			if(node.getLeftChild() != null){
				newLeft = new Node(node.getLeftChild().getData());
				newLeft.setParent(newNode);
				_clone(node.getLeftChild(), newLeft);
			}
			
			if(node.getRightChild() != null){
				newRight = new Node(node.getRightChild().getData());
				newRight.setParent(newNode);
				_clone(node.getRightChild(), newRight);
			}
			
			newNode.setLeftChild(newLeft);
			newNode.setRightChild(newRight);
			
			return null;
		}else{
			return node;
		}
	}
	
    public void printTree()
    {
        boolean first = true;
        String printVal = "";
        int count = 0;
        LinkedList<Node> currentLevel = new LinkedList<Node>();
        LinkedList<Node> nextLevel = new LinkedList<Node>();
        
        currentLevel.push(this.root);
        
        while (currentLevel.size() > 0)
        {
        	Node currNode = currentLevel.removeLast();
            if(currNode != null)
            {
                if(first)
                {
                    first = false;
                    printVal += count + ": ";
                    count++;
                }
                
                String direction = "";
                
                if(currNode != this.root && currNode.getParent().getLeftChild() != null
                		&& currNode == currNode.getParent().getLeftChild()){
                	direction = " left node of " + currNode.getParent().getData();
                }else if(currNode != this.root){
                	direction = " right node of " + currNode.getParent().getData();
                }
                
                printVal += currNode.getData() + direction + " ";
                if(currentLevel.size() > 0)
                {
                    printVal += "         ";
                }
                nextLevel.push(currNode.getLeftChild());
                nextLevel.push(currNode.getRightChild());
            }
            if(currentLevel.size() == 0)
            {
            	printVal += "\n";
                first = true;
                currentLevel = nextLevel;
                nextLevel = new LinkedList<Node>();
            }
        }
        System.out.print(printVal);
    }
	
	@Override
	public Node makeHeap(Comparable value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteMin() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean decreaseKey(Node key, Comparable updateValue) {
		// TODO Auto-generated method stub
		return false;
	}

	


	@Override
	public boolean union(MyHeap heap) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Comparable findMin() {
		// TODO Auto-generated method stub
		return null;
	}

	 
	
	
}
