package com.alldriver.alldriver.board.repository;

import com.alldriver.alldriver.board.document.BoardDocument;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import org.springframework.stereotype.Repository;


@Repository
public interface BoardSearchRepository extends ElasticsearchRepository<BoardDocument, Long> {

    @Query("{\"multi_match\": { " +
            "\"query\": \"?0\", " +
            "\"fields\": [\"title\", \"category\", \"cars\", \"jobs\", \"locations\", \"companyLocation\"] " +
            "}}")
    Page<BoardDocument> search(String keyword, Pageable pageable);

}
