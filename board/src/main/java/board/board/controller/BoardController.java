package board.board.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import board.board.dto.BoardDto;
import board.board.dto.BoardFileDto;
import board.board.service.BoardService;

@Controller
public class BoardController {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BoardService boardService;
	
	//게시글 목록
	@RequestMapping("/board/openBoardList.do")
	public ModelAndView openBoardList() throws Exception{
		log.debug("openBoardList");
		
		ModelAndView mv = new ModelAndView("/board/boardList");
		
		List<BoardDto> list = boardService.selectBoardList();
		mv.addObject("list",list);
		
		return mv;
	}
	
	//게시글 등록 화면 호출
	@RequestMapping("/board/openBoardWrite.do")
	public String openBoardWrite() throws Exception {
		return "/board/boardWrite";
	}
	
	//게시글 등록
	@RequestMapping("/board/insertBoard.do")
	public String insertBoard(BoardDto board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
		boardService.insertBoard(board, multipartHttpServletRequest);
		return "redirect:/board/openBoardList.do";
	}
	
	//게시글 상세화면
	@RequestMapping("/board/openBoardDetail.do")
	public ModelAndView openBoardDetail(@RequestParam int boardIdx) throws Exception {
		ModelAndView mv = new ModelAndView("/board/boardDetail");
		
		BoardDto board = boardService.selectBoardDetail(boardIdx);
		mv.addObject("board", board);
		
		return mv;
	}
	
	//게시글 수정
	@RequestMapping("/board/updateBoard.do")
	public String updateBoard(BoardDto board) throws Exception {
		boardService.updateBoard(board);
		return "redirect:/board/openBoardList.do";
	}
	
	//게시글 삭제
	@RequestMapping("/board/deleteBoard.do")
	public String deleteBoard(int boardIdx) throws Exception{
		boardService.deleteBoard(boardIdx);
		return "redirect:/board/openBoardList.do";
	}
	
	//게시글에 첨부한 파일 다운로드
	@RequestMapping("/board/downloadBoardFile.do")
	public void downloadBoardFile(@RequestParam int idx, @RequestParam int boardIdx, HttpServletResponse response) throws Exception{
		
		BoardFileDto boardFile = boardService.selectBoardFileInformation(idx, boardIdx); // 데이터베이스에서 선택된 파일의 정보를 조회
		
		if(ObjectUtils.isEmpty(boardFile) == false) {
			String fileName = boardFile.getOriginalFileName();
			
			byte[] files = FileUtils.readFileToByteArray(new File(boardFile.getStoredFilePath()));
			
			response.setContentType("application/octet-stream"); // 파일 유형 설정
			response.setContentLength(files.length);  // 파일 길이 설정

			//데이터형식/성향 설정 (attachment: 첨부파일)
			response.setHeader("Content-Disposition", "attachment; fileName=\""+URLEncoder.encode(fileName, "UTF-8")+"\";");
			response.setHeader("Content-Transfer-Encoding", "binary"); // 내용물 인코딩 방식 설정
			
			response.getOutputStream().write(files); // 버퍼의 출력스트림을 출력
			response.getOutputStream().flush(); // 버퍼에 남아있는 출력스트림을 출력
			response.getOutputStream().close(); // 출력스트림 닫기
		}
		
	}
}
