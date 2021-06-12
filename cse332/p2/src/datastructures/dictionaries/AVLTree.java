package datastructures.dictionaries;

import cse332.datastructures.trees.BinarySearchTree;

/**
 * @Description: AVLTree class. Same functionality as BSTTree, but
 * the left and right subtree of every node must have a height difference 
 * less than 2. 
 */
public class AVLTree<K extends Comparable<K>, V> extends BinarySearchTree<K, V> {
    // private field for storing past values
    private V saveOldValue;
    
    /**
     * @Description: Node class for the AVLTree. Store the height, which is
     * unique compared to BStNode.
     */
    public class AVLNode extends BSTNode {
        // height of the AVLNode
        public int height;
        
        @SuppressWarnings("unchecked")
        public AVLNode(K key, V value) {
            super(key, value);
            this.height = 0;
        }
    }

    public AVLTree() {
        super();
        this.saveOldValue = null;
    }

    @Override
    public V find(K key) {
        if (key == null)
            throw new IllegalArgumentException();
        AVLNode node = findNode(key);
        if (node == null)
            return null;
        return node.value;
    }

    /**
     * @Description: Private helper method for find. It looks through the 
     * tree to find the AVLNode that has the given key.
     */
    private AVLNode findNode(K key) {
        AVLNode current = cast(this.root);
        int child = -1;
        while (current != null) {
            int direction = Integer.signum(key.compareTo(current.key));
            if (direction == 0)
                return current;
            else {
                child = Integer.signum(direction + 1);
                current = cast(current.children[child]);
            }
        }
        return current;
    }

    @Override
    public V insert(K key, V value) {
        this.root = update(key, value, cast(this.root));
        if(this.saveOldValue != null) {
            V temp = this.saveOldValue;
            this.saveOldValue = null;
            return temp;
        }
        return null;
    }

    /**
     * @Description: Private method that will recursively traverse over the 
     * nodes and update the height and look for the right place to insert 
     * the key value pair. If it find that the AVLTree is broken it will 
     * rotate the nodes in order to fix it.
     */
    private AVLNode update(K key, V value, AVLNode current) {
        if (current == null) {
            size++;
            return new AVLNode(key, value);
        }
        if (key.compareTo(current.key) < 0)
            current.children[0] = update(key, value, cast(current.children[0]));
        else if (key.compareTo(current.key) > 0)
            current.children[1] = update(key, value, cast(current.children[1]));
        else {
            this.saveOldValue = current.value;
            current.value = value;
            return current;
        }
        int LH = getHeight(cast(current.children[0]));
        int RH = getHeight(cast(current.children[1]));
        current.height = Math.max(LH, RH) + 1;
        // the labeling of nodes:
        //  L   R
        // / \ / \
        // 1 2 3 4
        if (Math.abs(LH - RH) > 1) {
            // 1
            if (LH > RH && key.compareTo(current.children[0].key) < 0)
                return this.rightRotation(current);
            // 2
            if (LH > RH && key.compareTo(current.children[0].key) > 0) {
                current.children[0] = leftRotation(cast(current.children[0]));
                return this.rightRotation(current);
            }
            // 3
            if (LH < RH && key.compareTo(current.children[1].key) < 0) {
                current.children[1] = rightRotation(cast(current.children[1]));
                return this.leftRotation(current);
            }
            // 4
            if (LH < RH && key.compareTo(current.children[1].key) > 0)
                return this.leftRotation(current);
        }
        return current;
    }

    /**
     * @Description: This private method will rotate the given node to the right.
     */
    private AVLNode rightRotation(AVLNode A) {
        AVLNode B = cast(A.children[0]);
        AVLNode Bright = cast(B.children[1]);
        B.children[1] = A;
        A.children[0] = Bright;
        updateHeight(A);
        updateHeight(B);
        return B;
    }
    
    /**
     * @Description: This private method will rotate the given node to the left.
     */
    private AVLNode leftRotation(AVLNode A) {
        AVLNode B = cast(A.children[1]);
        AVLNode Bleft = cast(B.children[0]);
        B.children[0] = A;
        A.children[1] = Bleft;
        updateHeight(A);
        updateHeight(B);
        return B;
    }

    /**
     * @Description: Update the height of the node by looking at both child of 
     * the node and taking the maximum and adding 1.
     */
    private void updateHeight(AVLNode A) {
        A.height = Math.max(getHeight(cast(A.children[0])),
                getHeight(cast(A.children[1]))) + 1;
    }

    /**
     * @Description: This will make sure the null node will be counted as -1 
     * when calculating the height.
     */
    private int getHeight(AVLNode n) {
        if (n == null)
            return -1;
        return n.height;
    }

    /**
     * @Description: Cast BSTNode to an AVLNode.
     */
    private AVLNode cast(BSTNode bn) {
        return (AVLTree<K, V>.AVLNode) bn;
    }
}