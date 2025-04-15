package com.playfooty.backendCore.service;

import com.playfooty.backendCore.dto.PaginationRequestDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class BaseService {

    protected Pageable getPaginationAndSorting (PaginationRequestDTO pageRequest) {
        Sort sort = pageRequest.getSortDir().equalsIgnoreCase("desc") ?
                Sort.by(Sort.Order.desc(pageRequest.getSortBy())) :
                Sort.by(Sort.Order.asc(pageRequest.getSortBy()));

        return PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), sort);
    }
}
