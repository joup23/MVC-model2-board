package sec03.brd;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	private DataSource dataFactory;
	Connection conn;
	PreparedStatement pstmt;

	public BoardDAO() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public List selectAllArticles() {
		List<ArticleVO> articlesList = new ArrayList();
		try {
			conn = dataFactory.getConnection();
			String query = "SELECT LEVEL, articleNO, parentNO, title, content, id, writeDate" + " from t_board"
					+ " start with parentNO=0" + " connect by prior articleNO = parentNO"
					+ " order siblings by articleNO desc";

			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int level = rs.getInt("level");
				int articleNO = rs.getInt("articleNO");
				int parentNO = rs.getInt("parentNO");
				String title = rs.getString("title");
				String content = rs.getString("content");
				String id = rs.getString("id");
				Date writeDate = rs.getDate("writeDate");
				ArticleVO article = new ArticleVO();
				article.setLevel(level);
				article.setArticleNO(articleNO);
				article.setParentNO(parentNO);
				article.setTitle(title);
				article.setContent(content);
				article.setId(id);
				article.setWriteDate(writeDate);
				articlesList.add(article);
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return articlesList;
	}

	public int getNewArticleNO() {
		try {
			conn = dataFactory.getConnection();
			String query = "select max(articleNO) from t_board";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery(query);
			if (rs.next())
				return (rs.getInt(1) + 1);
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 0;
	}

	public int insertNewArticle(ArticleVO article) {
		int articleNO = getNewArticleNO();
		try {
			conn = dataFactory.getConnection();
			int parentNO = article.getParentNO();
			String titie = article.getTitle();
			String content = article.getContent();
			String id = article.getId();
			String imageFileName = article.getImageFileName();
			String query = "INSERT INTO t_board(articleNO, parentNO, title, content, imageFileName, id)"
					+ " VALUES(?,?,?,?,?,?)";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			pstmt.setInt(2, parentNO);
			pstmt.setString(3, titie);
			pstmt.setString(4, content);
			pstmt.setString(5, imageFileName);
			pstmt.setString(6, id);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return articleNO;
	}

	public ArticleVO selectArticle(int articleNO) {
		// TODO Auto-generated method stub
		ArticleVO article = new ArticleVO();
		try {
			conn = dataFactory.getConnection();
			String query = "select articleNO,parentNO,title,content, id, writeDate, imageFileName"
					+ " from t_board"
					+ " where articleNO=?";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			int _articleNO = rs.getInt("articleNO");
			int parentNO = rs.getInt("parentNO");
			String title = rs.getString("title");
			String content = rs.getString("content");
			
			String id = rs.getString("id");
			Date writeDate = rs.getDate("writeDate");
			String imageFileName = rs.getString("imageFileName");
			
			article.setArticleNO(_articleNO);
			article.setParentNO(parentNO);
			article.setTitle(title);
			article.setContent(content);
			
			article.setId(id);
			article.setWriteDate(writeDate);
			if( imageFileName != null && imageFileName.length() !=0 ) {
				article.setImageFileName(imageFileName);
			}
			rs.close();
			pstmt.close();
			conn.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return article;
	}

	public void updateArticle(ArticleVO article) {
		// TODO Auto-generated method stub
		int articleNO = article.getArticleNO();
		String title = article.getTitle();
		String content = article.getContent();
		String imageFileName = article.getImageFileName();
		try {
			conn = dataFactory.getConnection();
			String query = "update t_board set title=?, content=?";
			if(imageFileName != null && imageFileName.length() != 0)
			{
				query += ",imageFileName=?";
			} 
			query +=" where articleNO =?";
			
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			if(imageFileName != null && imageFileName.length() != 0)
			{
				pstmt.setString(3, imageFileName);
				pstmt.setInt(4, articleNO);
			}else {
				pstmt.setInt(3, articleNO);
			}
			pstmt.executeUpdate();
			pstmt.close();
			pstmt.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public List<Integer> selectRemovedArticles(int articleNO) {
		// TODO Auto-generated method stub
		List<Integer> articleNOList = new ArrayList<Integer>();
		
		try {
			conn = dataFactory.getConnection();
			String query = "select articleNO from t_board ";
			query += " start with articleNO = ?";
			query += " connect by prior articleNO = parentNO";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{
				articleNO = rs.getInt("articleNO");
				articleNOList.add(articleNO);
			}
			pstmt.close();
			conn.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return articleNOList;
	}

	public void deleteArticle(int articleNO) {
		// TODO Auto-generated method stub
		try
		{
			conn = dataFactory.getConnection();
			String query = "DELETE FROM t_board";
			query += " WHERE articleNO in(";
			query +="SELECT articleNO FROM t_board ";
			query += " START WITH articleNO = ?";
			query += " CONNECT BY PRIOR articleNO = parentNO)";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			pstmt.executeUpdate();
			pstmt.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public List selectAllArticles(Map pagingMap) {
		// TODO Auto-generated method stub
		List articlesList = new ArrayList();
		int section = (Integer) pagingMap.get("section");
		int pageNum = (Integer) pagingMap.get("pageNum");
		try {
			conn = dataFactory.getConnection();
			String query = "select * from( "
					+ "select rownum as recNum,"
					+ "LVL,"+"articleNO,"
					+ "parentNO," + "title," +" content,"
					+ "id,"+"writeDate"
					+ " from (select LEVEL as LVL, "
						+ "articleNO,"
						+ "parentNO,"
						+ "title,"
						+ "content,"
						+ "id,"
						+ "writeDate"
						+ " from t_board"
						+ " start with parentNO=0"
						+ " connect by prior articleNO = parentNO"
						+ " order SIBLINGS BY articleNO DESC)"
					+ ") "
					+ " where recNUM between(?-1)*100+(?-1)*10+1 and (?-1)*100+?*10";
			
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, section);
			pstmt.setInt(2, pageNum);
			pstmt.setInt(3, section);
			pstmt.setInt(4, pageNum);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{
				int level = rs.getInt("lvl");
				int articleNO = rs.getInt("articleNO");
				int parentNO = rs.getInt("parentNO");
				String title = rs.getString("title");
				String id = rs.getString("id");
				Date writeDate = rs.getDate("writeDate");
				ArticleVO article = new ArticleVO();
				
				article.setLevel(level);
				article.setArticleNO(articleNO);
				article.setParentNO(parentNO);
				article.setTitle(title);
				article.setId(id);
				article.setWriteDate(writeDate);
				articlesList.add(article);
			}
			rs.close();
			pstmt.close();
			conn.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return articlesList;
	}

	public int selectTotArticles() {
		// TODO Auto-generated method stub
		try {
			conn = dataFactory.getConnection();
			String query = "select count(articleNO) from t_board ";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
				return (rs.getInt(1));
			rs.close();
			pstmt.close();
			conn.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 0;
	}

	public List<ArticleVO> articleMember() {	//각 회원마다 게시물 갯수
		// TODO Auto-generated method stub
		List<ArticleVO> articleMemberList = new ArrayList<ArticleVO>();
		try {
			String i;
			conn = dataFactory.getConnection();
			String query = "select id, count(*) as lists" 
					+ " from t_board"
					+ " group by id"
					+ " order by lists desc";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
			
			String id = rs.getString("id");
			int lists = rs.getInt("lists");
			ArticleVO article = new ArticleVO();
			System.out.println(id+"/"+lists);
			article.setId(id);
			article.setLists(lists);
			articleMemberList.add(article);
			}
			rs.close();
			pstmt.close();
			conn.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return articleMemberList;
	}
}
