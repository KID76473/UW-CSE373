package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
// import priorityqueues.ArrayHeapMinPQ;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>(); // DoubleMapMinPQ ArrayHeapMinPQ
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        Map<V, E> edgeTo = new HashMap<>();
        if (Objects.equals(start, end)) {
            return edgeTo;
        }
        Map<V, Double> distanceTo = new HashMap<>();
        Set<V> processed = new HashSet<>();
        ExtrinsicMinPQ<V> minPQ = createMinPQ();

        distanceTo.put(start, 0.0);
        edgeTo.put(start, null);
        minPQ.add(start, 0.0);

        V current = start;
        while (!minPQ.isEmpty() && !processed.contains(end)) {
            current = minPQ.removeMin();
            processed.add(current);
            for (E e : graph.outgoingEdgesFrom(current)) {
                if (!edgeTo.containsKey(e.to())) {
                    distanceTo.put(e.to(), distanceTo.get(current) + e.weight());
                    edgeTo.put(e.to(), e);
                    minPQ.add(e.to(), distanceTo.get(e.to()));
                }
                else {
                    double oldDistance = distanceTo.get(e.to());
                    double newDistance = distanceTo.get(e.from()) + e.weight();
                    if (newDistance < oldDistance) {
                        distanceTo.put(e.to(), newDistance);
                        edgeTo.put(e.to(), e);
                        minPQ.changePriority(e.to(), newDistance);
                    }
                }
            }
        }
        edgeTo.remove(start);
        return edgeTo;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }
        else if (spt.containsKey(end) && spt.get(end) != null) {
            List<E> edges = new ArrayList<>();
            V current = end;
            while (!Objects.equals(start, current)) {
                edges.add(spt.get(current));
                current = spt.get(current).from();
            }
            Collections.reverse(edges);
            return new ShortestPath.Success<>(edges);
        }
        else {
            return new ShortestPath.Failure<>();
        }
    }

}
