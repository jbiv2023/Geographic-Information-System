import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class bufferPool {

    public class Node {
        Node next;
        Node prev;
        String key;
        String value;
        
        /*
         * Each node will contain a key and
         *  value pair
         */

        Node(String key, String value) { 
            this.key = key;
            this.value = value;
            this.next = null;
            this.prev = null;
        }

    }
    
    /*
     * Building our least recently used Cache with
     * multiple means of dealing with values
     */
 
   
    public class LRUCache{
        int capacity;
        Node head;
        Node tail;
        HashMap<String, Node> keyToNode;
        
        public LRUCache(int capacity) {
            this.capacity = capacity;
            head = new Node("head", "head");
            tail = new Node("tail", "tail");
            head.next = tail;
            tail.prev = head;
            keyToNode = new HashMap<>();
            
        }
        
        
        public boolean contains(String key) {
            return keyToNode.containsKey(key);
        }
        
        public String get(String key) {
            Node match =  keyToNode.get(key);

            return match.value;
        }
        
        public void add(String key, String value) {
            Node nodeToUpdate;
            if (keyToNode.containsKey(key)) {       
                nodeToUpdate = keyToNode.get(key);
                nodeToUpdate.value = value;
                removeNode(nodeToUpdate);
            }
            else {
                if (keyToNode.size() == capacity) {
                    evictOldest();
                }
                nodeToUpdate = new Node(key, value);
                keyToNode.put(key, nodeToUpdate);
            }
           
           moveToFront(nodeToUpdate);
        }
        
        
        
        public ArrayList<Node> getNodesInMRUOrder(){
            ArrayList<Node> nodesInOrder = new ArrayList<>();
            Node cur = head.next;
            while (cur != tail) {
                nodesInOrder.add(cur);
                cur = cur.next;
            }
            return nodesInOrder;
        }
        
        private void evictOldest() {
            String keyToRemoveFromMap = tail.prev.key;
            tail.prev.prev.next = tail;
            tail.prev = tail.prev.prev;
            keyToNode.remove(keyToRemoveFromMap);
            
        }
        
        
        private void removeNode(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev; 
        }
        
        
        private void moveToFront(Node node) {
            node.prev = head;
            node.next = head.next;

            head.next.prev = node;
            head.next = node;
            
        }
 
    
    }
    
    HashMap<String, Long> keyToOffset;
    HashMap<String, String> equivalentKeyFor;
    
    LRUCache records;
    LRUCache treeValue;
    static final int CACHE_CAPACITY = 15;
    

    public bufferPool () {
        keyToOffset = new HashMap<>();
        equivalentKeyFor = new HashMap<>();
        
        records = new LRUCache(CACHE_CAPACITY);
        treeValue = new LRUCache(CACHE_CAPACITY);
    }
    
    public boolean hasSeenInTree(long offset) {
       
        return treeValue.contains(String.valueOf(offset));
    }
    
    
    public boolean containsEquivalentKey(String key) {
        return equivalentKeyFor.containsKey(key);
    }
    
    public String getEquivalentKey(String key) {
        return equivalentKeyFor.get(key);
    }
    
    public void addEquivalentKey(String key, String equivalentInMap) {
       equivalentKeyFor.put(key, equivalentInMap);
    }
    
    
    public boolean containsKey(String key) {
        return records.contains(key);
    }
    
    
    public String getRecord(String key) {
        return records.get(key);
    }
    
    public void addRecord(String key, String record) {
        records.add(key, record);
    }

    
    public long getOffset(String key) {
        return keyToOffset.get(key);
    }
    
    public void addOffset(String key, long offset) {
        keyToOffset.put(key, offset);
    }
    
    /*
     * Displaying the pool
     */
           

    public void poolDisplay(BufferedWriter fw) throws IOException {
        System.out.println();
        
        fw.write("show pool: \n" );
        ArrayList<Node> orderedRecords = records.getNodesInMRUOrder();
        
        fw.write("MRU: \n");
        
        for (Node node : orderedRecords) {
            Long offset = getOffset(node.key);
            String record = node.value;
            fw.write("     " + offset + ":     " + record + "\n");
            
        }
  
        fw.write("\n");
        fw.write("LRU\n");
        fw.write("-------------------------------------------------------------------------------- \n" );

    }

}
