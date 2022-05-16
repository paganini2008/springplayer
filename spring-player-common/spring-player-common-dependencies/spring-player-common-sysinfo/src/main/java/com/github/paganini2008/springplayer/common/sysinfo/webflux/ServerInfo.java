package com.github.paganini2008.springplayer.common.sysinfo.webflux;

import static reactor.netty.Metrics.ACTIVE_CONNECTIONS;
import static reactor.netty.Metrics.CONNECTION_PROVIDER_PREFIX;
import static reactor.netty.Metrics.IDLE_CONNECTIONS;
import static reactor.netty.Metrics.PENDING_CONNECTIONS;
import static reactor.netty.Metrics.TOTAL_CONNECTIONS;

import java.lang.management.ManagementFactory;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import com.github.paganini2008.devtools.ArrayUtils;
import com.github.paganini2008.devtools.NumberUtils;
import com.github.paganini2008.devtools.io.FileUtils;
import com.github.paganini2008.devtools.net.NetUtils;
import com.github.paganini2008.devtools.time.Duration;
import com.github.paganini2008.devtools.time.LocalDateTimeUtils;
import com.github.paganini2008.springplayer.common.utils.JacksonUtils;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.Data;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

/**
 * 
 * ServerInfo
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class ServerInfo {

	@Data
	public class Cpu {

		private int count;
		private long total;
		private long sys;
		private long used;
		private long iowait;
		private long idle;
		private double[] usages;
		private double[] loadAverage;

		public long getTotal() {
			return total;
		}

		public String getSys() {
			return NumberUtils.format((double) sys / total * 100, "0.#%");
		}

		public String getUsed() {
			return NumberUtils.format((double) used / total * 100, "0.#%");
		}

		public String getIowait() {
			return NumberUtils.format((double) iowait / total * 100, "0.#%");
		}

		public String getIdle() {
			return NumberUtils.format((double) idle / total, "0.#%");
		}

		public String[] getUsages() {
			return Arrays.stream(usages).boxed().map(load -> load < 0 ? "N/A" : String.format("%.1f%%", load * 100))
					.toArray(i -> new String[i]);
		}

		public String[] getLoadAverage() {
			return Arrays.stream(loadAverage).boxed().map(load -> load < 0 ? "N/A" : String.format("%.2f", load))
					.toArray(i -> new String[i]);
		}
	}

	@Data
	public class Jvm {

		private long total;
		private long max;
		private long free;
		private String version;
		private String home;
		private String userDir;

		public String getTotal() {
			return FileUtils.formatSize(total, 1);
		}

		public String getMax() {
			return FileUtils.formatSize(max, 1);
		}

		public String getFree() {
			return FileUtils.formatSize(free, 1);
		}

		public String getUsed() {
			return FileUtils.formatSize(total - free, 1);
		}

		public String getVersion() {
			return version;
		}

		public String getHome() {
			return home;
		}

		public String getUsage() {
			return NumberUtils.format((double) (total - free) / total, "0.#%");
		}

		public String getName() {
			return ManagementFactory.getRuntimeMXBean().getVmName();
		}

		public String getStartTime() {
			long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
			return LocalDateTimeUtils.toLocalDateTime(startTime, null).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		}

		public String getRunTime() {
			long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
			return Duration.DAY.format(System.currentTimeMillis() - startTime);
		}
	}

	@Data
	public class Mem {

		private long total;
		private long free;
		private long swapTotal;
		private long swapUsed;

		public String getTotal() {
			return FileUtils.formatSize(total, 1);
		}

		public String getFree() {
			return FileUtils.formatSize(free, 1);
		}

		public String getSwapTotal() {
			return FileUtils.formatSize(swapTotal, 1);
		}

		public String getSwapUsed() {
			return FileUtils.formatSize(swapUsed, 1);
		}

		public String getUsed() {
			return FileUtils.formatSize(total - free, 1);
		}

		public String getUsage() {
			return NumberUtils.format((double) (total - free) / total, "0.#%");
		}
	}

	@Data
	public class Sys {
		private String deviceName;
		private String hostName;
		private String localAddr;
		private String osName;
		private String osArch;
	}

	@Data
	public class SysFile {

		private String dirName;
		private String typeName;
		private String name;
		private long total;
		private long free;

		public String getTotal() {
			return FileUtils.formatSize(total, 1);
		}

		public String getFree() {
			return FileUtils.formatSize(free, 1);
		}

		public String getUsed() {
			return FileUtils.formatSize(total - free, 1);
		}

		public String getUsage() {
			return NumberUtils.format((double) (total - free) / total, "0.#%");
		}
	}

	@Data
	public class HttpPool {

		private long total;
		private long idle;
		private long active;
		private long pending;

	}

	private Cpu cpu = new Cpu();

	private Mem mem = new Mem();

	private Jvm jvm = new Jvm();

	private Sys sys = new Sys();

	private List<SysFile> sysFiles = new LinkedList<>();

	private HttpPool httpPool = new HttpPool();

	public Cpu getCpu() {
		return cpu;
	}

	public void setCpu(Cpu cpu) {
		this.cpu = cpu;
	}

	public Mem getMem() {
		return mem;
	}

	public void setMem(Mem mem) {
		this.mem = mem;
	}

	public Jvm getJvm() {
		return jvm;
	}

	public void setJvm(Jvm jvm) {
		this.jvm = jvm;
	}

	public Sys getSys() {
		return sys;
	}

	public void setSys(Sys sys) {
		this.sys = sys;
	}

	public List<SysFile> getSysFiles() {
		return sysFiles;
	}

	public void setSysFiles(List<SysFile> sysFiles) {
		this.sysFiles = sysFiles;
	}

	public HttpPool getHttpPool() {
		return httpPool;
	}

	public void setHttpPool(HttpPool httpPool) {
		this.httpPool = httpPool;
	}

	public void refresh() throws Exception {
		SystemInfo si = new SystemInfo();
		HardwareAbstractionLayer hal = si.getHardware();
		setCpuInfo(hal.getProcessor());
		setMemInfo(hal.getMemory());
		setSysInfo();
		setJvmInfo();
		setSysFiles(si.getOperatingSystem());
		setHttpPoolInfo();
	}

	private void setCpuInfo(CentralProcessor processor) {
		long[] prevTicks = processor.getSystemCpuLoadTicks();
		Util.sleep(1000L);
		long[] ticks = processor.getSystemCpuLoadTicks();
		long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
		long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
		long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
		long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
		long sys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
		long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
		long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
		long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
		long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;

		cpu.setCount(processor.getLogicalProcessorCount());
		cpu.setTotal(totalCpu);
		cpu.setSys(sys);
		cpu.setUsed(user);
		cpu.setIowait(iowait);
		cpu.setIdle(idle);
		double[] usages = processor.getProcessorCpuLoadBetweenTicks();
		cpu.setUsages(usages);
		double[] loadAverage = processor.getSystemLoadAverage(3);
		cpu.setLoadAverage(loadAverage);
	}

	private void setMemInfo(GlobalMemory memory) {
		mem.setTotal(memory.getTotal());
		mem.setFree(memory.getAvailable());
		mem.setSwapTotal(memory.getSwapTotal());
		mem.setSwapUsed(memory.getSwapUsed());
	}

	private void setSysInfo() {
		Properties props = System.getProperties();
		sys.setHostName(NetUtils.getHostName());
		sys.setLocalAddr(NetUtils.getLocalHost());
		sys.setDeviceName(props.getProperty("user.name"));
		sys.setOsName(props.getProperty("os.name"));
		sys.setOsArch(props.getProperty("os.arch"));
	}

	private void setJvmInfo() {
		Runtime r = Runtime.getRuntime();
		jvm.setTotal(r.totalMemory());
		jvm.setMax(r.maxMemory());
		jvm.setFree(r.freeMemory());
		Properties props = System.getProperties();
		jvm.setVersion(props.getProperty("java.version"));
		jvm.setHome(props.getProperty("java.home"));
		jvm.setUserDir(props.getProperty("user.dir"));
	}

	private void setSysFiles(OperatingSystem os) {
		FileSystem fileSystem = os.getFileSystem();
		OSFileStore[] fsArray = fileSystem.getFileStores();
		for (OSFileStore fs : fsArray) {
			long total = fs.getTotalSpace();
			long free = fs.getUsableSpace();
			SysFile sysFile = new SysFile();
			sysFile.setDirName(fs.getMount());
			sysFile.setTypeName(fs.getType());
			sysFile.setName(fs.getName());
			sysFile.setTotal(total);
			sysFile.setFree(free);
			sysFiles.add(sysFile);
		}
	}

	private void setHttpPoolInfo() {
		final String[] meterNames = { CONNECTION_PROVIDER_PREFIX + TOTAL_CONNECTIONS, CONNECTION_PROVIDER_PREFIX + IDLE_CONNECTIONS,
				CONNECTION_PROVIDER_PREFIX + ACTIVE_CONNECTIONS, CONNECTION_PROVIDER_PREFIX + PENDING_CONNECTIONS };
		MeterRegistry meterRegistry = io.micrometer.core.instrument.Metrics.globalRegistry;
		List<Meter> meters = meterRegistry.getMeters();
		Map<String, Number> meterValues = meters.stream().filter(m -> ArrayUtils.contains(meterNames, m.getId().getName()))
				.collect(Collectors.toMap(m -> m.getId().getName(), m -> meterRegistry.get(m.getId().getName()).gauge().value(),
						(a, b) -> a, HashMap::new));
		if (meterValues.size() > 0) {
			httpPool.setTotal(meterValues.get(CONNECTION_PROVIDER_PREFIX + TOTAL_CONNECTIONS).longValue());
			httpPool.setIdle(meterValues.get(CONNECTION_PROVIDER_PREFIX + IDLE_CONNECTIONS).longValue());
			httpPool.setActive(meterValues.get(CONNECTION_PROVIDER_PREFIX + ACTIVE_CONNECTIONS).longValue());
			httpPool.setPending(meterValues.get(CONNECTION_PROVIDER_PREFIX + PENDING_CONNECTIONS).longValue());
		}

	}

	public static void main(String[] args) throws Exception {
		ServerInfo serverInfo = new ServerInfo();
		serverInfo.refresh();
		System.out.println(JacksonUtils.toJsonString(serverInfo));
	}
}
