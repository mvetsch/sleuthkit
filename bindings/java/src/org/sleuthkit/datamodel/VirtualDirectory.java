/*
 * SleuthKit Java Bindings
 * 
 * Copyright 2011-2016 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.datamodel;

import java.util.Collections;
import java.util.List;
import org.sleuthkit.datamodel.TskData.FileKnown;
import org.sleuthkit.datamodel.TskData.TSK_FS_ATTR_TYPE_ENUM;
import org.sleuthkit.datamodel.TskData.TSK_FS_META_TYPE_ENUM;
import org.sleuthkit.datamodel.TskData.TSK_FS_NAME_FLAG_ENUM;
import org.sleuthkit.datamodel.TskData.TSK_FS_NAME_TYPE_ENUM;

/**
 * A virtual directory that can be used as a parent for unallocated space files,
 * carved files, or derived files. A virtual directory can also be a data
 * source, with local/logical files as its children. Not a file system
 * directory.
 */
public class VirtualDirectory extends AbstractFile {

	/**
	 * The name given to a virtual directory that contains unallocated space
	 * files.
	 */
	public static final String NAME_UNALLOC = "$Unalloc"; //NON-NLS

	/**
	 * The name given to a virtual directory that contains carved files.
	 */
	public static final String NAME_CARVED = "$CarvedFiles"; //NON-NLS

	/**
	 * Constructs a virtual directory that can be used as a parent for
	 * unallocated space files, carved files, or derived files. A virtual
	 * directory can also be a data source, with local/logical files as its
	 * children. Not a file system directory.
	 *
	 * @param db The case database.
	 * @param objId The object id of the virtual directory.
	 * @param dataSourceObjectId The object id of the data source for the
	 * virtual directory; same as objId if the virtual directory is a data
	 * source.
	 * @param name The name of the virtual directory.
	 * @param dirType The TSK_FS_NAME_TYPE_ENUM for the virtual directory.
	 * @param metaType The TSK_FS_META_TYPE_ENUM for the virtual directory.
	 * @param dirFlag The TSK_FS_META_TYPE_ENUM for the virtual directory.
	 * @param metaFlags The meta flags for the virtual directory.
	 * @param size The size of the virtual directory, should be zero.
	 * @param md5Hash The MD5 hash for the virtual directory.
	 * @param knownState The known state for the virtual directory
	 * @param parentPath The parent path for the virtual directory, should be
	 * "/" if the virtual directory is a data source.
	 */
	VirtualDirectory(SleuthkitCase db, 
			long objId, 
			long dataSourceObjectId, 
			String name, 
			TSK_FS_NAME_TYPE_ENUM dirType, TSK_FS_META_TYPE_ENUM metaType, 
			TSK_FS_NAME_FLAG_ENUM dirFlag, short metaFlags,
			String md5Hash, FileKnown knownState, 
			String parentPath) {
		super(db, objId, dataSourceObjectId, TSK_FS_ATTR_TYPE_ENUM.TSK_FS_ATTR_TYPE_DEFAULT, (short) 0, name,
				TskData.TSK_DB_FILES_TYPE_ENUM.VIRTUAL_DIR, 0L, 0, dirType, metaType, dirFlag,
				metaFlags, 0L, 0L, 0L, 0L, 0L, (short) 0, 0, 0, md5Hash, knownState, parentPath, null);		
	}

	/**
	 * Indicates whether or not this virtual directory is a data source.
	 *
	 * @return True or false.
	 */
	public boolean isDataSource() {
		return (this.getDataSourceObjectId() == this.getId());
	}

	/**
	 * Gets the data source (e.g., image, virtual directory, etc.) for this
	 * virtual directory. If the virtual directory is itself a data source,
	 * returns the virtual directory.
	 *
	 * @return The data source.
	 * @throws TskCoreException if there was an error querying the case
	 * database.
	 */
	@Override
	public Content getDataSource() throws TskCoreException {
		if (this.getDataSourceObjectId() == this.getId()) {
			// This virtual directory is a data source.
			return this;
		} else {
			return super.getDataSource();
		}
	}

