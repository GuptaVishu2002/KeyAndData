import java.io.*;
 
class Instruction {
    int cmd;
    String optkey,optkey1;
    String optdata,optdata1;
}
 
class KeyAndData {
    String key;
    String data;
    public KeyAndData(String key, String data) {
        this.key = key;
        this.data = data;
    }
}
 
class Node {
    Node left;
    Node right;
    Node parent;
    KeyAndData keyAndData;
    
    public Node(String key, String data) {
        this.keyAndData = new KeyAndData(key,data);
        this.left = null;
        this.right = null;
        this.parent = null;
    }
    public Node(KeyAndData keyAndData) {
        this.keyAndData = keyAndData;
        this.left = null;
        this.right = null;
        this.parent = null;
    }
    static void printTxt(String s) {
        System.out.print(s);
    }
    void printTree() {
        printTxt("[");
        if( left != null ) { left.printTree(); }
        printTxt(" ");
        printThisNode();
        printTxt(" ");
        if( right != null ) { right.printTree(); }
        printTxt("]");
    }
    void printTree90() {
        printTxt("\n");
        printTree90(0);
    }
    void printTree90(int l) {
        if( right != null ) { right.printTree90(l+1); }
        for(int i=0; i<l*8; i++) { printTxt(" "); }
        printThisNode();
        printTxt("\n");
        if( left != null ) { left.printTree90(l+1); }
    }
    void printThisNode() {
        if( keyAndData == null ) {
            printTxt("(null)");
        } else {
            String key_i;
            String data_i;
            if( keyAndData.key != null ) {
                if( keyAndData.key.length() > 10 ) {
                    key_i = keyAndData.key.substring(0,10);
                } else {
                    key_i = keyAndData.key;
                }
            } else {
                key_i = "(null)";
            }
            if( keyAndData.data != null ) {
                if( keyAndData.data.length() > 10 ) {
                    data_i = keyAndData.data.substring(0,10);
                } else {
                    data_i = keyAndData.data;
                }
            } else {
                data_i = "(null)";
            }
            printTxt("(" + key_i + ":" + data_i + ")");
        }
    }
}
 
class rp8_1 {
    static final int NOP = 0;
    static final int HALT = 1;
    static final int SHOW = 2;
    static final int SHOW90 = 90;
    static final int INS = 3;
    static final int DEL = 4;
    static final int FIND = 5;
 
    static final int FNDR = 10;
    static final int FNDD = 20;
    static final int FDDR = 21;
    static final int FNDB = 22;
    static int COUNT = 0;
    
    Node rootnode = null;
    Node rootdatanode = null;
    java.util.Vector findrangebuf = new java.util.Vector();
    // java.util.Vector<Node> findrangebuf = new java.util.Vector<Node>();
    
    public static void main(String args[]) {
        (new rp8_1()).run_interpreter(); /* don't forget to change here! */
    }
    
    public static void printTxt(String s) {
        System.out.print(s);
    }
    
