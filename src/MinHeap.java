import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MinHeap<T extends Comparable> implements MyHeap {
	private Node root = null;
	private Node lastEditNode = null;
	private LinkedList<LinkedList<Node>> levels = new LinkedList<LinkedList<Node>>();
	private LinkedList<Node> prevLevel = new LinkedList<Node>();
	private LinkedList<Node> currLevel = new LinkedList<Node>();
	private LinkedList<Node> nxtLevel = new LinkedList<Node>();
	
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
		
		
		node = recursiveSearch(node.getLeftChild(), item);
			
		if(node == null){
			return recursiveSearch(node.getRightChild(), item);
		}else{
			return node;
		}
	}	
	
	private void shiftUp(Node nd){
		if(nd.getParent() != null){
			Comparable tmp = nd.getParent().getData();
			
			if(tmp.compareTo(nd.getData()) > 0){ //tmp < nd.data
				nd.getParent().setData(nd.getData());
				nd.setData(tmp);
				shiftUp(nd.getParent());
			}
		}
		return;
	}
	
	@Override
	public boolean insert(Comparable item){
		if(this.isEmpty()){
			this.root = new Node(item);
			this.lastEditNode = this.root();
			currLevel.add(this.lastEditNode);
			size++;
			return true;
		}else{
			Node newNode = new Node(item);			
			
			if(this.currLevel.peek().getLeftChild() == null){
				this.currLevel.peek().setLeftChild(newNode);
				newNode.setParent(this.currLevel.peek());
				nxtLevel.add(newNode);
				this.shiftUp(newNode);
			}else if(this.currLevel.peek().getRightChild() == null){
				this.currLevel.peek().setRightChild(newNode);
				newNode.setParent(this.currLevel.peek());
				nxtLevel.add(newNode);
				this.shiftUp(newNode);
			}else{
				this.currLevel.poll();
				if(this.currLevel.isEmpty()){
					this.currLevel = this.nxtLevel;
					this.nxtLevel = new LinkedList<Node>();
				}
				this.currLevel.peek().setLeftChild(newNode);
				newNode.setParent(this.currLevel.peek());
				this.nxtLevel.add(newNode);
				this.shiftUp(newNode);
			}
			size++;
			return true;
		}
	}
	
	public boolean delete(Node nd){
		if(this.isEmpty()){
			return false;
		}
		
		if(nd == this.root && nd.getLeftChild() == null && nd.getRightChild() == null){
			this.root = null;
			nd = null;
		}else{
			try {
				this.deleteAndShift(nd);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		size--;
		return true;
	}
	
	private Comparable deleteAndShift(Node nd) throws Exception{
		
		if(nd.getRightChild() == null && nd.getLeftChild() != null){
			Comparable tmp = nd.getData(); 
			nd.setData(nd.getLeftChild().getData());			
			return tmp;
		}
		
		if(nd.getRightChild() != null && nd.getLeftChild() != null){
			Comparable lData = nd.getLeftChild().getData();
			Comparable rData = nd.getRightChild().getData();
			
			int comp = lData.compareTo(rData);
			Comparable tmp = nd.getData(); 
			
			
			if(comp > 0){
				nd.setData(deleteAndShift(nd.getRightChild()));
			}else{
				nd.setData(deleteAndShift(nd.getLeftChild()));
			}
			
			return tmp;
		}
		
		
		if(nd.getRightChild() == null && nd.getLeftChild() == null){
			
			Comparable tmp = nd.getData(); 
			
			Node p = nd.getParent();
			
			if(p.getLeftChild() == nd){
				p.setLeftChild(null);
				//move child from the end of nextLevel to here
				//shift up
			}
			
			
			
			
			return tmp;
			
			
			
			
			
			
			
			
			
		}else{
			throw new Exception("Bad Heap");
		}
			
			
		
		
		
		
		
		

	}
	
	public LinkedList<Node> inOrder(){
		LinkedList<Node> lst = new LinkedList<Node>();
		this._inOrder(this.root, lst);
		return lst;
	}
	
	private void _inOrder(Node node, LinkedList<Node> lst){
		if(node != null){
			this._inOrder(node.getLeftChild(), lst);
			lst.add(node);
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
