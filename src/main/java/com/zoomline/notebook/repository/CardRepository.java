package com.zoomline.notebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zoomline.notebook.model.Card;

import java.util.List;

/** Handles the search queries related to the Cards table. */
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    /** Finds a list of cards belonging to a specific Topic ID.
     * @param topicId The ID of the topic in which to search.
     * @return The list of cards belonging to the topic.
     */
    List<Card> findAllByTopicId(Long topicId);

    /** Find a card by its ID the ID of the topic to which it belongs.
     * @param cardId The id of the card in which to search.
     * @param topicId The topic ID of the topic in which to card belongs.
     * @return The card that matches the Card ID and belonging to the Topic matching the Topic ID.
     */
    Card findByIdAndTopicId(Long cardId, Long topicId);

    Card findByQuestionAndTopicId(String question, Long topicId);
}
