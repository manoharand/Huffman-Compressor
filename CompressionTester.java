import org.junit.*;
import static org.junit.Assert.*;

/**
  * Test the data to DNA compression algorithms contained within HuffmanNode, HuffmanCompressor, and Decompressor.
  */
public class CompressionTester {

  /**
    * Test all methods associated with a HuffmanNode.
    */
  @Test
  public void testHuffmanNode() {
    HuffmanNode h = new HuffmanNode('x', 0, null, null, null, null);
    assertEquals("Should return 'x'", (Character)'x', h.getChar());
    assertEquals("Should return 0", 0, h.getFrequency());
    assertNull("Should return null", h.one);
    assertNull("Should return null", h.two);
    assertNull("Should return null", h.three);
    assertNull("Should return null", h.four);
    h.increaseFrequency();
    assertEquals("Should return 1", 1, h.getFrequency());
    HuffmanNode h1 = new HuffmanNode('a', 5, null, null, null, null);
    h.setOne(h1);
    assertEquals("Objects should be equal", h1, h.one);
    HuffmanNode h2 = new HuffmanNode('b', 0, null, null, null, null);
    h.setTwo(h2);
    assertEquals("Objects should be equal", h2, h.two);
    HuffmanNode h3 = new HuffmanNode('c', 0, null, null, null, null);
    h.setThree(h3);
    assertEquals("Objects should be equal", h3, h.three);
    HuffmanNode h4 = new HuffmanNode('d', 0, null, null, null, null);
    h.setFour(h4);
    assertEquals("Objects should be equal", h4, h.four);
    h.one.setDeleted();
    assertTrue("Should be deleted", h.one.getDeleted());
    h3.setBinary("ACTG");
    assertEquals("Nucleotide string should be ACTG", "ACTG", h3.getBinary());
    int comp = h.getFrequency() -  h.one.getFrequency();
    assertEquals("Should return -4", comp, h.compareTo(h1));
  }

  /**
    * Test the readInput() method of HuffmanCompressor.
    */
  @Test
  public void testReadInput() {
    HuffmanCompressor h = new HuffmanCompressor("testInputFile", "testOutputFile");
    h.readInput("testInputFile.txt");
    assertEquals((Character)'H', h.getReadArray()['H'].getChar());
    assertEquals((Character)'e', h.getReadArray()['e'].getChar());
    assertEquals((Character)'l', h.getReadArray()['l'].getChar());
    assertEquals((Character)'o', h.getReadArray()['o'].getChar());
    assertEquals((Character)'!', h.getReadArray()['!'].getChar());
    assertEquals(1, h.getReadArray()['H'].getFrequency());
    assertEquals(1, h.getReadArray()['e'].getFrequency());
    assertEquals(2, h.getReadArray()['l'].getFrequency());
    assertEquals(1, h.getReadArray()['o'].getFrequency());
    assertEquals(1, h.getReadArray()['!'].getFrequency());
  }

  /**
    * Test the merge() method of HuffmanCompressor.
    */
    @Test
    public void testMerge() {
      HuffmanCompressor h = new HuffmanCompressor("testInputFile", "testOutputFile");
      HuffmanNode h1 = new HuffmanNode('a', 1, null, null, null, null);
      HuffmanNode h2 = new HuffmanNode('b', 2, null, null, null, null);
      HuffmanNode h3 = new HuffmanNode('c', 3, null, null, null, null);
      HuffmanNode h4 = new HuffmanNode('d', 4, null, null, null, null);
      HuffmanNode merged = new HuffmanNode(null, 10, h1, h2, h3, h4);
      assertEquals(merged.getChar(), h.merge(h1, h2, h3, h4).getChar());
      assertEquals(merged.getFrequency(), h.merge(h1, h2, h3, h4).getFrequency());
      assertEquals(h1, h.merge(h1, h2, h3, h4).one);
      assertEquals(h2, h.merge(h1, h2, h3, h4).two);
      assertEquals(h3, h.merge(h1, h2, h3, h4).three);
      assertEquals(h4, h.merge(h1, h2, h3, h4).four);
    }

    /**
      * Test the createTree() method of HuffmanCompressor.
      */
      @Test
      public void testCreateTree() {
        HuffmanCompressor h = new HuffmanCompressor("testInputFile", "testOutputFile");

      }

}
