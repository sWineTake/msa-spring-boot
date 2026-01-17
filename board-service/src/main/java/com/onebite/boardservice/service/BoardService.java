package com.onebite.boardservice.service;

import com.onebite.boardservice.client.PointClient;
import com.onebite.boardservice.client.UserClient;
import com.onebite.boardservice.domain.Board;
import com.onebite.boardservice.domain.BoardsRepository;
import com.onebite.boardservice.dto.BoardResponseDto;
import com.onebite.boardservice.dto.CreateBoardRequestDto;
import com.onebite.boardservice.dto.UserResponseDto;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardsRepository boardsRepository;
    private final UserClient userClient;
    private final PointClient pointClient;

    @Transactional
    public void create(CreateBoardRequestDto dto) {

        // 게시글 작성 전 100 포인트 차감
        pointClient.deductPoints(dto.getUserId(), 100);

        // 게시글 작성
        Board board = Board.of(dto.getTitle(), dto.getContent(), dto.getUserId());
        boardsRepository.save(board);

        // 게시글 작성 완료 후 작성자에게 활동 점수 10점 부여
        userClient.addActivityScore(dto.getUserId(), 10);
    }

    public BoardResponseDto getBoard(Long boardId) {
        Board board = boardsRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        Optional<UserResponseDto> optionalUserResponseDto = userClient.fetchUser(board.getUserId());

        return new BoardResponseDto(
            board.getUserId(),
            board.getTitle(),
            board.getContent(),
            optionalUserResponseDto.orElse(null)
        );

    }

    public List<BoardResponseDto> getAllBoards() {
        List<Board> boards = boardsRepository.findAll();

        List<Long> userIds = boards.stream().map(Board::getUserId).distinct().toList();

        List<UserResponseDto> userResponseDtos = userClient.fetchUsersByIds(userIds);

        Map<Long, UserResponseDto> userResponseDtoMap = new HashMap<>();
        for (UserResponseDto dto : userResponseDtos) {
            userResponseDtoMap.put(dto.getUserId(), dto);
        }

        return boards.stream().map(obj ->
                new BoardResponseDto(
                    obj.getBoardId(),
                    obj.getTitle(),
                    obj.getContent(),
                    userResponseDtoMap.get(obj.getUserId()
                )
            )
        ).toList();

    }

}
