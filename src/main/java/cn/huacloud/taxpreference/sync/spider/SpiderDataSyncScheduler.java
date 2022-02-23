package cn.huacloud.taxpreference.sync.spider;

import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.config.SpiderDataSyncConfig;
import cn.huacloud.taxpreference.services.sync.mapper.SpiderDataSyncMapper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 爬虫数据同步调度器
 * @author wangkh
 */
@EnableScheduling
@RequiredArgsConstructor
@Component
public class SpiderDataSyncScheduler implements InitializingBean, SchedulingConfigurer {

    private final SpiderDataSyncConfig spiderDataSyncConfig;

    private final List<DataSyncJob<?, ?>> dataSyncJobs;

    private final SpiderDataSyncMapper spiderDataSyncMapper;

    // 初始化创建，不交由spring管理
    private DefaultDataSyncJobExecutor dataSyncJobExecutor;

    public synchronized void executeJobs(DataSyncJobParam dataSyncJobParam) {
        if (!spiderDataSyncConfig.getEnabled()) {
            // 未开启爬虫数据同步直接抛出异常
            throw BizCode._4405.exception();
        }
        // 获取指定同步的文档类型
        List<DocType> docTypes = dataSyncJobParam.getDocTypes();
        if (CollectionUtils.isEmpty(docTypes)) {
            // 若果指定类型为空，默认同步所有文档
            docTypes = Arrays.asList(DocType.values());
        }
        // 循环执行同步任务
        for (DataSyncJob<?, ?> dataSyncJob : dataSyncJobs) {
            if (docTypes.contains(dataSyncJob.getDocType())) {
                dataSyncJobExecutor.execute(dataSyncJob, dataSyncJobParam);
            }
        }
    }

    /**
     * 调度器初始化
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (!spiderDataSyncConfig.getEnabled()) {
            // 关闭爬虫同步功能直接返回
            return;
        }
        // 初始化爬虫数据库连接信息
        SpiderDataSyncConfig.DataSource dataSource = spiderDataSyncConfig.getDataSource();
        if (dataSource == null) {
            throw new IllegalArgumentException("开启爬虫数据同步，爬虫库数据连接配置信息不能为空");
        }
        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        BeanUtils.copyProperties(dataSource, dataSourceProperties);
        dataSourceProperties.setBeanClassLoader(SpiderDataSyncScheduler.class.getClassLoader());
        dataSourceProperties.afterPropertiesSet();
        // 创建jdbcTemplate
        // 初始化创建，不交由spring管理
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceProperties.initializeDataSourceBuilder().build());

        SpiderDataSyncConfig.Minio minio = spiderDataSyncConfig.getMinio();
        if (minio == null) {
            throw new IllegalArgumentException("开启爬虫数据同步，minio配置信息不能为空");
        }


        // 默认的任务执行器
        dataSyncJobExecutor = new DefaultDataSyncJobExecutor(jdbcTemplate, spiderDataSyncMapper);
        // 任务排序
        dataSyncJobs.sort(Comparator.comparing(DataSyncJob::order));
    }

    /**
     * 自定义调度配置
     */
    @Override
    public void configureTasks(@NotNull ScheduledTaskRegistrar taskRegistrar) {
        if (!spiderDataSyncConfig.getEnabled()) {
            return;
        }
        // 自定义cron任务
        CronTask cronTask = new CronTask(() -> {
            // TODO 未定义自动同步任务
        }, spiderDataSyncConfig.getCron());
        taskRegistrar.addCronTask(cronTask);
    }
}
