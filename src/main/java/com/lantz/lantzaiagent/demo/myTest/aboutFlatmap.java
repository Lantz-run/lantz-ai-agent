package com.lantz.lantzaiagent.demo.myTest;

import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.generation.augmentation.QueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/8/22
 *
 * @author Lantz
 * @version 1.0
 * @Description aboutFlamap
 * @since 1.8
 */
public class aboutFlatmap {
    public static void main(String[] args) {
        List<List<String>> listOfLists = Arrays.asList(
                Arrays.asList("a", "b", "c"),
                Arrays.asList("d", "e"),
                Arrays.asList("f", "g", "h", "i")
        );

    }
}
