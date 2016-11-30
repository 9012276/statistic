package ru.mk.Statistic.Domain;

import java.util.ArrayList;

public class HitRepository {

    private ArrayList<Hit> hits;

    public HitRepository() {
        hits = new ArrayList<Hit>();
    }

    public ArrayList<Hit> findByCounterId(short counterId) {
        ArrayList<Hit> result = new ArrayList<>();

        for(Hit hit: hits) {
            if (hit.getCounterId() != counterId) {
                continue;
            }

            result.add(hit);
        }

        return result;
    }

    public void persist(Hit hit) {
        this.hits.add(hit);
    }
}
