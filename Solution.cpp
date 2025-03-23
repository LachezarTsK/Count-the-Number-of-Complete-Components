
#include <vector>
using namespace std;

class UnionFind {

public:
    vector<int> parent;
    vector<int> numberOfNodes;
    vector<int> numberOfEdges;

    explicit UnionFind(int totalNodes) {
        parent.resize(totalNodes);
        numberOfNodes.resize(totalNodes);
        numberOfEdges.resize(totalNodes);

        for (int node = 0; node < totalNodes; ++node) {
            parent[node] = node;
            numberOfNodes[node] = 1;
        }

        /*
        Alternatively:
        parent.resize(totalNodes);
        ranges::iota(parent, 0);
        numberOfNodes.assign(totalNodes, 1);
        numberOfEdges.resize(totalNodes);
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
        }
        else {
            parent[parentFirst] = parentSecond;
            numberOfNodes[parentSecond] += numberOfNodes[parentFirst];
            numberOfEdges[parentSecond] += numberOfEdges[parentFirst] + 1;
        }
    }
};

class Solution {

public:
    int countCompleteComponents(int totalNodes, const vector<vector<int>>& edges) const {
        UnionFind unionFind(totalNodes);
        for (const auto& edge : edges) {
            unionFind.joinByRankForNumberOfNodes(edge[0], edge[1]);
        }
        return findNumberOfCompleteComponents(unionFind, totalNodes);
    }

private:
    int findNumberOfCompleteComponents(UnionFind unionFind, int totalNodes) const {

        /*
          A Boolean Vector for the visited can be applied efficiently, instead of a Hash Set,
          due to the small range of input nodes, [1, 50], as of March 2025.
         */
        vector<bool>visitedParentsOfCompleteComponents(totalNodes);
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

    bool isParentOfCompleteConnectedComponents(int numberOfNodesInGroup, int numberOfEdgesInGroup) const {
        int expectedNumberOfEdgesInGroup = numberOfNodesInGroup * (numberOfNodesInGroup - 1) / 2;
        return expectedNumberOfEdgesInGroup == numberOfEdgesInGroup;
    }
};
