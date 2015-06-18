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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jboss.forge.shell.ShellPrintWriter;


/**
 * TreeRepresentation for Files
 * @author Florian Hirsch
 */
public class TreeNode implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String EMPTY = "";
	
	private static final String SPACE = "    ";
	
	private static final String LINE_AND_SPACE = "\u2503   "; 

	private static final String FILE = "\u2523\u2501\u2501 ";

	private static final String TAIL = "\u2517\u2501\u2501 "; 
	
	private File file;
    
	private boolean isBasedir;
	
	private List<TreeNode> children;

    public TreeNode(File file, boolean isBasedir) {
        this.file = file;
        this.isBasedir = isBasedir;
        this.children = new ArrayList<TreeNode>();
    }
    
    public void addChildren(TreeNode child) {
    	children.add(child);
    }
    
    public void print(ShellPrintWriter writer) {
        print(EMPTY, true, writer);
    }

    private void print(String prefix, boolean isTail, ShellPrintWriter writer) {
    	String line = isBasedir 
    			? file.getAbsolutePath() 
				: String.format("%s%s%s", prefix, isTail ? TAIL : FILE, file.getName());
		writer.println(line);
        for (Iterator<TreeNode> iterator = children.iterator(); iterator.hasNext();) {
        	String next = String.format("%s%s", prefix, isTail ? isBasedir ? EMPTY : SPACE : LINE_AND_SPACE);
        	iterator.next().print(next, !iterator.hasNext(), writer);
        }
    }
    
    public File getFile() {
    	return file;
    }
    
    @Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
}