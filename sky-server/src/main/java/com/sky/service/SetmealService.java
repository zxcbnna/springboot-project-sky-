package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    void save(SetmealDTO setmealdto);

    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void deleteBatch(List<Long> ids);

    void update(SetmealDTO setmealDTO);

    SetmealVO getByIdWithDishes(Long id);

    void enableOrDisable(Integer status, Long id);
}
