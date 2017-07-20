# Huffman-Compressor
Base-4 Huffman Compression

### Compilation
`javac HuffmanNode.java`
`javac HuffmanCompressor.java`
`javac Decompressor.java`
`javac -classpath hamcrest-core-*.jar;junit-*.jar;. CompressionTester.java`

### Running
`java HuffmanCompressor <inputFileName> <outputFileName>`
`java Decompressor <outputFileName> <decompressedFileName>`
`javac -classpath hamcrest-core-*.jar;junit-*.jar;. org.junit.runner.JUnitCore CompressionTester`
