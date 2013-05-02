/*
** Copyright 2013 Software Composition Group, University of Bern. All rights reserved.
*/
package ch.unibe.scg.lexica;

import java.nio.file.attribute.FileTime;
import java.util.Objects;

public class FileInfo {

	private final long size;
	private final FileTime fileTime;
	
	public FileInfo(long size, FileTime fileTime) {
		Objects.requireNonNull(fileTime);
		
		this.size = size;
		this.fileTime = fileTime;
	}

	public long getSize() {
		return size;
	}
	
	public FileTime getFileTime() {
		return fileTime;
	}

}
