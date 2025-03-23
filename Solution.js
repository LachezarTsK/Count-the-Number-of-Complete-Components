
/**
 * @param {number} totalNodes
 * @param {number[][]} edges
 * @return {number}
 */
var countCompleteComponents = function (totalNodes, edges) {
    const unionFind = new UnionFind(totalNodes);
    for (let edge of edges) {
        unionFind.joinByRankForNumberOfNodes(edge[0], edge[1]);
    }
    return findNumberOfCompleteComponents(unionFind, totalNodes);
};

/**
 * @param {UnionFind} unionFind
 * @param {number} totalNodes
 * @return {number}
 */
function findNumberOfCompleteComponents(unionFind, totalNodes) {

    /*
     A Boolean Array for the visited can be applied efficiently, instead of a Hash Set, 
     due to the small range of input nodes, [1, 50], as of March 2025.
     */
    const visitedParentsOfCompleteComponents = new Array(totalNodes).fill(false);
    let numberOfCompleteComponents = 0;

    for (let node = 0; node < totalNodes; ++node) {
        const parent = unionFind.findParent(node);
        const numberOfNodesInGroup = unionFind.numberOfNodes[parent];
        const numberOfEdgesInGroup = unionFind.numberOfEdges[parent];

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

/**
 * @param {number} numberOfNodesInGroup
 * @param {number} numberOfEdgesInGroup
 * @return {boolean}
 */
function isParentOfCompleteConnectedComponents(numberOfNodesInGroup, numberOfEdgesInGroup) {
    const expectedNumberOfEdgesInGroup = numberOfNodesInGroup * (numberOfNodesInGroup - 1) / 2;
    return expectedNumberOfEdgesInGroup === numberOfEdgesInGroup;
}

class UnionFind {

    /**
     * @param {number} totalNodes
     */
    constructor(totalNodes) {
        this.parent = new Array(totalNodes);
        this.numberOfNodes = new Array(totalNodes);
        this.numberOfEdges = new Array(totalNodes);

        for (let node = 0; node < totalNodes; ++node) {
            this.parent[node] = node;
            this.numberOfNodes[node] = 1;
            this.numberOfEdges[node] = 0;
        }

        /*
         Alternatively:
         this.parent = Array.from(Array(totalNodes).keys());
         this.numberOfNodes = new Array(totalNodes).fill(1);
         this.numberOfEdges = new Array(totalNodes).fill(0);
         */
    }

    /**
     * @param {number} index
     * @return {number}
     */
    findParent(index) {
        if (this.parent[index] !== index) {
            this.parent[index] = this.findParent(this.parent[index]);
        }
        return this.parent[index];
    }

    /**
     * @param {number} first
     * @param {number} second
     * @return {void}
     */
    joinByRankForNumberOfNodes(first, second) {
        const parentFirst = this.findParent(first);
        const parentSecond = this.findParent(second);

        if (parentFirst === parentSecond) {
            ++this.numberOfEdges[parentFirst];
            return;
        }

        if (this.numberOfNodes[parentFirst] >= this.numberOfNodes[parentSecond]) {
            this.parent[parentSecond] = parentFirst;
            this.numberOfNodes[parentFirst] += this.numberOfNodes[parentSecond];
            this.numberOfEdges[parentFirst] += this.numberOfEdges[parentSecond] + 1;
        } else {
            this.parent[parentFirst] = parentSecond;
            this.numberOfNodes[parentSecond] += this.numberOfNodes[parentFirst];
            this.numberOfEdges[parentSecond] += this.numberOfEdges[parentFirst] + 1;
        }
    }
}
