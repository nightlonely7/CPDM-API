package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.models.assignRequests.AssignRequest;
import com.fpt.cpdm.services.AssigneeRequestService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssigneeRequestServiceIpml implements AssigneeRequestService {
    @Override
    public AssignRequest save(AssignRequest model) {
        return null;
    }

    @Override
    public List<AssignRequest> saveAll(List<AssignRequest> models) {
        return null;
    }

    @Override
    public AssignRequest findById(Integer id) {
        return null;
    }

    @Override
    public boolean existsById(Integer id) {
        return false;
    }

    @Override
    public List<AssignRequest> findAll() {
        return null;
    }

    @Override
    public List<AssignRequest> findAllById(List<Integer> ids) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void delete(AssignRequest model) {

    }

    @Override
    public void deleteAll(List<AssignRequest> models) {

    }

    @Override
    public void deleteAll() {

    }
}