	/**
	 * Gets the children of this virtual directory.
	 *
	 * @return List of children.
	 * @throws TskCoreException if there was an error querying the case
	 * database.
	 */
	@Override
	public List<Content> getChildren() throws TskCoreException {
		return getSleuthkitCase().getAbstractFileChildren(this);
	}

	/**
	 * Gets the object ids of the children of this virtual directory.
	 *
	 * @return List of child object ids.
	 * @throws TskCoreException if there was an error querying the case
	 * database.
	 */
	@Override
	public List<Long> getChildrenIds() throws TskCoreException {
		return getSleuthkitCase().getAbstractFileChildrenIds(this);
	}

	/**
	 * Gets the extents in terms of byte addresses of this virtual directory
	 * within its data source, always an empty list.
	 *
	 * @return An empty list.
	 * @throws TskCoreException if there was an error querying the case
	 * database.
	 */
	@Override
	public List<TskFileRange> getRanges() throws TskCoreException {
		return Collections.<TskFileRange>emptyList();
	}

	/**
	 * Does nothing, a virtual directory cannot be opened, read, or closed.
	 */
	@Override
	public void close() {
	}

	/**
	 * Indicates whether or not this virtual directory is the root of a file
	 * system, always returns false.
	 *
	 * @return False.
	 */
	@Override
	public boolean isRoot() {
		return false;
	}

	/**
	 * Accepts a content visitor (Visitor design pattern).
	 *
	 * @param visitor A ContentVisitor supplying an algorithm to run using this
	 * virtual directory as input.
	 * @return The output of the algorithm.
	 */
	@Override
	public <T> T accept(ContentVisitor<T> visitor) {
		return visitor.visit(this);
	}

	/**
	 * Accepts a Sleuthkit item visitor (Visitor design pattern).
	 *
	 * @param visitor A SleuthkitItemVisitor supplying an algorithm to run using
	 * this virtual directory as input.
	 * @return The output of the algorithm.
	 */
	@Override
	public <T> T accept(SleuthkitItemVisitor<T> visitor) {
		return visitor.visit(this);
	}

	/**
	 * Provides a string representation of this virtual directory.
	 *
	 * @param preserveState True if state should be included in the string
	 * representation of this object.
	 * @throws TskCoreException if there was an error querying the case
	 * database.
	 */
	@Override
	public String toString(boolean preserveState) {
		return super.toString(preserveState) + "VirtualDirectory [\t" + "]\t"; //NON-NLS
	}

	/**
	 * Constructs a virtual directory that can be used as a parent for
	 * unallocated space files, carved files, or derived files. A virtual
	 * directory can also be a data source, with local/logical files as its
	 * children. Not a file system directory.
	 *
	 * @param db The case database.
	 * @param objId The object id of the virtual directory.
	 * @param dataSourceObjectId The object id of the data source for the
	 * virtual directory; same as objId if the virtual directory is a data
	 * source.
	 * @param name The name of the virtual directory.
	 * @param dirType The TSK_FS_NAME_TYPE_ENUM for the virtual directory.
	 * @param metaType The TSK_FS_META_TYPE_ENUM for the virtual directory.
	 * @param dirFlag The TSK_FS_META_TYPE_ENUM for the virtual directory.
	 * @param metaFlags The meta flags for the virtual directory.
	 * @param size The size value for the virtual directory
	 * @param md5Hash The MD5 hash for the virtual directory.
	 * @param knownState The known state for the virtual directory
	 * @param parentPath The parent path for the virtual directory, should be
	 * "/" if the virtual directory is a data source.
	 * @deprecated Do not make subclasses outside of this package.
	 */
	@Deprecated
	@SuppressWarnings("deprecation")
	VirtualDirectory(SleuthkitCase db, 
			long objId, 
			String name, 
			TSK_FS_NAME_TYPE_ENUM dirType, TSK_FS_META_TYPE_ENUM metaType, 
			TSK_FS_NAME_FLAG_ENUM dirFlag, short metaFlags,
			long size, 
			String md5Hash, FileKnown knownState, 
			String parentPath) {
		this(db, objId, db.getDataSourceObjectId(objId), name, dirType, metaType, dirFlag, metaFlags, md5Hash, knownState, parentPath);
	}
	
}
