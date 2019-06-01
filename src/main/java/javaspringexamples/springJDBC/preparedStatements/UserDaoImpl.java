package javaspringexamples.springJDBC.preparedStatements;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * 
 * @author mounir.sahrani@gmail.com
 *
 */
public class UserDaoImpl implements UserDao {

	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private UserRowMapper userRowMapper = new UserRowMapper();

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
	}

	public User findById(long userId) {
		return jdbcTemplate.queryForObject("SELECT id, name, user_name, access_time, locked FROM USER WHERE id = ?",
				userRowMapper, userId);
	}

	public List<User> findByUserName(String userName) {
		PreparedStatementCreatorFactory psCreatorFactory = new PreparedStatementCreatorFactory(
				"SELECT id, name, user_name, access_time, locked FROM USER WHERE user_name like ?",
				new int[] { Types.VARCHAR });
		return jdbcTemplate.query(psCreatorFactory.newPreparedStatementCreator(new Object[] { userName }),
				userRowMapper);
	}

	public List<User> findLocked(boolean locked) {
		return jdbcTemplate.query("SELECT id, name, user_name, access_time, locked FROM USER WHERE locked = ?",
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setBoolean(1, locked);
					}
				}, userRowMapper);
	}
}
