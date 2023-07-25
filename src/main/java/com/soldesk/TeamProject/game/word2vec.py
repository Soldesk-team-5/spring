import numpy as np
import pandas as pd
from gensim.models import Word2Vec

df = pd.read_csv('./steam_game_data_forPjt9.csv', encoding="utf-8")

game_data = []
game_tags = []
for i in range(len(df)):
    list_1 = []
    list_1.append(df.loc[i, 'name'])
    list_1.append(list(set(df.loc[i, 'tag'].split(',') + df.loc[i, 'genre'].split(','))))
    game_tags.append(df.loc[i, 'tag'].split(','))
    game_data.append(list_1)

# Word2Vec 모델을 학습
model = Word2Vec(sentences=game_tags, vector_size=100, window=5, min_count=1, sg=1)

# 각 게임의 태그들을 해당하는 벡터로 변환
game_tag_vectors = {}
for idx, tags in enumerate(game_tags):
    game_tag_vectors[f'{idx}'] = [model.wv[tag] for tag in tags]

# 게임들 간의 태그 벡터를 비교하여 유사도를 측정
def cosine_similarity(vector1, vector2):
    dot_product = sum(a * b for a, b in zip(vector1, vector2))
    magnitude1 = sum(a ** 2 for a in vector1) ** 0.5
    magnitude2 = sum(b ** 2 for b in vector2) ** 0.5
    return dot_product / (magnitude1 * magnitude2)

# Game 0과 나머지 게임들의 평균 태그 벡터 유사도를 계산
avg_similarity_scores = []
for idx in range(1, len(game_tag_vectors)):
    similarities = cosine_similarity(game_tag_vectors['0'], game_tag_vectors[str(idx)])
    avg_similarity_scores.append((idx, similarities))

# 평균 태그 벡터 유사도를 유사도가 높은 순으로 정렬
avg_similarity_scores = sorted(avg_similarity_scores, key=lambda x: x[1][0], reverse=True)

print(f"Game 0과 나머지 게임들의 평균 태그 벡터 유사도:")
for idx, similarity_score in avg_similarity_scores:
    print(f"Game 0과 Game {idx}의 태그 벡터 유사도: {similarity_score[0]}")