    void run_interpreter() {
        Node nd,ndd;
        Instruction inst;
        
        do{
            inst = read_instruction();
            switch(inst.cmd) {
            case NOP:
                break;
            case SHOW:
                printTxt("key-index:");
                if(rootnode!=null) {rootnode.printTree(); }
                printTxt("\n");
                
                printTxt("data-index:");
                if(rootdatanode!=null) {rootdatanode.printTree(); }
                printTxt("\n");
                break;
            case SHOW90:
            	printTxt("key-index:");
            	printTxt("\n");
                if(rootnode!=null) {rootnode.printTree90(); } printTxt("\n");
                
                printTxt("data-index:");
                printTxt("\n");
                if(rootnode!=null) {rootdatanode.printTree90(); } printTxt("\n");
                break;    
            case INS:
                KeyAndData kd = new KeyAndData(inst.optkey,inst.optdata);
                Node node1 = new Node(kd);
                Node node2 = new Node(kd);
                insertNode(node1,rootnode);
                insertNode2(node2,rootdatanode); // prepare double-indices
                COUNT++;
                break;   
            case DEL:
            	while(COUNT != 0){
		            nd = deleteNodeByKey(inst.optkey,rootnode);
		            ndd = deleteNodeByData(inst.optkey,rootnode);
		            if( nd != null ) {
		                printTxt(" |Node:");
		                nd.printThisNode(); printTxt(" deleted.\n");
		            }else if( ndd != null ) {
		                printTxt(" |Node:");
		                ndd.printThisNode(); printTxt(" deleted.\n");
		            } else {
		            	if(nd == null){
		                printTxt(" |Key not found in key-index.\n");
		                }
		                else{
		                printTxt(" |Data not found in key-index.\n");
		                }
		            }
		            nd = deleteNodeByKey(inst.optkey,rootdatanode);
		            ndd = deleteNodeByData(inst.optkey,rootdatanode);
		            if( nd != null ) {
		                printTxt(" |Node:");
		                nd.printThisNode(); printTxt(" deleted.\n");
		            }else if( ndd != null ) {
		                printTxt(" |Node:");
		                ndd.printThisNode(); printTxt(" deleted.\n");
		            } else {
		                if(nd == null){
		                printTxt(" |Key not found in data-index.\n");
		                }
		                else{
		                printTxt(" |Data not found in data-index.\n");
		                }
		            }
		            COUNT--;
				}
                break;
            case FIND:
                nd = findNodeByKey(inst.optkey,rootnode);
                if( nd != null ) {
                    printTxt(" |found:");
                    nd.printThisNode(); printTxt(" \n");
                } else {
                    printTxt(" |Key not found.\n");
                }
                break;
            case FNDR:
                initNodeBuf();
                // don't confuse! optkey=rmin, optdata=rmax
                findNodeByKeyRange(inst.optkey,inst.optdata,rootnode);
                printAllInBuf();
                break;
            case FNDB:
                initNodeBuf();
                // don't confuse! optkey=rmin, optdata=rmax
                findNodeByRange(inst.optkey,inst.optkey1,inst.optdata,inst.optdata1,rootnode);
                printAllInBuf();
                break;    
            case FNDD:
                nd = findNodeByData(inst.optkey,rootdatanode);
                if( nd != null ) {
                    printTxt(" |found:");
                    nd.printThisNode(); printTxt(" \n");
                } else {
                    printTxt(" |Key not found.\n");
                }
                break;
            case FDDR:
                initNodeBuf();
                // don't confuse! optkey=rmin, optdata=rmax
                findNodeByDataRange(inst.optkey,inst.optdata,rootdatanode);
                printAllInBuf();
                break;
            }
        } while( inst.cmd != HALT );
        
        return;
    }
    
    int compare(KeyAndData one, KeyAndData two) {
        if( one == null || two == null ) { return(-1);}
        return(compareByKey( one.key, two ));
    }
 
    int compareByKey(String onekey, KeyAndData two ) {
        if( onekey == null || two == null || two.key == null ) { return(-1);}
        return( onekey.compareTo(two.key) );
    }
 
    int compareByData(String onedata, KeyAndData two ) {
        if( onedata == null || two == null || two.data == null ) { return(-1);}
        return( onedata.compareTo(two.data) );
    }
    
    void insertNode(Node newnode, Node node) {
        int compareflag;
        if( newnode == null ) {return;}
        
        if( node == null ) {
            rootnode = newnode;
            newnode.parent = null;
        } else {
            compareflag = compare(newnode.keyAndData,node.keyAndData);
            if( compareflag < 0 ) {
                if( node.left != null ) {
                    insertNode(newnode,node.left);
                } else {
                    node.left = newnode;
                    newnode.parent = node;
                }
            } else {
                if( node.right != null ) {
                    insertNode(newnode,node.right);
                } else {
                    node.right = newnode;
                    newnode.parent = node;
                }
            }
        }
        return;
    }
    void insertNode2(Node newnode, Node node) {
        int compareflag;
        if( newnode == null ) {return;}
        
        if( node == null ) {
            rootdatanode = newnode; // only this line is different
            newnode.parent = null;
        } else {
            compareflag = compareByData(newnode.keyAndData.data,node.keyAndData);
            if( compareflag < 0 ) {
                if( node.left != null ) {
                    insertNode2(newnode,node.left);
                } else {
                    node.left = newnode;
                    newnode.parent = node;
                }
            } else {
                if( node.right != null ) {
                    insertNode2(newnode,node.right);
                } else {
                    node.right = newnode;
                    newnode.parent = node;
                }
            }
        }
    }
    
