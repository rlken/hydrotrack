package com.hydrotrack.service;

import com.hydrotrack.model.*;
import com.hydrotrack.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduleService {

    private final FilterScheduleRepository scheduleRepository;
    private final FountainRepository fountainRepository;

    public ScheduleService(FilterScheduleRepository scheduleRepository, FountainRepository fountainRepository) {
        this.scheduleRepository = scheduleRepository;
        this.fountainRepository = fountainRepository;
    }


    public List<FilterSchedule> findAll() {
        return scheduleRepository.findAll();
    }

    public List<FilterSchedule> findActiveSchedules() {
        return scheduleRepository.findActiveSchedules();
    }

    public FilterSchedule findById(Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    public List<FilterSchedule> findByFountainId(Long fountainId) {
        return scheduleRepository.findByFountainFountainId(fountainId);
    }

    public List<FilterSchedule> findOverdueSchedules() {
        return scheduleRepository.findOverdueSchedules(LocalDate.now());
    }

    public List<FilterSchedule> findUpcomingSchedules(int days) {
        return scheduleRepository.findUpcomingSchedules(LocalDate.now(), LocalDate.now().plusDays(days));
    }

    public long countOverdue() {
        return scheduleRepository.countOverdue();
    }

    @Transactional
    public FilterSchedule createSchedule(Long fountainId, String filterType,
                                          Integer intervalDays, LocalDate nextDate, String priority) {
        Fountain fountain = fountainRepository.findById(fountainId)
                .orElseThrow(() -> new RuntimeException("Fountain not found"));

        FilterSchedule schedule = new FilterSchedule();
        schedule.setFountain(fountain);
        schedule.setFilterType(filterType);
        schedule.setReplacementIntervalDays(intervalDays);
        schedule.setNextReplacementDate(nextDate);
        schedule.setPriority(priority != null ? priority : "NORMAL");
        schedule.setStatus("SCHEDULED");

        return scheduleRepository.save(schedule);
    }

    @Transactional
    public FilterSchedule updateSchedule(Long id, String filterType, Integer intervalDays,
                                          LocalDate nextDate, String priority, String status) {
        FilterSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        schedule.setFilterType(filterType);
        schedule.setReplacementIntervalDays(intervalDays);
        schedule.setNextReplacementDate(nextDate);
        schedule.setPriority(priority);
        schedule.setStatus(status);

        return scheduleRepository.save(schedule);
    }

    @Transactional
    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }
}
