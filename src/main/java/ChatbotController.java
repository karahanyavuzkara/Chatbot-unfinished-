import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Properties;

@Controller
public class ChatbotController extends Application {

    @Autowired
    private StanfordCoreNLP coreNLP;

    private TextField userInputField;
    private Label chatArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chatbot GUI");

        // Create GUI components
        userInputField = new TextField();
        Button sendButton = new Button("Send");
        chatArea = new Label();

        // Set event handler for the send button
        sendButton.setOnAction(event -> analyzeInput());

        // Create a layout container and add components
        VBox root = new VBox(10);
        root.getChildren().addAll(chatArea, userInputField, sendButton);

        // Create the scene and set it on the stage
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);

        // Initialize your StanfordCoreNLP instance
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, sentiment");
        coreNLP = new StanfordCoreNLP(props);

        primaryStage.show();
    }

    private void analyzeInput() {
        String userInput = userInputField.getText();

        // Perform sentiment analysis using CoreNLP
        Annotation document = new Annotation(userInput);
        coreNLP.annotate(document);

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        String sentiment = sentences.get(0).get(SentimentCoreAnnotations.SentimentClass.class);

        String response;

        // Generate response based on user input
        if (userInput.toLowerCase().contains("hi") || userInput.toLowerCase().contains("hello")) {
            response = "Hello! How can I assist you?";
        } else {
            response = "I'm sorry, I didn't understand your input.";
        }

        // Update the chat conversation
        chatArea.setText("User: " + userInput + "\n");
        chatArea.setText(chatArea.getText() + "Chatbot: " + response + "\n");

        // Clear the user input field
        userInputField.clear();
    }
}
