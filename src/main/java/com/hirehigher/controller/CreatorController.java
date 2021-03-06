package com.hirehigher.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hirehigher.command.BackgroundImgVO;
import com.hirehigher.command.CreatorPageVO;
import com.hirehigher.command.CreatorVO;
import com.hirehigher.command.ProfileImgVO;
import com.hirehigher.command.UserVO;
import com.hirehigher.command.WorkBoardVO;
import com.hirehigher.creator.service.CreatorService;
import com.hirehigher.util.CreatorCriteria;
import com.hirehigher.util.CreatorPagingVO;


@Controller
@RequestMapping("/creator")
public class CreatorController {

	private int userType = 0;
	
	@Autowired
	@Qualifier("creatorService")
	private CreatorService creatorService;

	
	// 제작자 신청 화면
	@RequestMapping("/creatorApply")
	public String creatorApply() {
		
		
		return "/creator/creatorApply";
	}
	
	// 제작자 신청 요청
	@RequestMapping("applyForm")
	public String applyForm(CreatorVO creatorVO,
							HttpSession session,
							RedirectAttributes RA) {
		
		
		
		if(userType == 0) {
			
			
			/*------------------------제작자 신청--------------------------------*/
			UserVO userVO = (UserVO)session.getAttribute("userVO"); // session에 있는 userVO를 얻음
			creatorVO.setCreatorId(userVO.getUserId());
			String creatorId = creatorVO.getCreatorId(); // creatorId 변수에 creatorVO의 creatorId를 저장
			
			int result = creatorService.apply(creatorVO); // apply함수 결과를 result 변수에 저장
			
			if(result == 1) { // 요청 성공
				RA.addFlashAttribute("msg", "판매자 신청이 완료 되었습니다.");
			} else { // 요청 실패
				RA.addFlashAttribute("msg", "등록에 실패했습니다. 다시 시도해주세요");
			}
			
			
			/*------------------------userType 변경--------------------------------*/
			userVO.setUserType(1); // userVO의 userType 값을 1로 저장 (1인 경우 제작자)
			userType = userVO.getUserType(); // userType 변수에 userVO의 userType을 저장
			creatorService.apply1(userVO); // apply1 함수 결과
			
			
			/*------------------------creatorPage 기본 내용--------------------------------*/
			String pageId = userVO.getUserId(); // pageId 변수에 userVO의 userId를 저장
			
			String creatorEmail = userVO.getUserEmail(); // creatorEmail 변수에 userVO의 userEmail을 저장
			
			String creatorNick = userVO.getNickName(); // creatorNick 변수에 userVO의 nickname를 저장 
			
			String aboutMe = ""; // aboutMe 변수에 공백 저장
			
			String instaPath = ""; // instaPath 변수에 공백저장
			
			// DB 작업
			CreatorPageVO pageVO = new CreatorPageVO(pageId, creatorEmail, aboutMe, creatorNick, instaPath);
					
			creatorService.pageRegist(pageVO); // pageRegist 함수 결과
			
			
			/*------------------------profile이미지 기본 내용--------------------------------*/
			
			String profileId = userVO.getUserId(); // profileId 변수에 userVO의 userId를 저장
			
			// profilePath 변수에 업로드 경로 저장
			String profilePath = "C:\\Users\\woohyun\\Desktop\\Programming\\course\\sts-bundle\\workspace\\HireHigher\\src\\main\\webapp\\resources\\img\\creatorProfile";
			
			String profileLoca = "creatorProfile"; // profileLoca 변수에 폴더 경로 저장
				
			String profileName = "52822e5099fa4d9cb73a0636178393d2.png"; // profileName 변수에 변경된 파일명 저장
		
			String profileReal = "profile.png"; // profileReal 변수에 실제 파일명 저장
			
			// DB 작업
			ProfileImgVO profileVO = new ProfileImgVO(profileId, profilePath, profileLoca, profileName, profileReal);
			
			creatorService.profileRegist(profileVO);
			
			
			/*------------------------background이미지 기본 내용--------------------------------*/
			
			String backgroundId = userVO.getUserId(); // backgroundId 변수에 userVO의 userId를 저장
			
			// backgroundPath 변수에 업로드 경로 저장
			String backgroundPath = "C:\\Users\\woohyun\\Desktop\\Programming\\course\\sts-bundle\\workspace\\HireHigher\\src\\main\\webapp\\resources\\img\\creatorBackground";
			
			String backgroundLoca = "creatorBackground"; // backgroundLoca 변수에 폴더 경로 저장
			
			String backgroundName = "welcome.jpg"; // backgroundName 변수에 변경된 파일명 저장
			
			String backgroundReal = "welcome.jpg"; // backgroundReal 변수에 실제 파일명 저장
			
			// DB 작업
			BackgroundImgVO backgroundVO = new BackgroundImgVO(backgroundId, backgroundPath, backgroundLoca, backgroundName, backgroundReal);
			
			creatorService.backgroundRegist(backgroundVO);
			
			return "redirect:/creator/creatorDetail";
			
		} else {
			
			RA.addFlashAttribute("msg", "이미 판매자 신청이 완로된 계정입니다.");
			
			
			return "redirect:/creator/creatorApply";
		}
		
	}
	
