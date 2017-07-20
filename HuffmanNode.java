/**
  * Object to represent data stored in a Huffman Tree. Includes four child pointers, character, frequency, and a nucleotide representation.
  */
public class HuffmanNode {

  /** the character of the node */
  Character inChar;
  /** the frequency of occurrence of the character */
  int frequency;
  /** the first child ("A") */
  HuffmanNode one;
  /** the second child ("T") */
  HuffmanNode two;
  /** the third child ("C") */
  HuffmanNode three;
  /** the fourth child ("G") */
  HuffmanNode four;
  /** the nucleotide represntation of the data */
  String bin;
  /** indicate if the node has been deleted */
  boolean deleted = false;

  /**
    * Create a HuffmanNode with a specified char, frequency, and four children.
    * @param inChar the character
    * @param frequency the frequency
    * @param one the first child
    * @param two the second child
    * @param three the third child
    * @param four the fourth child
    */
  public HuffmanNode(Character inChar, int frequency, HuffmanNode one, HuffmanNode two, HuffmanNode three, HuffmanNode four) {
    this.inChar = inChar;
    this.frequency = frequency;
    this.one = one;
    this.two = two;
    this.three = three;
    this.four = four;
  }

  /**
    * Increment the frequency by one.
    */
  public void increaseFrequency() {
    this.frequency++;
  }

  /**
    * Set the first child.
    * @param one the node to represent the first child
    */
  public void setOne(HuffmanNode one) {
    this.one = one;
  }

  /**
    * Set the second child.
    * @param one the node to represent the second child
    */
  public void setTwo(HuffmanNode two) {
    this.two = two;
  }

  /**
    * Set the third child.
    * @param one the node to represent the third child
    */
  public void setThree(HuffmanNode three) {
    this.three = three;
  }

  /**
    * Set the fourth child.
    * @param one the node to represent the fourth child
    */
  public void setFour(HuffmanNode four) {
    this.four = four;
  }

  /**
    * Return the character of the node.
    * @return the character
    */
  public Character getChar() {
    return this.inChar;
  }

  /**
    * Delete a node.
    */
  public void setDeleted() {
    this.deleted = true;
  }

  /**
    * Check if a node has been deleted.
    * @return whether or not the node has been deleted
    */
  public boolean getDeleted() {
    return this.deleted;
  }

  /**
    * Get the frequency of the character represented by the node.
    * @return the frequency
    */
  public int getFrequency() {
    return this.frequency;
  }

  /**
    * Comparator for HuffmanNodes. Compares by frequency of character occurrence.
    * @param o the node to be compared to
    * @return negative if greater, positive if smaller, 0 if equal
    */
  public int compareTo(HuffmanNode o) {
    return (this.getFrequency() - o.getFrequency());
  }

  /**
    * Set the nucleotide representation of a character.
    * @param bin the nucleotide representation
    */
  public void setBinary(String bin) {
    this.bin = bin;
  }

  /**
    * Return the nucleotide representation of the character.
    * @return the nucleotide representation
    */
  public String getBinary() {
    return this.bin;
  }

}
