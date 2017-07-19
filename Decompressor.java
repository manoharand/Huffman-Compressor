import java.io.*;

public class Decompressor {

  private String[] code = {"AA", "CA", "TA", "GA", "AC", "CC", "TC", "GC", "AT", "CT"};
  private ArrayList<Character> readList = new ArrayList<Character>();
  private ArrayList<Character> writeList = nwe ArrayList<Character>();
  private HuffmanNode[] readArray = new HuffmanNode[128];

  public void readFile(inputFileName) {
    BufferedReader br = new BufferedReader();
    try {
      while ((x = br.read()) != -1) {
        readList.add((Character)x);
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
    }
  }

  public int frequency(String key) {
    for (int i = 0; i < code.length; i++) {
      if (code[i].equals(key))
        return i;
    }
  }

  public boolean isLeaf(HuffmanNode node) {
    if (node.one == null && node.two == null && node.tree == null && node.four == null)
      return true;
    else
      return false;
  }

  /** Create the tree. */

  public char decompress() {
    HuffmanNode root = readArray.get(0);
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
        root = readArray.get(0);
      }
    }
  }

}
