
package main

func countCompleteComponents(totalNodes int, edges [][]int) int {
    unionFind := NewUnionFind(totalNodes)
    for _, edge := range edges {
        unionFind.joinByRankForNumberOfNodes(edge[0], edge[1])
    }
    return findNumberOfCompleteComponents(unionFind, totalNodes)
}

func findNumberOfCompleteComponents(unionFind *UnionFind, totalNodes int) int {

    /*
      A Boolean Array for the visited can be applied efficiently, instead of a Hash Set,
      due to the small range of input nodes, [1, 50], as of March 2025.
    */
    visitedParentsOfCompleteComponents := make([]bool, totalNodes)
    numberOfCompleteComponents := 0

    for node := range totalNodes {
        parent := unionFind.findParent(node)
        numberOfNodesInGroup := unionFind.numberOfNodes[parent]
        numberOfEdgesInGroup := unionFind.numberOfEdges[parent]

        if !isParentOfCompleteConnectedComponents(numberOfNodesInGroup, numberOfEdgesInGroup) {
            continue
        }
        if !visitedParentsOfCompleteComponents[parent] {
            numberOfCompleteComponents++
            visitedParentsOfCompleteComponents[parent] = true
        }
    }

    return numberOfCompleteComponents
}

func isParentOfCompleteConnectedComponents(numberOfNodesInGroup int, numberOfEdgesInGroup int) bool {
    expectedNumberOfEdgesInGroup := numberOfNodesInGroup * (numberOfNodesInGroup - 1) / 2
    return expectedNumberOfEdgesInGroup == numberOfEdgesInGroup
}

type UnionFind struct {
    parent        []int
    numberOfNodes []int
    numberOfEdges []int
}

func NewUnionFind(totalNodes int) *UnionFind {
    unionFind := &UnionFind{
        parent:        make([]int, totalNodes),
        numberOfNodes: make([]int, totalNodes),
        numberOfEdges: make([]int, totalNodes),
    }

    for node := range totalNodes {
        unionFind.parent[node] = node
        unionFind.numberOfNodes[node] = 1
    }

    return unionFind
}

func (this *UnionFind) findParent(index int) int {
    if this.parent[index] != index {
        this.parent[index] = this.findParent(this.parent[index])
    }
    return this.parent[index]
}

func (this *UnionFind) joinByRankForNumberOfNodes(first int, second int) {
    parentFirst := this.findParent(first)
    parentSecond := this.findParent(second)

    if parentFirst == parentSecond {
        this.numberOfEdges[parentFirst]++
        return
    }

    if this.numberOfNodes[parentFirst] >= this.numberOfNodes[parentSecond] {
        this.parent[parentSecond] = parentFirst
        this.numberOfNodes[parentFirst] += this.numberOfNodes[parentSecond]
        this.numberOfEdges[parentFirst] += this.numberOfEdges[parentSecond] + 1
    } else {
        this.parent[parentFirst] = parentSecond
        this.numberOfNodes[parentSecond] += this.numberOfNodes[parentFirst]
        this.numberOfEdges[parentSecond] += this.numberOfEdges[parentFirst] + 1
    }
}
