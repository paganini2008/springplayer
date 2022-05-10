package com.github.paganini2008.springplayer.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import com.github.paganini2008.devtools.io.FileUtils;
import com.github.paganini2008.devtools.io.IOUtils;

/**
 * 
 * JStackUtils
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public abstract class JStackUtils {

	public static void jstack(File outputFile) throws Exception {
		FileOutputStream fos = null;
		try {
			fos = FileUtils.openOutputStream(outputFile);
			jstack(fos);
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

	public static void jstack(OutputStream stream) throws Exception {
		ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
		for (ThreadInfo threadInfo : threadMxBean.dumpAllThreads(true, true)) {
			stream.write(getThreadDumpString(threadInfo).getBytes());
		}
	}

	public static String jstackAsString() throws Exception {
		StringWriter writer = new StringWriter();
		jstack(writer);
		return writer.toString();
	}

	public static void jstack(Writer writer) throws Exception {
		ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
		for (ThreadInfo threadInfo : threadMxBean.dumpAllThreads(true, true)) {
			writer.write(getThreadDumpString(threadInfo));
		}
	}

	private static String getThreadDumpString(ThreadInfo threadInfo) {
		StringBuilder sb = new StringBuilder(
				"\"" + threadInfo.getThreadName() + "\"" + " Id=" + threadInfo.getThreadId() + " " + threadInfo.getThreadState());
		if (threadInfo.getLockName() != null) {
			sb.append(" on " + threadInfo.getLockName());
		}
		if (threadInfo.getLockOwnerName() != null) {
			sb.append(" owned by \"" + threadInfo.getLockOwnerName() + "\" Id=" + threadInfo.getLockOwnerId());
		}
		if (threadInfo.isSuspended()) {
			sb.append(" (suspended)");
		}
		if (threadInfo.isInNative()) {
			sb.append(" (in native)");
		}
		sb.append('\n');
		int i = 0;

		StackTraceElement[] stackTrace = threadInfo.getStackTrace();
		MonitorInfo[] lockedMonitors = threadInfo.getLockedMonitors();
		for (; i < stackTrace.length && i < 32; i++) {
			StackTraceElement ste = stackTrace[i];
			sb.append("\tat " + ste.toString());
			sb.append('\n');
			if (i == 0 && threadInfo.getLockInfo() != null) {
				Thread.State ts = threadInfo.getThreadState();
				switch (ts) {
				case BLOCKED:
					sb.append("\t-  blocked on " + threadInfo.getLockInfo());
					sb.append('\n');
					break;
				case WAITING:
					sb.append("\t-  waiting on " + threadInfo.getLockInfo());
					sb.append('\n');
					break;
				case TIMED_WAITING:
					sb.append("\t-  waiting on " + threadInfo.getLockInfo());
					sb.append('\n');
					break;
				default:
				}
			}

			for (MonitorInfo mi : lockedMonitors) {
				if (mi.getLockedStackDepth() == i) {
					sb.append("\t-  locked " + mi);
					sb.append('\n');
				}
			}
		}
		if (i < stackTrace.length) {
			sb.append("\t...");
			sb.append('\n');
		}

		LockInfo[] locks = threadInfo.getLockedSynchronizers();
		if (locks.length > 0) {
			sb.append("\n\tNumber of locked synchronizers = " + locks.length);
			sb.append('\n');
			for (LockInfo li : locks) {
				sb.append("\t- " + li);
				sb.append('\n');
			}
		}
		sb.append('\n');
		return sb.toString();
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(JStackUtils.jstackAsString());
	}

}
