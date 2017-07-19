import java.io.*;

public class Decompressor {

  private String[] code = {"AA", "CA", "TA", "GA", "AC", "CC", "TC", "GC", "AT", "CT"};
  private ArrayList<Character> readList = new ArrayList<Character>();
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

  /** Create the tree. */

  public char decompress() {

  }

}
