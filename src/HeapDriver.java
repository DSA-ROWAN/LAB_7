import java.math.*;
import java.util.LinkedList;
public class HeapDriver {

	public static void main(String[] args) {
		
		int[] ints = {434,802,
				115,496,216,57,
				335,277,8,	545,
				926,940,794,68,
				674,202,908,986,
				704,174,341,787,
				90,	323,637,739,
				174,551,682,331,
				207,801,473,808,
				638,238,842,74,
				751,621,220,652,
				370,957,725,176,
				47,	928,263,113
				};
		
		MinHeap<Integer> minHp = new MinHeap<Integer>();		
		
		for(int j = 0; j < 10; j++){
			
			for(int i = 0; i < 100; i++){
				minHp.insert(Math.random());
			}
			
			if(minHp.validateMinHeap()){
				System.out.println("Valid Min Heap");
			}else{
				System.out.println("InValid Min Heap");
			}

			minHp.printTree();
			while(minHp.size() > 0){
				LinkedList<Node> nds = minHp.inOrder();
				Node nd = nds.get((int)Math.random() * nds.size());
				minHp.delete(nd);
			}
			
			System.out.println("Heap is empty? : " + minHp.isEmpty());
		}
	}
}


