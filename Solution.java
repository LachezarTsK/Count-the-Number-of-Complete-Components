
public class Solution {

    public int countCompleteComponents(int totalNodes, int[][] edges) {
        UnionFind unionFind = new UnionFind(totalNodes);
        for (int[] edge : edges) {
            unionFind.joinByRankForNumberOfNodes(edge[0], edge[1]);
        }
        return findNumberOfCompleteComponents(unionFind, totalNodes);
    }

    private int findNumberOfCompleteComponents(UnionFind unionFind, int totalNodes) {

        /*
          A Boolean Array for the visited can be applied efficiently, instead of a Hash Set, 
          due to the small range of input nodes, [1, 50], as of March 2025.
         */
        boolean[] visitedParentsOfCompleteComponents = new boolean[totalNodes];
        int numberOfCompleteComponents = 0;

        for (int node = 0; node < totalNodes; ++node) {
            int parent = unionFind.findParent(node);
            int numberOfNodesInGroup = unionFind.numberOfNodes[parent];
            int numberOfEdgesInGroup = unionFind.numberOfEdges[parent];

            if (!isParentOfCompleteConnectedComponents(numberOfNodesInGroup, numberOfEdgesInGroup)) {
                continue;
            }
            if (!visitedParentsOfCompleteComponents[parent]) {
                ++numberOfCompleteComponents;
                visitedParentsOfCompleteComponents[parent] = true;
            }
        }

        return numberOfCompleteComponents;
    }

    private boolean isParentOfCompleteConnectedComponents(int numberOfNodesInGroup, int numberOfEdgesInGroup) {
        int expectedNumberOfEdgesInGroup = numberOfNodesInGroup * (numberOfNodesInGroup - 1) / 2;
        return expectedNumberOfEdgesInGroup == numberOfEdgesInGroup;
    }
}

class UnionFind {

    int[] parent;
    int[] numberOfNodes;
    int[] numberOfEdges;

    UnionFind(int totalNodes) {
        parent = new int[totalNodes];
        numberOfNodes = new int[totalNodes];
        numberOfEdges = new int[totalNodes];

        for (int node = 0; node < totalNodes; ++node) {
            parent[node] = node;
            this.numberOfNodes[node] = 1;
        }

        /*
        Alternatively:
        parent = IntStream.range(0, totalNodes).toArray();
        numberOfNodes = new int[totalNodes];
        Arrays.fill(numberOfNodes, 1);
        numberOfEdges = new int[numberOfEdges];
         */
    }

    int findParent(int index) {
        if (parent[index] != index) {
            parent[index] = findParent(parent[index]);
        }
        return parent[index];
    }

    void joinByRankForNumberOfNodes(int first, int second) {
        int parentFirst = findParent(first);
        int parentSecond = findParent(second);

        if (parentFirst == parentSecond) {
            ++numberOfEdges[parentFirst];
            return;
        }

        if (numberOfNodes[parentFirst] >= numberOfNodes[parentSecond]) {
            parent[parentSecond] = parentFirst;
            numberOfNodes[parentFirst] += numberOfNodes[parentSecond];
            numberOfEdges[parentFirst] += numberOfEdges[parentSecond] + 1;
        } else {
            parent[parentFirst] = parentSecond;
            numberOfNodes[parentSecond] += numberOfNodes[parentFirst];
            numberOfEdges[parentSecond] += numberOfEdges[parentFirst] + 1;
        }
    }
}
