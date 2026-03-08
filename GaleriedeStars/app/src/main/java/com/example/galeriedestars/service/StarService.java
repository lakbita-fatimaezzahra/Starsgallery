package com.example.galeriedestars.service;

import com.example.galeriedestars.beans.Star;
import com.example.galeriedestars.dao.IDao;
import java.util.ArrayList;
import java.util.List;

public class StarService implements IDao<Star> {

    private List<Star> stars;
    private static StarService instance;

    private StarService() {
        stars = new ArrayList<>();
        seed();
    }

    public static StarService getInstance() {
        if (instance == null) instance = new StarService();
        return instance;
    }

    private void seed() {
        stars.add(new Star("Kate Bosworth",
                "https://randomuser.me/api/portraits/women/1.jpg", 3.0f));
        stars.add(new Star("George Clooney",
                "https://randomuser.me/api/portraits/men/1.jpg", 3.0f));
        stars.add(new Star("Michelle Rodriguez",
                "https://randomuser.me/api/portraits/women/2.jpg", 5.0f));
        stars.add(new Star("Tom Cruise",
                "https://randomuser.me/api/portraits/men/2.jpg", 4.0f));
        stars.add(new Star("Emma Watson",
                "https://randomuser.me/api/portraits/women/3.jpg", 4.5f));
        stars.add(new Star("Leonardo DiCaprio",
                "https://randomuser.me/api/portraits/men/3.jpg", 4.8f));
    }

    @Override
    public boolean create(Star o) { return stars.add(o); }

    @Override
    public boolean update(Star o) {
        for (Star s : stars) {
            if (s.getId() == o.getId()) {
                s.setName(o.getName());
                s.setImg(o.getImg());
                s.setRating(o.getRating());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Star o) { return stars.remove(o); }

    @Override
    public Star findById(int id) {
        for (Star s : stars) if (s.getId() == id) return s;
        return null;
    }

    @Override
    public List<Star> findAll() { return stars; }
}