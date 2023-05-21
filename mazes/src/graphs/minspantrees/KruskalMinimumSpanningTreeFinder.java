package graphs.minspantrees;

import disjointsets.DisjointSets;
import disjointsets.UnionBySizeCompressingDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        // return new QuickFindDisjointSets<>();
        return new UnionBySizeCompressingDisjointSets<>();
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
        // Here's some code to get you started; feel free to change or rearrange it if you'd like.

        // sort edges in the graph in ascending weight order
        List<E> edges = new ArrayList<>(graph.allEdges());
        edges.sort(Comparator.comparingDouble(E::weight));

        DisjointSets<V> disjointSets = createDisjointSets();
        List<E> result = new ArrayList<>();

        for (V vertex : graph.allVertices()) {
            disjointSets.makeSet(vertex);
        }
        for (E edge : edges) {
            if (disjointSets.findSet(edge.from()) != disjointSets.findSet(edge.to())) {
                disjointSets.union(edge.from(), edge.to());
                result.add(edge);
            }
        }
        if (result.size() == graph.allVertices().size() - 1 ||
            (result.size() == 0 && graph.allVertices().size() == 0)) {
            return new MinimumSpanningTree.Success<>(result);
        }
        else {
            return  new MinimumSpanningTree.Failure<>();
        }
    }
}
