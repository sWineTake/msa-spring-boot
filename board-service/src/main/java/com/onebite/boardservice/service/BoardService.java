package com.onebite.boardservice.service;

import com.onebite.boardservice.domain.Board;
import com.onebite.boardservice.domain.BoardsRepository;
import com.onebite.boardservice.dto.CreateBoardRequestDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    private final BoardsRepository boardsRepository;

    public BoardService(BoardsRepository boardsRepository) {
        this.boardsRepository = boardsRepository;
    }

    @Transactional
    public void create(CreateBoardRequestDto dto) {
        Board board = Board.of(dto.getTitle(), dto.getContent(), dto.getUserId());
        boardsRepository.save(board);
    }

}
