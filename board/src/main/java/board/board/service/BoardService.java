package board.board.service;

import java.util.List;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import board.board.dto.BoardDto;
import board.board.dto.BoardFileDto;

public interface BoardService {
	
	List<BoardDto> selectBoardList() throws Exception; //게시판 목록
	
	void insertBoard(BoardDto board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception; //게시글 등록
	
	BoardDto selectBoardDetail(int boardIdx) throws Exception;
	
	void updateBoard(BoardDto board) throws Exception; //게시글 수정
	
	void deleteBoard(int boardIdx) throws Exception; //게시글 삭제\
	
	BoardFileDto selectBoardFileInformation(int idx, int boardIdx) throws Exception;

}
