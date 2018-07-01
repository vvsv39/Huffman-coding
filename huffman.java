import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class Huffman {

    // Node of the huffman tree
    class Node{
        int freq;
        char ch;
        Node left;
        Node right;
    }

    // This compares each node frequency
    class HuffmanComparator implements Comparator<Node> {
        public int compare(Node x, Node y)
        {
            return x.freq - y.freq;
        }
    }

    private Map<Character, Integer> huffMan;

    private String validChars = "abcdefghijklmnopqrst01234567890";

    private int total = 0;

    private int totalBits = 0;

    List<Map.Entry<Character, Integer>> list;

    List<Map.Entry<Character, String>> listCodes;

    public Huffman(){
        huffMan = new HashMap<>();
    }

    public void generateCode(Node root, String s, Map<Character, String> mapCodes)
    {
        if (root.left == null && root.right == null) {
            if(mapCodes != null){
                mapCodes.put(root.ch, s);
                totalBits += s.length();
            }
            return;
        }
        generateCode(root.left, s + "0", mapCodes);
        generateCode(root.right, s + "1", mapCodes);
    }

    public void createHuffmanCode(){
        PriorityQueue<Node> priorityQueue
                = new PriorityQueue<Node>(total, new HuffmanComparator());

        for(Map.Entry<Character, Integer> node:list){
            Node n = new Node();
            n.ch = node.getKey();
            n.freq = node.getValue();
            priorityQueue.add(n);
        }

        Node rootNode = null;
        while (priorityQueue.size() > 1) {
            Node firstMin = priorityQueue.peek();
            priorityQueue.poll();
            Node secMin = priorityQueue.peek();
            priorityQueue.poll();
            Node newNode = new Node();
            newNode.freq = firstMin.freq + secMin.freq;
            newNode.ch = '+';
            newNode.left = firstMin;
            newNode.right = secMin;
            rootNode = newNode;
            priorityQueue.add(newNode);
        }

        Map<Character, String> mapCodes = new HashMap<>();
        generateCode(rootNode, "", mapCodes);

        Set<Map.Entry<Character, String>> set = mapCodes.entrySet();
        listCodes = new ArrayList<>(set);
        Collections.sort( listCodes, new Comparator<Map.Entry<Character, String>>()
        {
            public int compare( Map.Entry<Character, String> o1, Map.Entry<Character, String> o2 )
            {
                if (o2.getValue().length() >  o1.getValue().length()){
                    return 1;
                }else {
                   return -1;
                }
            }
        });
    }

    private void sortHuffman(){

        Set<Map.Entry<Character, Integer>> set = huffMan.entrySet();
        list = new ArrayList<>(set);
        Collections.sort( list, new Comparator<Map.Entry<Character, Integer>>()
        {
            public int compare( Map.Entry<Character, Integer> o1, Map.Entry<Character, Integer> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );
    }

    public void writeOutput(String fileName){

        FileWriter writer = null;
        try{
            File file = new File(fileName);
            DecimalFormat f = new DecimalFormat("0.00");

            writer = new FileWriter(file);

            writer.write("Symbol\tFrequency\n");

            for(Map.Entry<Character, Integer> entry:list){
                double v = (entry.getValue()/(float)total)*100;
                String vStr = f.format(v);
                writer.write( entry.getKey()+"\t\t"+vStr+"%\n");
            }
            writer.write("\n");
            writer.write("Symbol\tHuffman Codes\n");
            for(Map.Entry<Character, String> entry:listCodes){
                writer.write( entry.getKey()+"\t\t"+entry.getValue()+"\n");
            }

            writer.write("\nTotal Bits: " + totalBits +"\n");

            writer.flush();
            writer.close();

        }catch (IOException ex){
            System.out.println(ex.getMessage());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }finally {
            try{
                if(writer != null){
                    writer.flush();
                    writer.close();
                }
            }catch (Exception e){}
        }

    }

    public void createHuffmanTree(String fileName){

        try{
            FileReader fileReader = new FileReader(new File(fileName));
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int r;
            while ((r = bufferedReader.read()) != -1){
                char ch = (char) r;
                String s = "" + ch;
                s = s.toLowerCase();
                ch = s.charAt(0);
                if(ch == ' '){
                    continue;
                }

                if(validChars.contains(s)) {
                    if (huffMan.containsKey(ch)) {
                        int value = huffMan.get(ch);
                        huffMan.put(ch, value + 1);
                    } else {
                        huffMan.put(ch, 1);
                    }
                    total++;
                }
            }

            sortHuffman();
            createHuffmanCode();

        }catch (FileNotFoundException ex){
            System.out.println(ex.getMessage());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

    }

    public static void main(String[] str){

        Huffman huffman = new Huffman();
        huffman.createHuffmanTree("infile.dat");
        huffman.writeOutput("outfile.dat");
    }

}