    Node deleteNode(Node node) {
        if( node == null ){return(null);}
        if( node.left == null ) {
            if( node.parent == null ) {
                rootnode = node.right;
            } else if( node.parent.left == node ) {
                node.parent.left = node.right;
            } else {
                node.parent.right = node.right;
            }
            if( node.right != null ) {
                node.right.parent = node.parent;
            }
            node.right = null;
            return(node);
        } else {
            return(deleteNode(node.left));
        }
    }
    
    Node deleteNodeByKey(String key, Node node) {
        int compareflag;
        Node nd;
        if( node == null ){ return null;}
        
        compareflag = compareByKey(key,node.keyAndData);
        
        node.printThisNode();  
        
        if( compareflag == 0 ) {
            if( node.right != null ) {
                nd = deleteNode(node.right);
                
                printTxt(" (deleted:"); nd.printThisNode(); printTxt(" ");
                node.printThisNode(); printTxt(" will be replaced with ");
                nd.printThisNode(); printTxt(") ");
                
                if( node.parent == null ) {
                    rootnode = nd;
                } else if( node.parent.left == node ) {
                    node.parent.left = nd;
                } else {
                    node.parent.right = nd;
                }
                nd.parent = node.parent;
                nd.left = node.left;
                nd.right = node.right;
                if( node.left != null ) {
                    node.left.parent = nd;
                }
                if( node.right != null ) {
                    node.right.parent = nd;
                }
                return(node);
            } else {
                if( node.parent == null ) {
                    rootnode = node.left;
                } else if( node.parent.left == node ) {
                    node.parent.left = node.left;
                } else {
                    node.parent.right = node.left;
                }
                if( node.left != null ) {
                    node.left.parent = node.parent;
                }
                return(node);
            }
        } else if( compareflag < 0 ) {
            return( deleteNodeByKey(key,node.left) );
        } else {
            return( deleteNodeByKey(key,node.right) );
        }
    }
    
    Node findNodeByKey(String key, Node node) {
        int compareflag;
        if( node == null ){return(null);}
        
        node.printThisNode();
        
        compareflag = compareByKey(key,node.keyAndData);
        
        if( compareflag == 0 ) {
            return(node);
        } else if( compareflag < 0 ) {
            return(findNodeByKey(key,node.left));
        } else {
            return(findNodeByKey(key,node.right));
        }
    }
    
    Node deleteNodeByData(String data, Node node) {
        int compareflag;
        Node nd;
        if( node == null ){ return null;}
        
        compareflag = compareByData(data,node.keyAndData);
        
        node.printThisNode();  
        
        if( compareflag == 0 ) {
            if( node.right != null ) {
                nd = deleteNode(node.right);
                
                printTxt(" (deleted:"); nd.printThisNode(); printTxt(" ");
                node.printThisNode(); printTxt(" will be replaced with ");
                nd.printThisNode(); printTxt(") ");
                
                if( node.parent == null ) {
                    rootnode = nd;
                } else if( node.parent.left == node ) {
                    node.parent.left = nd;
                } else {
                    node.parent.right = nd;
                }
                nd.parent = node.parent;
                nd.left = node.left;
                nd.right = node.right;
                if( node.left != null ) {
                    node.left.parent = nd;
                }
                if( node.right != null ) {
                    node.right.parent = nd;
                }
                return(node);
            } else {
                if( node.parent == null ) {
                    rootnode = node.left;
                } else if( node.parent.left == node ) {
                    node.parent.left = node.left;
                } else {
                    node.parent.right = node.left;
                }
                if( node.left != null ) {
                    node.left.parent = node.parent;
                }
                return(node);
            }
        } else if( compareflag < 0 ) {
            return( deleteNodeByData(data,node.left) );
        } else {
            return( deleteNodeByData(data,node.right) );
        }
    }
    
