import java.io.*;
import java.util.*;

/** 
  * Read a file and convert each character to a nucleotide representation using a Huffman Compressor. 
  */
public class HuffmanCompressor {

  /** Name of the input file with Strings to be compressed */
  private String inputFileName;
  /** Name of the file to be outputted */
  private String outputFileName;
  /** Array to store the characters read from the input file */
  private HuffmanNode[] readArray = new HuffmanNode[128];
  /** ArrayList to count frequencies and assemble tree */ 
  private ArrayList<HuffmanNode> readList = new ArrayList<HuffmanNode>(128);
  private StringBuilder str = new StringBuilder();
  /**store the root of the Huffman tree */
  private HuffmanNode rt;
  /** store the size of the ArrayList before creating a HuffmanTree from the list */ 
  private int size;

  /** 
    * Constructor
    * @param inputFileName the file to be inputted 
    * @param outputFileName the deliverable file 
    */
  public HuffmanCompressor(String inputFileName, String outputFileName) {
    this.inputFileName = inputFileName;
    this.outputFileName = outputFileName;
  }

  /** Method to read the input file and store its characters into first an array of HuffmanNodes with corresponding frequencies then into an ArrayList 
    * @param inputFile the file to be read
    */
  public void readInput(String inputFile) {
    try {
      BufferedReader inp = new BufferedReader(new FileReader(inputFile));
      int x;
      while ((x = inp.read()) != -1) {
        /** Immediately acccesses the location because the characters are stored in a location corresponding to their ASCII value. */
        if (readArray[x] == null)
          readArray[x] = new HuffmanNode((char)x, 1, null, null, null, null);
        else 
          readArray[x].increaseFrequency();
      }
      for (HuffmanNode n : readArray) {
        if (n != null)
          readList.add(n); 
      }
    }
    catch (FileNotFoundException e) {
      System.out.println("File not found.");
    } 
    catch (IOException e) {
      System.out.println("Input / output exception.");
    }
    /** Sort the ArrayList based on frequency of each character. */
    Collections.sort(readList, new Comparator<HuffmanNode>() {
      @Override
      public int compare(HuffmanNode h1, HuffmanNode h2) {
        return (h1.compareTo(h2)); 
      }
    });
    /** Generate a quaternary Huffman tree using the ArrayList. */
    this.createTree();
    this.fillArray();
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

  /** 
    * Method to create a HuffmanTree by merging the four lowest-frequency HuffmanNodes in the 
    * ArrayList containing nodes that represent unique characters from the inputted file with their corresponding frequencies
    */
  public void createTree() {     
    this.size = readList.size();
    for (int i = 0; readList.size() > 4; i++) {
        mergeNodes();
        Collections.sort(readList, new Comparator<HuffmanNode>() {
        @Override
        public int compare(HuffmanNode h1, HuffmanNode h2) {
          return (h1.compareTo(h2)); 
        }
      });
    }
    while (readList.size() % 4 != 0) 
      readList.add(new HuffmanNode(null, 0, null, null, null, null));
    mergeNodes();
    this.rt = readList.get(0);
    System.out.println(readList.size());
  }

  /**
    * Method to occupy each open index of the readArray with an empty HuffmanNode. 
    */
  public void fillArray() {
    for (int i = 0; i < readArray.length; i++) {
      if (readArray[i] == null) 
        readArray[i] = new HuffmanNode(null, 0, null, null, null, null);
    }
  }

  /**
    * Method to incrementally merge HuffmanNodes in the ArrayList. 
    */
  public void mergeNodes() {
    readList.add(merge(readList.get(0), readList.get(1), readList.get(2), readList.get(3)));
    for (int i = 0; i < 4; i++)
      readList.remove(0);
  }

  /** 
    * Method to traverse the tree and create Strings representing the nucleotide representation of each character in the inputted file based on their respective frequencies.
    * @param root the root of the tree 
    */
  public void traverse(HuffmanNode root) {
    while (root.one != null && root.one.getDeleted() == false) { 
      this.str.append("A");
      root = root.one;
    }
    if (root.two != null && root.two.getDeleted() == false) {
      this.str.append("T");
      traverse(root.two);
    }
    else if (root.three != null && root.three.getDeleted() == false) {
      this.str.append("C");
      traverse(root.three);
    }
    else if (root.four != null && root.four.getDeleted() == false) {
      this.str.append("G");
      traverse(root.four);
    }
    /** check that the node is a leaf node with a character assignment */
    if ((root.one == null || root.one.getDeleted()) && (root.two == null || root.two.getDeleted()) && (root.three == null || root.three.getDeleted()) && (root.four == null || root.four.getDeleted()) && root.getDeleted() == false) {
      if (root.getChar() != null) {
        System.out.printf("%c : %-4d : %s \n", root.getChar(), root.getFrequency(), this.str);
        readArray[root.getChar()].setBinary(this.str.toString());
      }
      this.str = new StringBuilder(); 
      root.setDeleted();
    }
  }

  /**
    * Method to generate a barcode to append to the beginning of the output file used to decompress the DNA. 
    * @return String barcode 
    */
  public String generateBarcode() {
    StringBuilder str = new StringBuilder();
    for (HuffmanNode x : readArray) {
      str.append(nucFreq(x.getFrequency()));
      str.append("TT");
    }
    return str.toString();
  }

  public String nucFreq(int freq) {
    String[] code = {"AA", "CA", "TA", "GA", "AC", "CC", "TC", "GC", "AT", "CT"};
    String str = "";
    if (freq == 0)
      return "AA";
    while (freq > 0) {
      int rem = freq % 10;
      freq = freq / 10;
      str = code[rem] + str;
    }
    return str;
  }


  /** 
    * Method to read a file, create a HuffmanTree, traverse through the tree, and output the nucleotide representation of the entire file to the given outputFile 
    * @param inputFileName the file to be read 
    * @param outputFileName the file to be outputted to 
    * @return the result of the execution 
    */
  public void huffmanCode(String inputFileName, String outputFileName) { 
    readInput(inputFileName);
    for (int i = 0; i < this.size; i++)
      this.traverse(this.rt);
    try {
      BufferedReader inp = new BufferedReader(new FileReader(inputFileName));
      BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName));
      int x;
      bw.write(generateBarcode());
      while ((x = inp.read()) != -1) {
        bw.write(readArray[x].getBinary());
      }
    }
    catch (FileNotFoundException e) {
      System.out.println( "FileNotFoundException");
    } 
    catch (IOException e) {
      System.out.println("IOException");
    }
  }
  
  /** 
    * Main method to run the program. 
    * @param args the input arguments
    */
  public static void main(String[] args) {
    HuffmanCompressor h = new HuffmanCompressor(args[0], args[1]);
    h.huffmanCode(args[0], args[1]);
  }
}
