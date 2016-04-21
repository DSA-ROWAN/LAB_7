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
			size--;
			return true;
		}else{
			try{
				this.deleteAndShift(nd);
				size--;
				return true;
			}catch(Exception err){
				return false;
			}
		}
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
                    printVal += "  ";
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
		if(this.isEmpty()){
			this.clear();
			this.insert(value);
		}
		
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
		LinkedList<Node> hpList = new LinkedList<Node>();
		this._inOrder(heap.makeHeap(null), hpList);
		LinkedList<Node> thsList = this.inOrder();
		
		boolean insert = false;
		
		for(Node hpNd : hpList){
			insert = true;
			for(Node nd : thsList){
				if(nd.getData().equals(hpNd.getData())){
					insert = false;
				}
			}
			if(insert == true){this.insert(hpNd.getData());}
		}
		
		return true;
	}

	@Override
	public Comparable findMin(){
		return root.getData();
	}	
	
	public boolean validateMinHeap(){
		if(this.isEmpty()){
			return true;
		}
		return this._validateMinHeap(this.root);
	}
	
	private boolean _validateMinHeap(Node nd){
		boolean resultL = true;
		boolean resultR = true;
		if(nd.getLeftChild() != null){
			if(nd.getLeftChild().getData().compareTo(nd.getData())  > 0){
				resultL = this._validateMinHeap(nd.getLeftChild());
			}else{
				return false;
			}
		}
		
		if(nd.getRightChild() != null){
			if(nd.getRightChild().getData().compareTo(nd.getData())  > 0){
				resultR = this._validateMinHeap(nd.getRightChild());
			}else{
				return false;
			}
		}
		
		return (resultL && resultR);
	}
}
