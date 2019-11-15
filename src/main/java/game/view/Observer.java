package game.view;


import game.model.Level;


import java.util.Map;

public interface Observer {
    void update();
    void save(Map<Integer, Level> oldLevel, int id, double score, double levelScore, int lives);
}
