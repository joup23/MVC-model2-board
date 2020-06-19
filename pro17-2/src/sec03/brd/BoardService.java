package sec03.brd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardService {
	BoardDAO boardDAO;
	
	public BoardService()
	{
		boardDAO = new BoardDAO();
	}
	public List<ArticleVO> listArticles() 
	{
		List<ArticleVO> articlesList = boardDAO.selectAllArticles();
		return articlesList;
	}
	public int addArticle(ArticleVO article) {
		// TODO Auto-generated method stub
		return boardDAO.insertNewArticle(article);
	}
	public ArticleVO viewArticle(int articleNO) {
		ArticleVO article = null;
		article = boardDAO.selectArticle(articleNO);
		return article;
	}
	public void modArticle(ArticleVO article) {
		// TODO Auto-generated method stub
		boardDAO.updateArticle(article);
	}
	public List<Integer> removeArticle(int articleNO) {
		// TODO Auto-generated method stub
		List<Integer> articleNOList = boardDAO.selectRemovedArticles(articleNO);
		boardDAO.deleteArticle(articleNO);
		return articleNOList;
	}
	public int addReply(ArticleVO article) {
		// TODO Auto-generated method stub
		return boardDAO.insertNewArticle(article);
	}
	public Map listArticles(Map<String,Integer> pagingMap) {
		// TODO Auto-generated method stub
		Map articlesMap = new HashMap();
		List<ArticleVO> articlesList = boardDAO.selectAllArticles(pagingMap);
		int totArticles = boardDAO.selectTotArticles();
		articlesMap.put("articlesList", articlesList);
		articlesMap.put("totArticles", totArticles);
		return articlesMap;
	}
	public List<ArticleVO> listArticleMember() {	//각 회원마다 게시물 갯수
		// TODO Auto-generated method stub
		List<ArticleVO> articleMember = boardDAO.articleMember();
		return articleMember;
	}
}
