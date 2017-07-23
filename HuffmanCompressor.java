import java.io.*;
import java.util.*;

/**
  * Compress a file using Huffman Encoding specified by a base-4 tree in which each branch is a separate nucleotide. The output file includes a barcode which specifies the frequency of each character in the input file that can be used to decode the file. The file itself is composed solely of nucleotides. Decodes all characters represented in UTF-8.
  */
public class HuffmanCompressor {

  /** Name of the input file with Strings to be compressed */
  private String inputFileName;
  /** Name of the file to be outputted */
  private String outputFileName;
  /** Array to store the characters read from the input file */
  private HuffmanNode[] readArray;
  /** ArrayList to count frequencies and assemble tree */
  private ArrayList<HuffmanNode> readList = new ArrayList<HuffmanNode>();
  /**Store the nucleotide parsing of the Huffman Tree. */
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

  /**
    * Check the max index of any character in a tree to create an array accordingly.
    * @param inputFile the name of the file to be read
    */
  public void checkSize(String inputFile) {
    try {
      BufferedReader inp = new BufferedReader(new FileReader(inputFile));
      int x;
      int size = 0;
      while ((x = inp.read()) != -1) {
        if (x > size)
          size = x;
      }
      readArray = new HuffmanNode[size + 1];
    }
    catch (IOException e) {}
  }

  /**
    * Method to read the input file and store its characters into first an array of HuffmanNodes with corresponding frequencies then into an ArrayList
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
  }

  /**
    * Return readList.
    * @return the list of HuffmanNodes arranged into a tree
    */
  public ArrayList<HuffmanNode> getReadList() {
    return readList;
  }

  /**
    * Return the readArray with all UTF-8 encoded characters and their frequencies.
    * @return the list of HuffmanNodes with characters and frequencies
    */
  public HuffmanNode[] getReadArray() {
    return readArray;
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
		while (readList.size() % 3 != 1) 
			readList.add(new HuffmanNode(null, 0, null, null, null, null));
		Collections.sort(readList, new Comparator<HuffmanNode>() {
      @Override
      public int compare(HuffmanNode h1, HuffmanNode h2) {
        return (h1.compareTo(h2));
      }
    });
    this.size = readList.size();
    for (int i = 0; readList.size() > 1; i++) {
      mergeNodes();
      /** Sort the ArrayList based on the frequency of each character. */
      Collections.sort(readList, new Comparator<HuffmanNode>() {
        @Override
        public int compare(HuffmanNode h1, HuffmanNode h2) {
          return (h1.compareTo(h2));
        }
      });
    }
    this.rt = readList.get(0);
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
    if (isLeaf(root)) {
      if (root.getChar() != null) {
        System.out.printf("%c : %-4d : %s \n", root.getChar(), root.getFrequency(), this.str);
        readArray[root.getChar()].setBinary(this.str.toString());
      }
      this.str = new StringBuilder();
      root.setDeleted();
    }
  }

  /**
    * Method to check whether the inputted node is a leaf node.
    * @param root the node to be checked
    * @return whether or not the node is a leaf
    */
  public boolean isLeaf(HuffmanNode root) {
    if ((root.one == null || root.one.getDeleted()) && (root.two == null || root.two.getDeleted()) && (root.three == null || root.three.getDeleted()) && (root.four == null || root.four.getDeleted()) && root.getDeleted() == false)
      return true;
    else
      return false;
  }

  /**
    * Method to generate a barcode to append to the beginning of the output file used to decompress the DNA.
    * @return String barcode
    */
  public String generateBarcode() {
    StringBuilder str = new StringBuilder();
    str.append(nucFreq(readArray.length));
    str.append("TT");
    for (HuffmanNode x : readArray) {
      str.append(nucFreq(x.getFrequency()));
      str.append("TT");
    }
    return str.toString();
  }

  /**
    * Method to return the frequency of a character as a nucleotide.
    * @param freq the frequency
    * @return the nucleotide representation of the frequency*/
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
    checkSize(inputFileName);
    readInput(inputFileName);
    createTree();
    fillArray();
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
      bw.close();
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
