package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.yandex.practicum.processor.ActionProcessor;
import ru.yandex.practicum.processor.SimilarityProcessor;

@SpringBootApplication
public class AnalyzerApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(AnalyzerApp.class, args);
        final ActionProcessor actionProcessor =
                context.getBean(ActionProcessor.class);
        SimilarityProcessor similarityProcessor =
                context.getBean(SimilarityProcessor.class);

        Thread hubEventsThread = new Thread(actionProcessor);
        hubEventsThread.setName("UserActionProcessorThread");
        hubEventsThread.start();

        Thread snapshotThread = new Thread(similarityProcessor);
        snapshotThread.setName("SimilarityProcessorThread");
        snapshotThread.start();
    }
}