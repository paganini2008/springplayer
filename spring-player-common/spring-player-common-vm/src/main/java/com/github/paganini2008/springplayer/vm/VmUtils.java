package com.github.paganini2008.springplayer.vm;

import java.io.InputStream;
import java.nio.charset.Charset;

import com.github.paganini2008.devtools.ArrayUtils;
import com.github.paganini2008.devtools.io.IOUtils;
import com.sun.tools.attach.VirtualMachine;

import sun.tools.attach.HotSpotVirtualMachine;

/**
 * 
 * VmUtils
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public abstract class VmUtils {

	public static String getThreadDumpInfo(int pid, Object... args) throws Exception {
		HotSpotVirtualMachine virtualMachine = (HotSpotVirtualMachine) VirtualMachine.attach(String.valueOf(pid));
		try {
			InputStream ins = virtualMachine.remoteDataDump(ArrayUtils.isNotEmpty(args) ? args : new Object[0]);
			return IOUtils.toString(ins, Charset.defaultCharset());
		} finally {
			virtualMachine.detach();
		}
	}

	public static String getHeapHistoInfo(int pid, Object... args) throws Exception {
		HotSpotVirtualMachine virtualMachine = (HotSpotVirtualMachine) VirtualMachine.attach(String.valueOf(pid));
		try {
			InputStream ins = virtualMachine.heapHisto(ArrayUtils.isNotEmpty(args) ? args : new Object[0]);
			return IOUtils.toString(ins, Charset.defaultCharset());
		} finally {
			virtualMachine.detach();
		}
	}

	public static String dumpHeap(int pid, Object... args) throws Exception {
		HotSpotVirtualMachine virtualMachine = (HotSpotVirtualMachine) VirtualMachine.attach(String.valueOf(pid));
		try {
			InputStream ins = virtualMachine.dumpHeap(ArrayUtils.isNotEmpty(args) ? args : new Object[0]);
			return IOUtils.toString(ins, Charset.defaultCharset());
		} finally {
			virtualMachine.detach();
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println(dumpHeap(15664, "-permstat"));
	}

}
