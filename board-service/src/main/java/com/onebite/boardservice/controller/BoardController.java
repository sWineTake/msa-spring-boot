package com.onebite.boardservice.controller;

import com.onebite.boardservice.domain.Board;
import com.onebite.boardservice.dto.CreateBoardRequestDto;
import com.onebite.boardservice.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }


    @PostMapping
    public ResponseEntity<Board> createBoard(@RequestBody CreateBoardRequestDto dto) {
        boardService.create(dto);
        return ResponseEntity.ok().build();
    }

}
