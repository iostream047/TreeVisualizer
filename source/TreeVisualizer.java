package source;
// import source.TreeNode;

public class TreeVisualizer{
    public static void main(String[] args) throws Exception{
    //     TreeNode root = new TreeNode(42);
    //     TreeNode ref = root.left;
    //     ref = new TreeNode(2022);
    //     // ref.val = 2022;
        
    //     root.left = new TreeNode(20);
    //     // root.left.val = 20;
        // BinTree tree = new BinTree("3,9,20,null,null,15,7");
        // BinTree tree = new BinTree("[1,2,3,null,4]");
        if(args.length != 1){
            throw new Exception("String to buld the tree not provided as command line argument"); 
        }
        BinTree tree = new BinTree(args[0]);
        System.out.println(tree.toString());
        // System.out.println(tree.in_order());
    }

}