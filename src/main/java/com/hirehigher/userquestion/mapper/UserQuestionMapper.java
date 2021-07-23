package com.hirehigher.userquestion.mapper;

import java.util.ArrayList;

import com.hirehigher.command.FaqListPageVO;
import com.hirehigher.command.InsertQuestionPageVO;
import com.hirehigher.command.QuestionDetailVO;

public interface UserQuestionMapper {

	public ArrayList<FaqListPageVO> getFaqList(); //모든 게시물 가져오기
	public int insertRegist(InsertQuestionPageVO vo);
	public InsertQuestionPageVO getUpdateInfo(int bno); //수정 게시판 데이터 가져오기
	public int updateData(InsertQuestionPageVO vo); //수정게시판 등록
	public ArrayList<InsertQuestionPageVO> getMtomList(); //문의내역 리스트
	public int answerData(InsertQuestionPageVO vo); //답변 등록
	public int updatedelete(int bno); //삭제
}
