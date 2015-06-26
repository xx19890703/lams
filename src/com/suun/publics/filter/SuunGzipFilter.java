/**
 *  Copyright 2003-2009 Terracotta, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.suun.publics.filter;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class SuunGzipFilter implements Filter {

	// / private static final Logger LOG =
	// LoggerFactory.getLogger(SuunGzipFilter.class);

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			// 使用我们自定义的响应包装器来包装原始的ServletResponse
			ResponseWrapper wrapper = new ResponseWrapper(
					(HttpServletResponse) response);
			// 这句话非常重要，注意看到第二个参数是我们的包装器而不是response
			chain.doFilter(request, wrapper);
			// 处理截获的结果并进行处理
			String result = wrapper.getResult();
			if (!result.equals("")) {
				result = result.replaceAll("\r?\n(\\s*\r?\n)+", "\r\n");
				// 输出最终的结果
				PrintWriter out = response.getWriter();
				out.write(result);
				out.flush();
				out.close();
			}
		} else
			chain.doFilter(request, response);

	}

	@Override
	public void init(FilterConfig config) throws ServletException {

	}

}
