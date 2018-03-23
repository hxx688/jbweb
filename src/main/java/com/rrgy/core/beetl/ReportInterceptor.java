package com.rrgy.core.beetl;

import org.beetl.sql.core.InterceptorContext;
import org.beetl.sql.ext.DebugInterceptor;

import com.rrgy.core.constant.Cst;
import com.rrgy.core.toolbox.kit.DateKit;

/**
 * 重写beetlsql输出的sql语句格式
 */
public class ReportInterceptor extends DebugInterceptor {

	@Override
	public void before(InterceptorContext ctx) {
		StringBuilder sb = new StringBuilder();
		ctx.put("logs", sb);
		
		if (!Cst.me().isDevMode()) {
			return;
		}
		String sqlId = ctx.getSqlId();
		if (this.isDebugEanble(sqlId)) {
			ctx.put("debug.time", System.currentTimeMillis());
		}

		sb.append("\nBlade beetlsql --------------------- " + DateKit.getTime() + " --------------------------------\n")
				.append("索引: " + ctx.getSqlId().replaceAll("\\s+", " ")).append("\n")
				.append("语句: " + ctx.getSql().replaceAll("\\s+", " ")).append("\n")
				.append("参数: " + formatParas(ctx.getParas()))
				.append("\n");

		RuntimeException ex = new RuntimeException();
		StackTraceElement[] traces = ex.getStackTrace();
		boolean found = false;
		for (StackTraceElement tr : traces) {
			if (!found && tr.getClassName().indexOf("SQLManager") != -1) {
				found = true;
			}
			if (found && !tr.getClassName().startsWith("org.beetl.sql.core") && !tr.getClassName().startsWith("com.sun")) {
				String className = tr.getClassName();
				String mehodName = tr.getMethodName();
				int line = tr.getLineNumber();
				sb.append("位置: " + className + "." + mehodName + "(" + tr.getFileName() + ":" + line + ")").append("\n");
				break;
			}
		}
		ctx.put("debug.sb", sb);
	}

	@Override
	public void after(InterceptorContext ctx) {
		if (!Cst.me().isDevMode()) {
			return;
		}
		long time = System.currentTimeMillis();
		long start = (Long) ctx.get("debug.time");
		
		StringBuilder sb = (StringBuilder) ctx.get("debug.sb");
		sb.append("时间: " + (time - start) + "ms").append("\n");

		if (ctx.isUpdate()) {
			sb.append("更新: [");
			if (ctx.getResult().getClass().isArray()) {
				int[] ret = (int[]) ctx.getResult();
				for (int i = 0; i < ret.length; i++) {
					sb.append(ret[i]);
					if (i != ret.length - 1) {
						sb.append(",");
					}
				}
			} else {
				sb.append(ctx.getResult());
			}
			sb.append("]");
		} else {
			sb.append("返回: ").append(ctx.getResult()).append("");
		}
		sb.append("\n").append("-----------------------------------------------------------------------------------------");
		println(sb.toString());
	}

}
