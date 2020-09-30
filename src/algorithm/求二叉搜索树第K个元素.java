package algorithm;

public class 求二叉搜索树第K个元素 {
    public static void main(String[] args) {

        int[] a = {2, 3, 4, 5, 6, 7, 8};
        TreeNode root = null;
        root = createTree(root, a, 0, a.length - 1);
        inOrderTraverse(root);
//        int x = findK(root, 3).val;
//        System.out.println("sdsssssssssssss:" + x);
    }

    private static TreeNode createTree(TreeNode root, int[] a, int begin, int end) {
        if (begin > end) {
            return null;
        }
        int mid = (begin + end) / 2;
        if (root == null) {
            root = new TreeNode(a[mid]);
        }
        root.leftChild = createTree(root.leftChild, a, begin, mid - 1);
        root.rightChild = createTree(root.rightChild, a, mid + 1, end);
        return root;
    }

    //中序遍历
    private static void inOrderTraverse(TreeNode root) {
        if (root != null){
            inOrderTraverse(root.leftChild);
            System.out.println(root.val);
            inOrderTraverse(root.rightChild);
        }
    }

    //查找第k个元素
    private static TreeNode findK(TreeNode parent, int k) {
        int index = 0;
        if (parent != null) {
            TreeNode left = findK(parent.leftChild, 3);
            if (left != null) return left;
            index++;
            if (index == k) return parent;
            TreeNode right = findK(parent.rightChild, k);
            if (right != null) return right;
        }
        return null;
    }
}