	// 제작자 페이지 , 수정 페이지 화면
	@RequestMapping({"/creatorDetail", "/creatorModify"})
	public void creatorDetail(HttpSession session,
								Model model) {
		
		UserVO userVO = (UserVO)session.getAttribute("userVO"); // session에 있는 userVO를 얻음
		
		/*------------------------제작자 정보(전공,경력,수상)--------------------------------*/
		
		String creatorId = userVO.getUserId(); // creatorId 변수에 userVO의 userId를 저장
		
		CreatorVO creatorVO = creatorService.creatorDetail(creatorId); // DB 결과를 CreatorVO 객체에 저장
		model.addAttribute("creatorVO", creatorVO); // 제작자 정보 전달
		
		/*------------------------제작자 자기소개--------------------------------*/
		String pageId = userVO.getUserId(); // pageId 변수에 userVO의 userId를 저장
		
		CreatorPageVO pageVO = creatorService.pageDetail(pageId); // DB 결과를 CreatorPageVO 객체에 저장
		model.addAttribute("pageVO", pageVO); // 제작자 자기소개 정보 전달
		
//		return "/creator/creatorDetail";
	}
	
	// 제작자 페이지 프로필 이미지 조회 요청
	@ResponseBody
	@RequestMapping(value="/profileGet", method= RequestMethod.GET)
	public ProfileImgVO profileGet(HttpSession session) {
		
		UserVO userVO = (UserVO)session.getAttribute("userVO"); // session에 있는 userVO를 얻음
		String profileId = userVO.getUserId(); // profileId 변수에 profileVO의 profileId를 저장
		
		ProfileImgVO profileVO = creatorService.profileGet(profileId); // DB 결과를 ProfileImgVO 객체에 저장
		
		return profileVO;
	}
	
	// 제작자 페이지 프로필 이미지 반환
	@ResponseBody
	@RequestMapping(value="/view/{profileLoca}/{profileName:.+}") // 경로에 특수문자를 허용
	public byte[] view(@PathVariable("profileLoca") String profileLoca,
					   @PathVariable("profileName") String profileName) {
		
		byte[] result = null;
		
		try {
			
			// 파일 데이터를 바이트데이터로 변환해서 반환
			
			File file = new File(CREATOR_PROFILE_CONSTANT.UPLOAD_PATH + "\\" + profileLoca + "\\" + profileName);
		
			result = FileCopyUtils.copyToByteArray(file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	// 제작자 페이지 백그라운드 이미지 조회 요청
	@ResponseBody
	@RequestMapping(value="/backgroundGet", method = RequestMethod.GET)
	public BackgroundImgVO backgroundGet(HttpSession session) {
		
		UserVO userVO = (UserVO)session.getAttribute("userVO"); //session에 있는 userVO를 얻음
		String backgroundId = userVO.getUserId(); // backgroundId 변수에 userVO의 userId를 저장
		
		BackgroundImgVO backgroundVo = creatorService.backgroundGet(backgroundId); // DB 결과를 BackgroundImgVO 객체에 저장
		
		
		return backgroundVo;
	}
	
	// 제작자 페이지 백그라운드 이미지 반환
	@ResponseBody
	@RequestMapping(value="/view1/{backgroundLoca}/{backgoundName:.+}") // 경로에 특수문자를 허용
	public byte[] view1(@PathVariable("backgroundLoca") String backgroundLoca,
					    @PathVariable("backgoundName") String backgoundName) {
		
		byte[] result = null;
		
		try {
			
			// 파일 데이터를 바이트데이터로 변환해서 반환
			
			File file = new File(CREATOR_BACKGROUND_CONSTANT.UPLOAD_PATH + "\\" + backgroundLoca + "\\" + backgoundName);
		
			result = FileCopyUtils.copyToByteArray(file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	// 제작자 페이지 게시판 페이징
	@ResponseBody
	@RequestMapping(value="creatorPageBoardList", produces="application/json")
	public HashMap<String, Object> creatorPageBoardList(@RequestBody CreatorCriteria cri) { // cri값이 들어오지 않으면 기본생성자로 startPage는 1, amount는 7로 초기화 
		
		String userName = cri.getUserName(); // 제작자 페이지에서 넘어온 userName
		int pageNum = cri.getPageNum(); //  제작자 페이지에서 넘어온 pageNum
		int amount = cri.getAmount(); //  제작자 페이지에서 넘어온 amount
		
		HashMap<String, Object> map = new HashMap<>();
		
		ArrayList<WorkBoardVO> list = creatorService.getList(pageNum, amount, userName); // DB 결과를 ArrayList 객체에 저장
		
		map.put("list", list); // 화면단에 workBoardVO를 답은 ArrayList를 반환
		
		int total = creatorService.getTotal(userName); // DB 결과를 total 변수에 저장
		
		CreatorPagingVO pagingVO = new CreatorPagingVO(cri, total);
		
		map.put("pagingVO", pagingVO); // 화면단에 pagingVO를 반환
		
		return map;
	}

//	// 제작자 페이지 수정 화면
//	@RequestMapping("/creatorModify")
//	public String creatorModify() {
//		
//		
//		
//		return "/creator/creatorModify";
//	}
}





















