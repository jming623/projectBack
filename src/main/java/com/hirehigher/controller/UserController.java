package com.hirehigher.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hirehigher.command.EmailAuthVO;
import com.hirehigher.command.JobBoardVO;
import com.hirehigher.command.UserVO;
import com.hirehigher.jobboard.service.JobBoardService;
import com.hirehigher.user.service.UserService;
import com.hirehigher.util.JobCriteria;
import com.hirehigher.util.JobPageVO;
import com.hirehigher.util.UserCriteria;
import com.hirehigher.util.UserPageVO;


@Controller
@RequestMapping("/user")
public class UserController {
	
	//userServie와 연결
	@Autowired
	@Qualifier("userService")
	private UserService userService;	
	
	//JavaMailSender와연결
	@Autowired
	JavaMailSender mailSender;
	
	
	//로그인화면
	@RequestMapping("/userLogin")
	public void userLogin() {
		
	}
	
	//로그인 처리
	@RequestMapping(value="loginForm", method=RequestMethod.POST)
	public ModelAndView loginForm(UserVO vo) {
		
		ModelAndView mv = new ModelAndView();
		
		UserVO userVO = userService.login(vo);

		if(userVO != null) { //로그인 성공
//			System.out.println(userVO.toString());
			mv.addObject("login", userVO);
		}else { //로그인 실패
			mv.addObject("msg", "아이디 비밀번호를 확인하세요");
			
		}
		
		return mv; //LoginSuccessHandler로 반환
	}
	
	//생년월일로 아이디찾기
	@ResponseBody
	@PostMapping(value="/findIdA", produces="application/json")
	public ArrayList<UserVO> findIdA(@RequestBody UserVO VO) {
		
		String userName = VO.getUserName();
		String birthDay = VO.getBirthDay();		
		
		ArrayList<UserVO> idList = userService.findIdA(userName ,birthDay );
		
		return idList;
	}
	
	//전화번호로 아이디찾기
	@ResponseBody
	@PostMapping(value="/findIdB", produces="application/json")
	public ArrayList<UserVO> findIdB(@RequestBody UserVO VO){
		
		String userName = VO.getUserName();
		String userCellNum = VO.getUserCellNum();
			
		ArrayList<UserVO> idList = userService.findIdB(userName, userCellNum);
		
		return idList;
	}
	
	//이메일로 비밀번호 발송
	@ResponseBody
	@PostMapping(value="/sendEmailPw", produces="application/json")
	public int sendEmailPw(@RequestBody UserVO vo) {
		
		String userId = vo.getUserId();
		String userName = vo.getUserName();
		String userEmail = vo.getUserEmail();
		
		System.out.println("userId:"+userId+"userName:"+userName+"userEmail:"+userEmail);
		
		String findedPw = userService.findPw(userId, userName, userEmail);
		
		if(findedPw == null) { //입력한정보에 일치하는 회원이없음
			return 0;
		}else {//findedPw가 null이 아니라면 실행
			String setfrom = "jming95623@gmail.com"; //보내는 사람 이메일
	        String tomail = userEmail; // 받는 사람 이메일
	        String title = userName+"회원님께서  요청하신 정보입니다_from:HireHigher"; // 제목
	        String content ="\r\n\r\n"+"회원님께서 요청하신 정보입니다."+"\r\n\r\n"+ "비밀번호는 " +findedPw+ " 입니다. "+"\r\n\r\n"+ "감사합니다.";
			
	        try {
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
//	        	SimpleMailMessage message = new SimpleMailMessage();
				messageHelper.setFrom(setfrom);
				messageHelper.setTo(tomail);
				messageHelper.setSubject(title);
				messageHelper.setText(content);
				
				mailSender.send(message);
							
			} catch (Exception e) {
				e.printStackTrace();			
			}
	        
	        return 1;
		}
	
	}
	
	
	//회원가입화면
	@RequestMapping("/userJoin")
	public void userJoin() {
		
	}
	
