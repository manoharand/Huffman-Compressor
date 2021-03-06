import java.io.*;
import java.util.*;

/**
  * Decompress a file composes solely of nucleotides and has a barcode appended to the front. Generated by HuffmanCompressor.
  */
public class Decompressor extends HuffmanCompressor {

  /** nucleotide representation of each base-10 digit */
  private String[] code = {"AA", "CA", "TA", "GA", "AC", "CC", "TC", "GC", "AT", "CT"};
  /** list containing every char from the input file */
  private ArrayList<Character> readList = new ArrayList<Character>();
  /** list containing the decompressed file */
  private ArrayList<Character> writeList = new ArrayList<Character>();
  /** the Huffman Tree */
  private ArrayList<HuffmanNode> nodeList = new ArrayList<HuffmanNode>();
  /** list containing HuffmanNodes with characters and their corresponding frequencies; size based on total UTF-8 characters */
  private HuffmanNode[] readArray = new HuffmanNode[1112064];

  /**
    * Constructor
    */
  public Decompressor() {
    super("", "");
  };

  /**
    * Read an input file into readList.
    * @param inputFileName the inputfile
    */
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

  /**
    * Read the barcode two bases at a time.
    * @return the frequency
    */
  public int readBases() {
    int freq = 0;
    while (!(readList.get(0).toString() + readList.get(1).toString()).equals("TT")) {
      /** Determine the frequency encoded by the DNA. */
      freq = (freq * 10) + frequency(readList.get(0).toString() + readList.get(1).toString());
      /** remove characters once they're read */
      readList.remove(0);
      readList.remove(0);
    }
    /** remove characters once they're read */
    readList.remove(0);
    readList.remove(0);
    return freq;
  }

  /**
    * Decode the barcode appended to the front of the DNA.
    */
  public void decodeBarcode() {
    int size = readBases();
    for (int i = 0; i < size; i++) {
      readArray[i] = new HuffmanNode((char)i, readBases(), null, null, null, null);
    }
    /** Start creating the tree. */
    for (HuffmanNode n : readArray) {
      if (n != null && n.getFrequency() != 0)
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

  /**
    * Decode the barcode and return frequency.
    * @param key the nucleotides
    * @return the frequency
    */
  public int frequency(String key) {
    for (int i = 0; i < code.length; i++) {
      if (code[i].equals(key))
        return i;
    }
    return -1;
  }

  /**
    * Checks whether the inputted node is a leaf node.
    * @param node the node inputted
    * @return whether or not the node is a leaf
    */
  @Override
  public boolean isLeaf(HuffmanNode node) {
    if (node.one == null && node.two == null && node.three == null && node.four == null)
      return true;
    else
      return false;
  }

  /**
    * Create a HuffmanTree by merging the four lowest-frequency HuffmanNodes in the ArrayList containing nodes that represent unique characters from the inputted file with their corresponding frequencies
    */
  
   public void createTree() {
		while (nodeList.size() % 3 != 1) 
			nodeList.add(new HuffmanNode(null, 0, null, null, null, null));
		Collections.sort(nodeList, new Comparator<HuffmanNode>() {
      @Override
      public int compare(HuffmanNode h1, HuffmanNode h2) {
        return (h1.compareTo(h2));
      }
    });
    for (int i = 0; nodeList.size() > 1; i++) {
      mergeNodes();
      /** Sort the ArrayList based on the frequency of each character. */
      Collections.sort(nodeList, new Comparator<HuffmanNode>() {
        @Override
        public int compare(HuffmanNode h1, HuffmanNode h2) {
          return (h1.compareTo(h2));
        }
      });
    }		
  }

  /**
    * Method to incrementally merge HuffmanNodes in the ArrayList.
    */
  @Override
  public void mergeNodes() {
    nodeList.add(super.merge(nodeList.get(0), nodeList.get(1), nodeList.get(2), nodeList.get(3)));
    for (int i = 0; i < 4; i++)
      nodeList.remove(0);
  }

  /**
    * Decompress the DNA by traversing through the Huffman Tree.
    */
  public void decompress() {
    HuffmanNode root = nodeList.get(0);
    /** Traverse based on the nucleotide */
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
        /** Add the leaf node to the tree */
        writeList.add(root.getChar());
        root = nodeList.get(0);
      }
    }
  }

  /**
    * Write the decompressed nucleotides to a text file.
    * @param outputFileName the file to be written to
    */
	public void writeToFile(String outputFileName) {
		try {
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName));
      for (Character x : writeList) {
				bw.write(x);
			}
    bw.close();
    }
		catch (IOException e) {}
	}

  /**
    * Execute the decompression
    * @param inputFileName file containing the DNA
    * @param outputFileName file containing the decompressed data
    */
	public void execute(String inputFileName, String outputFileName) {
		readFile(inputFileName);
		decodeBarcode();
		createTree();
		decompress();
		writeToFile(outputFileName);
	}

  /**
    * Run the program.
    * @param args the arguments
    */
	public static void main(String[] args) {
		Decompressor d = new Decompressor();
		d.execute(args[0], args[1]);
	}

}
