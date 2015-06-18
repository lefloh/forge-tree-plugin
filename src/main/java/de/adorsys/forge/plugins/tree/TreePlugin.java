/**
 * Copyright (C) 2013 Florian Hirsch
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.adorsys.forge.plugins.tree;

import java.io.File;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.ShellMessages;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.DefaultCommand;
import org.jboss.forge.shell.plugins.Help;
import org.jboss.forge.shell.plugins.Option;
import org.jboss.forge.shell.plugins.PipeOut;
import org.jboss.forge.shell.plugins.Plugin;

/**
 * Simple Implementation of the Tree Command
 * @author Florian Hirsch
 */
@Alias("tree")
@Help( "usage: tree [-ad] [starting-directory]\n")
public class TreePlugin implements Plugin {
	
	@Inject
	private Shell shell;
	
	@DefaultCommand
	public void tree(
			@Option(name = "all", shortName = "a", flagOnly = true, defaultValue = "false", help = "All files are listed.") boolean all,
			@Option(name = "directoriesOnly", shortName = "d", flagOnly = true, defaultValue = "false", help = "List directories only.") boolean directoriesOnly,
			@Option(help = "starting directory") String directoryPath,
			PipeOut out) {
		File workingDir = getWorkingDir(directoryPath);
		if (!workingDir.exists()) {
			ShellMessages.error(out, String.format("Error opening Directory %s", directoryPath));
			return;
		}
		processNode(new TreeNode(workingDir, true), all, directoriesOnly).print(out);
	}
	
	private TreeNode processNode(TreeNode node, boolean all, boolean directoriesOnly) {
		for (File file : node.getFile().listFiles()) {
			if (!all && file.isHidden()) {
				continue;
			}
			if (directoriesOnly && !file.isDirectory()) {
				continue;
			}
			if (file.isDirectory()) {
				node.addChildren(processNode(new TreeNode(file, false), all, directoriesOnly));
			} else {
				node.addChildren(new TreeNode(file, false));
			}
		}
		return node;
	}
	
	private File getWorkingDir(String path) {
		File currentDir = shell.getCurrentDirectory().getUnderlyingResourceObject();
		if (StringUtils.isBlank(path)) {
			return currentDir;
		}
		File file = new File(path);
		if (file.isAbsolute()) {
			return file;
		}
		return new File(currentDir, path);
	}
	
}
