# Puppet Bamboo Plugin

Continuous Integration for your Puppet code via Atlassian Bamboo.

This is a plugin for Atlassian Bamboo, integrating Puppet tools such as 
puppet-lint, puppet parser validate and MCollective.  

The goal is to fully-automate continuous deployment of Puppet code via Jira.

## Installation

Download and install from the [Atlassian Marketplace](https://marketplace.atlassian.com/plugins/org.croome.bamboo.plugins.bamboo-puppet-plugin)

## Current Features
Via a Bamboo task, this plugin enables you to run:
* Puppet Lint - Enforce [Puppet style guidelines](http://docs.puppetlabs.com/guides/style_guide.html) 
with `puppet-lint`

## Upcoming Features
* Puppet syntax validation - Ensure manifests compile with `puppet parser validate`
* Remote job execution - Run [mcollective](http://puppetlabs.com/mcollective) 
commands on remote nodes such as `mco puppet runonce`
* Remote integration testing - Run [serverspec](http://serverspec.org/) tests on remote nodes

For Puppet RSpec testing, see the 
[Bamboo Rake Plugin](https://marketplace.atlassian.com/plugins/au.id.wolfe.bamboo.rake-bamboo-plugin)

## Usage

### Puppet Lint Task
The Puppet Lint Task requires the [puppet-lint gem](http://rubygems.org/gems/puppet-lint) 
to be installed on build agents.  See http://puppet-lint.com for installation instructions 
and explanation of the various options.

## Windows Support

This plugin has not been tested on Windows.  Reports (postive and negative) welcome.

## Reporting bugs or incorrect results

If you find a bug in puppet-lint or its results, please create an issue in the
[issue tracker](https://github.com/croomes/bamboo-puppet-plugin/issues/).  Bonus
points will be awarded if you also include a patch that fixes the issue.

I don't consider myself a Java Developer, so please bear this in mind.  Non-bugfix 
code improvements are also very welcome.

## Thank You and Acknowledgements

Many thanks to the following people:

 * Tim Sharpe (@rodjek) - Author of [puppet-lint](http://puppet-lint.com/)

## Links

* [Puppet](http://puppetlabs.com/puppet)
* [puppet-lint](http://puppet-lint.com)
* [Bamboo](http://www.atlassian.com/software/bamboo/overview)

## License

Copyright 2013 Simon Croome

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
