
using System;

public class Solution
{
    public int CountCompleteComponents(int totalNodes, int[][] edges)
    {
        UnionFind unionFind = new UnionFind(totalNodes);
        foreach (int[] edge in edges)
        {
            unionFind.JoinByRankForNumberOfNodes(edge[0], edge[1]);
        }
        return FindNumberOfCompleteComponents(unionFind, totalNodes);
    }

    private int FindNumberOfCompleteComponents(UnionFind unionFind, int totalNodes)
    {
        /*
          A Boolean Array for the visited can be applied efficiently, instead of a Hash Set, 
          due to the small range of input nodes, [1, 50], as of March 2025.
         */
        bool[] visitedParentsOfCompleteComponents = new bool[totalNodes];
        int numberOfCompleteComponents = 0;

        for (int node = 0; node < totalNodes; ++node)
        {
            int parent = unionFind.FindParent(node);
            int numberOfNodesInGroup = unionFind.numberOfNodes[parent];
            int numberOfEdgesInGroup = unionFind.numberOfEdges[parent];

            if (!IsParentOfCompleteConnectedComponents(numberOfNodesInGroup, numberOfEdgesInGroup))
            {
                continue;
            }
            if (!visitedParentsOfCompleteComponents[parent])
            {
                ++numberOfCompleteComponents;
                visitedParentsOfCompleteComponents[parent] = true;
            }
        }

        return numberOfCompleteComponents;
    }

    private bool IsParentOfCompleteConnectedComponents(int numberOfNodesInGroup, int numberOfEdgesInGroup)
    {
        int expectedNumberOfEdgesInGroup = numberOfNodesInGroup * (numberOfNodesInGroup - 1) / 2;
        return expectedNumberOfEdgesInGroup == numberOfEdgesInGroup;
    }
}

class UnionFind
{
    public int[] parent;
    public int[] numberOfNodes;
    public int[] numberOfEdges;

    public UnionFind(int totalNodes)
    {
        parent = new int[totalNodes];
        numberOfNodes = new int[totalNodes];
        numberOfEdges = new int[totalNodes];

        for (int node = 0; node < totalNodes; ++node)
        {
            parent[node] = node;
            numberOfNodes[node] = 1;
        }

        /*
        Alternatively:
        parent = Enumerable.Range(0, totalNodes).ToArray();
        numberOfNodes = new int[totalNodes];
        Array.Fill(totalNodes, 1);
        numberOfEdges = new int[totalNodes];
        */
    }

    public int FindParent(int index)
    {
        if (parent[index] != index)
        {
            parent[index] = FindParent(parent[index]);
        }
        return parent[index];
    }

    public void JoinByRankForNumberOfNodes(int first, int second)
    {
        int parentFirst = FindParent(first);
        int parentSecond = FindParent(second);

        if (parentFirst == parentSecond)
        {
            ++numberOfEdges[parentFirst];
            return;
        }

        if (numberOfNodes[parentFirst] >= numberOfNodes[parentSecond])
        {
            parent[parentSecond] = parentFirst;
            numberOfNodes[parentFirst] += numberOfNodes[parentSecond];
            numberOfEdges[parentFirst] += numberOfEdges[parentSecond] + 1;
        }
        else
        {
            parent[parentFirst] = parentSecond;
            numberOfNodes[parentSecond] += numberOfNodes[parentFirst];
            numberOfEdges[parentSecond] += numberOfEdges[parentFirst] + 1;
        }
    }
}
