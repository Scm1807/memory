package algorithm;

public class 二叉树前中序求后序遍历 {
    public static void main(String[] args) {
        int[] pre = {1,2,3,4,86};
        int[] mid = {2,1,4,3,86};
        TreeNode root = preAndMidToPost(pre,mid);
        backPrint(root);
    }

    private static void backPrint(TreeNode root){
        if (root != null){
            backPrint(root.leftChild);
            backPrint(root.rightChild);
            System.out.println(root.val);
        }
    }

    private static TreeNode preAndMidToPost(int[] pre,int[] mid){
        return addNode(pre,mid,0,pre.length-1,0,mid.length-1);
    }
    private static TreeNode addNode(int[] pre, int[] mid, int preStart, int preEnd, int midStart, int midEnd) {

        if (preStart > preEnd || midStart > midEnd) return null;
        TreeNode root = new TreeNode(pre[preStart]);
        for (int i = midStart;i<=midEnd;i++){
            if (pre[preStart] == mid[i]){
                root.leftChild = addNode(pre,mid,preStart+1,preStart+(i-midStart),midStart,i-1);
                root.rightChild = addNode(pre,mid,preStart+(i-midStart)+1,preEnd,i+1,midEnd);
            }
        }
        return root;
    }
}

class TreeNode {
    int val;
    TreeNode leftChild;
    TreeNode rightChild;
    TreeNode(int val) {
        this.val = val;
    }
}
