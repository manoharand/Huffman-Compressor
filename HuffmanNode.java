public class HuffmanNode {
  
  Character inChar; 
  int frequency;
  HuffmanNode one;
  HuffmanNode two;
  HuffmanNode three;
  HuffmanNode four;
  String bin;
  boolean deleted = false;
   
  public HuffmanNode(Character inChar, int frequency, HuffmanNode one, HuffmanNode two, HuffmanNode three, HuffmanNode four) {
    this.inChar = inChar;
    this.frequency = frequency;
    this.one = one; 
    this.two = two;
    this.three = three;
    this.four = four;
  }

  public void increaseFrequency() {
    this.frequency++;
  } 

  public void setOne(HuffmanNode one) {
    this.one = one;
  }

  public void setTwo(HuffmanNode two) {
    this.two = two;
  }
  
  public void setThree(HuffmanNode three) {
    this.three = three;
  } 
  
  public void setFour(HuffmanNode four) {
    this.four = four;
  }

  public Character getChar() {
    return this.inChar;
  }

  public void setDeleted() {
    this.deleted = true;
  }

  public boolean getDeleted() { 
    return this.deleted;
  }

  public int getFrequency() {
    return this.frequency;
  }

  public int compareTo(HuffmanNode o) {
    return (this.getFrequency() - o.getFrequency());
  }

  public void setBinary(String bin) {
    this.bin = bin;
  }

  public String getBinary() {
    return this.bin;
  }

}
