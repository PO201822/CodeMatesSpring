package com.codecool.service;

import com.codecool.dto.CourierOrderDto;
import com.codecool.dto.JobsDto;

import java.util.List;

public interface CourierService {

    List<CourierOrderDto> getMyCurrentJobs();

    List<JobsDto> getAllJobs();

    List<CourierOrderDto> getCompletedJobs();
}
