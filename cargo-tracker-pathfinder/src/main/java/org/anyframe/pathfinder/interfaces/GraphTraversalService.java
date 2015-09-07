package org.anyframe.pathfinder.interfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.anyframe.pathfinder.infrastructure.GraphDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GraphTraversalService {

    @Autowired
    private GraphDao dao;
    
    private final Random random = new Random();
    
    private static final long ONE_MIN_MS = 1000 * 60;
    
    private static final long ONE_DAY_MS = ONE_MIN_MS * 60 * 24;

    @RequestMapping(value="/shortest-path", method=RequestMethod.GET)
    public TransitPaths findShortestPath(
//            @NotNull @Size(min = 5, max = 5) 
            @RequestParam(value="origin", required=true) String originUnLocode,
//            @NotNull @Size(min = 5, max = 5)
            @RequestParam(value="destination", required=true) String destinationUnLocode,
            @RequestParam(value="deadline", required=false) String deadline) {
    	
    	System.out.println("#### Pathfinder #1 called");
        Date date = nextDate(new Date());

        List<String> allVertices = dao.listLocations();
        allVertices.remove(originUnLocode);
        allVertices.remove(destinationUnLocode);

        int candidateCount = getRandomNumberOfCandidates();
        List<TransitPath> candidates = new ArrayList<>(
                candidateCount);

        for (int i = 0; i < candidateCount; i++) {
            allVertices = getRandomChunkOfLocations(allVertices);
            List<TransitEdge> transitEdges = new ArrayList<>(
                    allVertices.size() - 1);
            String firstLegTo = allVertices.get(0);

            Date fromDate = nextDate(date);
            Date toDate = nextDate(fromDate);
            date = nextDate(toDate);

            transitEdges.add(new TransitEdge(
                    dao.getVoyageNumber(originUnLocode, firstLegTo),
                    originUnLocode, firstLegTo, fromDate, toDate));

            for (int j = 0; j < allVertices.size() - 1; j++) {
                String current = allVertices.get(j);
                String next = allVertices.get(j + 1);
                fromDate = nextDate(date);
                toDate = nextDate(fromDate);
                date = nextDate(toDate);
                transitEdges.add(new TransitEdge(dao.getVoyageNumber(current,
                        next), current, next, fromDate, toDate));
            }

            String lastLegFrom = allVertices.get(allVertices.size() - 1);
            fromDate = nextDate(date);
            toDate = nextDate(fromDate);
            transitEdges.add(new TransitEdge(
                    dao.getVoyageNumber(lastLegFrom, destinationUnLocode),
                    lastLegFrom, destinationUnLocode, fromDate, toDate));

            candidates.add(new TransitPath(transitEdges));
        }

        return new TransitPaths(candidates);
    }

    private Date nextDate(Date date) {
        return new Date(date.getTime() + ONE_DAY_MS
                + (random.nextInt(1000) - 500) * ONE_MIN_MS);
    }

    private int getRandomNumberOfCandidates() {
        return 3 + random.nextInt(3);
    }

    private List<String> getRandomChunkOfLocations(List<String> allLocations) {
        Collections.shuffle(allLocations);
        int total = allLocations.size();
        int chunk = total > 4 ? 1 + new Random().nextInt(5) : total;
        return allLocations.subList(0, chunk);
    }
    
}
