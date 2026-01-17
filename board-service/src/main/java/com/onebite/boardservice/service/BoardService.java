package com.onebite.boardservice.service;

import com.onebite.boardservice.client.PointClient;
import com.onebite.boardservice.client.UserClient;
import com.onebite.boardservice.domain.Board;
import com.onebite.boardservice.domain.BoardsRepository;
import com.onebite.boardservice.dto.BoardResponseDto;
import com.onebite.boardservice.dto.CreateBoardRequestDto;
import com.onebite.boardservice.dto.UserResponseDto;
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

    // @Transactional
    public void create(CreateBoardRequestDto dto) {

        // 게시글 저장을 성공했는지 판단하는 플래그
        boolean isBoardCreated = false;
        Long savedBoardId = null;

        // 포인트 차감을 성공했는 지 판단하는 플래그
        boolean isPointDeduct = false;

        try {

            // 게시글 작성 전 100 포인트 차감
            pointClient.deductPoints(dto.getUserId(), 100);
            isPointDeduct = true;

            // 게시글 작성
            Board board = Board.of(dto.getTitle(), dto.getContent(), dto.getUserId());
            Board savedBoard = boardsRepository.save(board);
            savedBoardId = savedBoard.getBoardId();
            isBoardCreated = true;

            // 게시글 작성 완료 후 작성자에게 활동 점수 10점 부여
            userClient.addActivityScore(dto.getUserId(), 10);

        } catch (Exception e) {

            if (isBoardCreated) {
                // 게시글 작성 완료되었다면 보상 트랜잭션 => 게시글 삭제
                this.boardsRepository.deleteById(savedBoardId);
            }

            if (isPointDeduct) {
                // 저장된 포인트 보상 트랜잭션 => 포인트 삭제
                this.pointClient.addPoints(dto.getUserId(), 100);
            }

            // 실패 응답으로 처리하기 위해 예외 던지기
            throw e;

        }

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
