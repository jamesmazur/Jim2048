package code;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MyKeyListener implements KeyListener {

    private Model _model;
    
    public MyKeyListener(Model model) {
        _model = model;
    }
    
    @Override
    public void keyPressed(KeyEvent arg0) {
        int keyCode = arg0.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                _model.slideUp();
                break;
            case KeyEvent.VK_DOWN:
                _model.slideDown();
                break;
            case KeyEvent.VK_LEFT:
                _model.slideLeft();
                break;
            case KeyEvent.VK_RIGHT:
                _model.slideRight();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {

    }

    @Override
    public void keyTyped(KeyEvent arg0) {

    }
}
