package com.github.paganini2008.springplayer.i18n;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.paganini2008.devtools.NumberUtils;
import com.github.paganini2008.devtools.io.Directory;
import com.github.paganini2008.devtools.io.DirectoryFilter;
import com.github.paganini2008.devtools.io.DirectoryWalkerHandler;
import com.github.paganini2008.devtools.io.FileSearchUtils;
import com.github.paganini2008.devtools.io.FileUtils;
import com.github.paganini2008.devtools.io.ForkJoinDirectoryWalker;
import com.github.paganini2008.devtools.io.DirectoryWalker.Progressable;

public class TestMain {

	public static void main(String[] args) throws Exception{
		List<File> bigFiles = new ArrayList<>();
		File directory = new File("C:\\Users\\pagan");
		ForkJoinDirectoryWalker walker = new ForkJoinDirectoryWalker(directory, new DirectoryWalkerHandler() {
			
			@Override
			public void handleDirectoryEnd(File file, Directory directory, int depth) throws IOException {
				if (directory.getLength() > 50 * FileUtils.MB) {
					System.err.println("Show Dir: " + directory.getFile());
					bigFiles.add(directory.getFile());
				}else {
					System.out.println("Show Dir: " + file);
				}
			}
			
			@Override
			public void handleFile(File file, int depth) throws Exception {
				System.out.println("Show File: " + file);
			}
		});
		walker.setProgressable(new Progressable() {

			public void progress(int fileCount, int folderCount, long length, float completedRatio, long elapsed) {
				System.out.println("fileCount: " + fileCount + ", folderCount: " + folderCount + ", length: " + length
						+ ", completedRatio: " + NumberUtils.format(completedRatio, "0.00%") + ", elapsed: " + elapsed);
			}
		});
		Directory fileInfo = walker.walk();
		System.out.println(FileUtils.formatSize(fileInfo.getLength()));
		System.out.println("DirectoryWalker.main()");
		for(File f: bigFiles) {
			System.out.println("BigFile: "+f);
		}
		System.in.read();
		System.out.println("Completed.");
	}
	
	public static void main2(String[] args) throws Exception{
		File directory = new File("C:\\Users\\pagan");
		File[] files = FileSearchUtils.search(directory, new DirectoryFilter() {
			@Override
			public boolean accept(Directory fileInfo) {
				if (fileInfo.getLength() > 50 * FileUtils.MB) {
					return true;
				}
				return false;
			}
		}, 8, 5);
		for (File file : files) {
			System.out.println(file);
		}
	}

}