    Node findNodeByData(String data, Node node) {
        int compareflag;
        if( node == null ){return(null);}
        
        node.printThisNode();
        
        compareflag = compareByData(data,node.keyAndData);
        
        if( compareflag == 0 ) {
            return(node);
        } else if( compareflag < 0 ) {
            return(findNodeByData(data,node.left));
        } else {
            return(findNodeByData(data,node.right));
        }
    }
 
    void findNodeByKeyRange(String min, String max, Node node) {
        int compareflag0, compareflag1;
        if( node == null ){return;}
        
        node.printThisNode();
        
        compareflag0 = compareByKey(min,node.keyAndData);
        compareflag1 = compareByKey(max,node.keyAndData);
        
        if( compareflag0 < 0 && node.left != null ) { 
            findNodeByKeyRange(min,max,node.left);
            node.printThisNode();
        }
  
        if( compareflag0 <= 0 && compareflag1 >= 0 ) {
            addNodeToBuf(node);
        }
  
        if( compareflag1 > 0 && node.right != null) {
            findNodeByKeyRange(min,max,node.right);
            node.printThisNode();
        }
    }
    void findNodeByDataRange(String min, String max, Node node) {
        int compareflag0, compareflag1;
        if( node == null ){return;}
        
        node.printThisNode();
        
        compareflag0 = compareByData(min,node.keyAndData);
        compareflag1 = compareByData(max,node.keyAndData);
        
        if( compareflag0 < 0 && node.left != null ) { 
            findNodeByDataRange(min,max,node.left);
            node.printThisNode();
        }
  
        if( compareflag0 <= 0 && compareflag1 >= 0 ) {
            addNodeToBuf(node);
        }
  
        if( compareflag1 > 0 && node.right != null) {
            findNodeByDataRange(min,max,node.right);
            node.printThisNode();
        }
    }
    
    void findNodeByRange(String kmin, String kmax, String dmin, String dmax, Node node) {
        int compareflag0, compareflag1, compareflag2, compareflag3;
        if( node == null ){return;}
        
        node.printThisNode();
        
        compareflag0 = compareByKey(kmin,node.keyAndData);
        compareflag1 = compareByKey(kmax,node.keyAndData);
        compareflag2 = compareByData(dmin,node.keyAndData);
        compareflag3 = compareByData(dmax,node.keyAndData);
        
        if( compareflag0 < 0 && node.left != null && compareflag2 < 0 ) { 
            findNodeByRange(kmin,kmax,dmin,dmax,node.left);
            node.printThisNode();
        }
  
        if( compareflag0 <= 0 && compareflag1 >= 0  && compareflag2 <= 0 && compareflag3 >= 0) {
            addNodeToBuf(node);
        }
  
        if( compareflag1 > 0 && node.right != null && compareflag3 > 0 ) {
            findNodeByRange(kmin,kmax,dmin,dmax,node.right);
            node.printThisNode();
        }
    }
    