	//아이디 중복확인(비동기요청)
	@ResponseBody //@ResponseBody는 restController가 아닌 일반 컨트롤러에서 비동기요청이 일어난곳으로 return값을 보내주기위해 필요하다
	@GetMapping("/idCheck/{userId}") //$.getJSON으로 비동기요청을 받을때는 RequestMapping이 아니라 GetMapping으로 받아준다.
	public int idCheck(@PathVariable("userId") String userId) {
		
		int result = userService.idCheck(userId);
		
		return result;
	}
	
	//닉네임 중복확인(비동기요청)
	@ResponseBody
	@GetMapping("/nickNameCheck/{nickName}")
	public int nickNameCheck(@PathVariable("nickName") String nickName) {
		
		int result = userService.nickNameCheck(nickName);
		
		return result;
	}
	
//	//이메일 중복확인 1	(왜안되는지 모르겠음 .com으로 끝나면 406에러발생	)
//	@ResponseBody
//	@GetMapping(value="/emailCheck/{userEmail:.+}", produces="application/json" )
//	public int emailCheck(@PathVariable("userEmail") String userEmail) {
//		
//		System.out.println("emailCheck로 들어온 이메일:"+userEmail);
//		
//		int result = userService.emailCheck(userEmail);
//		
//		System.out.println("result:"+result);
//		return result;
//	}
	
	//이메일중복확인 2
	@ResponseBody
	@RequestMapping(value="/emailCheck", method=RequestMethod.POST)
	public int emailCheck(@RequestBody UserVO vo) {
		
		String userEmail = vo.getUserEmail();
		
		int result = userService.emailCheck(userEmail);
		
		return result;
	}
	
	//이메일 발송
	@ResponseBody
	@PostMapping(value="/sendEmail", produces="application/json")
	public EmailAuthVO sendEmail(@RequestBody UserVO vo) {
		
		String userEmail = vo.getUserEmail();		
		
		//인증키 생성
		UUID uuid = UUID.randomUUID(); //16진수 랜덤값	
		String keyCode = uuid.toString().replaceAll("-", "").substring(0, 6);
		
		String setfrom = "jming95623@gmail.com"; //보내는 사람 이메일
        String tomail = userEmail; // 받는 사람 이메일
        String title = "회원가입 인증 이메일 입니다."; // 제목
        String content ="\r\n\r\n"+"저희 홈페이지를 찾아주셔서 감사합니다"+"\r\n\r\n"+ "인증번호는 " +keyCode+ " 입니다. "+"\r\n\r\n"+ "인증번호를 홈페이지에 입력해주세요";
		
//        System.out.println(userEmail);
//        System.out.println(content);
       
        
        try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
//        	SimpleMailMessage message = new SimpleMailMessage();
			messageHelper.setFrom(setfrom);
			messageHelper.setTo(tomail);
			messageHelper.setSubject(title);
			messageHelper.setText(content);
			
			mailSender.send(message);
						
			
		} catch (Exception e) {
			e.printStackTrace();		
		}
        
