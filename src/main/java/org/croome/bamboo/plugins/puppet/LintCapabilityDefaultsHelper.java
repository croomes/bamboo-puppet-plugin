package org.croome.bamboo.plugins.puppet;

import com.atlassian.bamboo.v2.build.agent.capability.Capability;
import com.atlassian.bamboo.v2.build.agent.capability.AbstractFileCapabilityDefaultsHelper;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityImpl;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilitySet;

import org.apache.log4j.Logger;
import org.croome.bamboo.plugins.puppet.tasks.lint.LintTask;
import org.jetbrains.annotations.NotNull;

public class LintCapabilityDefaultsHelper extends AbstractFileCapabilityDefaultsHelper
{
	private static final Logger log = Logger.getLogger(LintCapabilityDefaultsHelper.class);

    @NotNull
    @Override
    protected String getExecutableName()
    {
        return LintTask.PUPPET_LINT_EXE_NAME;
    }

    @NotNull
    @Override
    protected String getCapabilityKey()
    {
    	return LintTask.PUPPET_LINT_CAPABILITY_PREFIX + "." + LintTask.PUPPET_LINT_LABEL;
    }
    
    @NotNull
    public CapabilitySet addDefaultCapabilities(@NotNull final CapabilitySet capabilitySet)
    {
    	Capability capability = new CapabilityImpl(LintTask.PUPPET_LINT_CAPABILITY_PREFIX + "." + LintTask.PUPPET_LINT_LABEL, LintTask.PUPPET_LINT_EXE_NAME);
        capabilitySet.addCapability(capability);
    	return capabilitySet;
    }
}