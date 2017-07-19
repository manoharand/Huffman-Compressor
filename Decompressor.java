import java.io.*;
import java.util.*;

public class Decompressor {

  private String[] code = {"AA", "CA", "TA", "GA", "AC", "CC", "TC", "GC", "AT", "CT"};
  private ArrayList<Character> readList = new ArrayList<Character>();
  private ArrayList<Character> writeList = new ArrayList<Character>();
  private ArrayList<HuffmanNode> nodeList = new ArrayList<HuffmanNode>();
  private HuffmanNode[] readArray = new HuffmanNode[128];

  public Decompressor() {};
  
  public void readFile(String inputFileName) {
    int x;
    try {
    BufferedReader br = new BufferedReader(new FileReader(inputFileName));
      while ((x = br.read()) != -1) {
        readList.add((char)x);
      }
    }
    catch (IOException e) {}
  }

  public void decodeBarcode() {
    for (int i = 0; i < 128; i++) {
      int freq = 0;
      /** "TT" is the separator key. */
      while (!(readList.get(0).toString() + readList.get(1).toString()).equals("TT")) {
        freq = (freq * 10) + frequency(readList.get(0).toString() + readList.get(1).toString());
        readList.remove(0);
        readList.remove(0);
      }
      readArray[i] = new HuffmanNode((char)i, freq, null, null, null, null);
      for (HuffmanNode n : readArray) {
        if (n != null)
          nodeList.add(n); 
      }
      /** Sort the ArrayList based on frequency of each character. */
    	Collections.sort(nodeList, new Comparator<HuffmanNode>() {
    	  @Override
    	  public int compare(HuffmanNode h1, HuffmanNode h2) {
    	    return (h1.compareTo(h2)); 
    	  }
    	});
    }
  }

  public int frequency(String key) {
    for (int i = 0; i < code.length; i++) {
      if (code[i].equals(key))
        return i;
    }
    return -1;
  }

  public boolean isLeaf(HuffmanNode node) {
    if (node.one == null && node.two == null && node.three == null && node.four == null)
      return true;
    else
      return false;
  }

    /** 
    * Method to create a HuffmanTree by merging the four lowest-frequency HuffmanNodes in the 
    * ArrayList containing nodes that represent unique characters from the inputted file with their corresponding frequencies
    */
  public void createTree() {     
    for (int i = 0; nodeList.size() > 4; i++) {
    	mergeNodes();
      Collections.sort(nodeList, new Comparator<HuffmanNode>() {
        @Override
        public int compare(HuffmanNode h1, HuffmanNode h2) {
          return (h1.compareTo(h2)); 
        }
      });
    }
    while (nodeList.size() % 4 != 0) 
      nodeList.add(new HuffmanNode(null, 0, null, null, null, null));
    mergeNodes();
  }
  
  /**
    * Method to incrementally merge HuffmanNodes in the ArrayList. 
    */
  public void mergeNodes() {
    nodeList.add(merge(nodeList.get(0), nodeList.get(1), nodeList.get(2), nodeList.get(3)));
    for (int i = 0; i < 4; i++)
      nodeList.remove(0);
  }
  
  /**
    * Method to merge four HuffmanNodes
    * @param h1 the first HuffmanNode
    * @param h2 the second HuffmanNode
    * @param h3 the third HuffmanNode 
    * @param h4 the fourth HuffmanNode 
    * @return the merged node 
    */
  public HuffmanNode merge(HuffmanNode h1, HuffmanNode h2, HuffmanNode h3, HuffmanNode h4) {
    return new HuffmanNode(null, h1.getFrequency() + h2.getFrequency() + h3.getFrequency() + h4.getFrequency(), h1, h2, h3, h4);
  }

  public void decompress() {
    HuffmanNode root = nodeList.get(0);
    while (readList.size() > 0) {
      if (readList.get(0) == 'A' && !isLeaf(root)) {
        root = root.one;
        readList.remove(0);
      }
      else if (readList.get(0) == 'T' && !isLeaf(root)) {
        root = root.two;
        readList.remove(0);
      }
      else if (readList.get(0) == 'C' && !isLeaf(root)) {
        root = root.three;
        readList.remove(0);
      }
      else if (readList.get(0) == 'G' && !isLeaf(root)) {
        root = root.four;
        readList.remove(0);
      }
      else if (isLeaf(root)) {
        writeList.add(root.getChar());
        root = nodeList.get(0);
      }
    }
  }

	public void writeToFile(String outputFileName) {
		try {
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName));
			for (Character x : writeList) 
				bw.write(x);
		}
		catch (IOException e) {}
	}
	
	public void execute(String inputFileName, String outputFileName) {
		readFile(inputFileName);
		decodeBarcode();
		createTree();
		decompress();
		writeToFile(outputFileName);
	}
	
	public static void main(String[] args) {
		Decompressor d = new Decompressor();
		d.execute(args[0], args[1]);
	}

}
