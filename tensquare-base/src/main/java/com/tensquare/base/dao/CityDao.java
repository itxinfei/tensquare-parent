package com.tensquare.base.dao;

import com.tensquare.base.pojo.City;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CityDao extends JpaRepository<City, String>, JpaSpecificationExecutor<City> {

    public List<City> findByNameLikeOrIshot(String name, String ishot);

    public List<City> findByNameLikeOrIshot(String name, String ishot, Pageable pageable);

    public City findByName(String name);
}
