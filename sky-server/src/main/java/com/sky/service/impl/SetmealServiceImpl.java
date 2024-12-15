package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    public void save(SetmealDTO setmealdto) {
        Setmeal setmeal = new Setmeal();

        BeanUtils.copyProperties(setmealdto,setmeal);

        setmealMapper.insert(setmeal);
        long setmealId = setmeal.getId();

        List<SetmealDish> setmealDishes = setmealdto.getSetmealDishes();

        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
        setmealDishMapper.insertBatch(setmealDishes);


    }
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO){
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    public void deleteBatch(List<Long> ids){
        for(Long id :ids){
            Setmeal setmeal = setmealMapper.getById(id);
            if(setmeal.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        for(Long id :ids){
            setmealMapper.deleteById(id);
        }
    }

    public void update(SetmealDTO setmealDTO){
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);

        setmealMapper.update(setmeal);

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        Long setmealId = setmealDTO.getId();

        setmealDishMapper.delete(setmealId);
        setmealDishes.forEach(setmealDish -> {setmealDish.setSetmealId(setmealId);});
        setmealDishMapper.insertBatch(setmealDishes);


    }

    public SetmealVO getByIdWithDishes(Long id){
        SetmealVO setmealVO = new SetmealVO();

        Setmeal setmeal = setmealMapper.getById(id);

        BeanUtils.copyProperties(setmeal,setmealVO);

        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);

        setmealVO.setSetmealDishes(setmealDishes);



        return setmealVO;
    }

    public void enableOrDisable(Integer status, Long id){
        if(status == StatusConstant.ENABLE){
            List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealIdWithStatus(id);
            if(setmealDishes != null && setmealDishes.size() > 0) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ENABLE_FAILED);
            }else{
                Setmeal setmeal = Setmeal.builder()
                        .status(status)
                        .id(id)
                        .build();
                setmealMapper.update(setmeal);
            }
        }else if(status == StatusConstant.DISABLE){
            Setmeal setmeal = Setmeal.builder()
                    .status(status)
                    .id(id)
                    .build();
            setmealMapper.update(setmeal);
        }
    }



}
