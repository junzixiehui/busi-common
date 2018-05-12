package com.junzixiehui.application.recoup.dao;


import com.junzixiehui.application.core.util.DateUtils;
import com.junzixiehui.application.recoup.entity.RecoupJobEntity;
import com.junzixiehui.application.recoup.enums.JobStatusEnum;
import com.junzixiehui.application.recoup.util.RecoupJobDaoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RecoupJobDao {
    @Autowired(required = false)
    @Qualifier("repairshopJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    /**
     * 得到所有待执行job
     *
     * @return
     */
    public RecoupJobEntity findOneByBusCode(String busCode) {
        RecoupJobEntity recoupJobEntity = null;
        try {
            recoupJobEntity = (RecoupJobEntity) this.jdbcTemplate.queryForObject("select id,job_name,job_class,job_status,job_json_param,start_time,complete_time,create_time,effect_time,retry_cur_times,retry_total_times,bus_code from " + RecoupJobDaoHelper.TABLE_NAME + " t where t.bus_code = ?", new Object[]{busCode}, new BeanPropertyRowMapper(RecoupJobEntity.class));
        } catch (EmptyResultDataAccessException e) {//queryForObject查询不到会抛异常
        } catch (IncorrectResultSizeDataAccessException e) {//queryForObject结果多于一个会抛异常
            throw new RuntimeException("busCode[" + busCode + "]查询结果不唯一");
        }
        return recoupJobEntity;
    }

    /**
     * 得到所有待执行job
     * 按照当前重试次数升序[失败次数越多的优先级越低]
     *
     * @return
     */
    public List<Long> getAllToBeExecutedIdList() {
        List<Long> idList = this.jdbcTemplate.queryForList("select id from " + RecoupJobDaoHelper.TABLE_NAME + " where job_status in (?, ?) and start_time <= ? order by retry_cur_times",
                new Object[]{JobStatusEnum.EXECUTE.getCode(), JobStatusEnum.EXECUTING.getCode(), DateUtils.formatNow2Long()},
                Long.class);
        return idList;
    }

    public RecoupJobEntity findOneById(long id) {
        RecoupJobEntity recoupJobEntity = null;
        try {
            recoupJobEntity = (RecoupJobEntity) this.jdbcTemplate.queryForObject("select * from " + RecoupJobDaoHelper.TABLE_NAME + " t where t.id = ?", new Object[]{id}, new BeanPropertyRowMapper(RecoupJobEntity.class));
        } catch (EmptyResultDataAccessException e) {//queryForObject查询不到会抛异常
        }
        return recoupJobEntity;
    }

    public RecoupJobEntity findSimpleOneById(long id) {
        RecoupJobEntity recoupJobEntity = null;
        try {
            recoupJobEntity = (RecoupJobEntity) this.jdbcTemplate.queryForObject("select id,job_name,job_class,job_status,job_json_param,start_time,complete_time,create_time,effect_time,retry_cur_times,retry_total_times,bus_code from " + RecoupJobDaoHelper.TABLE_NAME + " t where t.id = ?", new Object[]{id}, new BeanPropertyRowMapper(RecoupJobEntity.class));
        } catch (EmptyResultDataAccessException e) {//queryForObject查询不到会抛异常
        }
        return recoupJobEntity;
    }

    public void recordFailStatus(final RecoupJobEntity recoupJobEntity, final String failReason) {
        final long completeTime = DateUtils.formatNow2Long();
        final int retryCurTimes = recoupJobEntity.getRetryCurTimes() + 1;

        int count = this.jdbcTemplate.update("update " + RecoupJobDaoHelper.TABLE_NAME + " set job_status=?,retry_cur_times=?,complete_time=?,fail_reason=? where id = ? and job_status in (?, ?)",
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setString(1, JobStatusEnum.FAILED.getCode());
                        ps.setInt(2, retryCurTimes);
                        ps.setLong(3, completeTime);
                        ps.setString(4, failReason);
                        ps.setLong(5, recoupJobEntity.getId());
                        ps.setString(6, JobStatusEnum.EXECUTE.getCode());
                        ps.setString(7, JobStatusEnum.EXECUTING.getCode());
                    }
                });
        if (count == 1) {
            recoupJobEntity.setJobStatus(JobStatusEnum.FAILED.getCode());
            recoupJobEntity.setRetryCurTimes(retryCurTimes);
            recoupJobEntity.setCompleteTime(completeTime);
            recoupJobEntity.setFailReason(failReason);
        }
    }

    public void recordSuccessStatus(final RecoupJobEntity recoupJobEntity) {
        final long completeTime = DateUtils.formatNow2Long();
        final int retryCurTimes = recoupJobEntity.getRetryCurTimes() + 1;

        int count = this.jdbcTemplate.update("update " + RecoupJobDaoHelper.TABLE_NAME + " set job_status=?,retry_cur_times=?,complete_time=? where id = ? and job_status in (?, ?)",
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setString(1, JobStatusEnum.SUCCESSED.getCode());
                        ps.setInt(2, retryCurTimes);
                        ps.setLong(3, completeTime);
                        ps.setLong(4, recoupJobEntity.getId());
                        ps.setString(5, JobStatusEnum.EXECUTE.getCode());
                        ps.setString(6, JobStatusEnum.EXECUTING.getCode());
                    }
                });
        if (count == 1) {
            recoupJobEntity.setJobStatus(JobStatusEnum.SUCCESSED.getCode());
            recoupJobEntity.setRetryCurTimes(retryCurTimes);
            recoupJobEntity.setCompleteTime(completeTime);
        }
    }

    public void recordExecutingStatus(final RecoupJobEntity recoupJobEntity, final String failReason) {
        final int retryCurTimes = recoupJobEntity.getRetryCurTimes() + 1;

        int count = this.jdbcTemplate.update("update " + RecoupJobDaoHelper.TABLE_NAME + " set job_status=?,retry_cur_times=?,fail_reason=? where id = ? and job_status in (?, ?)",
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setString(1, JobStatusEnum.EXECUTING.getCode());
                        ps.setInt(2, retryCurTimes);
                        ps.setString(3, failReason);
                        ps.setLong(4, recoupJobEntity.getId());
                        ps.setString(5, JobStatusEnum.EXECUTE.getCode());
                        ps.setString(6, JobStatusEnum.EXECUTING.getCode());
                    }
                });
        if (count == 1) {
            recoupJobEntity.setJobStatus(JobStatusEnum.EXECUTING.getCode());
            recoupJobEntity.setRetryCurTimes(retryCurTimes);
        }
    }

    public int updateStartTimeById(final long id, final long startTime) {
        int count = this.jdbcTemplate.update("update " + RecoupJobDaoHelper.TABLE_NAME + " set start_time=? where id = ?",
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setLong(1, startTime);
                        ps.setLong(2, id);
                    }
                });
        return count;
    }

    public int updateEffectTimeById(final long id, final long effectTime) {
        int count = this.jdbcTemplate.update("update " + RecoupJobDaoHelper.TABLE_NAME + " set effect_time=? where id = ?",
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setLong(1, effectTime);
                        ps.setLong(2, id);
                    }
                });
        return count;
    }

    public int updateRetryTotalTimesById(final long id, final int retryTotalTimes) {
        int count = this.jdbcTemplate.update("update " + RecoupJobDaoHelper.TABLE_NAME + " set retry_total_times=? where id = ?",
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setInt(1, retryTotalTimes);
                        ps.setLong(2, id);
                    }
                });
        return count;
    }

    public void insert(final RecoupJobEntity recoupJobEntity) {
        String insertSql = "insert into " + RecoupJobDaoHelper.TABLE_NAME + "(job_class,job_name,job_desc,job_json_param," +
                "job_status,start_time,complete_time,create_time,update_time,effect_time," +
                "retry_cur_times,retry_total_times," +
                "fail_reason,async,bus_code,fail_alarm_flag) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (RecoupJobDaoHelper.isOracle()) {
            insertSql = "insert into " + RecoupJobDaoHelper.TABLE_NAME + "(id,job_class,job_name,job_desc,job_json_param," +
                    "job_status,start_time,complete_time,create_time,update_time,effect_time," +
                    "retry_cur_times,retry_total_times," +
                    "fail_reason,async,bus_code,fail_alarm_flag) values(aseq_t_recoup_job.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        }
        this.jdbcTemplate.update(insertSql,
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setString(1, recoupJobEntity.getJobClass());
                        ps.setString(2, recoupJobEntity.getJobName());
                        ps.setString(3, recoupJobEntity.getJobDesc());
                        ps.setString(4, recoupJobEntity.getJobJsonParam());
                        ps.setString(5, recoupJobEntity.getJobStatus());
                        ps.setLong(6, recoupJobEntity.getStartTime());
                        ps.setLong(7, recoupJobEntity.getCompleteTime());
                        ps.setLong(8, recoupJobEntity.getCreateTime());
                        ps.setLong(9, recoupJobEntity.getUpdateTime());
                        ps.setLong(10, recoupJobEntity.getEffectTime());
                        ps.setInt(11, recoupJobEntity.getRetryCurTimes());
                        ps.setInt(12, recoupJobEntity.getRetryTotalTimes());
                        ps.setString(13, recoupJobEntity.getFailReason());
                        ps.setInt(14, recoupJobEntity.getAsync());
                        ps.setString(15, recoupJobEntity.getBusCode());
                        ps.setInt(16, recoupJobEntity.getFailAlarmFlag());
                    }
                });
    }

    /**
     * 完成自动创建业务补偿表
     *
     * @throws SQLException
     */
    @PostConstruct
    void afterPropertiesSet() {
        RecoupJobDaoHelper.createTable(this.jdbcTemplate);
    }
}
