package Project2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TextAreaAnimationApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        TextFlow textFlow = new TextFlow();

        // Add initial text and image
        Text text1 = new Text("Step 1: Applying Initial Permutation...\n");
        Image image1 = new Image("birzeitLogo.png"); // Adjust the path as needed
        ImageView imageView1 = new ImageView(image1);

        textFlow.getChildren().addAll(text1, imageView1);

        VBox root = new VBox(textFlow);
        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle("TextFlow with Images Example");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Add dynamic update using Timeline
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(2), event -> {
                Text text2 = new Text("\nStep 2: Substitution with S-boxes...\n");
                Image image2 = new Image("Firas-company.png"); // Adjust the path as needed
                ImageView imageView2 = new ImageView(image2);

                textFlow.getChildren().addAll(text2, imageView2);
            })
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
