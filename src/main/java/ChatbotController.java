import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.List;
import java.util.Properties;

public class ChatbotController extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        //TODO
    }
    @Override
    public String getBotUsername() {
        //TODO
        return "karahanzodiac_bot";
    }
    @Override
    public String getBotToken() {
        //TODO
        return "6311821361:AAHb_oh-4K6oTvJ92drx3goT-wAWOrIxXKo";
    }
    private StanfordCoreNLP coreNLP;

    public static void main(String[] args) {
        SpringApplication.run(ChatbotController.class, args);
    }

    @RequestMapping(value = "/analyze", method = RequestMethod.POST)
    public String analyzeInput(@RequestParam("input") String userInput) {
        // Perform sentiment analysis using CoreNLP
        Annotation document = new Annotation(userInput);
        coreNLP.annotate(document);

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        String sentiment = sentences.get(0).get(SentimentCoreAnnotations.SentimentClass.class);

        String response;

        // Generate response based on user input
        if (userInput.toLowerCase().contains("hi") || userInput.toLowerCase().contains("hello")
                || userInput.toLowerCase().contains("hey") || userInput.toLowerCase().contains("selam")
        || userInput.toLowerCase().contains("merhaba")){
            response = "Hello! How can I assist you?";
        } else {
            response = "I'm sorry, I didn't understand your input.";
        }

        // Create a JSON response object
        ResponseObject responseObject = new ResponseObject(sentiment, response);

        // Return the JSON response
        return responseObject.toJson();
    }
}

class ResponseObject {
    private String sentiment;
    private String response;

    public ResponseObject(String sentiment, String response) {
        this.sentiment = sentiment;
        this.response = response;
    }

    // Getters and setters

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    // Convert object to JSON string
    public String toJson() {
        return "{\"sentiment\":\"" + sentiment + "\", \"response\":\"" + response + "\"}";
    }
}
