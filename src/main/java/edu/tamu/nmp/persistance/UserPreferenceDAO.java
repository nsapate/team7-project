package edu.tamu.nmp.persistance;

import java.util.List;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class UserPreferenceDAO {

	private String cql = "select movie_id, rating from nflix.ratings where user_id=? allow filtering";
	private String cql2 = "select movie_name,genre1 from nflix.movies where movie_id= ? allow filtering";
	private Session session;
	public void init() {
		CassandraConnect cc = new CassandraConnect();
		cc.connect("localhost", 9042);
		session = cc.getSession();
	}
	
	public void getPastMovieHistory(String id){
		init();
		PreparedStatement ps = session.prepare(cql);
		BoundStatement bs = ps.bind(new Integer(id));
		ResultSet rs = session.execute(bs);
		while(!rs.isExhausted()) {
			Row row = rs.one();
			System.out.print("Your Rating :"+row.getString("rating"));
			this.printMovieDetails(new Integer(row.getInt("movie_id")));
			System.out.println();
		}
		
	}
	
	private void printMovieDetails(Integer id) {
		PreparedStatement ps = session.prepare(cql2);
		BoundStatement bs = ps.bind(id);
		ResultSet rs = session.execute(bs);
		Row row = rs.one();
		System.out.print("	Movie Name:  "+row.getString(0));
		System.out.println(" 	Genre:  "+row.getString("Genre1"));

	}
	
	/*public static void main(String args[]) {
		UserPreferenceDAO mdao= new  UserPreferenceDAO();
		mdao.init();
		List<Row> r = mdao.getPastMovieHistory("123456");
//		System.out.println(r.get(0).getInt("movie_id"));
	}*/
}
