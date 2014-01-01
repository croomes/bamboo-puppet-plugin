package org.croome.bamboo.plugins.puppet;

import org.croome.bamboo.plugins.puppet.tasks.lint.LintTestParser;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class LintTestParserTest
{
	LintTestParser lintTestParser;

	@Test
	public void testSuccessFulTestParsing() throws Exception
	{
		load("/org/croome/bamboo/plugins/puppet/puppet-lint.out");
	}

	@Test
	public void testFailingTestParsing() throws Exception
	{
		load("/org/croome/bamboo/plugins/puppet/puppet-lint.out");
	}

	@Before
	public void setup()
	{
		lintTestParser = new LintTestParser();
	}

	public void load(String resource) throws IOException
	{
		final InputStream testLogStream = getClass().getResourceAsStream(resource);
		for (String line : (List<String>) IOUtils.readLines(testLogStream)) {
			lintTestParser.processLine(line);
		}
	}
}