		return new EmailAuthVO(keyCode);
	}
	
	//회원가입 처리
	@RequestMapping(value="/joinForm", method=RequestMethod.POST)
	public String joinForm(UserVO vo, RedirectAttributes RA){
		
		int result = userService.join(vo);
		
		if(result ==1) {
			RA.addFlashAttribute("msg", "가입을 축하합니다");
		}else {
			RA.addFlashAttribute("msg", "회원가입에 실패했습니다. 관리자에게 문의하세요");
		}
		
		return "redirect:/user/userLogin";
	}
	
	//마이페이지
	@RequestMapping("/mypage")
	public void mypage(HttpSession session, Model model) {
		
		//마이페이지 회원정보 불러오기
		UserVO userVO = (UserVO)session.getAttribute("userVO");//userVO는 LoginSuccessHandler에서 만들어 집니다.
		
		String userId = userVO.getUserId();
		
		UserVO userInfo = userService.getUserInfo(userId);
		
		model.addAttribute("userInfo", userInfo);
			
	}
	
	//마이페이지 채용공고 불러오기
	@ResponseBody
	@PostMapping(value="/mypageJobBoardList", produces="application/json")
	public HashMap<String, Object> mypageJobBoardList(@RequestBody UserCriteria cri){ //cri값이 mypage로부터 들어오지 않으면 기본생성자로 startPage1, amount가 10으로 초기화됨
				
		String userId = cri.getUserId();//마이페이지에서 넘어온 userId
		int pageNum = cri.getPageNum();//마이페이지에서 넘어온 pageNum
		int amount = cri.getAmount(); //마이페이지에서 넘어온 amount	
		
		System.out.println("마이페이지 채용공고 불러오기_ userId="+userId+" pageNum="+ pageNum + " amount="+ amount);
		
		HashMap<String, Object> map = new HashMap<>();		
		
		ArrayList<JobBoardVO> jobBoardList = userService.getJobBoardList(cri.getPageNum(),cri.getAmount(), userId);
		
//		System.out.println(jobBoardList.toString());
		
		map.put("list",jobBoardList);//화면단에 JobBoardVO 리스트를 담은 arrayList를 반환 
		
		int total = userService.getJobBoardTotal(userId);
		
		UserPageVO pageVO = new UserPageVO(cri,total);
		
		System.out.println(pageVO.toString());
		
		map.put("pageVO",pageVO); //화면단에 pageVO 반환 
		
		return map;
	}
		
	
	//마이페이지 문의내역 불러오기 
	
	
	

	//마이수정페이지
	@RequestMapping("/mypageModify")
	public void mypageModify(HttpSession session, Model model) {
		
		UserVO userVO = (UserVO)session.getAttribute("userVO");
		
		System.out.println("mypageModify:"+userVO.toString());
		
		model.addAttribute("userInfo", userVO);		
	}
	
	//수정페이지 이메일인증
	@ResponseBody
	@PostMapping(value="/sendEmailModify", produces="application/json")
	public EmailAuthVO sendEmailModify(@RequestBody UserVO vo) {
		
		String userEmail = vo.getUserEmail();
		
		//인증키 생성
			UUID uuid = UUID.randomUUID(); //16진수 랜덤값	
			String keyCode = uuid.toString().replaceAll("-", "").substring(0, 6);
				
			String setfrom = "jming95623@gmail.com"; //보내는 사람 이메일
		    String tomail = userEmail; // 받는 사람 이메일
		    String title = "회원정보수정 인증 이메일 입니다."; // 제목
		    String content ="\r\n\r\n"+"저희 홈페이지를 찾아주셔서 감사합니다"+"\r\n\r\n"+ "인증번호는 " +keyCode+ " 입니다. "+"\r\n\r\n"+ "인증번호를 홈페이지에 입력해주세요";
		    
		    try {
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
				messageHelper.setFrom(setfrom);
				messageHelper.setTo(tomail);
				messageHelper.setSubject(title);
				messageHelper.setText(content);
				
				mailSender.send(message);
											
			} catch (Exception e) {
				e.printStackTrace();		
			}
	        
		return new EmailAuthVO(keyCode);
	}
	
	//회원정보수정요청
	@RequestMapping("/modifyForm")
	public String modifyForm(UserVO vo, HttpSession session, RedirectAttributes RA) {
		
		System.out.println(vo.toString());
		
		int result = userService.modify(vo);
		
		if(result == 1) { //업데이트 성공
			RA.addFlashAttribute("msg", "회원정보가 수정되었습니다. 원활한 이용을 위해 다시 로그인해주시기 바랍니다.");			
			session.invalidate();//세션에 저장된 로그인 정보 삭제
		}else {
			RA.addFlashAttribute("msg", "회원정보 수정에 실패하였습니다. 관리자에게 문의해주세요");
		}
		
		return "redirect:/user/userLogin";
	}
	
	//접근실패 처리 (로그인을 하지않고 비정상적인 접근을 한 경우)
	@RequestMapping("/access_fail")
	public String accessFail(RedirectAttributes RA) {
		
		RA.addFlashAttribute("msg", "비정상적인 접근입니다. 로그인 이후 다시 시도해주세요");
		
		return "redirect:/user/userLogin";
	}
	
	//로그아웃
	@RequestMapping("/userLogout")
	public String userLogout(HttpSession session) {
		
		session.invalidate();
		
		return "redirect:/"; //홈화면 이동
	}
	
	
	
	
}
