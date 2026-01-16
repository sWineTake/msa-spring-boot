package com.onebite.boardservice.controller;

import com.onebite.boardservice.domain.Board;
import com.onebite.boardservice.dto.BoardResponseDto;
import com.onebite.boardservice.dto.CreateBoardRequestDto;
import com.onebite.boardservice.service.BoardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<Board> createBoard(@RequestBody CreateBoardRequestDto dto) {
        boardService.create(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> getBoard(@PathVariable("boardId") Long boardId) {
        BoardResponseDto board = boardService.getBoard(boardId);
        return ResponseEntity.ok().body(board);
    }


    @GetMapping("/all")
    public ResponseEntity<List<BoardResponseDto>> getAllBoards() {
        List<BoardResponseDto> allBoards = boardService.getAllBoards();
        return ResponseEntity.ok().body(allBoards);
    }
}
