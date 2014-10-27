package org.ij.service;

import org.ij.model.Trip;
import org.ij.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Abdelkader Midani on 26/10/14.
 */
@Service
public class DijktraAlgorithmService {
    private List<Trip> edges;
    private Set<String> settledNodes;
    private Set<String> unSettledNodes;
    private Map<String, String> predecessors;
    private Map<String, Integer> term;
    private Map<String, String> arrivalTimes;

    public void searchFastWay(List<Trip> edges, Trip todoTrip){

        this.edges = new ArrayList<Trip>(edges);

        settledNodes = new HashSet<String>();
        unSettledNodes = new HashSet<String>();
        predecessors = new HashMap<String, String>();
        term = new HashMap<String, Integer>();
        arrivalTimes = new HashMap<String, String>();

        term.put(todoTrip.getFrom(), 0);
        arrivalTimes.put(todoTrip.getFrom(), todoTrip.getDepartureTime());
        unSettledNodes.add(todoTrip.getFrom());
        while (unSettledNodes.size() > 0) {
            String node = getMinimum();
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalTerms(node);
        }
    }


    /*
     * This method returns the path from the source to the selected target and
     * NULL if no path exists
     */
    public LinkedList<String> getPath(String target) {
        LinkedList<String> path = new LinkedList<String>();
        String step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step+" - "+arrivalTimes.get(step));
        /*while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step+" - "+arrivalTimes.get(step));
        }*/
        // Put it into the correct order
        //Collections.reverse(path);
        return path;
    }

    private int getMinTerms(String destination) {
        Integer t = term.get(destination);
        if (t == null) {
            return Integer.MAX_VALUE;
        } else {
            return t;
        }
    }


    private String getMinimum() {
        String minimum = null;
        for (String vertex : unSettledNodes) {
            if (minimum == null) {
                minimum = vertex;
            } else {
                if (getMinTerms(vertex) < getMinTerms(minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }

    private int getTerm(String node, String target) {
        for (Trip edge : edges) {
            if (edge.getFrom().equals(node)
                    && edge.getTo().equals(target)) {
                arrivalTimes.put(target, edge.getArrivalTime());
                return edge.getTerm();
            }

        }
        //throw new RuntimeException("Should not happen");
        return Integer.MAX_VALUE;
    }

    private boolean isSettled(String vertex) {
        return settledNodes.contains(vertex);
    }

    private List<String> getNeighbors(String node) {
        List<String> neighbors = new ArrayList<String>();
        for (Trip edge : edges) {
            if (edge.getFrom().equals(node)
                    && !isSettled(edge.getTo())) {
                neighbors.add(edge.getTo());
            }
        }
        return neighbors;
    }

    private void findMinimalTerms(String node) {
        List<String> adjacentNodes = getNeighbors(node);
        for (String target : adjacentNodes) {
            boolean coherentTimes = true;
            for (Trip edge : edges) {
                if (edge.getFrom().equals(node)
                        && edge.getTo().equals(target)) {
                    if (!DateUtils.isBefore(edge.getDepartureTime(), arrivalTimes.get(node))) {
                        coherentTimes = false;
                        break;
                    }
                }

            }
            if(!coherentTimes){
                continue;
            }
            if (getMinTerms(target) > getMinTerms(node)
                    + getTerm(node, target)) {
                term.put(target, getMinTerms(node)
                        + getTerm(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }

    }
}
