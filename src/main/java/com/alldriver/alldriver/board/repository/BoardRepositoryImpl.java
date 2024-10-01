package com.alldriver.alldriver.board.repository;


import com.alldriver.alldriver.board.dto.response.BoardFindResponseDto;
import com.alldriver.alldriver.board.dto.query.BoardSearchCondition;


import com.alldriver.alldriver.board.dto.response.QBoardFindResponseDto;
import com.alldriver.alldriver.user.domain.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.alldriver.alldriver.board.domain.QBoard.board;
import static com.alldriver.alldriver.board.domain.QBoardBookmark.boardBookmark;
import static com.alldriver.alldriver.board.domain.QCar.car;
import static com.alldriver.alldriver.board.domain.QCarBoard.*;
import static com.alldriver.alldriver.board.domain.QJob.job;
import static com.alldriver.alldriver.board.domain.QJobBoard.jobBoard;
import static com.alldriver.alldriver.board.domain.QLocationBoard.locationBoard;
import static com.alldriver.alldriver.board.domain.QMainLocation.mainLocation;
import static com.alldriver.alldriver.board.domain.QSubLocation.subLocation;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardQueryDslRepository{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<BoardFindResponseDto> search(BoardSearchCondition condition, Pageable pageable, String userId) {
        QUser user1 = new QUser("user1");
        QUser user2 = new QUser("user2");

        NumberExpression<Integer> bookmarked = new CaseBuilder()
                .when(user2.userId.eq(userId))
                .then(1)
                .otherwise(0);
        JPAQuery<BoardFindResponseDto> query = jpaQueryFactory.
                select(new QBoardFindResponseDto(
                        board.id,
                        board.title,
                        user1.userId,
                        board.createdAt,
                        bookmarked)
                ).
                from(board).
                leftJoin(board.user, user1).
                leftJoin(board.boardBookmarks, boardBookmark).
                leftJoin(boardBookmark.user, user2);
        JPAQuery<Long> countQuery = jpaQueryFactory.select(board.count()).from(board);

        List<Long> carIds = condition.getCarIds();
        List<Long> jobIds = condition.getJobIds();
        Long mainLocationId = condition.getMainLocationId();
        List<Long> subLocationIds = condition.getSubLocationIds();
        if(carIds != null){
            query.leftJoin(board.carBoards, carBoard);
            query.leftJoin(carBoard.car, car);
            countQuery.leftJoin(board.carBoards, carBoard);
            countQuery.leftJoin(carBoard.car, car);
        }
        if(jobIds!= null){
            query.leftJoin(board.jobBoards, jobBoard);
            query.leftJoin(jobBoard.job, job);
            countQuery.leftJoin(board.jobBoards, jobBoard);
            countQuery.leftJoin(jobBoard.job, job);
        }
        if(subLocationIds != null){
            if(mainLocationId != null) {
                query.leftJoin(board.locationBoards, locationBoard).
                        leftJoin(locationBoard.subLocation, subLocation).
                        leftJoin(subLocation.mainLocation, mainLocation);
                countQuery.leftJoin(board.locationBoards, locationBoard).
                        leftJoin(locationBoard.subLocation, subLocation).
                        leftJoin(subLocation.mainLocation, mainLocation);
            }
            else{
                query.leftJoin(board.locationBoards, locationBoard).
                        leftJoin(locationBoard.subLocation, subLocation);
                countQuery.leftJoin(board.locationBoards, locationBoard).
                        leftJoin(locationBoard.subLocation, subLocation);
            }
        }
        else{
            if(mainLocationId != null) {
                query.leftJoin(board.locationBoards, locationBoard).
                        leftJoin(locationBoard.subLocation, subLocation).
                        leftJoin(subLocation.mainLocation, mainLocation);
                countQuery.leftJoin(board.locationBoards, locationBoard).
                        leftJoin(locationBoard.subLocation, subLocation).
                        leftJoin(subLocation.mainLocation, mainLocation);
            }
        }
        query.where(
                board.deleted.eq(false),
                carsIn(carIds),
                jobsIn(jobIds),
                mainLocationEq(mainLocationId),
                subLocationsIn(subLocationIds)
        ).orderBy(bookmarked.desc(), board.createdAt.desc()).
                offset(pageable.getOffset()).
                limit(pageable.getPageSize());

        countQuery.where(
                board.deleted.eq(false),
                carsIn(carIds),
                jobsIn(jobIds),
                mainLocationEq(mainLocationId),
                subLocationsIn(subLocationIds));

        List<BoardFindResponseDto> result = query.fetch();

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }



    private Predicate carsIn(List<Long> carIds) {
        return carIds!=null ?  car.id.in(carIds) : null;
    }
    private Predicate jobsIn(List<Long> jobIds) {
        return jobIds!=null ?  job.id.in(jobIds) : null;
    }
    private Predicate mainLocationEq(Long mainLocationId) {
        return mainLocationId != null ?  mainLocation.id.eq(mainLocationId) : null;
    }
    private Predicate subLocationsIn(List<Long> subLocationIds) {
        return subLocationIds!=null ?  subLocation.id.in(subLocationIds) : null;
    }
}
