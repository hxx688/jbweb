package com.rrgy.test.beetlsql;

import java.util.List;
import java.util.Map;

import org.beetl.sql.core.ClasspathLoader;
import org.beetl.sql.core.DefaultNameConversion;
import org.beetl.sql.core.Interceptor;
import org.beetl.sql.core.SQLLoader;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.db.MySqlStyle;
import org.junit.Test;

import com.rrgy.core.beetl.ReportInterceptor;
import com.rrgy.core.toolbox.kit.CharsetKit;

@SuppressWarnings("rawtypes")
public class BeetlTest {

	@Test
	public void test() {

		List<Map> list = getSqlManager().execute("select 'test' t from dual", Map.class, null);
		System.out.println(list);

	}

	public SQLManager getSqlManager() {
		MySqlStyle style = new MySqlStyle();
		MySqlConnection cs = new MySqlConnection();
		SQLLoader loader = new ClasspathLoader("/beetlsql");
		SQLManager sql = new SQLManager(style, loader, cs, new DefaultNameConversion(), new Interceptor[] { new ReportInterceptor() });
		sql.getSqlLoader().setCharset(CharsetKit.UTF_8);
		sql.getSqlLoader().setAutoCheck(true);
		return sql;
	}

}
