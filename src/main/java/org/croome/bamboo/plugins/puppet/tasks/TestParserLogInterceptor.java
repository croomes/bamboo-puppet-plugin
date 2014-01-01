package org.croome.bamboo.plugins.puppet.tasks;

import com.atlassian.bamboo.build.LogEntry;
import com.atlassian.bamboo.build.logger.LogInterceptor;
import com.atlassian.bamboo.results.tests.TestResults;

import org.apache.log4j.Logger;
import org.croome.bamboo.plugins.puppet.tasks.lint.LintTestParser;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class TestParserLogInterceptor implements LogInterceptor
{
    /**
	 * 
	 */
	private static final Logger log = Logger.getLogger(LintTestParser.class);
	
	// ------------------------------------------------------------------------------------------------------- Constants
    // ------------------------------------------------------------------------------------------------- Type Properties
	private static final long serialVersionUID = 1L;
    private final TestParser testParser;

    // ---------------------------------------------------------------------------------------------------- Dependencies
    // ---------------------------------------------------------------------------------------------------- Constructors

    public TestParserLogInterceptor(TestParser testParser)
    {
        this.testParser = testParser;
    }

    // ----------------------------------------------------------------------------------------------- Interface Methods

    public void intercept(@NotNull final LogEntry logEntry)
    {
    	testParser.processLine(logEntry.getUnstyledLog());
    }

    public void interceptError(@NotNull final LogEntry logEntry)
    {
    	testParser.processLine(logEntry.getLog());
    }

    // -------------------------------------------------------------------------------------------------- Action Methods
    // -------------------------------------------------------------------------------------------------- Public Methods

    public Set<TestResults> getFailingTestResults(Boolean failOnWarning)
    {
        return testParser.getFailingTestResults(failOnWarning);
    }

    public Set<TestResults> getSuccessfulTestResults()
    {
        return testParser.getSuccessfulTestResults();
    }

    // -------------------------------------------------------------------------------------- Basic Accessors / Mutators
}