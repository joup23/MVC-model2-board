package sec02.ex01;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/member/*")
public class MemberController extends HttpServlet{
	private static final long serialVersionUID = 1L;
	MemberDAO memberDAO;
	
	public void init() throws ServletException{
		memberDAO = new MemberDAO();
		
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doHandle(req,resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doHandle(req, resp);
	}

	private void doHandle(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		String nextPage=null;
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		String action = req.getPathInfo();
		System.out.println("action : "+action);
		
		if(action == null || action.equals("/listMembers.do"))
		{
			List<MemberVO> membersList=memberDAO.listMembers();
			req.setAttribute("membersList", membersList);
			nextPage = "/test01/listMembers.jsp";
		}else if(action.equals("/addMember.do"))
		{
			String id= req.getParameter("id");
			String pwd = req.getParameter("pwd");
			String name= req.getParameter("name");
			String email = req.getParameter("email");
			MemberVO memberVO = new MemberVO(id,pwd,name,email);
			memberDAO.addMember(memberVO);
			nextPage = "/member/listMembers.do";
		}else if(action.equals("/memberForm.do")) {
			nextPage = "/test01/memberForm.jsp";
		}else if(action.equals("/modMemberForm.do")){
			String id= req.getParameter("id");
			MemberVO memInfo = memberDAO.findMember(id);
			req.setAttribute("memInfo", memInfo);
			nextPage = "/test01/modMemberForm.jsp";
		}else if(action.equals("/modMember.do")) {
			String id= req.getParameter("id");
			String pwd= req.getParameter("pwd");
			String name = req.getParameter("name");
			String email = req.getParameter("email");
			MemberVO memberVO = new MemberVO(id, pwd,name,email);
			memberDAO.modMember(memberVO);
			req.setAttribute("msg", "modified");
			nextPage = "/member/listMembers.do";
		} else if(action.equals("/delMember.do")) {
			String id = req.getParameter("id");
			memberDAO.delMember(id);
			req.setAttribute("msg", "deleted");
			nextPage = "/member/listMembers.do";
		}else {
			List<MemberVO> memberList = memberDAO.listMembers();
			req.setAttribute("membersList", memberList);
			nextPage="/test01/listMembers.jsp";
		}
		RequestDispatcher dispatch = req.getRequestDispatcher(nextPage);
		dispatch.forward(req, resp);
		
	}
	
}
