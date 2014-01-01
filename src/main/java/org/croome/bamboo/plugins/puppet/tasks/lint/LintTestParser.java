package org.croome.bamboo.plugins.puppet.tasks.lint;

import com.atlassian.bamboo.results.tests.TestResults;
import com.atlassian.bamboo.resultsummary.tests.TestCaseResultErrorImpl;
import com.atlassian.bamboo.resultsummary.tests.TestState;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.croome.bamboo.plugins.puppet.tasks.TestParser;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test parser for Puppet Lint
 */
public class LintTestParser implements TestParser
{
    private static final Logger log = Logger.getLogger(LintTestParser.class);

    // ------------------------------------------------------------------------------------------------------- Constants
    private static final Pattern START_TESTCASE = Pattern.compile("^(\\S+) - .+$");
    private static final Pattern LINT_RESULT  = Pattern.compile("^(\\S+) - (ERROR|WARNING):\\s+(.+)\\s+on line\\s+(\\d+)(.*)$", Pattern.DOTALL);
    
    // ------------------------------------------------------------------------------------------------- Type Properties

    private final Set<TestResults> successfulTestResults = Sets.newHashSet();
    private final Set<TestResults> failingTestResults = Sets.newHashSet();

    private String currentSuiteName = "puppet-lint";
    private String currentTestName = "";
    private String nextTestName = "";
    private String currentTestDuration = "0";
    private StringBuilder currentTestInput = new StringBuilder();
    private HashMap <String, StringBuilder> testOutput = new LinkedHashMap <String, StringBuilder>();
    private HashMap <String, StringBuilder> testWarnings = new LinkedHashMap <String, StringBuilder>();
    private HashMap <String, StringBuilder> testErrors = new LinkedHashMap <String, StringBuilder>();

    // ---------------------------------------------------------------------------------------------------- Dependencies
    // ---------------------------------------------------------------------------------------------------- Constructors
    // ----------------------------------------------------------------------------------------------- Interface Methods
    // -------------------------------------------------------------------------------------------------- Action Methods
    // -------------------------------------------------------------------------------------------------- Public Methods

    @Override
    public Set<TestResults> getSuccessfulTestResults()
    {
		for (Map.Entry<String, StringBuilder> entry : testOutput.entrySet()) 
		{
    		String testName = entry.getKey();

    		if ( ! testErrors.containsKey(testName)) 
    		{
				final TestResults testResult = new TestResults(currentSuiteName, testName, currentTestDuration);
		        testResult.setSystemOut(entry.getValue().toString());
		        testResult.setState(TestState.SUCCESS);
		        successfulTestResults.add(testResult);
    		}
		}
        return successfulTestResults;
    }

    @Override
    public Set<TestResults> getFailingTestResults(Boolean failOnWarning)
    {
		for (Map.Entry<String, StringBuilder> entry : testOutput.entrySet()) 
		{
    		String testName = entry.getKey();
    		StringBuilder output = new StringBuilder();

            if (testErrors.containsKey(testName))
            	output.append(testErrors.get(testName).toString());
            if (failOnWarning && testWarnings.containsKey(testName))
            	output.append(testWarnings.get(testName).toString());

        	if (output.length() > 0)
        	{
        		final TestResults testResult = new TestResults(currentSuiteName, testName, currentTestDuration);
                testResult.setSystemOut(entry.getValue().toString());
            	testResult.setState(TestState.FAILED);
        		testResult.addError(new TestCaseResultErrorImpl(output.toString()));
            	failingTestResults.add(testResult);
            }
        }
        return failingTestResults;
    }

    // -------------------------------------------------------------------------------------- Basic Accessors / Mutators

    @Override
    public void processLine(@NotNull String input)
    {
    	String[] lines = input.split("\n");
    	for (String line : lines) 
    	{
	        final Matcher startTestCaseMatcher = START_TESTCASE.matcher(line);
	        if (startTestCaseMatcher.matches())
	        {
	        	nextTestName = startTestCaseMatcher.group(1);
	        	if (nextTestName.startsWith("./") || nextTestName.startsWith(".\\"))
	        		nextTestName = nextTestName.substring(2);
	        	
	        	if (currentTestInput.length() > 0)
	        	{
	        		processTest(currentTestName, currentTestInput.toString());
	            	endTestCase();
	        	}
	        	currentTestName = nextTestName;
	        }
	        
	        if (currentTestName.length() > 0)
	        {
	        	currentTestInput.append(line);
	        	currentTestInput.append('\n');
	        }
    	}
    }
    
    private void processTest(@NotNull String testName, @NotNull String test)
    {
        final Matcher testResultMatcher = LINT_RESULT.matcher(test);
        if (testResultMatcher.matches())
        {
        	recordTestOutput(testName, test);
        	
        	if (testResultMatcher.group(2).equals("ERROR"))
        		recordTestError(testName, test);
        	if (testResultMatcher.group(2).equals("WARNING"))
        		recordTestWarning(testName, test);
        }    	
    }    
    
    private void recordTestOutput(@NotNull String testName, @NotNull String line)
    {
        if (StringUtils.isNotEmpty(testName))
        {
        	StringBuilder currentTestOutput = testOutput.get(testName);
        	if (currentTestOutput == null)
        		testOutput.put(testName, new StringBuilder(line));
        	else
        		currentTestOutput.append(line);
        }
    }

    private void recordTestWarning(@NotNull String testName, @NotNull String line)
    {
        if (StringUtils.isNotEmpty(testName))
        {
        	StringBuilder currentTestWarnings = testWarnings.get(testName);
        	if (currentTestWarnings == null)
        		testWarnings.put(testName, new StringBuilder(line));
        	else
        		currentTestWarnings.append(line);
        }
    }
    
    private void recordTestError(@NotNull String testName, @NotNull String line)
    {
        if (StringUtils.isNotEmpty(testName))
        {
        	StringBuilder currentTestErrors = testErrors.get(testName);
        	if (currentTestErrors == null)
        		testErrors.put(testName, new StringBuilder(line));
        	else
        		currentTestErrors.append(line);
        }
    }
    
    private void endTestCase()
    {
        currentTestInput = new StringBuilder();
    }
}