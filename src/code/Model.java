package code;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Model {

    private HashMap<Point, Integer> _grid;
    private HashSet<IView> _views;
    private boolean _gameIsOver;
    private Random _random;

    public Model() {
        // Initialize _grid, with two random 2s (or possibly one or two 4s).
        _grid = new HashMap<Point, Integer>();
        for (int row = 0; row < Driver.GRID_SIZE; ++row) {
            for (int col = 0; col < Driver.GRID_SIZE; ++col) {
                _grid.put(new Point(row, col), 0);
            }
        }
        _random = new Random();
        Point firstPoint = new Point(_random.nextInt(Driver.GRID_SIZE), _random.nextInt(Driver.GRID_SIZE));
        _grid.put(firstPoint, _random.nextInt(5) == 0 ? 4 : 2);
        Point secondPoint = new Point(_random.nextInt(Driver.GRID_SIZE), _random.nextInt(Driver.GRID_SIZE));
        while (secondPoint.equals(firstPoint)) {
            secondPoint = new Point(_random.nextInt(Driver.GRID_SIZE), _random.nextInt(Driver.GRID_SIZE));
        }
        _grid.put(secondPoint, _random.nextInt(5) == 0 ? 4 : 2);
        // Initialize _views.
        _views = new HashSet<IView>();
        // Initialize _gameIsOver.
        _gameIsOver = false;
    }

    public int getGridValue(int row, int col) {
        return _grid.get(new Point(row, col));
    }

    public void addView(IView v) {
        v.update();
        _views.add(v);
    }
    
    private Point getUnoccupiedPoint() {
        ArrayList<Point> pointList = new ArrayList<Point>();
        for (int row = 0; row < Driver.GRID_SIZE; ++row) {
            for (int col = 0; col < Driver.GRID_SIZE; ++col) {
                if (getGridValue(row, col) == 0) {
                    pointList.add(new Point(row, col));
                }
            }
        }
        Collections.shuffle(pointList);
        if (pointList.size() == 0) {
            return null;
        } else {
            return pointList.get(0);
        }
    }
    
    private boolean checkForWin() {
        for (int row = 0; row < Driver.GRID_SIZE; ++row) {
            for (int col = 0; col < Driver.GRID_SIZE; ++col) {
                if (getGridValue(row, col) >= Driver.WIN_THRESHOLD) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean checkForLoss() {
        for (int row = 0; row < Driver.GRID_SIZE; ++row) {
            for (int col = 0; col < Driver.GRID_SIZE; ++col) {
                int curValue = getGridValue(row, col);
                if (curValue == 0 || curValue >= Driver.WIN_THRESHOLD) {
                    return false;
                }
                if (row > 0 && curValue == getGridValue(row - 1, col)) {
                    return false;
                }
                if (col > 0 && curValue == getGridValue(row, col - 1)) {
                    return false;
                }
                if (row < Driver.GRID_SIZE - 1 && curValue == getGridValue(row + 1, col)) {
                    return false;
                }
                if (col < Driver.GRID_SIZE - 1 && curValue == getGridValue(row, col + 1)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private void updateViews(boolean modelChanged) {
        if (modelChanged) {
            Point p = getUnoccupiedPoint();
            if (p != null) {
                _grid.put(p, _random.nextInt(10) == 0 ? 4 : 2);
            }
            for (IView v : _views) {
                if (checkForWin()) {
                    _gameIsOver = true;
                    v.updateGameWon();
                } else if (checkForLoss()) {
                    _gameIsOver = true;
                    v.updateGameLost();
                } else {
                    v.update();
                }
            }
        }
    }
    
    private boolean updateNumbersInOrder(ArrayList<Integer> numbersInOrder) {
        ArrayList<Integer> newNumbersInOrder = new ArrayList<Integer>();
        // First, get all of the nonzero entries.
        for (int i = 0; i < numbersInOrder.size(); ++i) {
            if (numbersInOrder.get(i) != 0) {
                newNumbersInOrder.add(numbersInOrder.get(i));
            }
        }
        
        // Next, combine pairs.
        for (int i = 0; i < newNumbersInOrder.size() - 1; ++i) {
            if (newNumbersInOrder.get(i).equals(newNumbersInOrder.get(i + 1))) {
                newNumbersInOrder.set(i, newNumbersInOrder.get(i) * 2);
                newNumbersInOrder.remove(i + 1);
            }
        }
        
        // Finally, pad the new list.
        while (newNumbersInOrder.size() < numbersInOrder.size()) {
            newNumbersInOrder.add(0);
        }
        
        boolean modelChanged = false;
        for (int i = 0; i < numbersInOrder.size(); ++i) {
            if (!newNumbersInOrder.get(i).equals(numbersInOrder.get(i))) {
                numbersInOrder.set(i, newNumbersInOrder.get(i));
                modelChanged = true;
            }
        }
        return modelChanged;
    }

    public void slideUp() {
        if (!_gameIsOver) {
            boolean modelChanged = false;
            for (int col = 0; col < Driver.GRID_SIZE; ++col) {
                // Get all of the numbers going down the current column.
                ArrayList<Integer> numbersInOrder = new ArrayList<Integer>();
                for (int row = 0; row < Driver.GRID_SIZE; ++row) {
                    numbersInOrder.add(getGridValue(row, col));
                }
                
                // Update them using the appropriate transformation.
                modelChanged |= updateNumbersInOrder(numbersInOrder);
                
                // Finally, actually update _grid for this column.
                for (int row = 0; row < Driver.GRID_SIZE; ++row) {
                    _grid.put(new Point(row, col), numbersInOrder.get(row));
                }
            }
            updateViews(modelChanged);
        }
    }

    public void slideDown() {
        if (!_gameIsOver) {
            boolean modelChanged = false;
            for (int col = 0; col < Driver.GRID_SIZE; ++col) {
                // Get all of the numbers going up the current column.
                ArrayList<Integer> numbersInOrder = new ArrayList<Integer>();
                for (int row = Driver.GRID_SIZE - 1; row >= 0; --row) {
                    numbersInOrder.add(getGridValue(row, col));
                }
                
                // Update them using the appropriate transformation.
                modelChanged |= updateNumbersInOrder(numbersInOrder);
                
                // Finally, actually update _grid for this column.
                for (int row = Driver.GRID_SIZE - 1; row >= 0; --row) {
                    _grid.put(new Point(row, col), numbersInOrder.get(Driver.GRID_SIZE - 1 - row));
                }
            }
            updateViews(modelChanged);
        }
    }

    public void slideLeft() {
        if (!_gameIsOver) {
            boolean modelChanged = false;
            for (int row = 0; row < Driver.GRID_SIZE; ++row) {
                // Get all of the numbers going right across the current row.
                ArrayList<Integer> numbersInOrder = new ArrayList<Integer>();
                for (int col = 0; col < Driver.GRID_SIZE; ++col) {
                    numbersInOrder.add(getGridValue(row, col));
                }
                
                // Update them using the appropriate transformation.
                modelChanged |= updateNumbersInOrder(numbersInOrder);
                
                // Finally, actually update _grid for this row.
                for (int col = 0; col < Driver.GRID_SIZE; ++col) {
                    _grid.put(new Point(row, col), numbersInOrder.get(col));
                }
            }
            updateViews(modelChanged);
        }
    }

    public void slideRight() {
        if (!_gameIsOver) {
            boolean modelChanged = false;
            for (int row = 0; row < Driver.GRID_SIZE; ++row) {
                // Get all of the numbers going left across the current row.
                ArrayList<Integer> numbersInOrder = new ArrayList<Integer>();
                for (int col = Driver.GRID_SIZE - 1; col >= 0; --col) {
                    numbersInOrder.add(getGridValue(row, col));
                }
                
                // Update them using the appropriate transformation.
                modelChanged |= updateNumbersInOrder(numbersInOrder);
                
                // Finally, actually update _grid for this row.
                for (int col = Driver.GRID_SIZE - 1; col >= 0; --col) {
                    _grid.put(new Point(row, col), numbersInOrder.get(Driver.GRID_SIZE - 1 - col));
                }
            }
            updateViews(modelChanged);
        }
    }

}