    void printAllInBuf() {
        int i;
        printTxt("\n |Found:");
        for(i = 0; i < findrangebuf.size(); i++ ) {
            Node nd = (Node)findrangebuf.elementAt(i);
            nd.printThisNode();
        }
        printTxt("\n");
    }
    void addNodeToBuf(Node node) {
        findrangebuf.add(node); /* ignore warning if no use of java-generics */
    }
    void initNodeBuf() {
        findrangebuf.clear();
    }
    
 
    Instruction read_instruction() {
        Instruction inst = new Instruction();
        
        try{
            BufferedReader br
                = new BufferedReader(new InputStreamReader(System.in));
            String buf;
            String[] param;
            System.err.print("cmd:");
            buf = br.readLine();
            param = buf.split(" ",0);
            if( param[0].equals("nop") ) {
                inst.cmd = NOP;
            } else if( param[0].equals("halt") ) {
                inst.cmd = HALT;
            } else if( param[0].equals("show") ) {
                inst.cmd = SHOW;
            } else if( param[0].equals("show90") ) {
                inst.cmd = SHOW90;
            } else if( param[0].equals("ins") ) {
                inst.cmd = INS;
                if (param.length > 2){
                    System.err.print("key:"+param[1]+"\n"+"data:"+param[2]+"\n");
                    inst.optkey  = param[1];
                    inst.optdata = param[2];
                }else{
                    System.err.print("key:");
                    buf = br.readLine();
                    inst.optkey = buf;
                    System.err.print("data:");
                    buf = br.readLine();
                    inst.optdata = buf;
                }
            } else if( param[0].equals("del") ) {
                inst.cmd = DEL;
                if (param.length > 1){
                    System.err.print("key:"+param[1]+"\n");
                    inst.optkey  = param[1];
                }else{
                    System.err.print("key:");
                    buf = br.readLine();
                    inst.optkey = buf;
                }
            } else if( param[0].equals("find") || param[0].equals("findkey") ) {
                inst.cmd = FIND;
                if (param.length > 1){
                    System.err.print("key:"+param[1]+"\n");
                    inst.optkey  = param[1];
                }else{
                    System.err.print("key:");
                    buf = br.readLine();
                    inst.optkey = buf;
                }
            } else if( param[0].equals("findrange") || param[0].equals("keyrange") ) {
                inst.cmd = FNDR;
                if (param.length > 2){
                    System.err.print("min:"+param[1]+"\n"+"max:"+param[2]+"\n");
                    inst.optkey  = param[1];
                    inst.optdata = param[2];
                }else{
                    System.err.print("min:");
                    buf = br.readLine();
                    inst.optkey = buf;
                    System.err.print("max:");
                    buf = br.readLine();
                    inst.optdata = buf;
                }
            } else if( param[0].equals("findboth") || param[0].equals("range") ) {
                inst.cmd = FNDB;
                if (param.length > 4){
                    System.err.print(" key min:"+param[1]+"\n"+" key max:"+param[2]+"\n");
                    inst.optkey  = param[1];
                    inst.optkey1 = param[2];
                    System.err.print(" data min:"+param[3]+"\n"+" data max:"+param[4]+"\n");
                    inst.optdata  = param[3];
                    inst.optdata1 = param[4];
                }else{
                    System.err.print("key min:");
                    buf = br.readLine();
                    inst.optkey = buf;
                    System.err.print("key max:");
                    buf = br.readLine();
                    inst.optkey1 = buf;
                    System.err.print("data min:");
                    buf = br.readLine();
                    inst.optdata = buf;
                    System.err.print("data max:");
                    buf = br.readLine();
                    inst.optdata1 = buf;
                }
            } else if( param[0].equals("datarange") ) {
                inst.cmd = FDDR;
                if (param.length > 2){
                    System.err.print("min:"+param[1]+"\n"+"max:"+param[2]+"\n");
                    inst.optkey  = param[1];
                    inst.optdata = param[2];
                }else{
                    System.err.print("min:");
                    buf = br.readLine();
                    inst.optkey = buf;
                    System.err.print("max:");
                    buf = br.readLine();
                    inst.optdata = buf;
                }
            } else if( param[0].equals("finddata") ) {
                inst.cmd = FNDD;
                if (param.length > 1){
                    System.err.print("data:"+param[1]+"\n");
                    inst.optkey  = param[1];
                }else{
                    System.err.print("data:");
                    buf = br.readLine();
                    inst.optkey = buf;
                }
            }
        } catch( Exception e) {
            System.err.println(e);
            inst.cmd = NOP;
        }
        return inst;
    }
}
