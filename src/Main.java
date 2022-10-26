import controller.Controller;
import model.GameModel;
import state.StackStateReturner;
import state.StackStateSaver;
import state.StateSaver;
import view.MainView;

import javax.swing.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //saver
        StateSaver stackStateSaver = StackStateSaver.getInstance();
        StackStateReturner stackStateReturner = StackStateReturner.getInstance((StackStateSaver) stackStateSaver);
        List<StateSaver> stateSavers = List.of(stackStateSaver);

        MainView view = new MainView();
        GameModel gameModel = new GameModel(view, stateSavers, stackStateReturner);
        Controller controller = new Controller(gameModel, view);
        view.setController(controller);

        JFrame game = new JFrame();

        game.setTitle("2048");
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setSize(450, 500);
        game.setResizable(true);

        game.add(view);

        game.setLocationRelativeTo(null);
        game.setVisible(true);
    }
}
