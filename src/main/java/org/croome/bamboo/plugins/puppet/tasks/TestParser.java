package org.croome.bamboo.plugins.puppet.tasks;

import com.atlassian.bamboo.results.tests.TestResults;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Defines a test parser for the Puppet Lint plugin
 */
public interface TestParser
{
    /**
     * Successful tests for processed logs
     * @return successful tests
     */
    Set<TestResults> getSuccessfulTestResults();
    
    /**
     * Failing tests for processed logs
     * @return failing tests
     */
    Set<TestResults> getFailingTestResults(Boolean failOnWarning);

    /**
     * Processes line of log
     * @param line
     */
    void processLine(@NotNull String line);

	
    
}