
class Solution {

    fun countCompleteComponents(totalNodes: Int, edges: Array<IntArray>): Int {
        val unionFind = UnionFind(totalNodes)
        for (edge in edges) {
            unionFind.joinByRankForNumberOfNodes(edge[0], edge[1])
        }
        return findNumberOfCompleteComponents(unionFind, totalNodes)
    }

    private fun findNumberOfCompleteComponents(unionFind: UnionFind, totalNodes: Int): Int {

        /*
          A Boolean Array for the visited can be applied efficiently, instead of a Hash Set,
          due to the small range of input nodes, [1, 50], as of March 2025.
         */
        val visitedParentsOfCompleteComponents = BooleanArray(totalNodes)
        var numberOfCompleteComponents = 0

        for (node in 0..<totalNodes) {
            val parent = unionFind.findParent(node)
            val numberOfNodesInGroup = unionFind.numberOfNodes[parent]
            val numberOfEdgesInGroup = unionFind.numberOfEdges[parent]

            if (!isParentOfCompleteConnectedComponents(numberOfNodesInGroup, numberOfEdgesInGroup)) {
                continue
            }
            if (!visitedParentsOfCompleteComponents[parent]) {
                ++numberOfCompleteComponents
                visitedParentsOfCompleteComponents[parent] = true
            }
        }

        return numberOfCompleteComponents
    }

    private fun isParentOfCompleteConnectedComponents(numberOfNodesInGroup: Int, numberOfEdgesInGroup: Int): Boolean {
        val expectedNumberOfEdgesInGroup = numberOfNodesInGroup * (numberOfNodesInGroup - 1) / 2
        return expectedNumberOfEdgesInGroup == numberOfEdgesInGroup
    }
}

class UnionFind(private val totalNodes: Int) {

    val parent = IntArray(totalNodes)
    val numberOfNodes = IntArray(totalNodes)
    val numberOfEdges = IntArray(totalNodes)

    init {
        for (node in 0..<totalNodes) {
            parent[node] = node
            this.numberOfNodes[node] = 1
        }
    }

    /*
      Alternatively:
      val parent = IntArray(totalNodes) { i -> i }
      val numberOfNodes = IntArray(totalNodes) { 1 }
      val numberOfEdges = IntArray(totalNodes)
    */

    fun findParent(index: Int): Int {
        if (parent[index] != index) {
            parent[index] = findParent(parent[index])
        }
        return parent[index]
    }

    fun joinByRankForNumberOfNodes(first: Int, second: Int) {
        val parentFirst = findParent(first)
        val parentSecond = findParent(second)

        if (parentFirst == parentSecond) {
            ++numberOfEdges[parentFirst]
            return
        }

        if (numberOfNodes[parentFirst] >= numberOfNodes[parentSecond]) {
            parent[parentSecond] = parentFirst
            numberOfNodes[parentFirst] += numberOfNodes[parentSecond]
            numberOfEdges[parentFirst] += numberOfEdges[parentSecond] + 1
        } else {
            parent[parentFirst] = parentSecond
            numberOfNodes[parentSecond] += numberOfNodes[parentFirst]
            numberOfEdges[parentSecond] += numberOfEdges[parentFirst] + 1
        }
    }
}
