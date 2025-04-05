package source;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BinTree {
    TreeNode root;
    int level_count;
    int node_count;
    int max_val;
    int last_level_gaps = 6 ; //spaces between last level nodes, when printed
    
    

    public TreeNode getRoot() {
        return root;
    }
    public int getLevel_count() {
        return level_count;
    }
    public int getNode_count() {
        return node_count;
    }
    private List<Integer> purse_list(String str){
        List<Integer> list = new ArrayList<Integer>();
        if(str.charAt(0) == '[' && str.charAt(str.length()-1) == ']'){
            str = str.substring(1, str.length()-1); //does not include the last index
        }
        else if(str.charAt(0) == '['){
            str = str.substring(1);
        }
        else if(str.charAt(str.length()-1) == ']'){
            str = str.substring(0, str.length()-1);
        }

        String[] components = str.split(",");
        for(String num : components){
            // System.out.println(num);
            if(num.equals("null")){
                list.add(null);
            }
            else{
                list.add(Integer.parseInt(num));
                }

                        
        }
        return list;
    }
    public BinTree(String str){
        List<Integer> levelOrder = purse_list(str);
        System.out.print("Building tree from: ");
        System.out.println(list_to_string(levelOrder));

        if(levelOrder.size() < 1 || levelOrder.get(0) == null){
            throw new IllegalArgumentException("Please provide at least one non-null node");
        }
        this.root = new TreeNode(levelOrder.get(0));
        max_val = root.val;
        Queue<TreeNode> current_level = new LinkedList<TreeNode>();
        Queue<TreeNode> next_level = new LinkedList<TreeNode>();
        current_level.add(root);
        if(levelOrder.size() > 1){
            level_count = 2; //initialise levels to 2.
        }else{
            level_count = 1;
        }
        node_count = 1;

        boolean skip_left = false; //when I need to skip a child pointer, and add to the next pinter instead
        // boolean skip_right = false;

        for(int i=1; i<levelOrder.size(); i++){
            // System.out.println(levelOrder.get(i));
            TreeNode new_node;
            if(levelOrder.get(i) != null){
                new_node = new TreeNode(levelOrder.get(i));
                if(new_node.val > max_val){
                    max_val = new_node.val;
                }
            }else{
                new_node = null;
            }
            
            if(current_level.size() != 0){
                TreeNode node_to_fill = current_level.peek();//node stays in queue should a right child be added
                if(node_to_fill.left == null && !skip_left){ 
                    node_to_fill.left = new_node;
                    if(new_node != null){
                        next_level.add(new_node);
                        node_count++;   
                    }
                    else{//flag this for skip on next iteration
                        skip_left = true;
                    }
                    
                }
                else{ //left added, or left skipped
                    node_to_fill.right = new_node;
                    if(new_node != null){
                        next_level.add(new_node);
                        node_count++;
                    }                 
                    skip_left = false; //reset flag   
                    current_level.remove(); 
                        //discard full node from candidates to fill.
                        //Basically BOTH left and right has been set to null or an actual value.
                }
            }else{
                //All nodes in the current level has been used up oops!
                current_level = next_level;
                next_level = new LinkedList<TreeNode>();
                level_count ++;
                i--; //So that the next iteration restarts from the current i.
            }
        }
        // last_level_gaps = count_digits(max_val) * 3; //*3 may be better    
        // last_level_gaps = 8; 
    }
    @Override
    public String toString(){
        int max_tree_width = (int)Math.floor(Math.pow(2, level_count-1));
        int node_width = count_digits(max_val);
        StringBuilder builder = new StringBuilder();
        builder.append("Levels: ");
        builder.append(level_count);
        builder.append(" ");
        builder.append("Nodes: ");
        builder.append(node_count);
        builder.append(" ");
        builder.append("max_tree_width: ");
        builder.append(max_tree_width);
        builder.append("\n");

        int[] member_gaps = new int[level_count];
        int[] initial_gaps = new int[level_count];
        member_gaps[member_gaps.length-1] = last_level_gaps + node_width; //center to center
        for(int i=member_gaps.length-2; i>=0; i--){ //last element already filled
            member_gaps[i] = member_gaps[i+1] * 2;
            initial_gaps[i] = initial_gaps[i+1] + member_gaps[i+1] / 2;
        }
        // System.out.println("member_gaps: " + ary_to_string(member_gaps));
        // System.out.println("initial_gaps: " + ary_to_string(initial_gaps));

        Queue<TreeNode> current_level = new LinkedList<TreeNode>();
        Queue<TreeNode> next_level = new LinkedList<TreeNode>();
        current_level.add(root);
        for(int i=0; i<level_count;i++){
            // builder.append(repeat_string(".", initial_gaps[i]));
            builder.append(repeat_string(" ", initial_gaps[i]));
            for(TreeNode parent:current_level){
                int parent_width;
                if(parent == null)
                {
                    // builder.append(repeat_string("_", node_width-1));
                    builder.append(repeat_string(" ", node_width-1));
                    builder.append("-");
                }
                else{
                    next_level.add(parent.left);
                    next_level.add(parent.right);
                    // builder.append(repeat_string("_",node_width-count_digits(parent.val)));
                    builder.append(repeat_string(" ",node_width-count_digits(parent.val)));
                    builder.append(parent);
                }
                // builder.append(repeat_string(".", member_gaps[i] - node_width)); //dist to center
                builder.append(repeat_string(" ", member_gaps[i] - node_width)); //dist to center
            }
            builder.append("\n");
            current_level = next_level;
            next_level = new LinkedList<TreeNode>();
            if(i < level_count-1){
                int repeat = (int) Math.pow(2, i);
                // System.out.println("\trepeat: "+repeat);
                for(int j=0; j<repeat ; j++){      
                    if(j == 0){
                        builder.append(repeat_string(" ", initial_gaps[i+1]));
                    }  
                    else{
                        builder.append(repeat_string(" ",member_gaps[i+1]-node_width));
                    }
                    builder.append(repeat_string(" ", node_width));
                    builder.append(repeat_string("_",member_gaps[i+1]-node_width));
                    builder.append(repeat_string(" ", node_width));
                }
                builder.append("\n");
                for(int j=0; j<repeat ; j++){      
                    if(j == 0){
                        builder.append(repeat_string(" ", initial_gaps[i+1]));
                    }  
                    else{
                        builder.append(repeat_string(" ",member_gaps[i+1]-node_width));
                    }
                    builder.append(repeat_string(" ", node_width-1));
                    builder.append("/"); //these two lines make one node_width
                    builder.append(repeat_string(" ",member_gaps[i+1]-node_width));
                    builder.append("\\"); 
                    builder.append(repeat_string(" ", node_width-1));
                }
                builder.append("\n");
            }
        }

        return builder.toString();
    }
    private String repeat_string(String c, int num){
        if(c==null || num<1){
            return "";
        }
        StringBuilder builder = new StringBuilder(c.length()*num);
        for(int i=0; i<num; i++){
            builder.append(c);
        }
        return builder.toString();
    }
    public String in_order(){
        List<TreeNode> accumulator = new ArrayList<TreeNode>();
        in_order_helper(root, accumulator);
        return list_to_string(accumulator);
    }
    private void in_order_helper(TreeNode node, List<TreeNode> accumulator){
        //verbose:
        // System.out.println("\tcurrent node: " + node);
        // if(node != null){
        //     System.out.println("\t\tleft: " + node.left);
        //     System.out.println("\t\tright: " + node.right);
        // }
        // else{
        //     System.out.println("\t\t--");
        //     System.out.println("\t\t--");
        // }
        
        if(node == null){
            return;
        }
        else{
            in_order_helper(node.left, accumulator);
            accumulator.add(node);
            in_order_helper(node.right, accumulator);
        }
    }
    private <T> String list_to_string(List<T> list){
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for(T element:list){
            if(element != null){
                builder.append(element.toString());
                builder.append(",");    
            }
            else{
                builder.append("null,");
            }
            
        }
        builder.deleteCharAt(builder.length()-1); //remove the last comma
        builder.append("]");
        return builder.toString();
    }
    private <T> String ary_to_string(T[] ary){
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for(T element: ary){
            if(element != null){
                builder.append(element.toString());
                builder.append(",");    
            }
            else{
                builder.append("null,");
            }
        }
        builder.deleteCharAt(builder.length()-1); //remove the last comma
        builder.append("]");
        return builder.toString();
    }
    private String ary_to_string(int[] ary){
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for(int element: ary){
            builder.append(element);
            builder.append(",");    
            //no nulls allowed
        }
        builder.deleteCharAt(builder.length()-1); //remove the last comma
        builder.append("]");
        return builder.toString();
    }
    private int count_digits(int num){
        num = Math.abs(num);
        if(num == 0){
            return 1;
        }
        int count = 0;
        while(num >0){
            num /= 10;
            count++;
        }
        return count;
    }
    

    
}
