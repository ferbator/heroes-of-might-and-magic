package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.awt.*;
import java.util.*;
import java.util.List;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {
    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        int WIDTH = 27;
        int HEIGHT = 21;

        // Преобразование списка существующих юнитов в набор занятых позиций
        Set<Point> obstacles = new HashSet<>();
        for (Unit unit : existingUnitList) {
            if (unit.isAlive()) {
                obstacles.add(new Point(unit.getxCoordinate(), unit.getyCoordinate()));
            }
        }

        Point start = new Point(attackUnit.getxCoordinate(), attackUnit.getyCoordinate());
        Point target = new Point(targetUnit.getxCoordinate(), targetUnit.getyCoordinate());

        // Структуры для алгоритма Дейкстры
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(n -> n.distance));
        Map<Point, Point> predecessors = new HashMap<>();
        Map<Point, Integer> distance = new HashMap<>();
        Set<Point> visited = new HashSet<>();

        distance.put(start, 0);
        queue.add(new Node(start, 0));

        // Алгоритм поиска кратчайшего пути
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            Point currentPoint = current.point;
            if (visited.contains(currentPoint)) continue;
            visited.add(currentPoint);

            if (currentPoint.equals(target)) break;

            for (Point neighbor : getNeighbors(currentPoint, WIDTH, HEIGHT)) {
                if (obstacles.contains(neighbor) && !neighbor.equals(target)) continue;
                int newDist = distance.get(currentPoint) + 1;
                if (newDist < distance.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    distance.put(neighbor, newDist);
                    predecessors.put(neighbor, currentPoint);
                    queue.add(new Node(neighbor, newDist));
                }
            }
        }

        // Восстановление пути из предшественников
        List<Edge> path = new ArrayList<>();
        if (!predecessors.containsKey(target) && !start.equals(target)) {
            return path; // путь не найден
        }

        Point step = target;
        while (step != null && !step.equals(start)) {
            path.add(new Edge(step.x, step.y));
            step = predecessors.get(step);
        }
        path.add(new Edge(start.x, start.y));
        Collections.reverse(path);

        return path;
    }

    private List<Point> getNeighbors(Point p, int width, int height) {
        List<Point> neighbors = new ArrayList<>();
        int x = p.x;
        int y = p.y;

        if (x > 0) neighbors.add(new Point(x - 1, y));
        if (x < width - 1) neighbors.add(new Point(x + 1, y));
        if (y > 0) neighbors.add(new Point(x, y - 1));
        if (y < height - 1) neighbors.add(new Point(x, y + 1));

        return neighbors;
    }

    private static class Node {
        Point point;
        int distance;

        Node(Point point, int distance) {
            this.point = point;
            this.distance = distance;
        }
    }
}
