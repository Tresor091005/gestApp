package Helper;

import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.List;

public class StageHelper {
    public static List<Stage> getStages() {
        List<Stage> stages = new ArrayList<>();
        for (Window window : Stage.getWindows()) {
            if (window instanceof Stage stage) {
                stages.add(stage);
            }
        }
        return stages;
    }
}
