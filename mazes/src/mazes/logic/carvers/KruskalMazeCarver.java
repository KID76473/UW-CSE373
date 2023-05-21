package mazes.logic.carvers;

// import graphs.BaseEdge;
import graphs.EdgeWithData;
// import graphs.minspantrees.MinimumSpanningTree;
import graphs.minspantrees.MinimumSpanningTreeFinder;
// import mazes.entities.LineSegment;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.logic.MazeGraph;

// import java.util.Collection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver extends MazeCarver {
    MinimumSpanningTreeFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder;
    private final Random rand;

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random();
    }

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder,
                             long seed) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random(seed);
    }

    @Override
    protected Set<Wall> chooseWallsToRemove(Set<Wall> walls) {
        Set<EdgeWithData<Room, Wall>> edges = new HashSet<>();
        for (Wall w : walls) { // assign random values to it
            edges.add(new EdgeWithData<>(w.getRoom1(), w.getRoom2(), rand.nextInt(), w));
        }
        // finding MST
        Collection<EdgeWithData<Room, Wall>> temp =
            this.minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(edges)).edges();
        Set<Wall> result = new HashSet<>();
        for (EdgeWithData<Room, Wall> x : temp) { // convert EdgeWithData to wall
            result.add(x.data());
        }
        return result;
    }
}
