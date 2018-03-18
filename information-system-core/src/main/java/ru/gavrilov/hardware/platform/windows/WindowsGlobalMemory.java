package ru.gavrilov.hardware.platform.windows;

import com.sun.jna.platform.win32.Kernel32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.hardware.common.AbstractGlobalMemory;
import ru.gavrilov.jna.platform.windows.Psapi;
import ru.gavrilov.util.windows.WmiUtil;

import java.util.List;
import java.util.Map;

public class WindowsGlobalMemory extends AbstractGlobalMemory {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(WindowsGlobalMemory.class);

    private transient Psapi.PERFORMANCE_INFORMATION perfInfo = new Psapi.PERFORMANCE_INFORMATION();

    private long lastUpdate = 0;

    @Override
    protected void updateMeminfo() {
        long now = System.currentTimeMillis();
        if (now - this.lastUpdate > 100) {
            if (!Psapi.INSTANCE.GetPerformanceInfo(this.perfInfo, this.perfInfo.size())) {
                LOG.error("Failed to get Performance Info. Error code: {}", Kernel32.INSTANCE.GetLastError());
                return;
            }
            this.memAvailable = this.perfInfo.PageSize.longValue() * this.perfInfo.PhysicalAvailable.longValue();
            this.memTotal = this.perfInfo.PageSize.longValue() * this.perfInfo.PhysicalTotal.longValue();
            this.swapTotal = this.perfInfo.PageSize.longValue()
                    * (this.perfInfo.CommitLimit.longValue() - this.perfInfo.PhysicalTotal.longValue());
            this.lastUpdate = now;
        }
    }

    @Override
    protected void updateSwap() {
        updateMeminfo();
        Map<String, List<Long>> usage = WmiUtil.selectUint32sFrom(null, "Win32_PerfRawData_PerfOS_PagingFile",
                "PercentUsage,PercentUsage_Base", "WHERE Name=\"_Total\"");
        if (!usage.get("PercentUsage").isEmpty()) {
            this.swapUsed = this.swapTotal * usage.get("PercentUsage").get(0) / usage.get("PercentUsage_Base").get(0);
        }
    }
}
