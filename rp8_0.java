import java.io.*;
 
class Instruction {
    int cmd;
    String optkey;
    String optdata;
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
 
class rp8_0 {
    static final int NOP = 0;
    static final int HALT = 1;
    static final int SHOW = 2;
    static final int SHOW90 = 90;
    static final int INS = 3;
    static final int DEL = 4;
    static final int FIND = 5;
 
    static final int FNDD = 20;
    
    Node rootnode = null;
    Node rootdatanode = null;
    
    public static void main(String args[]) {
        (new rp8_0()).run_interpreter(); /* don't forget to change here! */
    }
    
    public static void printTxt(String s) {
        System.out.print(s);
    }
    
    void run_interpreter() {
        Node nd;
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
                if(rootnode!=null) {rootnode.printTree90(); }
                printTxt("\n");
                
                printTxt("data-index:");
                if(rootdatanode!=null) {rootdatanode.printTree90(); }
                printTxt("\n");
                break;
            case INS:
                KeyAndData kd = new KeyAndData(inst.optkey,inst.optdata);
                Node node1 = new Node(kd);
                Node node2 = new Node(kd);
                insertNode(node1,rootnode);
                insertNode2(node2,rootdatanode); // prepare double-indices
                break;
            case DEL:
                // nd = deleteNodeByKey(inst.optkey,rootnode);
                // if( nd != null ) {
                //     printTxt(" |Node:");
                //     nd.printThisNode(); printTxt(" deleted.\n");
                // } else {
                //     printTxt(" |Key not found.\n");
                // }
                printTxt("delete is not supported yet.");
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
            case FNDD:
                nd = findNodeByData(inst.optkey,rootdatanode);
                if( nd != null ) {
                    printTxt(" |found:");
                    nd.printThisNode(); printTxt(" \n");
                } else {
                    printTxt(" |Key not found.\n");
                }
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
            } else if( param[0].equals("find") ) {
                inst.cmd = FIND;
                if (param.length > 1){
                    System.err.print("key:"+param[1]+"\n");
                    inst.optkey  = param[1];
                }else{
                    System.err.print("key:");
                    buf = br.readLine();
                    inst.optkey = buf;
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
