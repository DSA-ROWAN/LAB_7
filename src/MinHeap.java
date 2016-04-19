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
	
	private int size = 0;
	
	protected Node root(){
		return root;
	}
	
	@Override
	public boolean isEmpty(){
		if(root == null) return true;
		return false;
	}
	
	public void clear(){
		root = null;
		currLevel = new LinkedList<Node>();
		nxtLevel = new LinkedList<Node>(); 
	}
	
	public int size(){return this.size;}
	
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
	
	private Node shiftUp(Node nd){
		if(nd.getParent() != null){
			Comparable tmp = nd.getParent().getData();
			
			if(tmp.compareTo(nd.getData()) > 0){ //tmp < nd.data
				nd.getParent().setData(nd.getData());
				nd.setData(tmp);
				return shiftUp(nd.getParent());
			}
		}
		return nd;
	}
	

	
	@Override
	public boolean insert(Comparable item){
		if(this.isEmpty()){
			this.clear();
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
					for(Node nds : this.nxtLevel){
						this.currLevel.add(nds);
					}
					this.nxtLevel.clear();
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

			this.deleteAndShift(nd);

		}

		size--;
		return true;
	}
	
	private void deleteAndShift(Node nd){
		
		Node nullNode = this.nxtLevel.removeLast();
		
		nd.setData(nullNode.getData());
		
		if(nullNode.getParent().getLeftChild() == nullNode){
			nullNode.getParent().setLeftChild(null);
		}else{
			nullNode.getParent().setRightChild(null);
		}
		
		
		if(this.nxtLevel.isEmpty()){
			for(Node nds : this.currLevel){
				this.nxtLevel.add(nds);
			}
			this.currLevel.clear();
		}else if(this.nxtLevel.getLast().getParent() != this.currLevel.peek()){
			this.currLevel.push(this.nxtLevel.getLast().getParent());
		}
		nd = this.shiftDown(nd);
		this.shiftUp(nd);
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Node shiftDown(Node nd){
		Comparable tmp = nd.getData();
		
		if(nd.getLeftChild() != null && nd.getData().compareTo(nd.getLeftChild().getData()) > 0){
			nd.setData(nd.getLeftChild().getData());
			nd.getLeftChild().setData(tmp);
			return this.shiftDown(nd.getLeftChild());
		}
		
		if(nd.getRightChild() != null && nd.getData().compareTo(nd.getRightChild().getData()) > 0){
			nd.setData(nd.getRightChild().getData());
			nd.getRightChild().setData(tmp);
			return this.shiftDown(nd.getRightChild());
		}
		return nd;
	}
	
	private void printList(LinkedList<Node> lst){
		
		for(Node tmp : lst){
			System.out.print(tmp.getData() + ",");
		}
		System.out.println();
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
		this.clear();
		this.insert(value);
		return root;
	}

	@Override
	public boolean deleteMin() {
		this.delete(this.root);
		return false;
	}

	@Override
	public boolean decreaseKey(Node key, Comparable updateValue) {
		try{
			key.setData(updateValue);
			key = this.shiftDown(key);
			this.shiftUp(key);
			return true;
		}catch(Exception err){
			return false;
		}
	}

	@Override
	public boolean union(MyHeap heap) {
		LinkedList<Comparable> hpList = new LinkedList<Comparable>();
		LinkedList<Comparable> thsList = new LinkedList<Comparable>();
		LinkedList<Comparable> unList = new LinkedList<Comparable>();
		
		while(!heap.isEmpty()){
			hpList.add(heap.findMin());
			heap.deleteMin();
			thsList.add(this.findMin());
			this.deleteMin();
			
		}
		
		for(Comparable c : thsList){
			if(!unList.contains(c)){
				unList.add(c);
			}
		}
		
		for(Comparable c : thsList){
			if(!unList.contains(c)){
				unList.add(c);
			}
		}

		return false;
	}

	@Override
	public Comparable findMin(){
		return root.getData();
	}	
}
