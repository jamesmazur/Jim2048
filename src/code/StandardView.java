package code;

import java.awt.GridLayout;
import java.awt.Point;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class StandardView implements IView, Runnable {

    private Model _model;
    private JFrame _frame;
    private HashMap<Point,JButton> _buttonGrid;
    
    public StandardView(Model model) {
        _model = model;
        _buttonGrid = new HashMap<Point,JButton>();
        SwingUtilities.invokeLater(this);
    }
    
    @Override
    public void run() {
        _frame = new JFrame("Jim's 2048 Game");
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.getContentPane().setLayout(new GridLayout(Driver.GRID_SIZE, Driver.GRID_SIZE));
        for (int row = 0; row < Driver.GRID_SIZE; ++row) {
            for (int col = 0; col < Driver.GRID_SIZE; ++col) {
                JButton button = new JButton();
                button.addKeyListener(new MyKeyListener(_model));
                _buttonGrid.put(new Point(row, col), button);
                _frame.add(button);
            }
        }
        _model.addView(this);
        _frame.pack();
        _frame.setSize(75 * Driver.GRID_SIZE, 75 * Driver.GRID_SIZE);
        _frame.setLocationRelativeTo(null); // Center frame on screen.
        _frame.setVisible(true);
    }
    
    @Override
    public void update() {
        for (int row = 0; row < Driver.GRID_SIZE; ++row) {
            for (int col = 0; col < Driver.GRID_SIZE; ++col) {
                JButton button = _buttonGrid.get(new Point(row, col));
                Integer value = _model.getGridValue(row, col);
                button.setText(value == 0 ? "" : value.toString());
            }
        }
    }

    @Override
    public void updateGameWon() {
        update();
        JOptionPane.showMessageDialog(_frame, "Congratulations! You won!", "You Win", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void updateGameLost() {
        update();
        JOptionPane.showMessageDialog(_frame, "Sorry. You lost.", "You Lose", JOptionPane.INFORMATION_MESSAGE);
    }

}
