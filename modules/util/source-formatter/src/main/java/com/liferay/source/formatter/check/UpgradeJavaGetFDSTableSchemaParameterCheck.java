package com.liferay.source.formatter.check;

import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.source.formatter.check.util.JavaSourceUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Albert Gomes Cabral
 */
public class UpgradeJavaGetFDSTableSchemaParameterCheck
	extends BaseUpgradeMatcherReplacementCheck {

	@Override
	protected String afterFormat(
		String fileName, String absolutePath, String content,
		String newContent) {

		if (fileName.endsWith(".java") &&
			!newContent.contains("java.util.Locale")) {

			newContent = addNewImports(newContent);
		}

		return newContent;
	}

	@Override
	protected String formatMatcherIteration(
		String content, String newContent, Matcher matcher) {

		String methodCall = matcher.group();

		List<String> parameterList = JavaSourceUtil.getParameterList(
			methodCall);

		if (parameterList.size() == 1) {
			return newContent;
		}
		else {
			return StringUtil.replace(
				content, methodCall,
				StringUtil.replace(methodCall, matcher.group(1),
					"getFDSTableSchema(Locale locale)"));
		}

	}

	@Override
	protected String[] getNewImports() {
		return new String[] {"java.util.Locale"};
	}

	@Override
	protected Pattern getPattern() {
		return Pattern.compile(
			"@Override\\n\\w+\\s+FDSTableSchema\\s+(getFDSTableSchema\\(\\))");
	}

}