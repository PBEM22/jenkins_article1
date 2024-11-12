package article1be.board.service;

import article1be.amazonS3.service.AmazonS3Service;
import article1be.board.dto.BoardDTO;
import article1be.board.dto.PictureDTO;
import article1be.board.dto.RequestBoard;
import article1be.board.dto.RequestPicture;
import article1be.board.entity.Board;
import article1be.board.entity.Picture;
import article1be.board.repository.BoardRepository;
import article1be.board.repository.PictureRepository;
import article1be.reply.entity.Reply;
import article1be.reply.repository.ReplyRepository;
import article1be.security.util.SecurityUtil;
import article1be.user.entity.UserAuth;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private static final Logger log = LoggerFactory.getLogger(BoardService.class);
    private final BoardRepository boardRepository;
    private final PictureRepository pictureRepository;
    private final ReplyRepository replyRepository;
    private final AmazonS3Service amazonS3Service;

    // 게시글 목록 조회
    public List<BoardDTO> getBoardList(boolean isBlind) {
        if (!isBlind) {
            // 모든 이미지 목록을 가져옵니다.
            List<PictureDTO> pictureList = pictureRepository.findAll()
                    .stream()
                    .map(picture -> PictureDTO.builder()
                            .pictureSeq(picture.getPictureSeq())
                            .pictureBoardSeq(picture.getPictureBoardSeq())
                            .pictureOriginName(picture.getPictureOriginName())
                            .pictureChangedName(picture.getPictureChangedName())
                            .pictureUrl(picture.getPictureUrl())
                            .pictureType(picture.getPictureType())
                            .regDate(picture.getRegDate())
                            .delDate(picture.getDelDate())
                            .pictureIsDelete(picture.isPictureIsDelete())
                            .build())
                    .collect(Collectors.toList());

            // 게시글 목록을 가져옵니다.
            List<BoardDTO> boardDTOList = boardRepository.findByBoardIsBlindFalse()
                    .stream()
                    .map(board -> {
                        // 현재 게시글의 ID와 일치하는 이미지 목록을 필터링합니다.
                        List<PictureDTO> imagesForBoard = pictureList.stream()
                                .filter(picture -> picture.getPictureBoardSeq() == board.getBoardSeq()) // == 연산자로 비교
                                .collect(Collectors.toList());

                        // 게시글 DTO를 생성하고 이미지 목록을 추가합니다.
                        return BoardDTO.builder()
                                .boardSeq(board.getBoardSeq())
                                .userSeq(board.getUserSeq())
                                .boardTitle(board.getBoardTitle())
                                .boardContent(board.getBoardContent())
                                .regDate(board.getRegDate())
                                .upDate(board.getUpDate())
                                .delDate(board.getDelDate())
                                .boardIsBlind(board.getBoardIsBlind())
                                .boardIsNotice(board.getBoardIsNotice())
                                .boardPictureList(imagesForBoard) // 이미지 목록 추가
                                .build();
                    })
                    .collect(Collectors.toList());

            return boardDTOList;
        } else {
            List<BoardDTO> boardDTOList = boardRepository.findByBoardIsBlindTrue()
                    .stream()
                    .map(board -> BoardDTO.builder()
                            .boardSeq(board.getBoardSeq())
                            .userSeq(board.getUserSeq())
                            .boardTitle(board.getBoardTitle())
                            .boardContent(board.getBoardContent())
                            .regDate(board.getRegDate())
                            .upDate(board.getUpDate())
                            .delDate(board.getDelDate())
                            .boardIsBlind(board.getBoardIsBlind())
                            .boardIsNotice(board.getBoardIsNotice())
                            .build())
                    .collect(Collectors.toList());

            return boardDTOList;
        }
    }

    // 게시글 조회
    public BoardDTO getBoard(long boardSeq) {
        Optional<Board> optionalBoard = boardRepository.findById(boardSeq);

        if (optionalBoard.isPresent()) {

            Board board = optionalBoard.get();

            // 게시물 번호에 해당하는 사진들을 호출
            List<PictureDTO> pictureDTOList = pictureRepository.findByPictureBoardSeq(boardSeq)
                    .stream()
                    .map(picture -> PictureDTO.builder()
                            .pictureSeq(picture.getPictureSeq())
                            .pictureBoardSeq(picture.getPictureBoardSeq())
                            .pictureOriginName(picture.getPictureOriginName())
                            .pictureChangedName(picture.getPictureChangedName())
                            .pictureUrl(picture.getPictureUrl())
                            .pictureType(picture.getPictureType())
                            .regDate(picture.getRegDate())
                            .delDate(picture.getDelDate())
                            .pictureIsDelete(picture.isPictureIsDelete())
                            .build())
                    .collect(Collectors.toList());

            return BoardDTO.builder()
                    .boardSeq(board.getBoardSeq())
                    .userSeq(board.getUserSeq())
                    .boardTitle(board.getBoardTitle())
                    .boardContent(board.getBoardContent())
                    .boardPictureList(pictureDTOList)
                    .regDate(board.getRegDate())
                    .upDate(board.getUpDate())
                    .delDate(board.getDelDate())
                    .boardIsBlind(board.getBoardIsBlind())
                    .boardIsNotice(board.getBoardIsNotice())
                    .build();
        } else {
            return null;
        }
    }

    // 게시글 생성
    @Transactional
    public Board createBoard(RequestBoard newBoard) throws IOException {

        boolean isNotice = false;

        if (SecurityUtil.getCurrentUserAuthorities().equals(UserAuth.ADMIN)) {
            isNotice = true;
        }

        Board board = new Board().create(
                SecurityUtil.getCurrentUserSeq(),
                newBoard.getBoardTitle(),
                newBoard.getBoardContent(),
                isNotice
        );

        // 1. DB(Board)에 데이터 저장
        Board result = boardRepository.save(board);

        if (newBoard.getImageList() != null && !newBoard.getImageList().isEmpty()) {
            for (MultipartFile image : newBoard.getImageList()) {
                // 2. Amazon S3 버킷에 이미지 저장
                AmazonS3Service.MetaData metaData = amazonS3Service.upload(image);

                // 3. DB(Picture)에 데이터 저장
                RequestPicture requestPicture = new RequestPicture();
                requestPicture.setPictureBoardSeq(board.getBoardSeq());
                requestPicture.setPictureOriginName(metaData.getOriginalFileName());
                requestPicture.setPictureChangedName(metaData.getChangeFileName());
                requestPicture.setPictureUrl(metaData.getUrl());
                requestPicture.setPictureType(metaData.getType());

                Picture picture = new Picture();
                picture.create(requestPicture);

                pictureRepository.save(picture);
            }
        }

        return result;
    }

    // 게시글 삭제
    @Transactional
    public boolean deleteBoard(Long boardSeq) {
        // 0. 작성자 여부 확인
        Optional<Board> board = boardRepository.findById(boardSeq);

        // 작성자가 아닌 경우
        if (!Objects.equals(board.get().getUserSeq(), SecurityUtil.getCurrentUserSeq())) {
            return false;
        }
        // 어드민이 아닌 경우
        else if (SecurityUtil.getCurrentUserAuthorities().equals(UserAuth.USER)) {
            return false;
        }

        // 1. DB(BOARD)에 해당 게시글 블라인드 처리, 삭제일 등록
        board.get().setBlind();
        board.get().setDelDate();
        boardRepository.save(board.get());

        // 2. DB(PICTURE)에 해당 게시글 번호로 된 모든 데이터 블라인드 처리
        List<Picture> pictureList = pictureRepository.findByPictureBoardSeq(boardSeq);

        // 각 Picture 객체의 블라인드 처리
        for (Picture picture : pictureList) {
            picture.setBlind();
            picture.setDelDate();
            pictureRepository.save(picture); // 변경된 Picture 객체를 저장
        }

        // 3. DB(Reply)에 해당 댓글 모두 블라인드 처리
        List<Reply> replyList = replyRepository.findByBoardSeqAndReplyIsBlindFalse(boardSeq);

        for (Reply reply : replyList) {
            reply.setBlind();
            reply.setDelDate();
            replyRepository.save(reply);
        }

        // 삭제 성공시 true
        return true;
    }

    // 게시글 수정
    @Transactional
    public Board upDateBoard(Long boardSeq, RequestBoard modData) throws IOException {
        Optional<Board> optionalBoard = boardRepository.findById(boardSeq);

        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get(); // Board 객체를 꺼냄
            board.update(modData); // update 메소드 호출

            if (modData.getImageList() != null && !modData.getImageList().isEmpty()) {
                for (MultipartFile image : modData.getImageList()) {
                    AmazonS3Service.MetaData metaData = amazonS3Service.upload(image);
                    log.info("업로드 성공");

                    RequestPicture requestPicture = new RequestPicture();
                    requestPicture.setPictureBoardSeq(board.getBoardSeq());
                    requestPicture.setPictureOriginName(metaData.getOriginalFileName());
                    requestPicture.setPictureChangedName(metaData.getChangeFileName());
                    requestPicture.setPictureUrl(metaData.getUrl());
                    requestPicture.setPictureType(metaData.getType());

                    Picture picture = new Picture();
                    picture.create(requestPicture);
                    pictureRepository.save(picture);
                    log.info("DB 저장 성공, {}", picture.getPictureBoardSeq());
                }
            }

            return boardRepository.save(board); // 변경사항 저장
        } else {
            throw new EntityNotFoundException("게시글을 찾을 수 없습니다."); // 게시글이 없을 경우 예외 처리
        }
    }
}
