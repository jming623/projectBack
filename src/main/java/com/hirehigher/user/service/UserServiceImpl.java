package com.hirehigher.user.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hirehigher.command.InsertQuestionPageVO;
import com.hirehigher.command.JobBoardVO;
import com.hirehigher.command.LoginCountVO;
import com.hirehigher.command.UserVO;
import com.hirehigher.user.mapper.UserMapper;
import com.hirehigher.util.JobCriteria;

@Service("userService")
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	
	//로그인
	@Override
	public UserVO login(UserVO vo) {
		
		return userMapper.login(vo);
	}
	
	//카카오 로그인
	@Override
	public UserVO kakaoLogin(String userId) {
		
		return userMapper.kakaoLogin(userId);
	}
		
	//생년월일로 아이디찾기
	@Override
	public ArrayList<UserVO> findIdA(String userName, String birthDay) {
		
		System.out.println("서비스"+userName+ ", "+birthDay);
		
		return userMapper.findIdA(userName, birthDay);
	}
	
	//전화번호로 아이디찾기
	@Override
	public ArrayList<UserVO> findIdB(String userName, String userCellNum) {
		
		return userMapper.findIdB(userName, userCellNum);
	}
	
	//아이디 중복체크
	@Override
	public int idCheck(String userId) {
		
		return userMapper.idCheck(userId);
	}

	//닉네임 중복체크
	@Override
	public int nickNameCheck(String nickName) {
		
		return userMapper.nickNameCheck(nickName);
	}

	//이메일 중복체크
	@Override
	public int emailCheck(String userEmail) {
	
		return userMapper.emailCheck(userEmail);
	}

	//회원가입
	@Override
	public int join(UserVO vo) {
		
		return userMapper.join(vo);
	}

	//마이페이지 유저정보 불러오기
	@Override
	public UserVO getUserInfo(String userId) {
		
		return userMapper.getUserInfo(userId);
	}
	
	//비밀번호 찾기
	@Override
	public String findPw(String userId, String userName, String userEmail) {
		
		return userMapper.findPw(userId, userName, userEmail);
	}
	
	//회원정보 수정
	@Override
	public int modify(UserVO vo) {
		
		return userMapper.modify(vo);
	}
	
	//마이페이지 채용공고 게시글 불러오기
	@Override
	public ArrayList<JobBoardVO> getJobBoardList(int pageNum, int amount, String userId) {
		
		return userMapper.getJobBoardList(pageNum, amount, userId);
	}
	
	//마이페이지 채용공고 총게시글 수 불러오기
	@Override
	public int getJobBoardTotal(String userId) {
		
		return userMapper.getJobBoardTotal(userId);
	}

	@Override
	public ArrayList<InsertQuestionPageVO> getFaqBoardList(int pageNum, int amount, String userId) {
		
		return userMapper.getFaqBoardList(pageNum, amount, userId);
	}

	@Override
	public int getFaqBoardTotal(String userId) {
		
		return userMapper.getFaqBoardTotal(userId);
	}
	
	//이메일 정보가 포함된 카카오회원가입
	@Override
	public int kakaoJoin1(UserVO vo) {
		
		return userMapper.kakaoJoin1(vo);
	}
	
	//이메일 정보가 포함되지 않은 카카오회원가입
	@Override
	public int kakaoJoin2(UserVO vo) {
		
		return userMapper.kakaoJoin2(vo);
	}
	
	//회원가입 성공시 loginCount테이블에 유저정보 등록
	@Override
	public void registLoginCount(String userId) {
		
		userMapper.registLoginCount(userId);
	}
	
	//로그인이 제한된 시간불러오기
	@Override
	public LoginCountVO getloginLimitTime(String userId) {

		return userMapper.getloginLimitTime(userId);
	}

	//로그인 제한여부를 'N'으로 바꿔주는 메서드
	@Override
	public void setLoginLimitN(String userId) {
		
		userMapper.setLoginLimitN(userId);
	}
	
	//로그인 제한여부 불러오기
	@Override
	public LoginCountVO getLoginLimitStatus(String userId) {
		
		return userMapper.getLoginLimitStatus(userId);
	}

	//로그인 시도횟수를 0으로 리셋
	@Override
	public void resetLoginTryNum(String userId) {
		
		userMapper.resetLoginTryNum(userId);
	}
	
	//로그인을 실패한 마지막 시간 불러오기
	@Override
	public LoginCountVO getloginFailTime(String userId) {
		
		return userMapper.getloginFailTime(userId);
	}
	
	//로그인 시도 횟수 불러오기
	@Override
	public LoginCountVO getLoginTryNum(String userId) {
		
		return userMapper.getLoginTryNum(userId);
	}

	//로그인 시도횟수 +1
	@Override
	public void plusLoginTryNum(String userId, int addLoginTryNum) {
		
		userMapper.plusLoginTryNum(userId, addLoginTryNum);
	}

	//로그인 실패한 마지막시간 리셋
	@Override
	public void resetLoginFailTime(String userId) {
		
		userMapper.resetLoginFailTime(userId);
	}

	//로그인 제한여부 'Y'으로 변경
	@Override
	public void setLoginLimitY(String userId) {
		
		userMapper.setLoginLimitY(userId);
	}

	//로그인을 제한한시간 리셋
	@Override
	public void resetLoginLimitTime(String userId) {
		
		userMapper.resetLoginLimitTime(userId);
	}




	

	

}
