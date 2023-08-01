package com.soldesk.TeamProject.recommendSystem;

import jakarta.annotation.PostConstruct;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import org.springframework.scheduling.annotation.EnableScheduling;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;


@Service
@EnableScheduling
public class RsService {
    private Word2Vec model;

    public String signinUser;


    // 모델 불러와서 저장
    @PostConstruct
    public void loadWord2VecModel() throws Exception{
        System.setProperty("java.library.path", "/path/to/nd4j/library");
        try {
            this.model = WordVectorSerializer.readWord2VecModel (new File("src/main/resources/static/model/word2vec.bin.gz"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 모델 가져오기
    public Word2Vec getModel() {
        return model;
    }

    
    // 코사인 유사도 계산
    public double cosineSimilarity(INDArray vector1, INDArray vector2) {
        double dotProduct = Nd4j.getBlasWrapper().dot(vector1, vector2);
        double magnitude1 = vector1.norm2Number().doubleValue();
        double magnitude2 = vector2.norm2Number().doubleValue();
        return dotProduct / (magnitude1 * magnitude2);
    }

    // 게임 태그를 벡터로 변환
    public Map<String, List<INDArray>> convertGameTagsToVectors(Map<String, List<String>> gameTags) {
        Map<String, List<INDArray>> convertToVectorsMap = new HashMap<>();
        for (String key : gameTags.keySet()) {
            List<String> tags = gameTags.get(key);
            List<INDArray> vectors = new ArrayList<>();
            for (String tag : tags) {
                // Word2Vec 모델을 사용하여 태그를 벡터로 변환
                INDArray vector = model.getWordVectorMatrix(tag);
                vectors.add(vector);
            };
            convertToVectorsMap.put(key,vectors);
        };
        return convertToVectorsMap;
    };

    // 장바구니 태그들 벡터로 변환
    public List<INDArray> convertBasketTagToVectors(List<String> basketTags){
        List<INDArray> vectors = new ArrayList<>();
        for (String tag : basketTags) {
            // Word2Vec 모델을 사용하여 태그를 벡터로 변환
            INDArray vector = model.getWordVectorMatrix(tag);
            vectors.add(vector);
        };
        return vectors;
    };

    // basket 태그들과 다른 게임들 벡터 유사도 평균 계산 후, 게임 순으로 정렬해서 리스트화
    public List<String> calculateSimilarities(List<INDArray> basketVectorsList, Map<String, List<INDArray>> otherVectorsMap) {

        List<String> sortedGameList = new ArrayList<>();
        // 다른 게임들과의 유사도 계산
        Map<String, Double> gameSimilarityMap = new HashMap<>();
        for (String game : otherVectorsMap.keySet()) {
            List<INDArray> gameVectors = otherVectorsMap.get(game);
            double similaritySum = 0.0;
            for (INDArray vector1 : basketVectorsList) {
                double maxSimilarity = -1.0;
                for (INDArray vector2 : gameVectors) {
                    double similarity = cosineSimilarity(vector1, vector2);
                    if (similarity > maxSimilarity) {
                        maxSimilarity = similarity;
                    }
                }
                similaritySum += maxSimilarity;
            }
            gameSimilarityMap.put(game, similaritySum);
        }

        // 유사도 높은 순으로 게임을 정렬
        sortedGameList.addAll(gameSimilarityMap.keySet());
        sortedGameList.sort((game1, game2) -> Double.compare(gameSimilarityMap.get(game2), gameSimilarityMap.get(game1)));

        return sortedGameList;
    }

}
